package kz.aitu.visitor;

import kz.aitu.visitor.model.DirectoryNode;
import kz.aitu.visitor.model.FileNode;
import kz.aitu.visitor.visitor.TotalSizeVisitor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TotalSizeVisitorTest {

    private DirectoryNode root;
    private DirectoryNode a;

    private void buildTree() {
        root = new DirectoryNode("root");
        a = new DirectoryNode("a");
        DirectoryNode b = new DirectoryNode("b");
        root.add(new FileNode("r.bin", 100));
        root.add(a);
        a.add(new FileNode("a.bin", 200));
        a.add(b);
        b.add(new FileNode("b.bin", 300));
    }

    @Test
    void sumsFilesAcrossAllNestingLevels() {
        buildTree();

        assertEquals(600L, new TotalSizeVisitor().calculate(root));
    }

    @Test
    void subtreeSumCoversOnlyThatSubtree() {
        buildTree();

        assertEquals(500L, new TotalSizeVisitor().calculate(a));
    }

    @Test
    void emptyDirectoryHasZeroSize() {
        assertEquals(0L, new TotalSizeVisitor().calculate(new DirectoryNode("empty")));
    }

    @Test
    void singleFileRootReturnsItsOwnSize() {
        assertEquals(42L, new TotalSizeVisitor().calculate(new FileNode("one.txt", 42)));
    }

    @Test
    void repeatedRunsDoNotAccumulateState() {
        buildTree();
        TotalSizeVisitor visitor = new TotalSizeVisitor();

        long first = visitor.calculate(root);
        long second = visitor.calculate(root);
        long smaller = visitor.calculate(a);
        long again = visitor.calculate(root);

        assertEquals(600L, first);
        assertEquals(600L, second);
        assertEquals(500L, smaller);
        assertEquals(600L, again);
    }
}