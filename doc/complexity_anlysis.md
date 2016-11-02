It is well known that that the time complexity of the Sieve of Eratosthenes algorithm is O(nloglogn). We consider how this result is arrived at and how it compares to less optimal algorithms, such as the standard trial division as well as the optimized trial division algorithm that are often mistaken for the true Sieve of Eratosthenes algorithm. 

## Geuinine Sieve of Eratosthenes

To get a series for the time complexity of the Sieve of Eratosthenes, we use the prime theorem to construct a range over which to count. It tells us that a number, x, has about pi(x) = x/ ln(x) primes before it, and the i^th prime, p_i is equal to about i*ln(i). Thus, the total number of primes encountered is equal to the sum from i=1 to pi(sqrt(n)). Then, we notice that for every prime weencounter, there are n/p "crossings off" that must be made, which gives us our series:

  sum(i=1, pi(sqrt(n)), n/p_i).

which, by integral solution magic, is shown to be approximately equal to n*lnln(n) + O(n). Remember that this bound applies to generating all the primes below a given number, n, not the first n primes.   

## Trial Division