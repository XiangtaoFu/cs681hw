package umbcs681.hw14;

public interface FSVisitor {
    void visit(Directory directory);
    void visit(File file);
    void visit(Link link);
}
