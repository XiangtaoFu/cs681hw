package umbcs681.hw11;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ThreadSafeOptimisticBankAccountTest {

    @Test
    public void testDeposit() {
        ThreadSafeOptimisticBankAccount account = new ThreadSafeOptimisticBankAccount();
        account.deposit(100);
        assertEquals(100, account.getBalance());
    }

    @Test
    public void testWithdraw() {
        ThreadSafeOptimisticBankAccount account = new ThreadSafeOptimisticBankAccount();
        account.deposit(100);
        account.withdraw(50);
        assertEquals(50, account.getBalance());
    }

    @Test
    public void testConcurrentAccess() throws InterruptedException {
        ThreadSafeOptimisticBankAccount account = new ThreadSafeOptimisticBankAccount();
        
        Thread[] depositThreads = new Thread[10];
        Thread[] withdrawThreads = new Thread[10];
        
        for (int i = 0; i < 10; i++) {
            depositThreads[i] = new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    account.deposit(1);
                }
            });
            withdrawThreads[i] = new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    account.withdraw(1);
                }
            });
        }
        
        for (int i = 0; i < 10; i++) {
            depositThreads[i].start();
            withdrawThreads[i].start();
        }
        
        for (int i = 0; i < 10; i++) {
            depositThreads[i].join();
            withdrawThreads[i].join();
        }
        
        assertEquals(0, account.getBalance());
    }

    @Test
    public void testAtomicBooleanFlag() throws InterruptedException {
        ThreadSafeOptimisticBankAccount account = new ThreadSafeOptimisticBankAccount();
        DepositRunnable dr = new DepositRunnable(account);
        Thread t = new Thread(dr);
        
        t.start();
        Thread.sleep(50);
        dr.setDone();
        t.interrupt();
        t.join(1000);
        
        assertFalse(t.isAlive());
    }
}
