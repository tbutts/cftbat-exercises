(ns cftbat-exercises.13.core)

; 1. Extend the full-moon-behavior multimethod to add behavior for your own kind
;    of were-creature.

(require 'were-creatures)

(defmethod were-creatures/full-moon-behavior :bus
  [wc]
  (str (:name wc) " will honk at the moon"))

(were-creatures/full-moon-behavior {:were-type :bus
                                    :name "Mr. Wilson"})

; 2. Create a WereSimmons record type, and then extend the WereCreature protocol.
(import were_creatures.WereCreature)

;; Record fields are defined in a vector
(defrecord WereSimmons [name friends])

(extend-type WereSimmons
  were-creatures/WereCreature
  (full-moon-proto-behavior [wc]
    (str (.name wc) " organizes flash workouts with his " (.friends wc) " friends")))

(def richard (map->WereSimmons {:name "Richard" :friends (int 5e5)}))
(were-creatures/full-moon-proto-behavior richard)
; => "Richard organizes flash workouts with his 500000 friends"


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
(reduce + (map area [(->Rectangle 4 5) (->Rectangle 2 8) (->Triangle 5 10)]))
; => 61.0

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
(reduce + (map area [(Rectangle. 4 5) (Triangle. 5 10) (Circle. 5) (Trapezoid. 4 2.5 5)]))
; => 85.0


; 4. Create a role-playing game that implements behavior using multiple dispatch.

;; -TODO-

