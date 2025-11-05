package umbcs681.hw1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class FileCrawlingVisitorTest {

    private Directory rootDir;
    private File javaFile1, javaFile2, txtFile;
    private LocalDateTime cutoffDate;
    private FileCrawlingVisitor visitor;

    @BeforeEach
    public void setUp() {
        cutoffDate = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime beforeCutoff = LocalDateTime.of(2022, 1, 1, 0, 0);
        LocalDateTime afterCutoff = LocalDateTime.of(2024, 1, 1, 0, 0);
        rootDir = new Directory("root", afterCutoff);
        javaFile1 = new File("Test1.java", 100, beforeCutoff);
        javaFile2 = new File("Test2.java", 200, afterCutoff);
        txtFile = new File("readme.txt", 150, afterCutoff);
        rootDir.appendChild(javaFile1);
        rootDir.appendChild(javaFile2);
        rootDir.appendChild(txtFile);
        visitor = new FileCrawlingVisitor();
        rootDir.accept(visitor);
    }

    @Test
    public void testFilesMethodReturnsStream() {
        // Test that the files() method returns a Stream<File>
        Stream<File> fileStream = visitor.files();
        assertNotNull(fileStream, "The files() method should return non-null");
    }
    
    @Test
    public void testFilesMethodContainsAllFiles() {
        // Test that the stream contains all the files we added
        long count = visitor.files().count();
        assertEquals(3, count, "The stream should contain all 3 files");
    }
    
    @Test
    public void testFilterFilesByExtension() {
        long javaFilesCount = visitor.files()
            .filter(file -> file.getName().endsWith(".java"))
            .count();
        assertEquals(2, javaFilesCount);
    }
    
    @Test
    public void testFilterFilesByCreationTime() {
        long filesAfterCutoff = visitor.files()
            .filter(file -> file.getCreationTime().isAfter(cutoffDate))
            .count();
        assertEquals(2, filesAfterCutoff);
    }
    
    @Test
    public void testCombinedFiltering() {
        long javaFilesAfterCutoff = visitor.files()
            .filter(file -> file.getName().endsWith(".java"))
            .filter(file -> file.getCreationTime().isAfter(cutoffDate))
            .count();
        assertEquals(1, javaFilesAfterCutoff);
    }
} 