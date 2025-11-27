package umbcs681.hw12;

public class DirectoryRunnable implements Runnable {
    // Using volatile flag for 2-step termination (last name "Fu" starts with F, which is in A-K range)
    private volatile boolean done = false;
    private Directory directory;
    
    public DirectoryRunnable(Directory directory) {
        this.directory = directory;
    }
    
    public void setDone() {
        done = true;
    }
    
    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            if (done) break;
            directory.getChildren();
            directory.countChildren();
            directory.getSubDirectories();
            directory.getFiles();
            directory.getTotalSize();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                continue;
            }
        }
    }
}
