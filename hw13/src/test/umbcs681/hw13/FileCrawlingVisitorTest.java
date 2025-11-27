package umbcs681.hw13;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentLinkedQueue;

public class FileCrawlingVisitorTest {

    @Test
    public void testFileCrawlingVisitor() {
        Directory root = new Directory(null, "root", LocalDateTime.now());
        File file1 = new File(root, "file1.txt", 100, LocalDateTime.now());
        File file2 = new File(root, "file2.txt", 200, LocalDateTime.now());
        root.appendChild(file1);
        root.appendChild(file2);
        
        FileCrawlingVisitor visitor = new FileCrawlingVisitor();
        root.accept(visitor);
        
        assertEquals(2, visitor.getFiles().size());
    }

    @Test
    public void testMultiThreadedCrawling() throws InterruptedException {
        Directory root1 = new Directory(null, "root1", LocalDateTime.now());
        root1.appendChild(new File(root1, "f1.txt", 10, LocalDateTime.now()));
        
        Directory root2 = new Directory(null, "root2", LocalDateTime.now());
        root2.appendChild(new File(root2, "f2.txt", 20, LocalDateTime.now()));
        
        Directory root3 = new Directory(null, "root3", LocalDateTime.now());
        root3.appendChild(new File(root3, "f3.txt", 30, LocalDateTime.now()));
        
        ConcurrentLinkedQueue<File> sharedList = new ConcurrentLinkedQueue<>();
        
        Thread t1 = new Thread(new CrawlingRunnable(root1, sharedList));
        Thread t2 = new Thread(new CrawlingRunnable(root2, sharedList));
        Thread t3 = new Thread(new CrawlingRunnable(root3, sharedList));
        
        t1.start(); t2.start(); t3.start();
        t1.join(); t2.join(); t3.join();
        
        assertEquals(3, sharedList.size());
    }

    @Test
    public void testAcceptThreadSafe() throws InterruptedException {
        Directory root = new Directory(null, "root", LocalDateTime.now());
        for (int i = 0; i < 10; i++) {
            root.appendChild(new File(root, "file" + i + ".txt", 10, LocalDateTime.now()));
        }
        
        ConcurrentLinkedQueue<File> sharedList = new ConcurrentLinkedQueue<>();
        
        Thread[] threads = new Thread[5];
        for (int i = 0; i < 5; i++) {
            threads[i] = new Thread(new CrawlingRunnable(root, sharedList));
        }
        
        for (Thread t : threads) t.start();
        for (Thread t : threads) t.join();
        
        assertEquals(50, sharedList.size());
    }

    @Test
    public void testNestedDirectories() {
        Directory root = new Directory(null, "root", LocalDateTime.now());
        Directory sub1 = new Directory(root, "sub1", LocalDateTime.now());
        Directory sub2 = new Directory(sub1, "sub2", LocalDateTime.now());
        root.appendChild(sub1);
        sub1.appendChild(sub2);
        sub2.appendChild(new File(sub2, "deep.txt", 100, LocalDateTime.now()));
        
        FileCrawlingVisitor visitor = new FileCrawlingVisitor();
        root.accept(visitor);
        
        assertEquals(1, visitor.getFiles().size());
        assertEquals("deep.txt", visitor.getFiles().get(0).getName());
    }
}
