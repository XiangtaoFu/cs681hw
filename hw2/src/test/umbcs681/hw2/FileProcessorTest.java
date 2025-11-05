package umbcs681.hw2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Map;
import java.util.DoubleSummaryStatistics;
import java.time.LocalDateTime;

public class FileProcessorTest {

    private Directory rootDir;
    private LocalDateTime now;

    @BeforeEach
    public void setUp() {
        now = LocalDateTime.now();
        rootDir = new Directory("root", now);
        
        rootDir.appendChild(new File("Test1.java", 100, now));
        rootDir.appendChild(new File("Test2.java", 200, now));
        rootDir.appendChild(new File("readme.txt", 50, now));
        rootDir.appendChild(new File("notes.txt", 150, now));
        rootDir.appendChild(new File("document.pdf", 500, now));
        rootDir.appendChild(new File("README", 75, now));
    }

    @Test
    public void testGetFileStatsByExtension() {
        Map<String, DoubleSummaryStatistics> stats = FileProcessor.getFileStatsByExtension(rootDir);
        
        assertEquals(4, stats.size());
        
        assertTrue(stats.containsKey("java"), "Should contain java extension");
        assertTrue(stats.containsKey("txt"), "Should contain txt extension");
        assertTrue(stats.containsKey("pdf"), "Should contain pdf extension");
        assertTrue(stats.containsKey("no_extension"), "Should contain no_extension category");
        
        DoubleSummaryStatistics javaStats = stats.get("java");
        assertEquals(2, javaStats.getCount());
        assertEquals(100, javaStats.getMin());
        assertEquals(200, javaStats.getMax());
        assertEquals(150, javaStats.getAverage());
        assertEquals(300, javaStats.getSum());
        
        DoubleSummaryStatistics txtStats = stats.get("txt");
        assertEquals(2, txtStats.getCount());
        assertEquals(50, txtStats.getMin());
        assertEquals(150, txtStats.getMax());
        assertEquals(100, txtStats.getAverage());
        assertEquals(200, txtStats.getSum());
        
        DoubleSummaryStatistics pdfStats = stats.get("pdf");
        assertEquals(1, pdfStats.getCount());
        assertEquals(500, pdfStats.getSum());
        
        DoubleSummaryStatistics noExtStats = stats.get("no_extension");
        assertEquals(1, noExtStats.getCount());
        assertEquals(75, noExtStats.getSum());
    }

    @Test
    public void testGetFileCountsByExtension() {
        Map<String, Integer> counts = FileProcessor.getFileCountsByExtension(rootDir);
        
        assertEquals(4, counts.size());
        assertEquals(2, counts.get("java").intValue());
        assertEquals(2, counts.get("txt").intValue());
        assertEquals(1, counts.get("pdf").intValue());
        assertEquals(1, counts.get("no_extension").intValue());
    }

    @Test
    public void testGetTotalFilesSize() {
        int totalSize = FileProcessor.getTotalFilesSize(rootDir);
        assertEquals(1075, totalSize);
    }

    @Test
    public void testWithNestedDirectories() {
        Directory subDir = new Directory("subdir", now);
        subDir.appendChild(new File("nested.java", 300, now));
        rootDir.appendChild(subDir);
        
        Map<String, DoubleSummaryStatistics> stats = FileProcessor.getFileStatsByExtension(rootDir);
        DoubleSummaryStatistics javaStats = stats.get("java");
        assertEquals(3L, javaStats.getCount());
        assertEquals(600.0, javaStats.getSum(), 0.001);
    }
}
