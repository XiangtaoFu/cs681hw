package umbcs681.hw6;

public class ThreadSafetyTest {
    
    public static void main(String[] args) {
        System.out.println("RunnableCancellablePrimeGenerator Test");
        testCancellablePrimeGenerator();
        
        System.out.println("\nRunnableCancellablePrimeFactorizer Test");
        testCancellablePrimeFactorizer();
    }
    
    private static void testCancellablePrimeGenerator() {
        RunnableCancellablePrimeGenerator generator = new RunnableCancellablePrimeGenerator();
        Thread thread = new Thread(generator);
        
        thread.start();
        
        try {
            Thread.sleep(100);
            generator.setDone();
            thread.join();
            
            System.out.println("Prime generation cancelled. Found " + generator.getPrimes().size() + " primes:");
            System.out.println("Primes: " + generator.getPrimes());
            System.out.println("Is done: " + generator.isDone());
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    private static void testCancellablePrimeFactorizer() {
        long testNumber = 1234567890L;
        RunnableCancellablePrimeFactorizer factorizer = new RunnableCancellablePrimeFactorizer(testNumber);
        Thread thread = new Thread(factorizer);
        
        thread.start();
        
        try {
            Thread.sleep(50);
            factorizer.setDone();
            thread.join();
            
            System.out.println("Prime factorization of " + testNumber + " cancelled.");
            System.out.println("Found " + factorizer.getPrimeFactors().size() + " prime factors:");
            System.out.println("Prime factors: " + factorizer.getPrimeFactors());
            System.out.println("Is done: " + factorizer.isDone());
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
