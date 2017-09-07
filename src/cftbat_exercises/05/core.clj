(ns cftbat-exercises.05.core)

; 1. Create a function, `attr`, that you can call like (attr :intelligence) and that does the same thing.
(def character
  {:name "Smooches McCutes"
   :attributes {:intelligence 10
                :strength 4
                :dexterity 5}})

(def attr #(% (:attributes character)))
(def attr2 #((comp % :attributes) character))

; 2. Implement the comp function.
(defn my-comp
  "Comp implemented like clojure.core's `comp`"
  ([] (fn [] identity))
  ([f] (fn [& args] (apply f args)))
  ([f g] (fn [& args] (f (apply g args))))
  ([f g & fs]
   (reduce my-comp (into [f g] fs))))

(defn my-comp2
  "Comp implemented in a simpler form, without relying on argument overloading.
   Note this would be slower than the above."
  [& fs]
  (reduce
   (fn [f g] (fn [& args] (f (apply g args))))
   fs))

((my-comp inc inc #(* 2 %) +) 1 2 3)
; => 14

; 3. Implement the assoc-in function.
; Hint: use the assoc function and define its parameters as [m [k & ks] v] .
(defn my-assoc-in
  "Associates a value in a nested associative structure."
  [m [k & ks] v]
  (if (empty? ks)
    (assoc m k v)
    (assoc m k (my-assoc-in (get m k) ks v))))

; 4. Look up and use the update-in function.
(update-in {:outer {:inner 5}} [:outer :inner] * 2)
; => {:outer {:inner 10}}

; 5. Implement update-in
(defn my-update-in
  [m ks f & args]
  (my-assoc-in m ks (apply f (get-in m ks) args)))

(my-update-in {:outer {:inner 5}} [:outer :inner] * 2)
; => {:outer {:inner 10}}

