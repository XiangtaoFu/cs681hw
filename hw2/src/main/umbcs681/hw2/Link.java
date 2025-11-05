package umbcs681.hw2;

import java.time.LocalDateTime;

public class Link implements FSElement {
    private String name;
    private LocalDateTime creationTime;
    private FSElement target;
    
    public Link(String name, LocalDateTime creationTime, FSElement target) {
        this.name = name;
        this.creationTime = creationTime;
        this.target = target;
    }
    
    public String getName() {
        return name;
    }
    
    public LocalDateTime getCreationTime() {
        return creationTime;
    }
    
    public FSElement getTarget() {
        return target;
    }
    
    @Override
    public void accept(FSVisitor v) {
        v.visit(this);
    }
}