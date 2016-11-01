## Prime Generation Algorithms (funding circle coding challenge)

This repository includes several algorithms for generating prime numbers, incuding a trival brute force algorithm, several trial division algorithms, and serveral genuine Sieve Of Eratosthenes algorithms. As is documented throughout the code, many of the algorithms are based Haskell implementations discussed in Melissa E. O'Neil's great paper on functional prime number generation called The Genuine Sieve of Eratosthenes. On top of naive algoirthms implemented independently, I show how beautiful, functional haskell code for generating primes translates seamlessly to Clojure.

## How to Run
- Clone this repository
- at the root, run `lein compile`, then `lein uberjar`.
- then, `java -jar target/funding_circle-standalone.jar`. You should see a table printed of the cartessian product of the first 10 primes.

## Dependencies
- clojure.data.priority-map. Leiningen will download this automatically when compiling the project.

## Notes
- See tests/funding_circle simple tests of each function.
- See tests/funding_circle/performance_tests for empirical performance tests.
