package umbcs681.hw2;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.DoubleSummaryStatistics;
import java.time.LocalDateTime;

public class FileProcessorTest {

    @Test
    public void testGetFileStatsByExtension() {
        LocalDateTime now = LocalDateTime.now();
        List<File> files = Arrays.asList(
                new File("Test1.java", 100, now),
                new File("Test2.java", 200, now),
                new File("readme.txt", 50, now),
                new File("notes.txt", 150, now),
                new File("document.pdf", 500, now),
                new File("README", 75, now)
        );

        Map<String, DoubleSummaryStatistics> stats = FileProcessor.getFileStatsByExtension(files);
        
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
}
