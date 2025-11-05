package umbcs681.hw6;

public class RunnablePrimeGenerator extends PrimeGenerator implements Runnable {
    
    @Override
    public void run() {
        generatePrimes();
    }
    
    @Override
    public void generatePrimes() {
        long candidate = 2;
        while (candidate <= 1000) {
            if (isPrime(candidate)) {
                primes.add(candidate);
            }
            candidate++;
        }
    }
}