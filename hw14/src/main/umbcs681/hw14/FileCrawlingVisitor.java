package umbcs681.hw14;

import java.util.concurrent.ConcurrentLinkedQueue;

public class FileCrawlingVisitor implements FSVisitor {
    private ConcurrentLinkedQueue<File> files = new ConcurrentLinkedQueue<>();
    // Using volatile flag for 2-step termination (last name "Fu" starts with F, which is in A-K range)
    private volatile boolean done = false;

    public void setDone() {
        done = true;
    }

    public boolean isDone() {
        return done;
    }

    @Override
    public void visit(Directory directory) {
        if (done) {
            return;
        }
    }

    @Override
    public void visit(File file) {
        if (done) {
            return;
        }
        files.add(file);
    }

    @Override
    public void visit(Link link) {
        if (done) {
            return;
        }
    }

    public ConcurrentLinkedQueue<File> getFiles() {
        return files;
    }
}
