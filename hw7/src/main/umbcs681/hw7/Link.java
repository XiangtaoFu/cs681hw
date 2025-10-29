package umbcs681.hw7;

import java.time.LocalDateTime;

public class Link extends FSElement {
    private FSElement target;
    
    public Link(Directory parent, String name, FSElement target, LocalDateTime creationTime) {
        super(parent, name, 0, creationTime); // Link size is always 0
        this.target = target;
    }
    
    @Override
    public boolean isDirectory() {
        return false;
    }
    
    public FSElement getTarget() {
        return target;
    }
    
    public void setTarget(FSElement target) {
        this.target = target;
    }
}
