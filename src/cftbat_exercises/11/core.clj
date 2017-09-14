(ns cftbat-exercises.11.core
  (:require [clojure.core.async :refer [>! <! >!! <!! go-loop chan close!
                                        alts! alts!! timeout]]))

(def possibilities "Results of intense laboring"
  '("Bill & Ted" "Waterworld" "Cannibal the Musical" "Die Hard" "War Games"))

(defn new-worker
  "Spawns a new worker w/ id, consuming from inbound chan, and producing values on
   the returned outbound chan. The worker will close when sent a :terminate value
   over the inbound channel.
   The 'work' is all bogus random waiting around, to simulate number crunching."
  [inbound id]
  (let [outbound (chan)]
    (go-loop []
      (let [job (<! inbound)]
        (if-not (= job :terminate) ; condp is also a good choice when working with channels
          (do (<! (timeout (rand-int 500)))
              (>! outbound {:id id
                            :choice (rand-nth possibilities)})
              (recur))
          (do (close! outbound)))))
    outbound))

(defn simulate
  "Working stub of a fan-out processing pipeline. Hands out jobs to a worker
   pool that work separately on each task, produce a result, which is then
   merged into a cumulative total on a central thread.

   In this test, the return value is a tally of random votes for a film.
   The value of each job is ignored.

   Working with core.async in this way was very natural, coming from my
   background in Golang, which shares the CSP style for concurrent programming."
  ([worker-count jobs]
   (let [job-queue (chan)
         workers (map #(new-worker job-queue (keyword (str %))) (range worker-count))
         done (chan)]

     ;; Set up routine to read job results from any worker.
     (go-loop [n 0
               results {}]
       (if-not (>= n (count jobs))
         (let [[{:keys [id choice]} c] (alts! workers)]
           ;; Do something with the result. Save to DB, send for further processing, etc.
           #_(println id "chose:" choice "!") ;; Slow, but somewhat fun to watch.
           (recur (inc n) (merge-with + results {choice 1})))
         ;; When all jobs have been processed, send a stop signal to each worker through
         ;; the same shared input queue used to submit jobs to.
         (do (dotimes [_ (count workers)] (>! job-queue :terminate))
             (>! done results))))

     ;; Submit each job to the queue.
     (dorun (map (fn submit [job] (>!! job-queue job)) jobs))

     ;; Wait for signal on the done channel (either a val, or explict close action)
     (<!! done)))) ;; Open channels which are parked will eventually be tagged for GC.


; Sample runs
(comment
  (time (simulate 100 (range 1000)))
  ; => "Elapsed time: 2879.16205 msecs"
  ; => {"War Games" 207, "Cannibal the Musical" 192, "Waterworld" 194, "Die Hard" 224, "Bill & Ted" 183}
  (time (simulate 1000 (range 1000)))
  ; => "Elapsed time: 589.50058 msecs"
  ; ...

  ; Overcommitting too many workers does not noticably impact results or performance
  ; (by no means is this empirical, but I these runs a handful of times)
  ; This further establishes that go blocks are very cheap to create:
  (time (simulate 1000 (range 5)))
  ; => "Elapsed time: 439.004266 msecs"
  (time (simulate 5    (range 5)))
  ; => "Elapsed time: 483.983695 msecs"

  ; However, there is an upper limit on the number of workers:
  (time (simulate 2000 (range 2000)))
  ; -!!!- java.lang.AssertionError: Assert failed: No more than 1024 pending takes are allowed on a single channel.
  ; -!!!- (< (.size takes) impl/MAX-QUEUE-SIZE)
  )

