package umbcs681.hw1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class StreamIterateTest {

    private Directory rootDir;
    private Directory subDir1, subDir2;
    private File txtFile, xmlFile, jsonFile;
    private Link link1;
    private LocalDateTime now;

    @BeforeEach
    public void setUp() {
        now = LocalDateTime.now();
        rootDir = new Directory("root", now);
        
        subDir1 = new Directory("docs", now);
        subDir2 = new Directory("media", now);
        
        txtFile = new File("readme.txt", 100, now);
        xmlFile = new File("config.xml", 200, now);
        jsonFile = new File("data.json", 300, now);
        
        link1 = new Link("shortcut", now, txtFile);
        
        rootDir.appendChild(subDir1);
        rootDir.appendChild(subDir2);
        rootDir.appendChild(txtFile);
        rootDir.appendChild(xmlFile);
        rootDir.appendChild(jsonFile);
        rootDir.appendChild(link1);
    }

    @Test
    public void testIterateChildrenToList() {
        List<FSElement> children = rootDir.iterateChildren()
            .collect(Collectors.toList());
        
        assertEquals(6, children.size());
        assertTrue(children.contains(subDir1));
        assertTrue(children.contains(txtFile));
        assertTrue(children.contains(link1));
    }

    @Test
    public void testIterateFilesWithFilter() {
        List<File> largeFiles = rootDir.iterateFiles()
            .filter(file -> file.getSize() > 150)
            .collect(Collectors.toList());
        
        assertEquals(2, largeFiles.size());
        assertTrue(largeFiles.contains(xmlFile));
        assertTrue(largeFiles.contains(jsonFile));
    }

    @Test
    public void testIterateFilesWithMap() {
        List<String> fileNames = rootDir.iterateFiles()
            .map(File::getName)
            .collect(Collectors.toList());
        
        assertEquals(3, fileNames.size());
        assertTrue(fileNames.contains("readme.txt"));
        assertTrue(fileNames.contains("config.xml"));
        assertTrue(fileNames.contains("data.json"));
    }

    @Test
    public void testIterateChildrenParallelProcessing() {
        long directoryCount = rootDir.iterateChildren()
            .parallel()
            .filter(element -> element instanceof Directory)
            .count();
        
        assertEquals(2, directoryCount);
    }

    @Test
    public void testIterateFilesStatistics() {
        int totalSize = rootDir.iterateFiles()
            .mapToInt(File::getSize)
            .sum();
        
        assertEquals(600, totalSize);
        
        int maxSize = rootDir.iterateFiles()
            .mapToInt(File::getSize)
            .max()
            .orElse(0);
        
        assertEquals(300, maxSize);
    }

    @Test
    public void testIterateWithLimitAndSkip() {
        List<FSElement> limitedChildren = rootDir.iterateChildren()
            .skip(1)
            .limit(3)
            .collect(Collectors.toList());
        
        assertEquals(3, limitedChildren.size());
    }

    @Test
    public void testIterateFilesGrouping() {
        long fileCount = rootDir.iterateFiles().count();
        long directoryCount = rootDir.iterateChildren()
            .filter(element -> element instanceof Directory)
            .count();
        
        assertEquals(3, fileCount);
        assertEquals(2, directoryCount);
        
        long totalElements = rootDir.iterateChildren().count();
        assertEquals(6, totalElements);
    }
}