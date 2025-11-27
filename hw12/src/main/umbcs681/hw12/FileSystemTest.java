package umbcs681.hw12;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FileSystemTest {
    
    public static void main(String[] args) {
        Directory root = new Directory(null, "root", LocalDateTime.now());
        File file = new File(root, "file.txt", 100, LocalDateTime.now());
        Link link = new Link(root, "link", file, LocalDateTime.now());
        root.appendChild(file);
        root.appendChild(link);
        
        FileSystem.getFileSystem().appendRootDir(root);
        
        List<DirectoryRunnable> dirRunnables = new ArrayList<>();
        List<FileRunnable> fileRunnables = new ArrayList<>();
        List<LinkRunnable> linkRunnables = new ArrayList<>();
        List<FileSystemRunnable> fsRunnables = new ArrayList<>();
        List<Thread> threads = new ArrayList<>();
        
        for (int i = 0; i < 10; i++) {
            DirectoryRunnable dr = new DirectoryRunnable(root);
            dirRunnables.add(dr);
            threads.add(new Thread(dr));
        }
        
        for (int i = 0; i < 10; i++) {
            FileRunnable fr = new FileRunnable(file);
            fileRunnables.add(fr);
            threads.add(new Thread(fr));
        }
        
        for (int i = 0; i < 10; i++) {
            LinkRunnable lr = new LinkRunnable(link);
            linkRunnables.add(lr);
            threads.add(new Thread(lr));
        }
        
        for (int i = 0; i < 10; i++) {
            FileSystemRunnable fsr = new FileSystemRunnable();
            fsRunnables.add(fsr);
            threads.add(new Thread(fsr));
        }
        
        for (Thread t : threads) {
            t.start();
        }
        
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        for (DirectoryRunnable dr : dirRunnables) {
            dr.setDone();
        }
        for (FileRunnable fr : fileRunnables) {
            fr.setDone();
        }
        for (LinkRunnable lr : linkRunnables) {
            lr.setDone();
        }
        for (FileSystemRunnable fsr : fsRunnables) {
            fsr.setDone();
        }
        
        for (Thread t : threads) {
            t.interrupt();
        }
        
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        System.out.println("All 40 threads terminated.");
    }
}
