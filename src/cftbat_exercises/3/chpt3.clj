(ns cftbat-exercises-chpt3.core
  "Send help I'm retarded"
  (:gen-class))

; 4. Write a function, mapset, that works like map except
;    the return value is a set:
(defn mapset
  "Works like map, except a set is returned"
  [f coll]
  (into (hash-set) (map f coll)))

(mapset inc [1 1 2 2]); #{3 2}

(def asym-hobbit-body-parts [{:name "head" :size 3}
                             {:name "left-eye" :size 1}
                             {:name "left-ear" :size 1}
                             {:name "mouth" :size 1}
                             {:name "nose" :size 1}
                             {:name "neck" :size 2}
                             {:name "left-shoulder" :size 3}
                             {:name "left-upper-arm" :size 3}
                             {:name "chest" :size 10}
                             {:name "back" :size 10}
                             {:name "left-forearm" :size 3}
                             {:name "abdomen" :size 6}
                             {:name "left-kidney" :size 1}
                             {:name "left-hand" :size 2}
                             {:name "left-knee" :size 2}
                             {:name "left-thigh" :size 4}
                             {:name "left-lower-leg" :size 3}
                             {:name "left-achilles" :size 1}
                             {:name "left-foot" :size 2}])

(defn matching-part
  [part]
  {:name (clojure.string/replace (:name part) #"^left-" "right-")
   :size (:size part)})

(defn better-symmetrize-body-parts
  "Expects a seq of maps that have a :name and :size"
  [asym-body-parts]
  (reduce (fn [final-body-parts part]
            (into final-body-parts (set [part (matching-part part)])))
          []
          asym-body-parts))

(def asym-alien-body-parts  [{:name "head" :size 3}
                             {:name "first-eye" :size 1}
                             {:name "first-ear" :size 1}
;                             {:name "mouth" :size 1}
;                             {:name "nose" :size 1}
;                             {:name "neck" :size 2}
;                             {:name "first-shoulder" :size 3}
                             {:name "first-upper-arm" :size 3}
                             {:name "chest" :size 10}
;                             {:name "back" :size 10}
;                             {:name "first-forearm" :size 3}
;                             {:name "abdomen" :size 6}
;                             {:name "first-kidney" :size 1}
;                             {:name "first-hand" :size 2}
;                             {:name "first-knee" :size 2}
;                             {:name "first-thigh" :size 4}
;                             {:name "first-lower-leg" :size 3}
                             {:name "first-achilles" :size 1}
                             {:name "first-foot" :size 2}])
(defn match-part
  "Creates a matching body part by replacing the
   init-pos regex with the pos string"
  [part init-pos pos]
  {:name (clojure.string/replace (:name part) init-pos pos)
   :size (:size part)})

(defn part-and-matching-four-parts
  [part]
  (let [first-position #"^first-"
        other-positions ["second-" "third-" "fourth-" "fifth-"]]
    (if (clojure.string/starts-with? (:name part) "first-")
      (reduce (fn [final-parts pos]
                  (conj final-parts (match-part part first-position pos)))
               [part]
               other-positions)
      [part])))

(defn sym-alien-body-parts
  "Expects a seq of maps that have a :name and :size"
  [asym-body-parts]
  (reduce (fn [final-body-parts part]
            (into final-body-parts (part-and-matching-four-parts part)))
          []
          asym-body-parts))

; (sym-alien-body-parts asym-alien-body-parts)

