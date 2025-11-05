package umbcs681.hw6;

import java.util.concurrent.locks.ReentrantLock;
import java.util.List;
import java.util.ArrayList;

public class RunnableCancellablePrimeFactorizer extends RunnablePrimeFactorizer {
    private boolean done = false;
    private ReentrantLock lock = new ReentrantLock();
    
    public RunnableCancellablePrimeFactorizer(long number) {
        super(number);
    }
    
    public void setDone() {
        lock.lock();
        try {
            done = true;
        } finally {
            lock.unlock();
        }
    }
    
    @Override
    public void generatePrimeFactors() {
        long n = number;
        long factor = 2;
        
        while (factor * factor <= n) {
            lock.lock();
            try {
                if (done) {
                    System.out.println("Stopped generating prime factors.");
                    primeFactors.clear();
                    break;
                }
            } finally {
                lock.unlock();
            }
            
            if (n % factor == 0) {
                lock.lock();
                try {
                    if (!done) {
                        primeFactors.add(factor);
                    }
                } finally {
                    lock.unlock();
                }
                n /= factor;
            } else {
                factor++;
            }
        }
        
        lock.lock();
        try {
            if (!done && n > 1) {
                primeFactors.add(n);
            }
        } finally {
            lock.unlock();
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
    public List<Long> getPrimeFactors() {
        lock.lock();
        try {
            return new ArrayList<>(primeFactors);
        } finally {
            lock.unlock();
        }
    }
}