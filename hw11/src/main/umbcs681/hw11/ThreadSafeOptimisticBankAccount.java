package umbcs681.hw11;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ThreadSafeOptimisticBankAccount implements BankAccount {
    private double balance = 0;
    private ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    
    @Override
    public void deposit(double amount) {
        rwLock.writeLock().lock();
        try {
            balance += amount;
        } finally {
            rwLock.writeLock().unlock();
        }
    }
    
    @Override
    public void withdraw(double amount) {
        rwLock.writeLock().lock();
        try {
            balance -= amount;
        } finally {
            rwLock.writeLock().unlock();
        }
    }
    
    @Override
    public double getBalance() {
        rwLock.readLock().lock();
        try {
            return balance;
        } finally {
            rwLock.readLock().unlock();
        }
    }
}
