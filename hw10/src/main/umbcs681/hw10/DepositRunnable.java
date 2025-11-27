package umbcs681.hw10;

public class DepositRunnable implements Runnable {
    // Using volatile flag for 2-step termination (last name "Fu" starts with F, which is in A-K range)
    private volatile boolean done = false;
    private ThreadSafeBankAccount account;
    
    public DepositRunnable(ThreadSafeBankAccount account) {
        this.account = account;
    }
    
    public void setDone() {
        done = true;
    }
    
    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            if (done) break;
            account.deposit(10);
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                continue;
            }
        }
    }
}
