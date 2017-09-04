(ns fwpd.core)

(def filename "src/cftbat_exercises/4/suspects.csv")

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

(defn glitter-filter-to-name-list
  [minimum-glitter records]
  (map :name (glitter-filter minimum-glitter records)))

(defn append
  "Adds a suspect to the list of suspects"
  [vamp-suspects suspect]
  (concat vamp-suspects [suspect]))

(def validators {:name string?
                 :glitter-index number?})

(defn validate
  "Predicate checks that :name and :glitter-index are present"
  [req-keywords suspect]
  (reduce (fn [acc [vamp-key value]]
            (and
             acc
             (contains? req-keywords vamp-key)
             (apply (get req-keywords vamp-key) [value])))
          true
          suspect))

(def validate-vampire-suspect #(validate validators %))

(defn safe-append
  "Validates before appending"
  [vamp-suspects suspect]
  (if (validate-vampire-suspect suspect)
    (append vamp-suspects suspect)
    vamp-suspects))

(defn unparse-line
  [line]
  (clojure.string/join "," (sort-by number? (map second line))))

(defn unparse-suspects
  "Converts map of suspects back to CSV string"
  [suspects]
  (clojure.string/join "\n" (map unparse-line suspects)))

; (validate-vampire-suspect {:name "Morvis Fleshdrinking" :glitter-index 5}) ; => true
; (validate-vampire-suspect {:name "Mr Poopy Butthole" :haha #"10/10"}) ; => false

; (def vamp-suspects (mapify (parse (slurp filename))))
;; (= vamp-suspects
;;    (safe-append vamp-suspects {:name "Mr Poopy Butthole" :haha #"10/10"})) ; => true

; (unparse-suspects vamp-suspects)
; => "Edward Cullen,10\nBella Swan,0\nCharlie Swan,0\nJacob Black,3\nCarlisle Cullen,6"


