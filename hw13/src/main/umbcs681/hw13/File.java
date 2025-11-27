package umbcs681.hw13;

import java.time.LocalDateTime;

public class File extends FSElement {
    
    public File(Directory parent, String name, int size, LocalDateTime creationTime) {
        super(parent, name, size, creationTime);
    }
    
    @Override
    public boolean isDirectory() {
        return false;
    }
    
    @Override
    public void accept(FSVisitor v) {
        lock.lock();
        try {
            v.visit(this);
        } finally {
            lock.unlock();
        }
    }
}
