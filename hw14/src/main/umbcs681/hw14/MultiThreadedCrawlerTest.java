package umbcs681.hw14;

import java.util.concurrent.ConcurrentLinkedQueue;

public class MultiThreadedCrawlerTest {
    
    private static ConcurrentLinkedQueue<File> sharedFiles = new ConcurrentLinkedQueue<>();

    public static void main(String[] args) {
        Directory tree1 = FileSystem.createTree1();
        Directory tree2 = FileSystem.createTree2();
        Directory tree3 = FileSystem.createTree3();

        FileCrawlingVisitor v1 = new FileCrawlingVisitor();
        FileCrawlingVisitor v2 = new FileCrawlingVisitor();
        FileCrawlingVisitor v3 = new FileCrawlingVisitor();

        CrawlingRunnable r1 = new CrawlingRunnable(tree1, v1, sharedFiles);
        CrawlingRunnable r2 = new CrawlingRunnable(tree2, v2, sharedFiles);
        CrawlingRunnable r3 = new CrawlingRunnable(tree3, v3, sharedFiles);

        Thread t1 = new Thread(r1, "Crawler-1");
        Thread t2 = new Thread(r2, "Crawler-2");
        Thread t3 = new Thread(r3, "Crawler-3");

        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Total files found: " + sharedFiles.size());
        System.out.println("\nFiles in shared list:");
        for (File file : sharedFiles) {
            System.out.println("  - " + file.getName() + " (" + file.getSize() + " bytes)");
        }

        demonstrateTermination();
    }

    private static void demonstrateTermination() {
        ConcurrentLinkedQueue<File> testFiles = new ConcurrentLinkedQueue<>();
        
        Directory tree = FileSystem.createTree1();
        FileCrawlingVisitor visitor = new FileCrawlingVisitor();
        CrawlingRunnable runnable = new CrawlingRunnable(tree, visitor, testFiles);
        
        Thread crawler = new Thread(runnable);
        crawler.start();
        
        runnable.setDone();
        crawler.interrupt();
        
        try {
            crawler.join();
            System.out.println("Termination test: " + testFiles.size() + " files");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
