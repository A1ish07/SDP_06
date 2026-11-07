package kz.aitu.visitor.visitor;

import kz.aitu.visitor.model.DirectoryNode;
import kz.aitu.visitor.model.Element;
import kz.aitu.visitor.model.FileNode;

public class TotalSizeVisitor implements Visitor {

    private long total;

    public long calculate(Element root) {
        total = 0;
        root.accept(this);
        return total;
    }

    @Override
    public void visit(FileNode file) {
        total += file.getSize();
    }

    @Override
    public void visit(DirectoryNode directory) {
        for (Element child : directory.getChildren()) {
            child.accept(this);
        }
    }
}