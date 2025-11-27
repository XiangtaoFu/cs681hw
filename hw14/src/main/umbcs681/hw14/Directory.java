package umbcs681.hw14;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Directory extends FSElement {
    private ConcurrentLinkedQueue<FSElement> children = new ConcurrentLinkedQueue<>();

    public Directory(Directory parent, String name, int size, LocalDateTime creationTime) {
        super(parent, name, size, creationTime);
    }

    public ConcurrentLinkedQueue<FSElement> getChildren() {
        return children;
    }

    public void appendChild(FSElement child) {
        children.add(child);
    }

    public int countChildren() {
        return children.size();
    }

    public ConcurrentLinkedQueue<Directory> getSubDirectories() {
        ConcurrentLinkedQueue<Directory> subDirs = new ConcurrentLinkedQueue<>();
        for (FSElement element : children) {
            if (element.isDirectory()) {
                subDirs.add((Directory) element);
            }
        }
        return subDirs;
    }

    public ConcurrentLinkedQueue<File> getFiles() {
        ConcurrentLinkedQueue<File> files = new ConcurrentLinkedQueue<>();
        for (FSElement element : children) {
            if (element.isFile()) {
                files.add((File) element);
            }
        }
        return files;
    }

    public ConcurrentLinkedQueue<Link> getLinks() {
        ConcurrentLinkedQueue<Link> links = new ConcurrentLinkedQueue<>();
        for (FSElement element : children) {
            if (element.isLink()) {
                links.add((Link) element);
            }
        }
        return links;
    }

    public int getTotalSize() {
        int total = 0;
        for (FSElement element : children) {
            if (element.isDirectory()) {
                total += ((Directory) element).getTotalSize();
            } else if (element.isFile()) {
                total += element.getSize();
            }
        }
        return total;
    }

    @Override
    public boolean isDirectory() {
        return true;
    }

    @Override
    public boolean isFile() {
        return false;
    }

    @Override
    public boolean isLink() {
        return false;
    }

    @Override
    public void accept(FSVisitor visitor) {
        visitor.visit(this);
        for (FSElement child : children) {
            child.accept(visitor);
        }
    }
}
