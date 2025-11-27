package umbcs681.hw12;

import java.time.LocalDateTime;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class FSElement {
    protected String name;
    protected int size;
    protected LocalDateTime creationTime;
    protected Directory parent;
    protected ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    
    public FSElement(Directory parent, String name, int size, LocalDateTime creationTime) {
        this.parent = parent;
        this.name = name;
        this.size = size;
        this.creationTime = creationTime;
    }
    
    public Directory getParent() {
        rwLock.readLock().lock();
        try {
            return parent;
        } finally {
            rwLock.readLock().unlock();
        }
    }
    
    public String getName() {
        rwLock.readLock().lock();
        try {
            return name;
        } finally {
            rwLock.readLock().unlock();
        }
    }
    
    public int getSize() {
        rwLock.readLock().lock();
        try {
            return size;
        } finally {
            rwLock.readLock().unlock();
        }
    }
    
    public LocalDateTime getCreationTime() {
        rwLock.readLock().lock();
        try {
            return creationTime;
        } finally {
            rwLock.readLock().unlock();
        }
    }
    
    public abstract boolean isDirectory();
    
    public void setName(String name) {
        rwLock.writeLock().lock();
        try {
            this.name = name;
        } finally {
            rwLock.writeLock().unlock();
        }
    }
    
    public void setSize(int size) {
        rwLock.writeLock().lock();
        try {
            this.size = size;
        } finally {
            rwLock.writeLock().unlock();
        }
    }
}
