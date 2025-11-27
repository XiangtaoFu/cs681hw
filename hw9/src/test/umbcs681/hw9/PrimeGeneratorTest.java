package umbcs681.hw9;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class PrimeGeneratorTest {

    @Test
    public void testCancellationWithInterrupt() throws InterruptedException {
        RunnableCancellablePrimeGenerator gen = new RunnableCancellablePrimeGenerator();
        Thread t = new Thread(gen);
        t.start();
        
        Thread.sleep(50);
        gen.setDone();
        t.interrupt();
        t.join(1000);
        
        assertTrue(gen.isDone());
        assertFalse(t.isAlive());
    }

    @Test
    public void testTwoStepTermination() throws InterruptedException {
        RunnableCancellablePrimeGenerator gen = new RunnableCancellablePrimeGenerator();
        Thread t = new Thread(gen);
        t.start();
        
        Thread.sleep(50);
        
        assertFalse(gen.isDone());
        
        gen.setDone();
        t.interrupt();
        t.join(1000);
        
        assertTrue(gen.isDone());
        assertFalse(t.isAlive());
    }

    @Test
    public void testVolatileFlag() throws InterruptedException {
        RunnableCancellablePrimeGenerator gen = new RunnableCancellablePrimeGenerator();
        Thread t = new Thread(gen);
        
        assertFalse(gen.isDone());
        gen.setDone();
        assertTrue(gen.isDone());
    }

    @Test
    public void testInterruptWakesThread() throws InterruptedException {
        RunnableCancellablePrimeGenerator gen = new RunnableCancellablePrimeGenerator();
        Thread t = new Thread(gen);
        t.start();
        
        Thread.sleep(50);
        long startTime = System.currentTimeMillis();
        gen.setDone();
        t.interrupt();
        t.join(1000);
        long elapsed = System.currentTimeMillis() - startTime;
        
        assertTrue(elapsed < 3000);
        assertFalse(t.isAlive());
    }
}
