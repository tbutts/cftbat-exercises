(ns cftbat-exercises.12.core
  (:import [javax.swing JFrame JLabel JPanel]))

;; The official docs cover java interop well:
;; Link: https://clojure.org/reference/java_interop

(defn disgusting-gui
  "Build & displays a Swing Frame"
  []
  (let [panel (JPanel.)]
    (doto (JFrame. "Java Interop Example")
      (.setDefaultCloseOperation JFrame/DISPOSE_ON_CLOSE)
      (.setSize 300 250)
      (.add (doto panel
              (.setBackground (java.awt.Color. 0x60 0xDD 0x80))
              (.add (JLabel. "Speak of the devil!"))))
      (.setLocationRelativeTo nil)
      (.setVisible true))

    @(future (Thread/sleep 1000))

    (doto panel
      (.add (JLabel. "Clojure!"))
      (.revalidate)
      (.repaint)))
  nil)


;; I had seen a showcase of Clojure before, which included a link to this
;; SO answer which demonstrates very handy java interop, as well as lazy sequences.
;; Link: https://stackoverflow.com/a/7941430

(defn prime?
  "Return true if number is prime within (1 - 0.5^(certainty))"
  ([n] (prime? n 5))
  ([n certainty] (.isProbablePrime (BigInteger/valueOf n) certainty)))

(defn primes
  "Return the first n prime numbers as a list"
  [n]
  (->> (range 1 Integer/MAX_VALUE) ; Across most natural numbers
       (take-nth 2) ; Take every number, skipping over even numbers
       (filter prime?) ; Select only primes
       (take n) ; Stop after selecting n number of primes
       (cons 2))) ; Prepend 2, the only even prime number

;; There's also type hinting and support for Java primitives, when needed.
;;
;; Performance optimization, as always, is an art that requires benchmarks.
;; Read, and _re-read_ the Clojure.org Java Interop page (linked above.)
;;
;; Just adding type hints and unboxing can often times result in slower code.
;; Only use primitives inside loops, otherwise they will be boxed

(defn int->binstr
  "Converts an integer to a String of its binary representation"
  [^Integer n]
  (let [s (java.lang.StringBuilder.)]
    (loop [next (int n)]
      (when (> next 0)
        (.append s (if (odd? next) "1" "0"))
        (recur (quot next 2))))
    (.toString (.reverse s))))


;; And remember that file io is simplified with `slurp`, `spit` the `with-open` macro,
;; and every thing else in `clojure.java.io`

