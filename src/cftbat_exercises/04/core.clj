(ns cftbat-exercises.04.core)

(def filename "src/cftbat_exercises/04/suspects.csv")

(def vamp-keys [:name :glitter-index])

(defn str->int
  [str]
  (Integer. str))

(def conversions {:name identity
                  :glitter-index str->int})

(defn convert
  [vamp-key value]
  ((get conversions vamp-key) value))

(defn parse
  "Convert a CSV into rows of columns"
  [string]
  (map #(clojure.string/split % #",")
       (clojure.string/split string #"\n")))

(defn mapify
  "Return a seq of maps like {:name \"Edward Cullen\" :glitter-index 10}"
  [rows]
  (map (fn [unmapped-row]
         (reduce (fn [row-map [vamp-key value]]
                   (assoc row-map vamp-key (convert vamp-key value)))
                 {}
                 (map vector vamp-keys unmapped-row)))
       rows))

(defn glitter-filter
  [minimum-glitter records]
  (filter #(>= (:glitter-index %) minimum-glitter) records))

; 1. Turn the result of your glitter filter into a list of names.
(defn glitter-filter-to-name-list
  [minimum-glitter records]
  (map :name (glitter-filter minimum-glitter records)))

; 2. Write a function, append, which will append a new suspect to your list of suspects.
(defn append
  "Adds a suspect to the list of suspects"
  [vamp-suspects suspect]
  (concat vamp-suspects [suspect]))

(def suspect-validators {:name string?
                 :glitter-index number?})

; 3. Write a function, validate, which will check that :name and :glitter-index
;    are present when you append. The validate function should accept two arguments:
;    a map of keywords to validating functions, similar to conversions,
;    and the record to be validated.
(defn validate
  "Predicate which checks that :name and :glitter-index are present"
  [validators suspect]
  (reduce
   (fn [is-valid [vamp-key value]]
     (and
      is-valid
      (contains? validators vamp-key)
      ((get validators vamp-key) value)))
   true
   suspect))

(def validate-vampire-suspect #(validate suspect-validators %))

(defn safe-append
  "Validates before appending"
  [vamp-suspects suspect]
  (if (validate-vampire-suspect suspect)
    (append vamp-suspects suspect)
    vamp-suspects))

; 4. Write a function that will take your list of maps and convert it back to a CSV string.
;    You'll need to use the clojure.string/join function.
(defn unparse-line
  [line]
  (clojure.string/join "," (sort-by number? (map second line))))

(defn unparse-suspects
  "Converts map of suspects back to CSV string"
  [suspects]
  (clojure.string/join "\n" (map unparse-line suspects)))

(validate-vampire-suspect {:name "Morvis Fleshdrinking" :glitter-index 5}) ; => true
(validate-vampire-suspect {:name "Mr Poopy Butthole" :bad-key #"10/10"}) ; => false

;; (def vamp-suspects (mapify (parse (slurp filename))))

; Verify that safe-append returns the original list of suspects when given an invalid suspect.
;; (= vamp-suspects
;;    (safe-append vamp-suspects {:name "Mr Poopy Butthole" :bad-key #"10/10"})) ; => true

;; (unparse-suspects vamp-suspects)
;; => "Edward Cullen,10\nBella Swan,0\nCharlie Swan,0\nJacob Black,3\nCarlisle Cullen,6"

