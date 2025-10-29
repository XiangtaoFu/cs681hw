package umbcs681.hw1;

import java.util.LinkedList;
import java.util.Map;
import java.util.DoubleSummaryStatistics;
import java.util.stream.Stream;
import java.util.stream.Collectors;

public class FileCrawlingVisitor implements FSVisitor {
    private LinkedList<File> files = new LinkedList<>();
    
    @Override
    public void visit(Link link) {
        
    }
    
    @Override
    public void visit(Directory dir) {
        
    }
    
    @Override
    public void visit(File file) {
        
        files.add(file);
    }
    
    public LinkedList<File> getFiles() {
        return files;
    }
    
    // New method that returns a stream of File objects
    public Stream<File> files() {
        return files.stream();
    }
    
    // New method for HW 1-2: Group files by extension and calculate size statistics
    public Map<String, DoubleSummaryStatistics> getFileStatsByExtension() {
        return files.stream()
            .collect(Collectors.groupingBy(
                // Group by file extension
                file -> {
                    String name = file.getName();
                    int lastDotIndex = name.lastIndexOf('.');
                    return lastDotIndex > 0 ? name.substring(lastDotIndex + 1) : "no_extension";
                },
                // Calculate statistics on file sizes
                Collectors.summarizingDouble(File::getSize)
            ));
    }
}
