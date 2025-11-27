package umbcs681.hw8;

import java.util.List;
import java.util.ArrayList;

public class RunnableCancellablePrimeFactorizer extends RunnablePrimeFactorizer {
    private volatile boolean done = false;
    
    public RunnableCancellablePrimeFactorizer(long number) {
        super(number);
    }
    
    public void setDone() {
        done = true;
    }
    
    @Override
    public void generatePrimeFactors() {
        long n = number;
        
        while (n % 2 == 0 && !done) {
            primeFactors.add(2L);
            n = n / 2;
        }
        
        for (long i = 3; i <= Math.sqrt(n) && !done; i += 2) {
            while (n % i == 0 && !done) {
                primeFactors.add(i);
                n = n / i;
            }
        }
        
        if (n > 2 && !done) {
            primeFactors.add(n);
        }
        
        if (done) {
            System.out.println("Stopped factorizing " + number);
            primeFactors.clear();
        }
    }
    
    public boolean isDone() {
        return done;
    }
    
    @Override
    public List<Long> getPrimeFactors() {
        return new ArrayList<>(primeFactors);
    }
}
