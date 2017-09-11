(ns cftbat-exercises.09.core
  (:require [clojure.test :refer [with-test is are]])
  (:import (java.net URLEncoder)))

; 1. Write a function that takes a string as an argument and searches
;    for it on Bing and Google using the slurp function. Your function
;    should return the HTML of the first page returned by the search.

;; Oh, I failed to read that the slurp function is capable of doing this exercise,
;;     and I ran off to get it working with http-kit (which was really nice!)
(comment
  ;; Note: The error returned by httpkit's get on the response map is an Exception.
  ;;       Exceptions can be interacted with using the same methods as in Java.
  ;;       I imagine this fact will be covered in the Java interop Chapter (#12).
  ;;       I did not realize this at first, and tried to prod at it like a Clojure map.

  (defn search
    [url search-term p]
    (let [resp (http/get url {:timeout 2500 :query-params {:q search-term}})
          {:keys [body error]} @resp]
      (when (not error) (deliver p body))))
  )

(defn search
  "Queries the given search engine url with the appended search-term, and
   delivers the HTML response to ret-promise. The way the query search-term
   is included is not guaranteed to work, due to differences in APIs between
   search engines. In the event of an error, a big ol' IOException is thrown."
  [url search-term ret-promise]
  (->> (str url "?q=" (URLEncoder/encode search-term))
       (slurp)
       (deliver ret-promise)))

(defn race-search-engines
  "Runs a search on each url, and returns the html of the first engine
   to respond. The slower requests will not be preempted when the first
   completes. Will timeout after 3 seconds."
  [search-term urls]
  (let [p (promise)]
    (doseq [url urls]
      (future (search url search-term p)))
    (deref p 3000 "<html><body>Error: No results</body></html>")))

; 2. Update your function so it takes a second argument consisting of
;    the search engines to use.

(defn complete-url [url] (str "https://" url "/search"))
(def bing-and-google (map complete-url ["www.bing.com" "www.google.com"]))

(defn bingoogle-search
  "Search bing AND google, returning the html from whichever was fastest."
  [query]
  (race-search-engines query bing-and-google))

;; Exercise 1 & 2 test -- is network-dependent
(comment
  (bingoogle-search "Rich Hickey"))

; 3. Create a new function that takes a search term and search engines
;    as arguments, and returns a vector of the URLs from the first page
;    of search results from each search engine.

(def link-regex #"<a [^>]*href=\"(http://[^\"]+)\"[^>]*>")
(defn scrape-links
  [results]
  (map second (re-seq link-regex results)))

(defn multi-search
  "Queries for search-term at each search engine specified in urls.
   Each request will timeout in 1 second.
   Results are deduplicated and returned as a vector."
  [search-term urls]
  (->
   (mapcat (fn query-search-engine [url p]
          (future (search url search-term p))
          (scrape-links (deref p 1000 "")))
        urls (repeatedly promise))
   distinct
   vec))

;; Test for Exercise 3
;; If I kept http-kit as a require, it has a testing helper
;; that removes the network connectivity requirement.
;; Hopefully I'll remember this fact later.
(comment
  (def the-big-three (conj bing-and-google (complete-url "search.yahoo.com")))

  (clojure.pprint/pprint
   (map #(multi-search % the-big-three)
        ["is google evil" "fire tornados" "gandalf's wife"])
   ))
