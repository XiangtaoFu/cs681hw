package umbcs681.hw8;

public class RunnablePrimeGenerator extends PrimeGenerator implements Runnable {
    
    @Override
    public void run() {
        generatePrimes();
    }
    
    @Override
    public void generatePrimes() {
        long candidate = 2;
        while (candidate <= 100000) {
            if (isPrime(candidate)) {
                primes.add(candidate);
            }
            candidate++;
        }
    }
}
