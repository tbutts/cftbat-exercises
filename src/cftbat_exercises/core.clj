(ns cftbat-exercises.core
  (:require [cftbat-exercises.03.core :as c03]
            [cftbat-exercises.04.core :as c04]
            [cftbat-exercises.05.core :as c05]
            [cftbat-exercises.07.core :as c07]
            [cftbat-exercises.08.core :as c08]
            [cftbat-exercises.09.core :as c09]
            )
  )

(defn alias-interns
  "Find functions available in a namespace via a quoted alias"
  ; Here, the "thread-first (->)" macro places the map of ns-aliases first in
  ; the parameters of each function. So, with `get`, `namespace-alias` is
  ; actually the 2nd param. This macro, as discussed in Chapter 7, does its
  ; magic to rewrite internal code after Clojure's reader, but before evaluation.
  ; The code itself is data that can be manipulated!
  ;
  ; If you run macroexpand on this, the result is:
  ; (ns-interns (get (ns-aliases *ns*) (quote c03)))
  [namespace-alias]
  (-> *ns*
      ns-aliases
      (get namespace-alias)
      ns-interns))

(defn -main
  [& args]
  (println "A Brave & True greeting!")
  (println)
  (println "Try running me in the REPL - `lein repl`")
  (println "Then you can examine what's available with ns* functions:")
  (println "  (ns-interns 'cftbat-exercises.03.core)")
  (println)
  (println "Or via one of the available aliases and a helper fn:")
  (println "  (alias-interns 'c03)  ; where 'cXX is the chapter number."))

