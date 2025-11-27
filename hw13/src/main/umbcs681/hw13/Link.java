package umbcs681.hw13;

import java.time.LocalDateTime;

public class Link extends FSElement {
    private FSElement target;
    
    public Link(Directory parent, String name, FSElement target, LocalDateTime creationTime) {
        super(parent, name, 0, creationTime);
        this.target = target;
    }
    
    @Override
    public boolean isDirectory() {
        return false;
    }
    
    public FSElement getTarget() {
        return target;
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
