package umbcs681.hw12;

import java.time.LocalDateTime;
import java.util.LinkedList;

public class Directory extends FSElement {
    private LinkedList<FSElement> children;
    
    public Directory(Directory parent, String name, LocalDateTime creationTime) {
        super(parent, name, 0, creationTime);
        this.children = new LinkedList<>();
    }
    
    @Override
    public boolean isDirectory() {
        return true;
    }
    
    public LinkedList<FSElement> getChildren() {
        rwLock.readLock().lock();
        try {
            return new LinkedList<>(children);
        } finally {
            rwLock.readLock().unlock();
        }
    }
    
    public void appendChild(FSElement child) {
        rwLock.writeLock().lock();
        try {
            children.add(child);
        } finally {
            rwLock.writeLock().unlock();
        }
    }
    
    public int countChildren() {
        rwLock.readLock().lock();
        try {
            return children.size();
        } finally {
            rwLock.readLock().unlock();
        }
    }
    
    public LinkedList<Directory> getSubDirectories() {
        rwLock.readLock().lock();
        try {
            LinkedList<Directory> subDirs = new LinkedList<>();
            for (FSElement child : children) {
                if (child.isDirectory()) {
                    subDirs.add((Directory) child);
                }
            }
            return subDirs;
        } finally {
            rwLock.readLock().unlock();
        }
    }
    
    public LinkedList<File> getFiles() {
        rwLock.readLock().lock();
        try {
            LinkedList<File> files = new LinkedList<>();
            for (FSElement child : children) {
                if (!child.isDirectory() && child instanceof File) {
                    files.add((File) child);
                }
            }
            return files;
        } finally {
            rwLock.readLock().unlock();
        }
    }
    
    public int getTotalSize() {
        rwLock.readLock().lock();
        try {
            int totalSize = 0;
            for (FSElement child : children) {
                totalSize += child.getSize();
            }
            return totalSize;
        } finally {
            rwLock.readLock().unlock();
        }
    }
}
