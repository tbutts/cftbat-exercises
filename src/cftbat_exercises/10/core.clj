(ns cftbat-exercises.10.core
  (:require [clojure.test :refer [are is with-test]]))

; 1. Create an atom with the initial value 0, use swap! to increment it a
;    couple of times, and then dereference it.
(defn atom-demo
  []
  (do (let [atom-man (atom 0)
            a-couple-of-times 3.1417e5]
        (dotimes [_ a-couple-of-times]
          (swap! atom-man inc))
        @atom-man))
  )

(with-test
  (def ex1)
  (is (= (atom-demo) 314170)))

; 2. Create a function that uses futures to parallelize the task of downloading
;    random quotes from http://www.braveclojure.com/random-quote using
;    (slurp "http://www.braveclojure.com/random-quote"). The futures should
;    update an atom that refers to a total word count for all quotes.
;    The function will take the number of quotes to download as an argument
;    and return the atom’s final value. Keep in mind that you'll need to
;    ensure that all futures have finished before returning the atom's final
;    value. Here's how you would call it and an example result:
;        (quote-word-count 5)
;        => {"ochre" 8, "smoothie" 2}

(defn alerter
  "Returns a function that, when called as many times as the cap,
  delivers on the given promise, signalling a task is complete."
  [cap p]
  (let [calls (atom 0)]
    (fn [& args]
      (when (>= (swap! calls inc) cap)
        (deliver p :finished)))))

(def random-quote-url "https://www.braveclojure.com/random-quote")

(defn random-quote-word-freqs
  "Snags a random quote from Brave Clojure's site, and returns a map
   of occurrences of each word in the quote - e.g. {\"wife\" 2}.
   Quotes are assumed to appear in two lines, the first being the quote
   itself, and the second being the author's name (which is ignored)."
  []
  (->> (slurp random-quote-url)
       clojure.string/split-lines
       first
       clojure.string/lower-case
       (re-seq #"\w+'?\w*")
       frequencies))

(defn quote-word-count
  "Downloads quote-num quotes from Brave Clojure, and generates a word count
   across all quotes. Automatically times out after 5 seconds.
   Sychronization is managed with a stateful watch on the local word-counts atom.
   This could have also been done with a map on a lazy sequence of promises,
   or a (ref ...) with a promise and the word-counts atom inside, but I wanted to
   try out the watch callback functionality."
  [quote-num]
  (let [word-counts (atom {})
        finished (promise)]
    (add-watch word-counts :alert (alerter quote-num finished))
    (dotimes [n quote-num]
      (future (swap! word-counts (partial merge-with + (random-quote-word-freqs)))))
    (if (deref finished 5000 nil)
      @word-counts
      :timeout-occured)))

(comment (quote-word-count 5)) ; Sample: {"his" 2, "idiot" 1, "not" 1, "experience" 1, ...}

; 3. Create representations of two characters in a game. The first character has
;    15 hit points out of a total of 40. The second character has a healing potion
;    in his inventory. Use refs and transactions to model the consumption of the
;    healing potion and the first character healing.

(def inventory-base {:potions 0})

(defn duder
  "Creates a new Model RPG character"
  ([name hp] (duder name hp {}))
  ([name hp inventory]
   {:name name
    :hit-points hp
    :inventory (merge inventory-base inventory)}))

(defn inventory-checker
  "Validates the inventory of an RPG character. Item counts cannot be below 0."
  [{:keys [inventory]}]
  (if-let [[k v] (some (fn [[k v]] (when (< v 0) [k v])) inventory)]
    (throw (IllegalStateException. (str "Can't have negative amount of: " k " = " v)))
    true))

(defn stateful-duder
  "Takes the same args as `duder`, and returns a stateful Ref, which can
   be altered. Updated state will be checked with `inventory-checker`, and
   denied with an exception if the new state is invalid."
  [& args]
  (ref (apply duder args) :validator inventory-checker))

(def max-hp "Maximum health any character may have." 40)
(def potion-regen "Amount added to health when consuming a potion." 30)

; These next two defns are not wrapped in dosync, to enable composition.

(defn consume-potion
  "Remove 1 potion from inventory, and health at most the value of `potion-regen`,
   up to `max-hp`."
  [dude]
  (alter dude update-in [:inventory :potions] dec)
  (alter dude update-in [:hit-points] #(min (+ % potion-regen) max-hp)))

(defn give-potion
  "Trade potions between character inventories."
  [giver receiver]
  (alter giver    update-in [:inventory :potions] dec)
  (alter receiver update-in [:inventory :potions] inc))

(defn swipe-potion-from-buddy-and-damn-him-to-death
  "Hero takes his buddy's only potion, and drinks it.
   Please note the lack of consent, which was noticably omitted from the exercise text:

   'model the consumption of the healing potion and the first character healing.'"
  [hero gullible-buddy]
  (dosync
   (give-potion gullible-buddy hero)
   (consume-potion hero)))

(with-test
  (def ex3)

  (defn assert-stats [guy health potions]
    (and (= (get-in @guy [:inventory :hit-points]) health)
         (= (get-in @guy [:inventory :potions])    potions)))

  (let [tudyk  (stateful-duder "Tudyk Ramstead" 15)
        martin (stateful-duder "Martin Cheesehands" 40 {:potions 1})]

    (are [guy pots] assert-stats
      tudyk  15 0
      martin 40 1)

    (swipe-potion-from-buddy-and-damn-him-to-death tudyk martin)
    (are [guy health potions] assert-stats
      tudyk  40 0
      martin 40 0)

    (is (thrown-with-msg?
         IllegalStateException #"Can't have negative amount of: :potions"
         (swipe-potion-from-buddy-and-damn-him-to-death tudyk martin))))
  )

