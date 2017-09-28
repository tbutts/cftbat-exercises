(ns cftbat-exercises.13.core
  (:require [clojure.test :refer [are is with-test]]))

; 1. Extend the full-moon-behavior multimethod to add behavior for your own kind
;    of were-creature.

(require '(cftbat-exercises.13 [were-creatures :as were-creatures]))

(defmethod were-creatures/full-moon-behavior :bus
  [wc]
  (str (:name wc) " will honk at the moon"))

(were-creatures/full-moon-behavior {:were-type :bus
                                    :name "Mr. Wilson"})

; 2. Create a WereSimmons record type, and then extend the WereCreature protocol.
(import cftbat_exercises.13.were_creatures.WereCreature)

;; Record fields are defined in a vector
(defrecord WereSimmons [name friends])

(extend-type WereSimmons
  were-creatures/WereCreature
  (full-moon-proto-behavior [wc]
    (str (.name wc) " organizes flash workouts with his " (.friends wc) " friends")))


(with-test
  (def richard (map->WereSimmons {:name "Richard" :friends (int 5e5)}))
  (is (= (were-creatures/full-moon-proto-behavior richard)
         "Richard organizes flash workouts with his 500000 friends")))


; 3. Create your own protocol, and then extend it using extend-type and extend-protocol.

;; Here comes a super interesting protocol:
(defprotocol IShape
  (area [x]))
; This essentially creates a multimethod, `area`,
; that dispatches based on the first arg's type.

; Records or Types can be defined:
(defrecord Rectangle [w h])
(defrecord Triangle [b h])

; And then given implementation dynamically, even in a different namespace & project,
; using extend-protocol
(extend-protocol IShape
  Rectangle
  (area [shape] (* (:w shape) (:h shape)))

  Triangle
  (area [shape] (* 0.5 (:b shape) (:h shape))))


; Example
(with-test
  (def shape-protocol)
  (is (= 61.0
         (reduce + (map area [(->Rectangle 4 5) (->Rectangle 2 8) (->Triangle 5 10)])))))


; The protocol can be implemented when defining the record/type
(defrecord Circle [r]
  IShape
  (area [circle] (* (:r circle) (:r circle))))

; Or you can implement multiple protocols with extend-type
(defrecord Trapezoid [h b1 b2])
(extend-type Trapezoid
  IShape
  (area [tzoid] (* (:h tzoid) 0.5 (+ (:b1 tzoid) (:b2 tzoid)))))


; Another example, with the new record types
(with-test
  (def dynamically-extended-protocol)
  (is (= 85.0
         (reduce + (map area [(Rectangle. 4 5) (Triangle. 5 10) (Circle. 5) (Trapezoid. 4 2.5 5)])))))



; 4. Create a role-playing game that implements behavior using multiple dispatch.

;; It is difficult to find good uses for this, even the author admits.
;; For further reading, ref: `https://clojure.org/reference/multimethods`

(defn calc-base-damage
  [attacker defender]
  (max 0 (- (get-in attacker [:weapon :wpn-damage] 0) (:armor defender 0))))

(defmulti attack (fn [attacker defender] (mapv (juxt :class :race) [attacker defender])))

(defmethod attack [[:Warrior :Human] [:Warrior :Orc]]
  [man orc]
  (let [damage (Math/round (* 1.10 (calc-base-damage man orc)))
        text (str "The *human war machine* punctures the ~mighty orc~ for " damage " damage!")]
    {:damage damage :text text}))
(defmethod attack [[:Warrior :Human] [:Grand-Vizier :Undead]]
  [man skele]
  {:damage 0 :text "The *human war machine* whizzes right through the ~Skeletal Trickster~, dealing no damage!"})
(defmethod attack [[:Warrior :Human] [:Warrior :Human]]
  [man brother]
  {:damage 9999 :text (str "The *human war machine* commits despicable homicide against"
                           "~his compatriot~, " (:name brother) "!")})
(defmethod attack :default
  [thing1 thing2]
  (let [race-name #(name (:race %1 %2))
        class-name #(name (:class %1 %2))
        damage (calc-base-damage thing1 thing2)
        text (str "The *" (race-name thing1 "Unknown") " " (class-name thing1 "Attacker")
                  "* inflicts " damage " damage to the poor ~"
                  (race-name thing2 "Defender") "~!")]
    {:damage damage :text text}))

(with-test
  (def multi-method-test)

  (def Galahad {:name "Squire Galahad"
                :class :Warrior :race :Human
                :weapon {:name "Excalibur" :wpn-damage 35} :armor 10})

  (def Orc-dude {:name "Rutgh-nar, Bone Splitter"
                 :class :Warrior :race :Orc
                 :weapon {:name "Gnasher" :wpn-damage 17} :armor 14})

  (def Undead-mage {:name "Kang Overseer"
                    :class :Grand-Vizier :race :Undead
                    :weapon {:name "Staff of Eons" :wpn-damage 44} :armor 2})

  (def Brother {:name "Percival"
                :class :Warrior :race :Human})

  (def Bug {:name "P-Body"
            :class :Beast :race :Cricket
            :weapon {:name "Little Dangly things" :wpn-damage 1} :armor 1})

  (are [att def dmg txt] (let [{:keys [damage text]} (attack att def)]
                           (and (= damage dmg)
                                (= text   txt)))
    Galahad Orc-dude 23 "The *human war machine* punctures the ~mighty orc~ for 23 damage!"
    Galahad Undead-mage 0 "The *human war machine* whizzes right through the ~Skeletal Trickster~, dealing no damage!"
    Galahad Brother 9999 "The *human war machine* commits despicable homicide against~his compatriot~, Percival!"
    Galahad Bug 34 "The *Human Warrior* inflicts 34 damage to the poor ~Cricket~!"
    Bug Orc-dude 0 "The *Cricket Beast* inflicts 0 damage to the poor ~Orc~!"
    )
  )

