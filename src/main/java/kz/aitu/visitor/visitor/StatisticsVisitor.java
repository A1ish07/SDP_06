package kz.aitu.visitor.visitor;

import kz.aitu.visitor.model.DirectoryNode;
import kz.aitu.visitor.model.Element;
import kz.aitu.visitor.model.FileNode;

public class StatisticsVisitor implements Visitor {

    private int fileCount;
    private int directoryCount;
    private int maxDepth;
    private int currentDepth;

    public void collect(Element root) {
        reset();
        root.accept(this);
    }

    @Override
    public void visit(FileNode file) {
        fileCount++;
        updateMaxDepth();
    }

    @Override
    public void visit(DirectoryNode directory) {
        directoryCount++;
        updateMaxDepth();
        currentDepth++;
        for (Element child : directory.getChildren()) {
            child.accept(this);
        }
        currentDepth--;
    }

    public int getFileCount() {
        return fileCount;
    }

    public int getDirectoryCount() {
        return directoryCount;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    private void updateMaxDepth() {
        maxDepth = Math.max(maxDepth, currentDepth);
    }

    private void reset() {
        fileCount = 0;
        directoryCount = 0;
        maxDepth = 0;
        currentDepth = 0;
    }
}