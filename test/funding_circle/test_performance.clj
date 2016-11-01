(ns funding_circle.test-performance
  (:require [funding_circle.prime-algorithms :as algs]))

defn test-perf [n]
(time (vec (take n algs/brute-force)))
(time (vec (algs/eager-trial-division n)))
(if (<= n 1000)
  (time (vec (take n algs/lazy-trial-division)))
  (println "n/a"))
#_(time (vec (take n) algs/lazy-trial-division-xf))
(time (vec (take n algs/optimized-trial-division)))
(time (vec (algs/segmented-sieve 547)))

(nth algs/wheel2357-primes 100)
;; Algorithm | brute-force | eager TD | optimized TD |  Segmented Sieve | wheel2357 Sieve | # primes
;; time (ms) |   0.052281  | 2.018853 |   0.00433    |     0.006848     |    0.006669     | 10
;;           |   0.045831  | 2.018853 |   0.00441    |     2.700833     |    0.003956     | 100
;;           |   1065.666  | 292.2991 |   33.0679    |     55.74185     |    25.25332     | 1000
;;           |   121592.5  | 22592.33 |   719.024    |     1101.805     |    552.2367     | 10000   

(comment 
  n = 10
  "Elapsed time: 0.052281 msecs"
  "Elapsed time: 2.018853 msecs"
  "Elapsed time: 0.008612 msecs"
  "Elapsed time: 0.00433 msecs"
  "Elapsed time: 0.006848 msecs"
  "Elapsed time: 14.320875 msecs"
  "Elapsed time: 0.009321 msecs"
  "Elapsed time: 0.016807 msecs"
  "Elapsed time: 0.009309 msecs"
  "Elapsed time: 0.006669 msecs")

(comment
  n = 100
  "Elapsed time: 0.045831 msecs"
  "Elapsed time: 11.465948 msecs"
  "Elapsed time: 0.010282 msecs"
  "Elapsed time: 0.004413 mecs"
  "Elapsed time: 0.003689 msecs"
  "Elapsed time: 10.622899 msecs"
  "Elapsed time: 0.007634 msecs"
  "Elapsed time: 0.008717 msecs"
  "Elapsed time: 0.004159 msecs"
  "Elapsed time: 0.003956 msecs")

(comment
  n = 1000
  "Elapsed time: 1065.666048 msecs"
  "Elapsed time: 292.299156 msecs"
  "Elapsed time: 281.399255 msecs"
  "Elapsed time: 3744.66926 msecs"
  "Elapsed time: 33.067946 msecs"
  "Elapsed time: 10.863569 msecs"
  "Elapsed time: 89.525804 msecs"
  "Elapsed time: 51.747521 msecs"
  "Elapsed time: 32.577526 msecs"
  "Elapsed time: 25.253315 msecs")



(test-perf 10000)
(comment
  n = 10000
  "Elapsed time: 121592.487994 msecs"
  "Elapsed time: 22592.329256 msecs"
  "n/a"
  "n/a"
  "Elapsed time: 719.024495 msecs"
  "Elapsed time: 89.874843 msecs"
  "Elapsed time: 1470.472715 msecs"
  "Elapsed time: 877.911898 msecs"
  "Elapsed time: 700.199554 msecs"
  "Elapsed time: 552.236703 msecs")
