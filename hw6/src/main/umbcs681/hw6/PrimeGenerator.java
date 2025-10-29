package umbcs681.hw6;

import java.util.List;
import java.util.ArrayList;

public abstract class PrimeGenerator {
    protected List<Long> primes;
    
    public PrimeGenerator() {
        this.primes = new ArrayList<>();
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
    
    public abstract void generatePrimes();
    
    public List<Long> getPrimes() {
        return new ArrayList<>(primes);
    }
}