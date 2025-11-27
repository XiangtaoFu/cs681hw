package umbcs681.hw13;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CrawlingRunnable implements Runnable {
    // Using volatile flag for 2-step termination (last name "Fu" starts with F, which is in A-K range)
    private volatile boolean done = false;
    private Directory root;
    private ConcurrentLinkedQueue<File> sharedList;
    
    public CrawlingRunnable(Directory root, ConcurrentLinkedQueue<File> sharedList) {
        this.root = root;
        this.sharedList = sharedList;
    }
    
    public void setDone() {
        done = true;
    }
    
    @Override
    public void run() {
        if (done) return;
        
        FileCrawlingVisitor visitor = new FileCrawlingVisitor();
        root.accept(visitor);
        LinkedList<File> files = visitor.getFiles();
        
        sharedList.addAll(files);
    }
}
