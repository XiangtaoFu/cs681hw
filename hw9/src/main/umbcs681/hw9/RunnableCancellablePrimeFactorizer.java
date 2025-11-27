package umbcs681.hw9;

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
    
    public boolean isDone() {
        return done;
    }
    
    @Override
    public void generatePrimeFactors() {
        long n = number;
        long divisor = 2;
        
        while (n > 1 && divisor <= n) {
            if (done) {
                System.out.println("Stopped...");
                primeFactors.clear();
                break;
            }
            if (n % divisor == 0) {
                primeFactors.add(divisor);
                n /= divisor;
            } else {
                divisor++;
            }
            
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                continue;
            }
        }
    }
    
    @Override
    public List<Long> getPrimeFactors() {
        return new ArrayList<>(primeFactors);
    }
}
