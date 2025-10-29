package umbcs681.hw6;

public class ThreadSafetyTest {
    
    public static void main(String[] args) {
        System.out.println("=== HW6-1: RunnableCancellablePrimeGenerator Test ===");
        testCancellablePrimeGenerator();
        
        System.out.println("\n=== HW6-2: RunnableCancellablePrimeFactorizer Test ===");
        testCancellablePrimeFactorizer();
    }
    
    private static void testCancellablePrimeGenerator() {
        RunnableCancellablePrimeGenerator generator = new RunnableCancellablePrimeGenerator();
        Thread thread = new Thread(generator);
        
        // Start the thread
        thread.start();
        
        // Let it run for a short time, then cancel
        try {
            Thread.sleep(100); // Let it run for 100ms
            generator.setDone(); // Cancel the operation
            thread.join(); // Wait for thread to finish
            
            System.out.println("Prime generation cancelled. Found " + generator.getPrimes().size() + " primes:");
            System.out.println("Primes: " + generator.getPrimes());
            System.out.println("Is done: " + generator.isDone());
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    private static void testCancellablePrimeFactorizer() {
        long testNumber = 1234567890L; // A large number for factorization
        RunnableCancellablePrimeFactorizer factorizer = new RunnableCancellablePrimeFactorizer(testNumber);
        Thread thread = new Thread(factorizer);
        
        // Start the thread
        thread.start();
        
        // Let it run for a short time, then cancel
        try {
            Thread.sleep(50); // Let it run for 50ms
            factorizer.setDone(); // Cancel the operation
            thread.join(); // Wait for thread to finish
            
            System.out.println("Prime factorization of " + testNumber + " cancelled.");
            System.out.println("Found " + factorizer.getPrimeFactors().size() + " prime factors:");
            System.out.println("Prime factors: " + factorizer.getPrimeFactors());
            System.out.println("Is done: " + factorizer.isDone());
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
