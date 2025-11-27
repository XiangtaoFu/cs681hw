package umbcs681.hw12;

import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class FileSystem {
    private static FileSystem instance = null;
    private static ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private LinkedList<Directory> rootDirs;
    
    private FileSystem() {
        this.rootDirs = new LinkedList<>();
    }
    
    public static FileSystem getFileSystem() {
        rwLock.writeLock().lock();
        try {
            if (instance == null) {
                instance = new FileSystem();
            }
            return instance;
        } finally {
            rwLock.writeLock().unlock();
        }
    }
    
    public LinkedList<Directory> getRootDirs() {
        rwLock.readLock().lock();
        try {
            return new LinkedList<>(rootDirs);
        } finally {
            rwLock.readLock().unlock();
        }
    }
    
    public void appendRootDir(Directory root) {
        rwLock.writeLock().lock();
        try {
            rootDirs.add(root);
        } finally {
            rwLock.writeLock().unlock();
        }
    }
}
