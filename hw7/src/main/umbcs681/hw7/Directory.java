package umbcs681.hw7;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class Directory extends FSElement {
    private LinkedList<FSElement> children;
    
    public Directory(Directory parent, String name, LocalDateTime creationTime) {
        super(parent, name, 0, creationTime); // Directory size is always 0
        this.children = new LinkedList<>();
    }
    
    @Override
    public boolean isDirectory() {
        return true;
    }
    
    public LinkedList<FSElement> getChildren() {
        return children;
    }
    
    public void appendChild(FSElement child) {
        children.add(child);
    }
    
    public int countChildren() {
        return children.size();
    }
    
    public LinkedList<Directory> getSubDirectories() {
        LinkedList<Directory> subDirs = new LinkedList<>();
        for (FSElement child : children) {
            if (child.isDirectory()) {
                subDirs.add((Directory) child);
            }
        }
        return subDirs;
    }
    
    public LinkedList<File> getFiles() {
        LinkedList<File> files = new LinkedList<>();
        for (FSElement child : children) {
            if (!child.isDirectory() && child instanceof File) {
                files.add((File) child);
            }
        }
        return files;
    }
    
    public int getTotalSize() {
        int totalSize = 0;
        for (FSElement child : children) {
            totalSize += child.getSize();
        }
        return totalSize;
    }
}