(ns cftbat-exercises.07.core)

; 1. Use the list function, quoting, and read-string to create a list that,
;    when evaluated, prints your first name and your favorite sci-fi movie.
(def sci-fi-list (list (read-string "println") '"John" '"Interstellar"))
sci-fi-list ; => (println "John" "Interstellar")
(defn sci-fi-printer [] (eval sci-fi-list)) ; prints "John Interstellar"

; 2. Create an infix function that takes a list like (1 + 3 * 4 - 5)
;    and transforms it into the lists that Clojure needs in order to
;    correctly evaluate the expression using operator precedence rules.
(def precedence {'* 1 '/ 1 '+ 0 '- 0})
(defn operator? [sym] (get precedence sym))
(defn lower-precedence? [op1 op2] (<= (precedence op1) (precedence op2)))

(defn flatten1 "Flattens one level deep" [coll] (apply concat coll))

(defn shunting-yard
  "Dijkstra's infix to reverse polish notation algorithm, in Clojure. For a
   full description, see `https://en.wikipedia.org/wiki/Shunting-yard_algorithm`"
  [tokens]
  (flatten1
   (reduce
    (fn [[output stack] token]
      (if (operator? token)
        (let [[higher lower] (split-with (partial lower-precedence? token) stack)]
          [(vec (concat output higher)) (cons token lower)])
        [(conj output token) stack]))
    [[] ()]
    tokens)))

(defn rpn->sexprs
  "Transforms a list in RPN to a list of ready-to-eval s-expressions.
   Walks the list of rpns, and inserts inner list nodes in (op num1 num2) form.
   E.g. (rpn->sexprs (1 3 4 * + 5 -)) => (- (+ 1 (* 3 4)) 5)"
  [rpn]
  (first
   (reduce
    (fn [sexprs token]
      (if (operator? token)
        (let [[expr1 expr2 & tail] sexprs] (cons (list token expr2 expr1) tail))
        (cons token sexprs)))
    ()
    rpn)))

(def infix (comp rpn->sexprs shunting-yard))
(def infix-calc (comp eval infix))

; Test Ex. 2:
(infix '(1 + 3 * 4 - 5))
; => (- (+ 1 (* 3 4)) 5)

(infix-calc  '(1 + 3 * 4 - 5))
; => 8

