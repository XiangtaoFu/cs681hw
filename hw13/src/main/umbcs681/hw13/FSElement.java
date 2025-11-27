package umbcs681.hw13;

import java.time.LocalDateTime;
import java.util.concurrent.locks.ReentrantLock;

public abstract class FSElement {
    protected String name;
    protected int size;
    protected LocalDateTime creationTime;
    protected Directory parent;
    protected ReentrantLock lock = new ReentrantLock();
    
    public FSElement(Directory parent, String name, int size, LocalDateTime creationTime) {
        this.parent = parent;
        this.name = name;
        this.size = size;
        this.creationTime = creationTime;
    }
    
    public Directory getParent() {
        return parent;
    }
    
    public String getName() {
        return name;
    }
    
    public int getSize() {
        return size;
    }
    
    public LocalDateTime getCreationTime() {
        return creationTime;
    }
    
    public abstract boolean isDirectory();
    
    public abstract void accept(FSVisitor v);
}
