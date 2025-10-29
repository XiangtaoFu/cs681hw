package umbcs681.hw6;

import java.util.concurrent.locks.ReentrantLock;

public class RunnableCancellablePrimeGenerator extends RunnablePrimeGenerator {
    private boolean done = false;
    private ReentrantLock lock = new ReentrantLock();
    
    public void setDone() {
        lock.lock();
        try {
            done = true;
        } finally {
            lock.unlock();
        }
    }
    
    @Override
    public void generatePrimes() {
        long candidate = 2;
        while (candidate <= 1000) {
            lock.lock();
            try {
                if (done) {
                    break;
                }
            } finally {
                lock.unlock();
            }
            
            if (isPrime(candidate)) {
                primes.add(candidate);
            }
            candidate++;
        }
    }
    
    public boolean isDone() {
        lock.lock();
        try {
            return done;
        } finally {
            lock.unlock();
        }
    }
}