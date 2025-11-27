package umbcs681.hw13;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MultiThreadedCrawlerTest {
    
    public static void main(String[] args) {
        Directory root1 = createTree1();
        Directory root2 = createTree2();
        Directory root3 = createTree3();
        
        ConcurrentLinkedQueue<File> sharedList = new ConcurrentLinkedQueue<>();
        
        List<CrawlingRunnable> runnables = new ArrayList<>();
        List<Thread> threads = new ArrayList<>();
        
        CrawlingRunnable r1 = new CrawlingRunnable(root1, sharedList);
        CrawlingRunnable r2 = new CrawlingRunnable(root2, sharedList);
        CrawlingRunnable r3 = new CrawlingRunnable(root3, sharedList);
        
        runnables.add(r1);
        runnables.add(r2);
        runnables.add(r3);
        
        threads.add(new Thread(r1));
        threads.add(new Thread(r2));
        threads.add(new Thread(r3));
        
        for (Thread t : threads) {
            t.start();
        }
        
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        System.out.println("Total files found: " + sharedList.size());
        for (File f : sharedList) {
            System.out.println("  - " + f.getName());
        }
    }
    
    private static Directory createTree1() {
        Directory root = new Directory(null, "drive1", LocalDateTime.now());
        Directory docs = new Directory(root, "docs", LocalDateTime.now());
        root.appendChild(docs);
        docs.appendChild(new File(docs, "doc1.txt", 100, LocalDateTime.now()));
        docs.appendChild(new File(docs, "doc2.txt", 200, LocalDateTime.now()));
        root.appendChild(new File(root, "readme.txt", 50, LocalDateTime.now()));
        return root;
    }
    
    private static Directory createTree2() {
        Directory root = new Directory(null, "drive2", LocalDateTime.now());
        Directory src = new Directory(root, "src", LocalDateTime.now());
        root.appendChild(src);
        src.appendChild(new File(src, "Main.java", 500, LocalDateTime.now()));
        src.appendChild(new File(src, "Test.java", 300, LocalDateTime.now()));
        return root;
    }
    
    private static Directory createTree3() {
        Directory root = new Directory(null, "drive3", LocalDateTime.now());
        Directory images = new Directory(root, "images", LocalDateTime.now());
        root.appendChild(images);
        images.appendChild(new File(images, "photo1.jpg", 1000, LocalDateTime.now()));
        images.appendChild(new File(images, "photo2.jpg", 2000, LocalDateTime.now()));
        images.appendChild(new File(images, "photo3.jpg", 1500, LocalDateTime.now()));
        return root;
    }
}
