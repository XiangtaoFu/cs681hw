package umbcs681.hw2;

import java.util.*;
import java.util.stream.Collectors;
import java.util.DoubleSummaryStatistics;
import java.time.LocalDateTime;

public class FileProcessor {
    // Group files by extension and get SummaryStatistics for each group
    public static Map<String, DoubleSummaryStatistics> getFileStatsByExtension(List<File> files) {
        return files.stream()
            .collect(Collectors.groupingBy(
                // Group by file extension
                file -> {
                    String name = file.getName();
                    int lastDotIndex = name.lastIndexOf('.');
                    return lastDotIndex > 0 ? name.substring(lastDotIndex + 1) : "no_extension";
                },
                // Calculate SummaryStatistics on file sizes
                Collectors.summarizingDouble(File::getSize)
            ));
    }

    public static void main(String[] args) {
        LocalDateTime now = LocalDateTime.now();
        List<File> files = Arrays.asList(
                new File("Test1.java", 100, now),
                new File("Test2.java", 200, now),
                new File("readme.txt", 50, now),
                new File("notes.txt", 150, now),
                new File("document.pdf", 500, now),
                new File("README", 75, now)
        );

        Map<String, DoubleSummaryStatistics> stats = getFileStatsByExtension(files);
        
        System.out.println("File Statistics by Extension:");
        stats.forEach((extension, statistics) -> {
            System.out.println(extension + ": " + statistics);
        });
    }
}
