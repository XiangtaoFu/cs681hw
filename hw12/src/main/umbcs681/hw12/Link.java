package umbcs681.hw12;

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
        rwLock.readLock().lock();
        try {
            return target;
        } finally {
            rwLock.readLock().unlock();
        }
    }
    
    public void setTarget(FSElement target) {
        rwLock.writeLock().lock();
        try {
            this.target = target;
        } finally {
            rwLock.writeLock().unlock();
        }
    }
}
