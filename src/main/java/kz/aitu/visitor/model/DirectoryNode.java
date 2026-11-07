package kz.aitu.visitor.model;

import kz.aitu.visitor.visitor.Visitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DirectoryNode implements Element {

    private final String name;
    private final Map<String, Element> children = new LinkedHashMap<>();
    private DirectoryNode parent;

    public DirectoryNode(String name) {
        NameValidator.validate(name);
        this.name = name;
    }

    public void add(Element child) {
        if (child == null) {
            throw new IllegalArgumentException("Child must not be null");
        }
        if (child.getParent() != null) {
            throw new IllegalStateException("Element already has a parent: " + child.getName());
        }
        if (children.containsKey(child.getName())) {
            throw new IllegalArgumentException("Duplicate name in directory: " + child.getName());
        }
        if (child == this || isDescendantOf(child)) {
            throw new IllegalArgumentException("Cycle detected: " + child.getName());
        }
        children.put(child.getName(), child);
        child.assignParent(this);
    }

    public List<Element> getChildren() {
        return Collections.unmodifiableList(new ArrayList<>(children.values()));
    }

    private boolean isDescendantOf(Element candidate) {
        DirectoryNode current = this.parent;
        while (current != null) {
            if (current == candidate) {
                return true;
            }
            current = current.parent;
        }
        return false;
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