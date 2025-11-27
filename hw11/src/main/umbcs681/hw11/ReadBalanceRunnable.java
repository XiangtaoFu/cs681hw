package umbcs681.hw11;

import java.util.concurrent.atomic.AtomicBoolean;

public class ReadBalanceRunnable implements Runnable {
    private AtomicBoolean done = new AtomicBoolean(false);
    private ThreadSafeOptimisticBankAccount account;
    
    public ReadBalanceRunnable(ThreadSafeOptimisticBankAccount account) {
        this.account = account;
    }
    
    public void setDone() {
        done.set(true);
    }
    
    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            if (done.get()) break;
            account.getBalance();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                continue;
            }
        }
    }
}
