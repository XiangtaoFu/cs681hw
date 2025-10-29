package umbcs681.hw6;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class PrimeGeneratorTest {
    
    @Test
    public void testRunnablePrimeGenerator() throws InterruptedException {
        RunnablePrimeGenerator generator = new RunnablePrimeGenerator();
        Thread thread = new Thread(generator);
        thread.start();
        thread.join();
        
        List<Long> primes = generator.getPrimes();
        assertNotNull(primes);
        assertTrue(primes.size() > 0);
        for (Long prime : primes) {
            assertTrue(isPrime(prime));
        }
        for (int i = 1; i < primes.size(); i++) {
            assertTrue(primes.get(i) > primes.get(i - 1));
        }
    }
    
    @Test
    public void testCancellablePrimeGenerator() throws InterruptedException {
        RunnableCancellablePrimeGenerator generator = new RunnableCancellablePrimeGenerator();
        Thread thread = new Thread(generator);
        thread.start();
        
        Thread.sleep(10);
        generator.setDone();
        thread.join();
        assertTrue(generator.isDone());
        List<Long> primes = generator.getPrimes();
        assertNotNull(primes);
    }
    
    @Test
    public void testCancellablePrimeGeneratorCompleteRun() throws InterruptedException {
        RunnableCancellablePrimeGenerator generator = new RunnableCancellablePrimeGenerator();
        Thread thread = new Thread(generator);
        thread.start();
        thread.join();
        
        List<Long> primes = generator.getPrimes();
        assertNotNull(primes);
        assertTrue(primes.size() > 0);
        assertTrue(primes.contains(2L));
        assertTrue(primes.contains(3L));
        assertTrue(primes.contains(5L));
        assertTrue(primes.contains(7L));
    }
    
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    public void testPrimeGeneratorDoesNotHang() throws InterruptedException {
        RunnablePrimeGenerator generator = new RunnablePrimeGenerator();
        Thread thread = new Thread(generator);
        thread.start();
        thread.join();
        
        assertTrue(generator.getPrimes().size() > 0);
    }
    
    @Test
    public void testMultiplePrimeGeneratorsSimultaneously() throws InterruptedException {
        RunnablePrimeGenerator gen1 = new RunnablePrimeGenerator();
        RunnablePrimeGenerator gen2 = new RunnablePrimeGenerator();
        
        Thread thread1 = new Thread(gen1);
        Thread thread2 = new Thread(gen2);
        
        thread1.start();
        thread2.start();
        
        thread1.join();
        thread2.join();
        
        List<Long> primes1 = gen1.getPrimes();
        List<Long> primes2 = gen2.getPrimes();
        
        assertNotNull(primes1);
        assertNotNull(primes2);
        assertEquals(primes1.size(), primes2.size());
        for (int i = 0; i < primes1.size(); i++) {
            assertEquals(primes1.get(i), primes2.get(i));
        }
    }
    
    @Test
    public void testCancellablePrimeGeneratorThreadSafety() throws InterruptedException {
        RunnableCancellablePrimeGenerator generator = new RunnableCancellablePrimeGenerator();
        Thread generatorThread = new Thread(generator);
        generatorThread.start();
        Thread canceller1 = new Thread(() -> generator.setDone());
        Thread canceller2 = new Thread(() -> generator.setDone());
        canceller1.start();
        canceller2.start();
        canceller1.join();
        canceller2.join();
        generatorThread.join();
        assertTrue(generator.isDone());
    }
    

    private boolean isPrime(long n) {
        if (n <= 1) return false;
        if (n <= 3) return true;
        if (n % 2 == 0 || n % 3 == 0) return false;
        
        for (long i = 5; i * i <= n; i += 6) {
            if (n % i == 0 || n % (i + 2) == 0) {
                return false;
            }
        }
        return true;
    }
}

