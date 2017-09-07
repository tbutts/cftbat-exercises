(ns cftbat-exercises.core
  (:require [cftbat-exercises.03.core :as c03]
            [cftbat-exercises.04.core :as c04]
            [cftbat-exercises.05.core :as c05]))

(defn alias-interns
  "Find functions available in a namespace via a quoted alias"
  [namespace-alias]
  (-> *ns*
      ns-aliases
      (#(get % namespace-alias))
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

