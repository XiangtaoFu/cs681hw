package umbcs681.hw7;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class MultiThreadedFileSystemTest {
    private static final int NUM_THREADS = 10;
    private static final int NUM_CALLS_PER_THREAD = 100;
    
    public static void main(String[] args) {
        System.out.println("=== HW7: Thread-Safe FileSystem Singleton Test ===");
        
        // Test 1: Multiple threads calling getFileSystem() concurrently
        testConcurrentFileSystemAccess();
        
        // Test 2: Verify only one instance is created
        testSingletonInstance();
        
        // Test 3: Test file system operations
        testFileSystemOperations();
    }
    
    private static void testConcurrentFileSystemAccess() {
        System.out.println("\n--- Test 1: Concurrent FileSystem Access ---");
        
        CountDownLatch latch = new CountDownLatch(NUM_THREADS);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger errorCount = new AtomicInteger(0);
        
        // Create multiple threads that call getFileSystem() concurrently
        for (int i = 0; i < NUM_THREADS; i++) {
            final int threadId = i;
            Thread thread = new Thread(() -> {
                try {
                    for (int j = 0; j < NUM_CALLS_PER_THREAD; j++) {
                        FileSystem fs = FileSystem.getFileSystem();
                        if (fs != null) {
                            successCount.incrementAndGet();
                        } else {
                            errorCount.incrementAndGet();
                        }
                        
                        // Small delay to increase chance of race conditions
                        Thread.sleep(1);
                    }
                } catch (Exception e) {
                    errorCount.incrementAndGet();
                    System.err.println("Thread " + threadId + " error: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
            thread.start();
        }
        
        try {
            latch.await();
            System.out.println("Total successful calls: " + successCount.get());
            System.out.println("Total errors: " + errorCount.get());
            System.out.println("Expected total calls: " + (NUM_THREADS * NUM_CALLS_PER_THREAD));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    private static void testSingletonInstance() {
        System.out.println("\n--- Test 2: Singleton Instance Verification ---");
        
        // Get multiple references to FileSystem
        FileSystem fs1 = FileSystem.getFileSystem();
        FileSystem fs2 = FileSystem.getFileSystem();
        FileSystem fs3 = FileSystem.getFileSystem();
        
        // Verify they are the same instance
        System.out.println("fs1 == fs2: " + (fs1 == fs2));
        System.out.println("fs2 == fs3: " + (fs2 == fs3));
        System.out.println("fs1 == fs3: " + (fs1 == fs3));
        
        if (fs1 == fs2 && fs2 == fs3) {
            System.out.println("✅ Singleton pattern working correctly!");
        } else {
            System.out.println("❌ Singleton pattern failed!");
        }
    }
    
    private static void testFileSystemOperations() {
        System.out.println("\n--- Test 3: File System Operations ---");
        
        FileSystem fs = FileSystem.getFileSystem();
        
        // Create some directories and files
        LocalDateTime now = LocalDateTime.now();
        Directory rootDir = new Directory(null, "root", now);
        Directory homeDir = new Directory(rootDir, "home", now);
        Directory userDir = new Directory(homeDir, "user", now);
        
        File file1 = new File(userDir, "document.txt", 1024, now);
        File file2 = new File(userDir, "image.jpg", 2048, now);
        
        Link link1 = new Link(userDir, "link_to_doc", file1, now);
        
        // Add files to directories
        userDir.appendChild(file1);
        userDir.appendChild(file2);
        userDir.appendChild(link1);
        
        homeDir.appendChild(userDir);
        rootDir.appendChild(homeDir);
        
        // Add root directory to file system
        fs.appendRootDir(rootDir);
        
        // Display file system structure
        System.out.println("File System Structure:");
        displayDirectory(rootDir, 0);
        
        // Test directory operations
        System.out.println("\nDirectory Statistics:");
        System.out.println("Root directories count: " + fs.getRootDirs().size());
        System.out.println("Root directory children: " + rootDir.countChildren());
        System.out.println("Home directory children: " + homeDir.countChildren());
        System.out.println("User directory children: " + userDir.countChildren());
        System.out.println("User directory files: " + userDir.getFiles().size());
        System.out.println("User directory subdirs: " + userDir.getSubDirectories().size());
        System.out.println("Total size of user directory: " + userDir.getTotalSize() + " bytes");
    }
    
    private static void displayDirectory(Directory dir, int level) {
        String indent = "  ".repeat(level);
        System.out.println(indent + "📁 " + dir.getName() + "/");
        
        for (FSElement child : dir.getChildren()) {
            if (child.isDirectory()) {
                displayDirectory((Directory) child, level + 1);
            } else if (child instanceof Link) {
                Link link = (Link) child;
                System.out.println(indent + "  🔗 " + link.getName() + " -> " + link.getTarget().getName());
            } else {
                System.out.println(indent + "  📄 " + child.getName() + " (" + child.getSize() + " bytes)");
            }
        }
    }
}
