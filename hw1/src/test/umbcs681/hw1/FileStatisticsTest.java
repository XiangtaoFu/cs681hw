package umbcs681.hw1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.DoubleSummaryStatistics;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class FileStatisticsTest {

    private Directory rootDir;
    private FileCrawlingVisitor visitor;

    @BeforeEach
    public void setUp() {
        LocalDateTime now = LocalDateTime.now();
        
        rootDir = new Directory("root", now);
        
        File javaFile1 = new File("Test1.java", 100, now);
        File javaFile2 = new File("Test2.java", 200, now);
        File txtFile1 = new File("readme.txt", 50, now);
        File txtFile2 = new File("notes.txt", 150, now);
        File pdfFile = new File("document.pdf", 500, now);
        File noExtFile = new File("README", 75, now);
        
        rootDir.appendChild(javaFile1);
        rootDir.appendChild(javaFile2);
        rootDir.appendChild(txtFile1);
        rootDir.appendChild(txtFile2);
        rootDir.appendChild(pdfFile);
        rootDir.appendChild(noExtFile);
        
        visitor = new FileCrawlingVisitor();
        rootDir.accept(visitor);
    }

    @Test
    public void testFileGroupingByExtension() {
        Map<String, DoubleSummaryStatistics> stats = visitor.getFileStatsByExtension();
        
        assertEquals(4, stats.size());
        
        assertTrue(stats.containsKey("java"));
        assertTrue(stats.containsKey("txt"));
        assertTrue(stats.containsKey("pdf"));
        assertTrue(stats.containsKey("no_extension"));
        
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
} 