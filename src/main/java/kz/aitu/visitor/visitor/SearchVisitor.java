package kz.aitu.visitor.visitor;

import kz.aitu.visitor.model.DirectoryNode;
import kz.aitu.visitor.model.Element;
import kz.aitu.visitor.model.FileNode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.function.Predicate;

public class SearchVisitor implements Visitor {

    private final Predicate<String> nameCriterion;
    private final long minSize;

    private final List<String> matches = new ArrayList<>();
    private final Deque<String> pathSegments = new ArrayDeque<>();
    private Element root;

    public SearchVisitor(Predicate<String> nameCriterion, long minSize) {
        if (nameCriterion == null) {
            throw new IllegalArgumentException("Name criterion must not be null");
        }
        if (minSize < 0) {
            throw new IllegalArgumentException("Minimum size must not be negative: " + minSize);
        }
        this.nameCriterion = nameCriterion;
        this.minSize = minSize;
    }

    public List<String> search(Element root) {
        reset(root);
        root.accept(this);
        List<String> sorted = new ArrayList<>(matches);
        Collections.sort(sorted);
        return Collections.unmodifiableList(sorted);
    }

    @Override
    public void visit(FileNode file) {
        if (matches(file)) {
            matches.add(buildPath(file.getName()));
        }
    }

    @Override
    public void visit(DirectoryNode directory) {
        boolean isRoot = directory == root;
        if (!isRoot) {
            pathSegments.addLast(directory.getName());
        }
        for (Element child : directory.getChildren()) {
            child.accept(this);
        }
        if (!isRoot) {
            pathSegments.removeLast();
        }
    }

    private boolean matches(FileNode file) {
        return nameCriterion.test(file.getName()) && file.getSize() >= minSize;
    }

    private String buildPath(String fileName) {
        List<String> parts = new ArrayList<>(pathSegments);
        parts.add(fileName);
        return String.join("/", parts);
    }

    private void reset(Element newRoot) {
        root = newRoot;
        matches.clear();
        pathSegments.clear();
    }
}