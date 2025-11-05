package umbcs681.hw1;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    
    public LinkedList<Directory> getSubDirectories() {
        return children.stream()
            .filter(element -> element instanceof Directory)
            .map(element -> (Directory) element)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    public LinkedList<File> getFiles() {
        return children.stream()
            .filter(element -> element instanceof File)
            .map(element -> (File) element)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    public LinkedList<Link> getLinks() {
        return children.stream()
            .filter(element -> element instanceof Link)
            .map(element -> (Link) element)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    public Stream<FSElement> iterateChildren() {
        return Stream.iterate(0, i -> i < children.size(), i -> i + 1)
            .map(children::get);
    }

    public Stream<File> iterateFiles() {
        return iterateChildren()
            .filter(element -> element instanceof File)
            .map(element -> (File) element);
    }

    @Override
    public void accept(FSVisitor v) {
        v.visit(this);
        for (FSElement child : children) {
            child.accept(v);
        }
    }
}
