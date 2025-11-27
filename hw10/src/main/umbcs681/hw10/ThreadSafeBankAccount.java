package umbcs681.hw10;

import java.util.concurrent.locks.ReentrantLock;

public class ThreadSafeBankAccount implements BankAccount {
    private double balance = 0;
    private ReentrantLock lock = new ReentrantLock();
    
    @Override
    public void deposit(double amount) {
        lock.lock();
        try {
            balance += amount;
        } finally {
            lock.unlock();
        }
    }
    
    @Override
    public void withdraw(double amount) {
        lock.lock();
        try {
            balance -= amount;
        } finally {
            lock.unlock();
        }
    }
    
    @Override
    public double getBalance() {
        lock.lock();
        try {
            return balance;
        } finally {
            lock.unlock();
        }
    }
}
