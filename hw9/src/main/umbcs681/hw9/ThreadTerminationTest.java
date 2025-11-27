package umbcs681.hw9;

import java.util.List;

public class ThreadTerminationTest {
    
    public static void main(String[] args) {
        testPrimeGenerator();
        testPrimeFactorizer();
    }
    
    private static void testPrimeGenerator() {
        RunnableCancellablePrimeGenerator gen = new RunnableCancellablePrimeGenerator();
        Thread t = new Thread(gen);
        t.start();
        
        try {
            Thread.sleep(100);
            gen.setDone();
            t.interrupt();
            t.join(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    private static void testPrimeFactorizer() {
        RunnableCancellablePrimeFactorizer factorizer = 
            new RunnableCancellablePrimeFactorizer(84L);
        Thread t = new Thread(factorizer);
        t.start();
        
        try {
            Thread.sleep(100);
            factorizer.setDone();
            t.interrupt();
            t.join(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
