package umbcs681.hw1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class DirectoryMethodsTest {

    private Directory rootDir;
    private Directory subDir1, subDir2;
    private File file1, file2, file3;
    private Link link1;
    private LocalDateTime now;

    @BeforeEach
    public void setUp() {
        now = LocalDateTime.now();
        rootDir = new Directory("root", now);
        
        subDir1 = new Directory("documents", now);
        subDir2 = new Directory("images", now);
        
        file1 = new File("readme.txt", 100, now);
        file2 = new File("config.xml", 200, now);
        file3 = new File("data.json", 150, now);
        
        link1 = new Link("shortcut", now, file1);
        
        rootDir.appendChild(subDir1);
        rootDir.appendChild(subDir2);
        rootDir.appendChild(file1);
        rootDir.appendChild(file2);
        rootDir.appendChild(file3);
        rootDir.appendChild(link1);
    }

    @Test
    public void testGetSubDirectories() {
        LinkedList<Directory> subDirs = rootDir.getSubDirectories();
        
        assertEquals(2, subDirs.size());
        assertTrue(subDirs.contains(subDir1));
        assertTrue(subDirs.contains(subDir2));
    }

    @Test
    public void testGetFiles() {
        LinkedList<File> files = rootDir.getFiles();
        
        assertEquals(3, files.size());
        assertTrue(files.contains(file1));
        assertTrue(files.contains(file2));
        assertTrue(files.contains(file3));
    }

    @Test
    public void testGetLinks() {
        LinkedList<Link> links = rootDir.getLinks();
        
        assertEquals(1, links.size());
        assertTrue(links.contains(link1));
    }

    @Test
    public void testIterateChildren() {
        Stream<FSElement> childStream = rootDir.iterateChildren();
        
        long count = childStream.count();
        assertEquals(6, count);
    }

    @Test
    public void testIterateFiles() {
        Stream<File> fileStream = rootDir.iterateFiles();
        
        long fileCount = fileStream.count();
        assertEquals(3, fileCount);
    }

    @Test
    public void testIterateChildrenFiltering() {
        long dirCount = rootDir.iterateChildren()
            .filter(element -> element instanceof Directory)
            .count();
        
        assertEquals(2, dirCount);
    }

    @Test
    public void testIterateFilesOperations() {
        int totalSize = rootDir.iterateFiles()
            .mapToInt(File::getSize)
            .sum();
        
        assertEquals(450, totalSize);
    }

    @Test
    public void testEmptyDirectory() {
        Directory emptyDir = new Directory("empty", now);
        
        assertEquals(0, emptyDir.getSubDirectories().size());
        assertEquals(0, emptyDir.getFiles().size());
        assertEquals(0, emptyDir.getLinks().size());
        assertEquals(0, emptyDir.iterateChildren().count());
        assertEquals(0, emptyDir.iterateFiles().count());
    }
}