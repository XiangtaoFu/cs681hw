package umbcs681.hw8;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class PrimeGeneratorTest {

    @Test
    public void testRunnablePrimeGenerator() throws InterruptedException {
        RunnablePrimeGenerator gen = new RunnablePrimeGenerator();
        Thread thread = new Thread(gen);
        thread.start();
        thread.join();
        
        List<Long> primes = gen.getPrimes();
        assertFalse(primes.isEmpty());
        assertTrue(primes.contains(2L));
        assertTrue(primes.contains(3L));
        assertTrue(primes.contains(5L));
        assertTrue(primes.size() > 1000);
    }

    @Test
    public void testRunnableCancellablePrimeGenerator() throws InterruptedException {
        RunnableCancellablePrimeGenerator gen = new RunnableCancellablePrimeGenerator();
        Thread thread = new Thread(gen);
        thread.start();
        
        Thread.sleep(10);
        
        gen.setDone();
        thread.join(1000);
        
        assertTrue(gen.isDone());
        assertFalse(thread.isAlive());
    }

    @Test
    public void testRunnableCancellablePrimeGeneratorComplete() throws InterruptedException {
        RunnableCancellablePrimeGenerator gen = new RunnableCancellablePrimeGenerator();
        Thread thread = new Thread(gen);
        thread.start();
        thread.join();
        
        List<Long> primes = gen.getPrimes();
        assertFalse(primes.isEmpty());
        assertTrue(primes.contains(2L));
        assertTrue(primes.size() > 1000);
    }

    @Test
    public void testVolatileVisibility() throws InterruptedException {
        RunnableCancellablePrimeGenerator gen = new RunnableCancellablePrimeGenerator();
        Thread thread = new Thread(gen);
        thread.start();
        
        Thread.sleep(5);
        
        gen.setDone();
        
        thread.join(1000);
        
        assertFalse(thread.isAlive());
        assertTrue(gen.isDone());
    }
}
