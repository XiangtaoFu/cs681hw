package umbcs681.hw7;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class FileSystemTest {
    
    @BeforeEach
    public void setUp() {
    }
    
    @Test
    public void testFileSystemSingleton() {
        FileSystem fs1 = FileSystem.getFileSystem();
        FileSystem fs2 = FileSystem.getFileSystem();
        assertSame(fs1, fs2);
    }
    
    @Test
    public void testCreateDirectory() {
        Directory root = new Directory(null, "root", LocalDateTime.now());
        
        assertNotNull(root);
        assertEquals("root", root.getName());
        assertTrue(root.isDirectory());
        assertEquals(0, root.getSize());
    }
    
    @Test
    public void testCreateFile() {
        Directory root = new Directory(null, "root", LocalDateTime.now());
        File file = new File(root, "test.txt", 100, LocalDateTime.now());
        
        assertNotNull(file);
        assertEquals("test.txt", file.getName());
        assertFalse(file.isDirectory());
        assertEquals(100, file.getSize());
        assertEquals(root, file.getParent());
    }
    
    @Test
    public void testCreateLink() {
        Directory root = new Directory(null, "root", LocalDateTime.now());
        File file = new File(root, "test.txt", 100, LocalDateTime.now());
        Link link = new Link(root, "link-to-test", file, LocalDateTime.now());
        
        assertNotNull(link);
        assertEquals("link-to-test", link.getName());
        assertFalse(link.isDirectory());
        assertEquals(0, link.getSize());
        assertEquals(file, link.getTarget());
    }
    
    @Test
    public void testDirectoryChildren() {
        Directory root = new Directory(null, "root", LocalDateTime.now());
        File file1 = new File(root, "file1.txt", 100, LocalDateTime.now());
        File file2 = new File(root, "file2.txt", 200, LocalDateTime.now());
        Directory subDir = new Directory(root, "subdir", LocalDateTime.now());
        
        root.appendChild(file1);
        root.appendChild(file2);
        root.appendChild(subDir);
        
        assertEquals(3, root.countChildren());
        
        LinkedList<File> files = root.getFiles();
        assertEquals(2, files.size());
        assertTrue(files.contains(file1));
        assertTrue(files.contains(file2));
        
        LinkedList<Directory> subDirs = root.getSubDirectories();
        assertEquals(1, subDirs.size());
        assertTrue(subDirs.contains(subDir));
    }
    
    @Test
    public void testDirectoryTotalSize() {
        Directory root = new Directory(null, "root", LocalDateTime.now());
        File file1 = new File(root, "file1.txt", 100, LocalDateTime.now());
        File file2 = new File(root, "file2.txt", 200, LocalDateTime.now());
        
        root.appendChild(file1);
        root.appendChild(file2);
        
        assertEquals(300, root.getTotalSize());
    }
    
    @Test
    public void testMultiThreadedFileSystemAccess() throws InterruptedException {
        int numThreads = 10;
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        CountDownLatch latch = new CountDownLatch(numThreads);
        List<FileSystem> fileSystems = new ArrayList<>();
        
        for (int i = 0; i < numThreads; i++) {
            executor.submit(() -> {
                try {
                    FileSystem fs = FileSystem.getFileSystem();
                    synchronized (fileSystems) {
                        fileSystems.add(fs);
                    }
                } finally {
                    latch.countDown();
                }
            });
        }
        
        latch.await(5, TimeUnit.SECONDS);
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        assertEquals(numThreads, fileSystems.size());
        FileSystem first = fileSystems.get(0);
        for (FileSystem fs : fileSystems) {
            assertSame(first, fs);
        }
    }
    
    @Test
    public void testMultiThreadedDirectoryModification() throws InterruptedException {
        Directory root = new Directory(null, "root", LocalDateTime.now());
        int numThreads = 10;
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        CountDownLatch latch = new CountDownLatch(numThreads);
        
        for (int i = 0; i < numThreads; i++) {
            final int index = i;
            executor.submit(() -> {
                try {
                    File file = new File(root, "file" + index + ".txt", 100, LocalDateTime.now());
                    synchronized (root) {
                        root.appendChild(file);
                    }
                } finally {
                    latch.countDown();
                }
            });
        }
        
        latch.await(5, TimeUnit.SECONDS);
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        assertEquals(numThreads, root.countChildren());
    }
    
    @Test
    public void testConcurrentRootDirAddition() throws InterruptedException {
        FileSystem fs = FileSystem.getFileSystem();
        int numThreads = 10;
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        CountDownLatch latch = new CountDownLatch(numThreads);
        
        for (int i = 0; i < numThreads; i++) {
            final int index = i;
            executor.submit(() -> {
                try {
                    Directory root = new Directory(null, "root" + index, LocalDateTime.now());
                    synchronized (fs) {
                        fs.appendRootDir(root);
                    }
                } finally {
                    latch.countDown();
                }
            });
        }
        
        latch.await(5, TimeUnit.SECONDS);
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        assertEquals(numThreads, fs.getRootDirs().size());
    }
    
    @Test
    public void testFileRename() {
        Directory root = new Directory(null, "root", LocalDateTime.now());
        File file = new File(root, "oldname.txt", 100, LocalDateTime.now());
        
        assertEquals("oldname.txt", file.getName());
        
        file.setName("newname.txt");
        assertEquals("newname.txt", file.getName());
    }
    
    @Test
    public void testFileResize() {
        Directory root = new Directory(null, "root", LocalDateTime.now());
        File file = new File(root, "test.txt", 100, LocalDateTime.now());
        
        assertEquals(100, file.getSize());
        
        file.setSize(200);
        assertEquals(200, file.getSize());
    }
    
    @Test
    public void testLinkTargetChange() {
        Directory root = new Directory(null, "root", LocalDateTime.now());
        File file1 = new File(root, "file1.txt", 100, LocalDateTime.now());
        File file2 = new File(root, "file2.txt", 200, LocalDateTime.now());
        Link link = new Link(root, "mylink", file1, LocalDateTime.now());
        
        assertEquals(file1, link.getTarget());
        
        link.setTarget(file2);
        assertEquals(file2, link.getTarget());
    }
    
    @Test
    public void testNestedDirectories() {
        Directory root = new Directory(null, "root", LocalDateTime.now());
        Directory level1 = new Directory(root, "level1", LocalDateTime.now());
        Directory level2 = new Directory(level1, "level2", LocalDateTime.now());
        File file = new File(level2, "deepfile.txt", 100, LocalDateTime.now());
        
        root.appendChild(level1);
        level1.appendChild(level2);
        level2.appendChild(file);
        
        assertEquals(1, root.countChildren());
        assertEquals(1, level1.countChildren());
        assertEquals(1, level2.countChildren());
        assertEquals(root, level1.getParent());
        assertEquals(level1, level2.getParent());
        assertEquals(level2, file.getParent());
    }
    
    @Test
    public void testMixedDirectoryContent() {
        Directory root = new Directory(null, "root", LocalDateTime.now());
        File file1 = new File(root, "file1.txt", 100, LocalDateTime.now());
        Directory subDir = new Directory(root, "subdir", LocalDateTime.now());
        File file2 = new File(root, "file2.txt", 200, LocalDateTime.now());
        Link link = new Link(root, "link", file1, LocalDateTime.now());
        
        root.appendChild(file1);
        root.appendChild(subDir);
        root.appendChild(file2);
        root.appendChild(link);
        
        assertEquals(4, root.countChildren());
        assertEquals(2, root.getFiles().size());
        assertEquals(1, root.getSubDirectories().size());
    }
    
    @Test
    public void testEmptyDirectory() {
        Directory root = new Directory(null, "empty", LocalDateTime.now());
        
        assertEquals(0, root.countChildren());
        assertEquals(0, root.getFiles().size());
        assertEquals(0, root.getSubDirectories().size());
        assertEquals(0, root.getTotalSize());
    }
}

