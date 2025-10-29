package umbcs681.hw7;

import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;

public class FileSystem {
    private static FileSystem instance = null;
    private static ReentrantLock lock = new ReentrantLock();
    private LinkedList<Directory> rootDirs;
    
    private FileSystem() {
        this.rootDirs = new LinkedList<>();
    }
    
    public static FileSystem getFileSystem() {
        lock.lock();
        try {
            if (instance == null) {
                instance = new FileSystem();
            }
            return instance;
        } finally {
            lock.unlock();
        }
    }
    
    public LinkedList<Directory> getRootDirs() {
        return rootDirs;
    }
    
    public void appendRootDir(Directory root) {
        rootDirs.add(root);
    }
}