package umbcs681.hw14;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentLinkedQueue;

public class FileSystem {
    private static FileSystem instance = null;
    private ConcurrentLinkedQueue<Directory> rootDirs = new ConcurrentLinkedQueue<>();

    private FileSystem() {}

    public static FileSystem getFileSystem() {
        if (instance == null) {
            instance = new FileSystem();
        }
        return instance;
    }

    public ConcurrentLinkedQueue<Directory> getRootDirs() {
        return rootDirs;
    }

    public void appendRootDir(Directory root) {
        rootDirs.add(root);
    }

    public void clearRootDirs() {
        rootDirs.clear();
    }

    public static Directory createTree1() {
        LocalDateTime now = LocalDateTime.now();
        Directory root = new Directory(null, "photos", 0, now);
        new File(root, "photo1.jpg", 100, now);
        new File(root, "photo2.jpg", 200, now);
        new File(root, "photo3.jpg", 150, now);
        for (FSElement child : new FSElement[]{
            new File(root, "photo1.jpg", 100, now),
            new File(root, "photo2.jpg", 200, now),
            new File(root, "photo3.jpg", 150, now)
        }) {
            root.appendChild(child);
        }
        return root;
    }

    public static Directory createTree2() {
        LocalDateTime now = LocalDateTime.now();
        Directory root = new Directory(null, "documents", 0, now);
        Directory subDir = new Directory(root, "work", 0, now);
        root.appendChild(subDir);
        subDir.appendChild(new File(subDir, "doc1.txt", 50, now));
        subDir.appendChild(new File(subDir, "doc2.txt", 75, now));
        root.appendChild(new File(root, "readme.txt", 25, now));
        return root;
    }

    public static Directory createTree3() {
        LocalDateTime now = LocalDateTime.now();
        Directory root = new Directory(null, "code", 0, now);
        Directory srcDir = new Directory(root, "src", 0, now);
        root.appendChild(srcDir);
        srcDir.appendChild(new File(srcDir, "Main.java", 500, now));
        srcDir.appendChild(new File(srcDir, "Test.java", 300, now));
        return root;
    }
}
