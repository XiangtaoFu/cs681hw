package umbcs681.hw8;

import java.util.List;
import java.util.ArrayList;

public abstract class PrimeFactorizer {
    protected long number;
    protected List<Long> primeFactors;
    
    public PrimeFactorizer(long number) {
        this.number = number;
        this.primeFactors = new ArrayList<>();
    }
    
    protected boolean isPrime(long n) {
        if (n <= 1) return false;
        if (n <= 3) return true;
        if (n % 2 == 0 || n % 3 == 0) return false;
        
        for (long i = 5; i * i <= n; i += 6) {
            if (n % i == 0 || n % (i + 2) == 0) {
                return false;
            }
        }
        return true;
    }
    
    public abstract void generatePrimeFactors();
    
    public List<Long> getPrimeFactors() {
        return new ArrayList<>(primeFactors);
    }
    
    public long getNumber() {
        return number;
    }
}
