(ns were-creatures)

;; Sample code from CftBaT Chapter 13 text, used with the exercises in core.clj

(defmulti full-moon-behavior
  (fn [were-creature] (:were-type were-creature)))

(defmethod full-moon-behavior :wolf
  [were-creature]
  (str (:name were-creature) " will howl and murder"))

(defmethod full-moon-behavior :simmons
  [were-creature]
  (str (:name were-creature) " will encourage people and sweat to the oldies"))

(full-moon-behavior {:were-type :wolf
                     :name "Rachel from next door"})
; => "Rachel from next door will howl and murder"

(full-moon-behavior {:name "Andy the baker"
                     :were-type :simmons})
; => "Andy the baker will encourage people and sweat to the oldies"

(defmethod full-moon-behavior nil
  [were-creature]
  (str (:name were-creature) " will stay at home and eat ice cream"))

(full-moon-behavior {:were-type nil
                     :name "Martin the nurse"})
; => "Martin the nurse will stay at home and eat ice cream"

(defmethod full-moon-behavior :default
  [were-creature]
  (str (:name were-creature) " will stay up all night fantasy footballing"))

(full-moon-behavior {:were-type :office-worker
                     :name "Jimmy from sales"})
; => "Jimmy from sales will stay up all night fantasy footballing"



(defprotocol WereCreature
  (full-moon-proto-behavior [x]))

(defrecord WereWolf [name title]
  WereCreature
  (full-moon-proto-behavior [x]
    (str name " will howl and murder")))

(full-moon-proto-behavior (map->WereWolf {:name "Lucian" :title "CEO of Melodrama"}))
; => "Lucian will howl and murder"

(comment
  (ns random-namespace
    (:require [were-creatures]))
  (defmethod were-creatures/full-moon-behavior :bill-murray
    [were-creature]
    (str (:name were-creature) " will be the most likeable celebrity"))
  (were-creatures/full-moon-behavior {:name "Laura the intern"
                                      :were-type :bill-murray}))
; => "Laura the intern will be the most likeable celebrity"

