package umbcs681.hw2;

import java.time.LocalDateTime;

public class File {
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
} 