package umbcs681.hw9;

import java.util.List;
import java.util.ArrayList;

public abstract class PrimeFactorizer {
    protected long number;
    protected List<Long> primeFactors;
    
    public PrimeFactorizer(long number) {
        this.number = number;
        this.primeFactors = new ArrayList<>();
    }
    
    public abstract void generatePrimeFactors();
    
    public List<Long> getPrimeFactors() {
        return new ArrayList<>(primeFactors);
    }
    
    public long getNumber() {
        return number;
    }
}
