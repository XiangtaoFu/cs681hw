package umbcs681.hw12;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;

public class FSElementTest {

    @Test
    public void testDirectoryThreadSafe() throws InterruptedException {
        Directory root = new Directory(null, "root", LocalDateTime.now());
        
        Thread[] threads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                root.appendChild(new File(root, "file" + index, 10, LocalDateTime.now()));
            });
        }
        
        for (Thread t : threads) t.start();
        for (Thread t : threads) t.join();
        
        assertEquals(10, root.countChildren());
    }

    @Test
    public void testFileThreadSafe() throws InterruptedException {
        Directory root = new Directory(null, "root", LocalDateTime.now());
        File file = new File(root, "test.txt", 100, LocalDateTime.now());
        
        Thread[] threads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(() -> {
                file.getName();
                file.getSize();
            });
        }
        
        for (Thread t : threads) t.start();
        for (Thread t : threads) t.join();
        
        assertEquals("test.txt", file.getName());
    }

    @Test
    public void testLinkThreadSafe() throws InterruptedException {
        Directory root = new Directory(null, "root", LocalDateTime.now());
        File file = new File(root, "target.txt", 50, LocalDateTime.now());
        Link link = new Link(root, "link", file, LocalDateTime.now());
        
        Thread[] threads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(() -> {
                link.getTarget();
                link.getName();
            });
        }
        
        for (Thread t : threads) t.start();
        for (Thread t : threads) t.join();
        
        assertEquals(file, link.getTarget());
    }

    @Test
    public void testFileSystemSingleton() throws InterruptedException {
        Thread[] threads = new Thread[10];
        FileSystem[] results = new FileSystem[10];
        
        for (int i = 0; i < 10; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                results[index] = FileSystem.getFileSystem();
            });
        }
        
        for (Thread t : threads) t.start();
        for (Thread t : threads) t.join();
        
        for (int i = 1; i < 10; i++) {
            assertSame(results[0], results[i]);
        }
    }
}
