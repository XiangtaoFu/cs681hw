package umbcs681.hw1;

import java.time.LocalDateTime;

public class File implements FSElement {
    private String name;
    private int size;
    private LocalDateTime creationTime;
    
    public File(String name, int size, LocalDateTime creationTime) {
        this.name = name;
        this.size = size;
        this.creationTime = creationTime;
    }
    
    public String getName() {
        return name;
    }
    
    public int getSize() {
        return size;
    }
    
    public LocalDateTime getCreationTime() {
        return creationTime;
    }
    
    @Override
    public void accept(FSVisitor v) {
        v.visit(this);
    }
}
