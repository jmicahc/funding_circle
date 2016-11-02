## Prime Generation Algorithms (funding circle coding challenge)

This repository includes several algorithms for generating prime numbers, incuding a trival brute force algorithm, several trial division algorithms, and serveral genuine Sieve Of Eratosthenes algorithms. Consistent with the Clojure philosophy of leveraging the rich academic literature--and as someone with a cognitive science background where the ONLY unit of consuption is the paper--many of the algorithms are based on Haskell implementations discussed in Melissa E. O'Neil's great paper on functional prime number generation called The Genuine Sieve of Eratosthenes. On top of naive algoirthms implemented independently, I show how beautiful, functional haskell code for generating primes translates seamlessly to Clojure.


## How to Run
- Clone this repository
- Make sure leiningen version `2.7.1` or greater is installed (any version > 2.0 will probably work too).
- At the root, run `lein compile`, then `lein uberjar`
- Then, `target/funding_circle-0.1.0-SNAPSHOT-standalone.jar`. You should see a table printed of the product of the first 10 primes.


## Dependencies
- Clojure version 1.7+
- Leiningen version 2.0+
- clojure.data.priority-map. Leiningen will download this automatically when compiling the project.


## Performance
Time to Calculate the first N primes (ms)

| # Primes  | brute-force | eager TD | optimized TD |  Segmented Sieve | wheel2357 Sieve |
| ----------| -----------:| --------:| ------------:|-----------------:|----------------:|
|   10      |   0.052281  | 2.018853 |   0.00433    |     0.006848     |    0.006669     |
|   100     |   11.64237  | 10.70934 |   6.11690    |     3.638701     |    3.219721     | 
|   1000    |   1065.666  | 292.2991 |   33.0679    |     55.74185     |    25.25332     |
|   10000   |   121592.5  | 22592.33 |   719.024    |     1101.805     |    552.2367     |

These results are consistent with theoretical predictions. See doc/complexity_analysis.md for more details.


## Notes
- See test/funding_circle for simple tests of each function
- See test/funding_circle/performance_tests for empirical performance tests (summary below)
- See inline comments for more explainations.