package umbcs681.hw13;

import java.util.LinkedList;

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
}
