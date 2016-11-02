(ns funding_circle.prime-algorithms
  (:require [clojure.data.priority-map :refer [priority-map]]))

;; Consistent with Clojure philosophy of leveraging the rich
;; academic literature, and as someone with a background in cognitive
;; science for which the only real unit of consumption is the paper,
;; many of these algorithms are based on the excellent paper by
;; Melissa E. O'Neil called The Genuine Sieve of Eratosthenes, which
;; includes several Haskell algorithms that in most cases port beautifully 
;; to Clojure and which show that the Sieve of Eratosthenes can be implemented in 
;; a lazy, functional style without (too much) loss of performance.



(def brute-force
  "Brute force prime generation algorithm works by testing if
   the nth number has no lesser numbers that divide it.
  
   Time complexity is O(n^2)."
  (cons 2 (filter (fn [n]
                    (loop [i 2]
                      (if (= i n) n
                          (when (pos? (mod n i))
                            (recur (inc i))))))
                  (iterate inc 3))))

(defn eager-trial-division
  "This is the best algorithm I came up with on my own. Turns 
   out it what is called a trial division algorithm. It works by testing 
   whether the nth number can be written as a composite of previous 
   primes. Eagerly builds up a vector of n primes.

  Time complexity is O( n^2 / log(n^2) )."
  ([n]
   (loop [prev [2] i 1]
     (if (== i n) prev
       (recur (->> (iterate inc (peek prev))
                   (filter (fn [n] (not-any? zero? (map #(rem n %) prev))))
                   first
                   (conj prev))
              (inc i))))))

(def lazy-trial-division
  "The above is actually equivalent to the common trial division algorithm
   that is often mistaken for the Sieve of Eratosthenes algorithm. It is 
   expressed in the paper as the haskell equivalent of the following elegant
   but slow algorithm. Unfortunately the stack blows for large n with this
   algorithm.

   Time complexity is O( n^2 / log(n)^2 )."
  (letfn [(step [[prime & xs]]
            (lazy-seq (cons prime (filter #(pos? (rem % prime)) (step xs)))))]
    (step (iterate inc 2))))


(def lazy-trial-division-xf
  "An attempt to implement the lazy trial division algorithm above using 
   transducers in the hopes of eliminating the overhead of realizing 
   intermediate filter results. Turns out this is actually incredibly slower."
  (letfn [(step [n f]
            (let [[p n] (sequence f (iterate inc n))]
              (lazy-seq (cons p (step n (comp f (filter #(pos? (mod % p)))))))))]
    (cons 2 (step 3 (filter #(pos? (mod % 2)))))))


(def optimized-trial-division
  "By noticing that any x > sqrt(n) cannot divide n, we can achieve a substantial
   practical speedup of the lazy trial division algorithm. This algorithm is the
   same as above, except it only takes primes from previous primes that are
   less than the square root of the nth prime. Note that lazy sequences seem to
   cache when bound to a root var, so we don't recompute primes on recursive calls.
   This is in fact the optimal trial division algorithm (asymptotically speaking).

   Time complexity is O( n*sqrt(n) / log(n)^2 )."
  (lazy-seq
    (letfn [(candidate-factors [x]
              (take-while (fn [p] (<= (* p p) x)) optimized-trial-division))
            (prime? [x]
              (not-any? zero? (map #(mod x %) (candidate-factors x))))]
      (cons 2 (filter prime? (iterate inc 3))))))


(defn segmented-sieve
  "Here is an implementation of the segmented sieve algorithm 
   described at: https://en.wikipedia.org/wiki/Sieve_of_Eratosthenes
   with the segment size equal to square root of n.
   This algorithm produces all primes <= n. Unfortunately,
   there doesn't seem to be an elegant way to produce the first
   n primes with this algoirhtm. One would want to estimate the
   number of primes, P(n), as a function of n. The prime number theorem
   could work here (P(n) ~ nlog(n)), but the erorr is large for small n, making
   it tricky to described a good closed formula for (over) estimating P(n).

   The advantage of the segmented Sieve Algorithm over tranditional Seive is 
   that it reduces the space complexity. It works by first computing the first 
   primes < sqrt(n) with a typical sieve algorithm. The Sieve algorithm, by the way,
   works by 'crossing off' all multiples of n greater than n for all n from 2 to sqrt(n).
   Everything not crossed off is then a prime. After initial sieving, segmented sieve 
   adds all those primes in x > sqrt(n) + 1 in sqrt(n) sized chucks which are not 
   divisible by any of the computed primes. Note that for any product, x * y = n,
   max(x, y) <= sqrt(n), so any composite less than n is divisible by some 
   number, x <= sqrt(n); this is why we only neeed to sieve primes up to sqrt(n).

   You will notice that segmented sieve does not translate very idomatically to Clojure.
   Further, this algorithm uses seqs, which are amazing slow compared to 
   loop-based iterations. This is included merely for completeness as an example of
   the Segmented Sieve algorithm. If you wanted to really go down this road, you would
   be best off interoping with a third-party java library.

   Time complexity is O( nloglogn )."
  ([n]
   (letfn [(sieve [n]
             (let [a (boolean-array n true)]
               (aset a 0 false)
               (aset a 1 false)
               (doseq [i (range 2 (Math/sqrt n))
                       :when (aget a i)
                       j (iterate (partial + i) (* i i))
                       :while (< j n)]
                 (aset a j false))
               (loop [ret (transient []) i 0]
                 (if (== i n) (persistent! ret)
                     (recur (if (aget a i) (conj! ret i) ret)
                            (inc i))))))
           (segment [n]
             (let [segment-size (.intValue (Math/ceil (Math/sqrt n)))
                   primes (sieve segment-size)
                   segments (partition segment-size segment-size []
                                       (range (inc segment-size) n))]
               (into primes
                     (for [segment segments
                           n segment
                           :when (not-any? zero? (map #(mod n %) primes))]
                       n))))]
     (segment n))))


(deftype Cell [value]
  clojure.lang.IDeref
  (deref [this] value))


(defn incremental-sieve
  "This is a lazy, incremental version of the Sieve of Eratosthenes 
   algorithm implemented using a priority map. This incredibly beautiful 
   algorithm works by storing lazy sequences of the multiples of each 
   prime, with priority equal to the next multiple in the sequence. Thus, 
   the priority map--viewed as a priority queue--maintains the property 
   that the priority of the first item in the queue is always the next 
   composite number. Thus a number, x, less than the next composite must 
   be prime, otherwise queue property would be violated. 

   The algorithm ports quite beautifully to Clojure, except for the unfortunate
   fact that lazy-sequences are evaluated when associated into the 
   priority map provided in Clojure.data. We hack this temporarily by wrapping
   the sequence in a cell to prevent eager evaluation.

   Time complexity is O( nloglogn ). Note that for a typical sieve, n
   referes to the number of primes less than n, but finding the nth prime is
   asymptotically the same, since log(n*log(n)) = log(n) + log(log(n)); the extra
   logarithms drops out."
  ([[x & xs]]
   (letfn [(insert-prime [prime xs table]
             (assoc table (Cell. (map (partial * prime) xs)) (* prime prime)))
           (sieve [[x & xs] table]
             (letfn [(adjust [table]
                       (let [[cell y] (peek table)
                             [y* & ys] @cell]
                         (if (<= y x)
                           (recur (assoc (pop table) (Cell. ys) y*))
                           table)))]
               (let [next-composite (second (peek table))]
                 (if (<= next-composite x) (recur xs (adjust table))
                     (lazy-seq (cons x (sieve xs (insert-prime x xs table))))))))]
     (lazy-seq (cons x (sieve xs (insert-prime x xs (priority-map))))))))


;; A side-bennifit of this approach is that we can now quite easily improve
;; performance by adding wheel factoriaion without needing to change
;; the sieve algorithm at all. Wheel factorization is a natural
;; extension of the intuitive idea that we can avoid multiples of 2 by
;; starting at 3 and incrementally adding 2 so that we never even touch
;; the even numbers (since clearly none of them are prime). It is a bit tedious to
;; describe, but the idea is to imagine creating a wheel with a spoke
;; for every number between 1 and the product of the first n base primes.
;; We then eliminate spokes as follows: first, eliminate spokes 
;; corresponding to the base primes, then eliminate spokes that are
;; multiples of the base primes. Now, any numbers
;; that lie on the spokes that remain are not multiples of the base primes.
;; In this picture, a "spoke" corresponds to numbers in  xn + n, xn + 2n, ...
;; Algorithmically, we implement this by describing the nth spoke as some
;; number plus the previous spoke, so that ith number generated by the wheel
;; is equal to the sum of all the previous numbers encountered. 
;; For example, given a wheel with base primes 2 and 3, we see that 5 and 1 
;; are the remaining spokes. Starting at 5, we add 2 then 4 alternatively to 
;; produce all numbers that lie on those spokes. Here are the first 4 wheel 
;; factorizations (notice that the wheel circumference increases exponentially
;; in the number of base primes):

(def wheel2 (lazy-seq (cons 2 wheel2)))


(def wheel23 (lazy-seq (cons 2 (cons 4 wheel23))))


(def wheel235
  (lazy-seq (cons 4 (cons 2 (cons 4 (cons 2 (cons 4 (cons 6 (cons 2 (cons 6 wheel235))))))))))


(def wheel2357 (cycle (list 2 4 2 4 6 2 6 4 2 4 6 6 2 6
                            4 2 6 4 6 8 4 2 4 2 4 8 6 4
                            6 2 4 6 2 6 6 4 2 4 6 2 6 4
                            2 4 2 10 2 10)))


(defn spin
  "Takes a wheel and a starting prime and produces and infinite, 
   increasing lazy sequence of numbers equivalent to the diference between
   Z+ and multiples of the wheel's base primes."
  [[x & wheel] n] (lazy-seq (cons n (spin wheel (+ n x)))))


(def wheel2-primes (lazy-seq (list* 2 (incremental-sieve (spin wheel2 3)))))


(def wheel23-primes (lazy-seq (list* 2 3 (incremental-sieve (spin wheel23 5)))))


(def wheel235-primes (lazy-seq (list* 2 3 5 (incremental-sieve (spin wheel235 7)))))


(def wheel2357-primes (lazy-seq (list* 2 3 5 7 (incremental-sieve (spin wheel2357 11)))))
