package kz.aitu.visitor.model;

import kz.aitu.visitor.visitor.Visitor;

public class FileNode implements Element {

    private final String name;
    private final long size;
    private DirectoryNode parent;

    public FileNode(String name, long size) {
        NameValidator.validate(name);
        if (size < 0) {
            throw new IllegalArgumentException("File size must not be negative: " + size);
        }
        this.name = name;
        this.size = size;
    }

    public long getSize() {
        return size;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public DirectoryNode getParent() {
        return parent;
    }

    @Override
    public void assignParent(DirectoryNode newParent) {
        this.parent = newParent;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}