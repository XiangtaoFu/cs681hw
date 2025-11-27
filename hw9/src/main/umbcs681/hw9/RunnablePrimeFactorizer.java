package umbcs681.hw9;

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
        
        while (n % 2 == 0) {
            primeFactors.add(2L);
            n = n / 2;
        }
        
        for (long i = 3; i <= Math.sqrt(n); i += 2) {
            while (n % i == 0) {
                primeFactors.add(i);
                n = n / i;
            }
        }
        
        if (n > 2) {
            primeFactors.add(n);
        }
    }
}
