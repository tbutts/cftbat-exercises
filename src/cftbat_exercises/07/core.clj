(ns cftbat-exercises.07.core)

; 1. Use the list function, quoting, and read-string to create a list that,
;    when evaluated, prints your first name and your favorite sci-fi movie.
(def sci-fi-list (list (read-string "println") '"John" '"Interstellar"))
sci-fi-list ; => (println "John" "Interstellar")
(eval sci-fi-list) ; prints "John Interstellar"

; 2. Create an infix function that takes a list like (1 + 3 * 4 - 5)
;    and transforms it into the lists that Clojure needs in order to
;    correctly evaluate the expression using operator precedence rules.

; TODO: As just a function, without any macro magic? I may do this later.
(defn infix
  [symbols] ; build an AST from these
  )

