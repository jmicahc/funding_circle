(ns funding_circle.core
  (:import clojure.lang.MapEntry java.util.Map clojure.lang.PersistentTreeMap)
  (:require [clojure.data.priority-map :refer [priority-map]]
            [clojure.pprint :refer [print-table]]
            [funding_circle.prime-algorithms :as algs])
  (:gen-class))


(defn infinite-table
  "Creates an infinite, lazy matrix for which
   the (i,j)th entry is given by (f i j). rows/columns
   are given by xs/ys, m/n = start-row/start-column, 
   s/t = end-row/end-column."
  ([f] (infinite-table (range) (range) f))
  ([xs f] (infinite-table xs xs f))
  ([xs ys f] (map (fn [x] (map (fn [y] (f x y)) ys)) xs))
  ([xs ys f m n]
   (let [mat (infinite-table xs ys f)]
     (lazy-seq (drop m (map #(drop n %) mat)))))
  ([xs ys f m n s t]
   (let [mat (infinite-table xs ys f m n)]
     (take s (map #(take t %) mat)))))



(defn print-primes [primes start n]
  "Prints a table representing the cartessian product
   of primes from start to start + n."
  (let [table (infinite-table primes primes * start start n n)]
    (println "Multiplication table of prime numbers\n")
    (print "       ")
    (doseq [prime (take n (drop start primes))]
      (print (format "% 7d" prime)))
    (println)
    (doseq [i (range (inc n))]
      (print "_______"))
    (doseq [[row prime] (map list table (take n (drop start primes)))]
      (print "\n" (format "% 4d" prime) "|")
      (doseq [item row]
        (print (format "% 7d" item))))
    (println)))


(defn -main
  "Program entry point. Print product table of primes 1 to 10 
   using a lazy Sieve Of Eratosthenes algorithm with a 2/3/5/7
   factorization wheel."
  [& args]
  (print-primes algs/wheel2357-primes 0 10))
