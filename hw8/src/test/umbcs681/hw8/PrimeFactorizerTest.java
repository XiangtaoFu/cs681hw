package umbcs681.hw8;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class PrimeFactorizerTest {

    @Test
    public void testRunnablePrimeFactorizer() throws InterruptedException {
        RunnablePrimeFactorizer factorizer = new RunnablePrimeFactorizer(84);
        Thread thread = new Thread(factorizer);
        thread.start();
        thread.join();
        
        List<Long> factors = factorizer.getPrimeFactors();
        assertEquals(4, factors.size());
        assertTrue(factors.contains(2L));
        assertTrue(factors.contains(3L));
        assertTrue(factors.contains(7L));
    }

    @Test
    public void testRunnablePrimeFactorizerLargeNumber() throws InterruptedException {
        RunnablePrimeFactorizer factorizer = new RunnablePrimeFactorizer(1024);
        Thread thread = new Thread(factorizer);
        thread.start();
        thread.join();
        
        List<Long> factors = factorizer.getPrimeFactors();
        assertEquals(10, factors.size());
        assertTrue(factors.stream().allMatch(f -> f == 2L));
    }

    @Test
    public void testRunnableCancellablePrimeFactorizer() throws InterruptedException {
        RunnableCancellablePrimeFactorizer factorizer = 
            new RunnableCancellablePrimeFactorizer(2305843009213693951L);
        Thread thread = new Thread(factorizer);
        thread.start();
        
        Thread.sleep(50);
        
        factorizer.setDone();
        thread.join(1000);
        
        assertTrue(factorizer.isDone());
        assertFalse(thread.isAlive());
    }

    @Test
    public void testRunnableCancellablePrimeFactorizerComplete() throws InterruptedException {
        RunnableCancellablePrimeFactorizer factorizer = 
            new RunnableCancellablePrimeFactorizer(84);
        Thread thread = new Thread(factorizer);
        thread.start();
        thread.join();
        
        List<Long> factors = factorizer.getPrimeFactors();
        assertFalse(factors.isEmpty());
        assertEquals(4, factors.size());
    }

    @Test
    public void testVolatileVisibility() throws InterruptedException {
        RunnableCancellablePrimeFactorizer factorizer = 
            new RunnableCancellablePrimeFactorizer(999999999);
        Thread thread = new Thread(factorizer);
        thread.start();
        
        Thread.sleep(10);
        
        factorizer.setDone();
        
        thread.join(1000);
        
        assertFalse(thread.isAlive());
        assertTrue(factorizer.isDone());
    }

    @Test
    public void testPrimeNumber() throws InterruptedException {
        RunnablePrimeFactorizer factorizer = new RunnablePrimeFactorizer(17);
        Thread thread = new Thread(factorizer);
        thread.start();
        thread.join();
        
        List<Long> factors = factorizer.getPrimeFactors();
        assertEquals(1, factors.size());
        assertEquals(17L, factors.get(0));
    }
}
