package umbcs681.hw10;

public class ReadBalanceRunnable implements Runnable {
    // Using volatile flag for 2-step termination (last name "Fu" starts with F, which is in A-K range)
    private volatile boolean done = false;
    private ThreadSafeBankAccount account;
    
    public ReadBalanceRunnable(ThreadSafeBankAccount account) {
        this.account = account;
    }
    
    public void setDone() {
        done = true;
    }
    
    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            if (done) break;
            account.getBalance();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                continue;
            }
        }
    }
}
