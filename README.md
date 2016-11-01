## Prime Generation Algorithms (funding circle coding challenge)

This repository includes several algorithms for generating prime numbers, incuding a trival brute force algorithm, several trial division algorithms, and serveral genuine Sieve Of Eratosthenes algorithms. As is documented throughout the code, many of the algorithms are based Haskell implementations discussed in Melissa E. O'Neil's great paper on functional prime number generation called The Genuine Sieve of Eratosthenes. On top of naive algoirthms implemented independently, I show how beautiful, functional haskell code for generating primes translates seamlessly to Clojure.

## How to Run
- Clone this repository
- at the root, run `lein compile`, then `lein uberjar`.
- then, `java -jar target/funding_circle-standalone.jar`. You should see a table printed of the cartessian product of the first 10 primes.

## Dependencies
- clojure.data.priority-map. Leiningen will download this automatically when compiling the project.

## Notes
- See test/funding_circle simple tests of each function.
- See test/funding_circle/performance_tests for empirical performance tests.

## Performane
Time to Calculate the first N primes (ms)

| # Primes  | brute-force | eager TD | optimized TD |  Segmented Sieve | wheel2357 Sieve |
| ----------| -----------:| --------:| ------------:|-----------------:|----------------:|
|   10      |   0.052281  | 2.018853 |   0.00433    |     0.006848     |    0.006669     |
|   100     |   0.045831  | 2.018853 |   0.00441    |     2.700833     |    0.003956     | 
|   1000    |   1065.666  | 292.2991 |   33.0679    |     55.74185     |    25.25332     |
|   10000   |   121592.5  | 22592.33 |   719.024    |     1101.805     |    552.2367     |
