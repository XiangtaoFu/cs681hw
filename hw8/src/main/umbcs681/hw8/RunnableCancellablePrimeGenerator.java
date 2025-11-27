package umbcs681.hw8;

import java.util.List;
import java.util.ArrayList;

public class RunnableCancellablePrimeGenerator extends RunnablePrimeGenerator {
    private volatile boolean done = false;
    
    public void setDone() {
        done = true;
    }
    
    @Override
    public void generatePrimes() {
        long candidate = 2;
        while (candidate <= 100000 && !done) {
            if (isPrime(candidate)) {
                primes.add(candidate);
            }
            candidate++;
        }
        
        if (done) {
            System.out.println("Stopped generating prime numbers.");
            primes.clear();
        }
    }
    
    public boolean isDone() {
        return done;
    }
    
    @Override
    public List<Long> getPrimes() {
        return new ArrayList<>(primes);
    }
}
