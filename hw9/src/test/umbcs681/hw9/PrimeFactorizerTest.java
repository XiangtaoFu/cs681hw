package umbcs681.hw9;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class PrimeFactorizerTest {

    @Test
    public void testCancellationWithInterrupt() throws InterruptedException {
        RunnableCancellablePrimeFactorizer factorizer = 
            new RunnableCancellablePrimeFactorizer(999999999999L);
        Thread t = new Thread(factorizer);
        t.start();
        
        Thread.sleep(50);
        factorizer.setDone();
        t.interrupt();
        t.join(1000);
        
        assertTrue(factorizer.isDone());
        assertFalse(t.isAlive());
    }

    @Test
    public void testTwoStepTermination() throws InterruptedException {
        RunnableCancellablePrimeFactorizer factorizer = 
            new RunnableCancellablePrimeFactorizer(1000000L);
        Thread t = new Thread(factorizer);
        t.start();
        
        Thread.sleep(50);
        
        assertFalse(factorizer.isDone());
        
        factorizer.setDone();
        t.interrupt();
        t.join(1000);
        
        assertTrue(factorizer.isDone());
        assertFalse(t.isAlive());
    }

    @Test
    public void testVolatileFlag() throws InterruptedException {
        RunnableCancellablePrimeFactorizer factorizer = 
            new RunnableCancellablePrimeFactorizer(84);
        
        assertFalse(factorizer.isDone());
        factorizer.setDone();
        assertTrue(factorizer.isDone());
    }

    @Test
    public void testInterruptWakesThread() throws InterruptedException {
        RunnableCancellablePrimeFactorizer factorizer = 
            new RunnableCancellablePrimeFactorizer(1000000L);
        Thread t = new Thread(factorizer);
        t.start();
        
        Thread.sleep(50);
        long startTime = System.currentTimeMillis();
        factorizer.setDone();
        t.interrupt();
        t.join(1000);
        long elapsed = System.currentTimeMillis() - startTime;
        
        assertTrue(elapsed < 3000);
        assertFalse(t.isAlive());
    }
}
