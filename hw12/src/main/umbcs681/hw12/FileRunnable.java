package umbcs681.hw12;

public class FileRunnable implements Runnable {
    // Using volatile flag for 2-step termination (last name "Fu" starts with F, which is in A-K range)
    private volatile boolean done = false;
    private File file;
    
    public FileRunnable(File file) {
        this.file = file;
    }
    
    public void setDone() {
        done = true;
    }
    
    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            if (done) break;
            file.getName();
            file.getSize();
            file.getCreationTime();
            file.getParent();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                continue;
            }
        }
    }
}
