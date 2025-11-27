package umbcs681.hw14;

import java.util.concurrent.ConcurrentLinkedQueue;

public class CrawlingRunnable implements Runnable {
    private Directory rootDir;
    private FileCrawlingVisitor visitor;
    private ConcurrentLinkedQueue<File> sharedList;
    // Using volatile flag for 2-step termination (last name "Fu" starts with F, which is in A-K range)
    private volatile boolean done = false;

    public CrawlingRunnable(Directory rootDir, FileCrawlingVisitor visitor, ConcurrentLinkedQueue<File> sharedList) {
        this.rootDir = rootDir;
        this.visitor = visitor;
        this.sharedList = sharedList;
    }

    public void setDone() {
        done = true;
        visitor.setDone();
    }

    @Override
    public void run() {
        try {
            if (done) {
                return;
            }
            
            crawl(rootDir);
            
            if (done) {
                return;
            }
            
            for (File file : visitor.getFiles()) {
                if (done) {
                    break;
                }
                sharedList.add(file);
            }
        } catch (Exception e) {
            System.err.println("Error during crawling: " + e.getMessage());
        }
    }

    private void crawl(FSElement element) {
        if (done) {
            return;
        }
        
        element.accept(visitor);
    }
}
