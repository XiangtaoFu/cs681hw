package umbcs681.hw6;

public class RunnablePrimeFactorizer extends PrimeFactorizer implements Runnable {
    
    public RunnablePrimeFactorizer(long number) {
        super(number);
    }
    
    @Override
    public void run() {
        generatePrimeFactors();
    }
    
    @Override
    public void generatePrimeFactors() {
        long n = number;
        long factor = 2;
        
        while (factor * factor <= n) {
            if (n % factor == 0) {
                primeFactors.add(factor);
                n /= factor;
            } else {
                factor++;
            }
        }
        
        if (n > 1) {
            primeFactors.add(n);
        }
    }
}