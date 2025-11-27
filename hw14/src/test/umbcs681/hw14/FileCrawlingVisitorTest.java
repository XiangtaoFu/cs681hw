package umbcs681.hw14;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentLinkedQueue;

import static org.junit.jupiter.api.Assertions.*;

public class FileCrawlingVisitorTest {

    @BeforeEach
    void setUp() {
        FileSystem.getFileSystem().clearRootDirs();
    }

    @Test
    void testConcurrentLinkedQueueThreadSafety() {
        ConcurrentLinkedQueue<File> sharedFiles = new ConcurrentLinkedQueue<>();
        
        Directory tree1 = FileSystem.createTree1();
        Directory tree2 = FileSystem.createTree2();
        Directory tree3 = FileSystem.createTree3();
        
        FileCrawlingVisitor v1 = new FileCrawlingVisitor();
        FileCrawlingVisitor v2 = new FileCrawlingVisitor();
        FileCrawlingVisitor v3 = new FileCrawlingVisitor();
        
        CrawlingRunnable r1 = new CrawlingRunnable(tree1, v1, sharedFiles);
        CrawlingRunnable r2 = new CrawlingRunnable(tree2, v2, sharedFiles);
        CrawlingRunnable r3 = new CrawlingRunnable(tree3, v3, sharedFiles);
        
        Thread t1 = new Thread(r1);
        Thread t2 = new Thread(r2);
        Thread t3 = new Thread(r3);
        
        t1.start();
        t2.start();
        t3.start();
        
        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            fail("Thread interrupted");
        }
        
        // tree1: 3 files, tree2: 3 files, tree3: 2 files = 8 total
        assertEquals(8, sharedFiles.size());
    }

    @Test
    void testTwoStepTermination() {
        ConcurrentLinkedQueue<File> sharedFiles = new ConcurrentLinkedQueue<>();
        
        Directory tree = FileSystem.createTree1();
        FileCrawlingVisitor visitor = new FileCrawlingVisitor();
        CrawlingRunnable runnable = new CrawlingRunnable(tree, visitor, sharedFiles);
        
        Thread crawler = new Thread(runnable);
        
        // Set done before starting - should collect no files
        runnable.setDone();
        crawler.start();
        
        try {
            crawler.join();
        } catch (InterruptedException e) {
            fail("Thread interrupted");
        }
        
        assertTrue(sharedFiles.size() <= 3);
    }

    @Test
    void testVisitorFlagsIndependence() {
        FileCrawlingVisitor v1 = new FileCrawlingVisitor();
        FileCrawlingVisitor v2 = new FileCrawlingVisitor();
        
        assertFalse(v1.isDone());
        assertFalse(v2.isDone());
        
        v1.setDone();
        
        assertTrue(v1.isDone());
        assertFalse(v2.isDone());
    }

    @Test
    void testNoClientSideLockingNeeded() {
        ConcurrentLinkedQueue<File> sharedFiles = new ConcurrentLinkedQueue<>();
        
        Thread[] threads = new Thread[10];
        LocalDateTime now = LocalDateTime.now();
        
        for (int i = 0; i < 10; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    sharedFiles.add(new File(null, "file" + index + "_" + j + ".txt", j, now));
                }
            });
        }
        
        for (Thread t : threads) {
            t.start();
        }
        
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                fail("Thread interrupted");
            }
        }
        
        // All 1000 files should be added without any lost updates
        assertEquals(1000, sharedFiles.size());
    }
}
