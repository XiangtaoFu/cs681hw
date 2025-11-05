package umbcs681.hw6;

import java.util.concurrent.locks.ReentrantLock;
import java.util.List;
import java.util.ArrayList;

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
                    System.out.println("Stopped generating prime numbers.");
                    primes.clear();
                    break;
                }
            } finally {
                lock.unlock();
            }
            
            if (isPrime(candidate)) {
                lock.lock();
                try {
                    if (!done) {
                        primes.add(candidate);
                    }
                } finally {
                    lock.unlock();
                }
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
    
    @Override
    public List<Long> getPrimes() {
        lock.lock();
        try {
            return new ArrayList<>(primes);
        } finally {
            lock.unlock();
        }
    }
}