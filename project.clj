(defproject cftbat-exercises "0.1.0-SNAPSHOT"
  :description "Exercise solutions for Clojure for the Brave and True"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/core.async "0.3.443"]]
  :main ^:skip-aot cftbat-exercises.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
