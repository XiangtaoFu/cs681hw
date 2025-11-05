package umbcs681.hw2;

import java.time.LocalDateTime;
import java.util.LinkedList;

public class Directory implements FSElement {
    private String name;
    private LocalDateTime creationTime;
    private LinkedList<FSElement> children;
    
    public Directory(String name, LocalDateTime creationTime) {
        this.name = name;
        this.creationTime = creationTime;
        this.children = new LinkedList<>();
    }
    
    public String getName() {
        return name;
    }
    
    public LocalDateTime getCreationTime() {
        return creationTime;
    }
    
    public LinkedList<FSElement> getChildren() {
        return children;
    }
    
    public void appendChild(FSElement child) {
        children.add(child);
    }
    
    @Override
    public void accept(FSVisitor v) {
        v.visit(this);
        for (FSElement child : children) {
            child.accept(v);
        }
    }
}