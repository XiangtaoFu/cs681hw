package umbcs681.hw10;

import java.util.ArrayList;
import java.util.List;

public class BankAccountTest {
    
    public static void main(String[] args) {
        ThreadSafeBankAccount account = new ThreadSafeBankAccount();
        
        List<DepositRunnable> depositRunnables = new ArrayList<>();
        List<WithdrawRunnable> withdrawRunnables = new ArrayList<>();
        List<ReadBalanceRunnable> readRunnables = new ArrayList<>();
        List<Thread> threads = new ArrayList<>();
        
        for (int i = 0; i < 10; i++) {
            DepositRunnable dr = new DepositRunnable(account);
            depositRunnables.add(dr);
            threads.add(new Thread(dr));
        }
        
        for (int i = 0; i < 10; i++) {
            WithdrawRunnable wr = new WithdrawRunnable(account);
            withdrawRunnables.add(wr);
            threads.add(new Thread(wr));
        }
        
        for (int i = 0; i < 10; i++) {
            ReadBalanceRunnable rr = new ReadBalanceRunnable(account);
            readRunnables.add(rr);
            threads.add(new Thread(rr));
        }
        
        for (Thread t : threads) {
            t.start();
        }
        
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        for (DepositRunnable dr : depositRunnables) {
            dr.setDone();
        }
        for (WithdrawRunnable wr : withdrawRunnables) {
            wr.setDone();
        }
        for (ReadBalanceRunnable rr : readRunnables) {
            rr.setDone();
        }
        
        for (Thread t : threads) {
            t.interrupt();
        }
        
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        System.out.println("Final balance: " + account.getBalance());
    }
}
