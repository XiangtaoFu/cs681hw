package umbcs681.hw13;

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
        lock.lock();
        try {
            return new LinkedList<>(children);
        } finally {
            lock.unlock();
        }
    }
    
    public void appendChild(FSElement child) {
        lock.lock();
        try {
            children.add(child);
        } finally {
            lock.unlock();
        }
    }
    
    public int countChildren() {
        lock.lock();
        try {
            return children.size();
        } finally {
            lock.unlock();
        }
    }
    
    public LinkedList<Directory> getSubDirectories() {
        lock.lock();
        try {
            LinkedList<Directory> subDirs = new LinkedList<>();
            for (FSElement child : children) {
                if (child.isDirectory()) {
                    subDirs.add((Directory) child);
                }
            }
            return subDirs;
        } finally {
            lock.unlock();
        }
    }
    
    public LinkedList<File> getFiles() {
        lock.lock();
        try {
            LinkedList<File> files = new LinkedList<>();
            for (FSElement child : children) {
                if (!child.isDirectory() && child instanceof File) {
                    files.add((File) child);
                }
            }
            return files;
        } finally {
            lock.unlock();
        }
    }
    
    @Override
    public void accept(FSVisitor v) {
        lock.lock();
        try {
            v.visit(this);
            for (FSElement child : children) {
                child.accept(v);
            }
        } finally {
            lock.unlock();
        }
    }
}
