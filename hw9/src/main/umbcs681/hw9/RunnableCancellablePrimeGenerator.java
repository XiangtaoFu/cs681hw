package umbcs681.hw9;

import java.util.List;
import java.util.ArrayList;

public class RunnableCancellablePrimeGenerator extends RunnablePrimeGenerator {
    private volatile boolean done = false;
    
    public void setDone() {
        done = true;
    }
    
    public boolean isDone() {
        return done;
    }
    
    @Override
    public void generatePrimes() {
        long candidate = 2;
        while (candidate <= 100000) {
            if (done) {
                System.out.println("Stopped...");
                primes.clear();
                break;
            }
            if (isPrime(candidate)) {
                primes.add(candidate);
            }
            candidate++;
            
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                continue;
            }
        }
    }
    
    @Override
    public List<Long> getPrimes() {
        return new ArrayList<>(primes);
    }
}
