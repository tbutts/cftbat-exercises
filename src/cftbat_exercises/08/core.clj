(ns cftbat-exercises.08.core)

; 1. Write the macro when-valid so that it behaves similarly to when.
;    Here is an example of calling it:
(comment
  (when-valid order-details order-details-validations
              (println "It's a success!")
              (render :success)))
;    When the data is valid, the println and render forms should be evaluated,
;    and when-valid should return nil if the data is invalid.


; Here is the necessary validate function and helpers from the Chapter 8 text
(defn error-messages-for
  "Return a seq of error messages"
  [to-validate message-validator-pairs]
  (map first (filter #(not ((second %) to-validate))
                     (partition 2 message-validator-pairs))))

(defn validate
  "Returns a map with a vector of errors for each key"
  [to-validate validations]
  (reduce (fn [errors validation]
            (let [[fieldname validation-check-groups] validation
                  value (get to-validate fieldname)
                  error-messages (error-messages-for value validation-check-groups)]
              (if (empty? error-messages)
                errors
                (assoc errors fieldname error-messages))))
          {}
          validations))


; Solution to Ex. 1:
(defmacro when-valid
  [to-validate validations & body]
  `(if (empty? (validate ~to-validate ~validations))
        (do ~@body)))

; Test Ex. 1:
(def order-strict-validations {:price ["Price must not be negative" (complement neg?)]})
(def order-validations {:price ["Price must be a number" number?]})
(def order-details {:name "Tide Laundry Detrg." :price -0.54})

; Order is invalid, thus nothing is printed and nil is returned:
(comment (when-valid order-details order-strict-validations
                     (println "Invalid order, this will not print!")
                     (assoc order-details :valid true)))
; => nil

; Order is valid, prints and returns marked order
(comment (when-valid order-details order-validations
                     (println "Tests passed, valid order!")
                     (assoc order-details :valid true)))
; => {:name "Tide Laundry Detrg.", :price -0.54, :valid true}

; 2. You saw that and is implemented as a macro. Implement or as a macro.

; Here's the source for `and`, from the book:
(comment
  (defmacro and
  "Evaluates exprs one at a time, from left to right. If a form
  returns logical false (nil or false), and returns that value and
  doesn't evaluate any of the other expressions, otherwise it returns
  the value of the last expr. (and) returns true."
    {:added "1.0"}
    ([] true)
    ([x] x)
    ([x & next]
     `(let [and# ~x]
        (if and# (and ~@next) and#))))
  )

(defmacro or'
  "Docstring from clojure.core/or:
  Evaluates exprs one at a time, from left to right. If a form
  returns a logical true value, or returns that value and doesn't
  evaluate any of the other expressions, otherwise it returns the
  value of the last expression. (or) returns nil."
  ([] nil)
  ([x] x)
  ([x & next]
   `(let [or# ~x]
      (if or# or# (or' ~@next)))))

; Test Ex. 2:
(or  false nil "Truthy string" :also-true); => "Truthy string"
(or' false nil "Truthy string" :also-true); => "Truthy string"

; 3. In Chapter 5 you created a series of functions (c-int, c-str, c-dex)
;    to read an RPG character's attributes. Write a macro that defines
;    an arbitrary number of attribute-retrieving functions using one macro call.
;    Here's how you would call it:
(comment (defattrs c-int :intelligence
           c-str :strength
           c-dex :dexterity))


(defmacro defattrs
  ([] nil)
  ([f k] `(def ~f (comp ~k :attributes)))
  ([f k & rest]
   `(do (defattrs ~f ~k)
        (defattrs ~@rest))))

; Test Ex. 3:
(defattrs
  c-int :intelligence
  c-str :strength
  c-dex :dexterity)

; Check that the functions created via the macro
; are all defined in the current namespace:
(every? (ns-interns *ns*) '(c-int c-str c-dex)) ; => true

(def character
  {:name "Smooches McCutes"
   :attributes {:intelligence 10
                :strength 4
                :dexterity 5}})

(c-int character) ; => 10
(c-str character) ; => 4
(c-dex character) ; => 5

