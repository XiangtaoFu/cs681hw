package umbcs681.hw6;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class PrimeFactorizerTest {
    
    @Test
    public void testRunnablePrimeFactorizer() throws InterruptedException {
        long number = 84;
        RunnablePrimeFactorizer factorizer = new RunnablePrimeFactorizer(number);
        Thread thread = new Thread(factorizer);
        thread.start();
        thread.join();
        
        List<Long> factors = factorizer.getPrimeFactors();
        assertNotNull(factors);
        assertTrue(factors.size() > 0);
        long product = 1;
        for (Long factor : factors) {
            product *= factor;
        }
        assertEquals(number, product);
        assertEquals(4, factors.size());
    }
    
    @Test
    public void testPrimeNumberFactorization() throws InterruptedException {
        long number = 17;
        RunnablePrimeFactorizer factorizer = new RunnablePrimeFactorizer(number);
        Thread thread = new Thread(factorizer);
        thread.start();
        thread.join();
        
        List<Long> factors = factorizer.getPrimeFactors();
        assertEquals(1, factors.size());
        assertEquals(17L, factors.get(0));
    }
    
    @Test
    public void testLargeNumberFactorization() throws InterruptedException {
        long number = 1000;
        RunnablePrimeFactorizer factorizer = new RunnablePrimeFactorizer(number);
        Thread thread = new Thread(factorizer);
        thread.start();
        thread.join();
        
        List<Long> factors = factorizer.getPrimeFactors();
        assertNotNull(factors);
        long product = 1;
        for (Long factor : factors) {
            product *= factor;
        }
        assertEquals(number, product);
    }
    
    @Test
    public void testCancellablePrimeFactorizer() throws InterruptedException {
        long number = 123456789L;
        RunnableCancellablePrimeFactorizer factorizer = new RunnableCancellablePrimeFactorizer(number);
        Thread thread = new Thread(factorizer);
        thread.start();
        Thread.sleep(1);
        factorizer.setDone();
        thread.join();
        assertTrue(factorizer.isDone());
    }
    
    @Test
    public void testCancellablePrimeFactorizerCompleteRun() throws InterruptedException {
        long number = 60;
        RunnableCancellablePrimeFactorizer factorizer = new RunnableCancellablePrimeFactorizer(number);
        Thread thread = new Thread(factorizer);
        thread.start();
        thread.join();
        
        List<Long> factors = factorizer.getPrimeFactors();
        assertNotNull(factors);
        long product = 1;
        for (Long factor : factors) {
            product *= factor;
        }
        assertEquals(number, product);
        assertEquals(4, factors.size());
    }
    
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    public void testFactorizerDoesNotHang() throws InterruptedException {
        RunnablePrimeFactorizer factorizer = new RunnablePrimeFactorizer(100);
        Thread thread = new Thread(factorizer);
        thread.start();
        thread.join();
        
        assertTrue(factorizer.getPrimeFactors().size() > 0);
    }
    
    @Test
    public void testMultipleFactorizersSimultaneously() throws InterruptedException {
        long number = 120;
        RunnablePrimeFactorizer fact1 = new RunnablePrimeFactorizer(number);
        RunnablePrimeFactorizer fact2 = new RunnablePrimeFactorizer(number);
        
        Thread thread1 = new Thread(fact1);
        Thread thread2 = new Thread(fact2);
        
        thread1.start();
        thread2.start();
        
        thread1.join();
        thread2.join();
        
        List<Long> factors1 = fact1.getPrimeFactors();
        List<Long> factors2 = fact2.getPrimeFactors();
        
        assertNotNull(factors1);
        assertNotNull(factors2);
        assertEquals(factors1.size(), factors2.size());
        for (int i = 0; i < factors1.size(); i++) {
            assertEquals(factors1.get(i), factors2.get(i));
        }
    }
    
    @Test
    public void testCancellableFactorizerThreadSafety() throws InterruptedException {
        RunnableCancellablePrimeFactorizer factorizer = new RunnableCancellablePrimeFactorizer(999999999L);
        Thread factorizerThread = new Thread(factorizer);
        factorizerThread.start();
        Thread canceller1 = new Thread(() -> factorizer.setDone());
        Thread canceller2 = new Thread(() -> factorizer.setDone());
        canceller1.start();
        canceller2.start();
        canceller1.join();
        canceller2.join();
        factorizerThread.join();
        assertTrue(factorizer.isDone());
    }
    
    @Test
    public void testSmallNumbers() throws InterruptedException {
        RunnablePrimeFactorizer factorizer = new RunnablePrimeFactorizer(2);
        Thread thread = new Thread(factorizer);
        thread.start();
        thread.join();
        
        List<Long> factors = factorizer.getPrimeFactors();
        assertEquals(1, factors.size());
        assertEquals(2L, factors.get(0));
    }
    
    @Test
    public void testPowerOfTwo() throws InterruptedException {
        long number = 64;
        RunnablePrimeFactorizer factorizer = new RunnablePrimeFactorizer(number);
        Thread thread = new Thread(factorizer);
        thread.start();
        thread.join();
        
        List<Long> factors = factorizer.getPrimeFactors();
        assertEquals(6, factors.size());
        for (Long factor : factors) {
            assertEquals(2L, factor);
        }
    }
}

