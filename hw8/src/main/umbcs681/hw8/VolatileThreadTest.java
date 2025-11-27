package umbcs681.hw8;

public class VolatileThreadTest {
    
    public static void main(String[] args) {
        testPrimeGeneratorWithVolatile();
        System.out.println();
        testPrimeFactorizerWithVolatile();
        System.out.println();
        testMultipleCancellations();
    }
    
    private static void testPrimeGeneratorWithVolatile() {
        System.out.println("Test 1: Prime Generator");
        
        RunnableCancellablePrimeGenerator gen = new RunnableCancellablePrimeGenerator();
        Thread thread = new Thread(gen);
        thread.start();
        
        try {
            Thread.sleep(50);
            gen.setDone();
            thread.join(1000);
            System.out.println("Terminated: " + !thread.isAlive());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        RunnableCancellablePrimeGenerator gen2 = new RunnableCancellablePrimeGenerator();
        Thread thread2 = new Thread(gen2);
        thread2.start();
        
        try {
            thread2.join();
            System.out.println("Generated " + gen2.getPrimes().size() + " primes");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    private static void testPrimeFactorizerWithVolatile() {
        System.out.println("Test 2: Prime Factorizer");
        
        long num = 999999999L;
        RunnableCancellablePrimeFactorizer factorizer = new RunnableCancellablePrimeFactorizer(num);
        Thread thread = new Thread(factorizer);
        thread.start();
        
        try {
            Thread.sleep(10);
            factorizer.setDone();
            thread.join(1000);
            System.out.println("Terminated: " + !thread.isAlive());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        RunnableCancellablePrimeFactorizer factorizer2 = new RunnableCancellablePrimeFactorizer(84L);
        Thread thread2 = new Thread(factorizer2);
        thread2.start();
        
        try {
            thread2.join();
            System.out.println("Factors of 84: " + factorizer2.getPrimeFactors());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    private static void testMultipleCancellations() {
        System.out.println("Test 3: Multiple Threads");
        
        int n = 5;
        RunnableCancellablePrimeGenerator[] gens = new RunnableCancellablePrimeGenerator[n];
        Thread[] threads = new Thread[n];
        
        for (int i = 0; i < n; i++) {
            gens[i] = new RunnableCancellablePrimeGenerator();
            threads[i] = new Thread(gens[i]);
            threads[i].start();
        }
        
        try {
            Thread.sleep(10);
            for (int i = 0; i < n; i++) {
                gens[i].setDone();
            }
            
            int count = 0;
            for (int i = 0; i < n; i++) {
                threads[i].join(1000);
                if (!threads[i].isAlive()) count++;
            }
            System.out.println(count + "/" + n + " threads terminated");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
