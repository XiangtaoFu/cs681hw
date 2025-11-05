package umbcs681.hw2;

import java.util.*;
import java.util.stream.Collectors;
import java.util.DoubleSummaryStatistics;
import java.time.LocalDateTime;

public class FileProcessor {
    public static Map<String, DoubleSummaryStatistics> getFileStatsByExtension(Directory rootDir) {
        FileCrawlingVisitor visitor = new FileCrawlingVisitor();
        rootDir.accept(visitor);
        return visitor.getFiles().stream()
            .collect(Collectors.groupingBy(
                file -> {
                    String name = file.getName();
                    int lastDotIndex = name.lastIndexOf('.');
                    return lastDotIndex > 0 ? name.substring(lastDotIndex + 1) : "no_extension";
                },
                Collectors.summarizingDouble(File::getSize)
            ));
    }

    public static Map<String, Integer> getFileCountsByExtension(Directory rootDir) {
        FileCrawlingVisitor visitor = new FileCrawlingVisitor();
        rootDir.accept(visitor);
        return visitor.getFiles().stream()
            .collect(Collectors.groupingBy(
                file -> {
                    String name = file.getName();
                    int lastDotIndex = name.lastIndexOf('.');
                    return lastDotIndex > 0 ? name.substring(lastDotIndex + 1) : "no_extension";
                },
                Collectors.reducing(0, e -> 1, Integer::sum)
            ));
    }

    public static int getTotalFilesSize(Directory rootDir) {
        FileCrawlingVisitor visitor = new FileCrawlingVisitor();
        rootDir.accept(visitor);
        return visitor.getFiles().stream()
            .map(File::getSize)
            .reduce(0, Integer::sum);
    }

    public static void main(String[] args) {
        LocalDateTime now = LocalDateTime.now();
        Directory rootDir = new Directory("root", now);
        
        rootDir.appendChild(new File("Test1.java", 100, now));
        rootDir.appendChild(new File("Test2.java", 200, now));
        rootDir.appendChild(new File("readme.txt", 50, now));
        rootDir.appendChild(new File("notes.txt", 150, now));
        rootDir.appendChild(new File("document.pdf", 500, now));
        rootDir.appendChild(new File("README", 75, now));

        Map<String, DoubleSummaryStatistics> stats = getFileStatsByExtension(rootDir);
        
        System.out.println("File Statistics by Extension:");
        stats.forEach((extension, statistics) -> {
            System.out.println(extension + ": " + statistics);
        });

        System.out.println("\nFile Counts by Extension:");
        Map<String, Integer> counts = getFileCountsByExtension(rootDir);
        counts.forEach((extension, count) -> {
            System.out.println(extension + ": " + count + " files");
        });

        System.out.println("\nTotal Files Size: " + getTotalFilesSize(rootDir) + " bytes");
    }
}
