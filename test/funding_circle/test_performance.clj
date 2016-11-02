(ns funding_circle.test-performance
  (:require [funding_circle.prime-algorithms :as algs]))

(defn test-perf [n]
  (time (vec (take n algs/brute-force)))
  (time (vec (algs/eager-trial-division n)))
  (if (<= n 1000)
    (time (vec (take n algs/lazy-trial-division)))
    (println "n/a"))
  (time (vec (take n algs/lazy-trial-division-xf)))
  (time (vec (take n algs/optimized-trial-division)))
  (time (vec (algs/segmented-sieve 51)))
  (time (vec (take n algs/wheel2-primes)))
  (time (vec (take n algs/wheel23-primes)))
  (time (vec (take n algs/wheel235-primes)))
  (time (vec (take n algs/wheel2357-primes)))
  nil)

;; Time to Calculate the first N primes (ms)

;; # Primes  | brute-force | eager TD | optimized TD |  Segmented Sieve | wheel2357 Sieve |
;; ----------|:----------- | --------:| ------------:|-----------------:|----------------:|
;;   10      |   0.052281  | 2.018853 |   0.00433    |     0.006848     |    0.006669     |
;;   100     |   0.045831  | 2.018853 |   0.00441    |     2.700833     |    0.003956     | 
;;   1000    |   1065.666  | 292.2991 |   33.0679    |     55.74185     |    25.25332     |
;;   10000   |   121592.5  | 22592.33 |   719.024    |     1101.805     |    552.2367     |

#_(test-perf 10)
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

#_(test-perf 100)
(comment
  n = 100
  "Elapsed time: 11.642368 msecs"
  "Elapsed time: 10.709341 msecs"
  "Elapsed time: 72.783503 msecs"
  "Elapsed time: 80.616424 msecs"
  "Elapsed time: 6.116903 msecs"
  "Elapsed time: 3.638701 msecs"
  "Elapsed time: 21.179786 msecs"
  "Elapsed time: 5.905872 msecs"
  "Elapsed time: 17.319495 msecs"
  "Elapsed time: 3.219721 msecs")

#_(test-perf 1000)
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



#_(test-perf 10000)
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
