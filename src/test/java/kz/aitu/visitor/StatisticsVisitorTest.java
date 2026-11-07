package kz.aitu.visitor;

import kz.aitu.visitor.model.DirectoryNode;
import kz.aitu.visitor.model.FileNode;
import kz.aitu.visitor.visitor.StatisticsVisitor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StatisticsVisitorTest {

    @Test
    void countsAndDepthForNestedTree() {
        DirectoryNode root = new DirectoryNode("root");
        DirectoryNode a = new DirectoryNode("a");
        DirectoryNode b = new DirectoryNode("b");
        root.add(new FileNode("f1", 1));
        root.add(a);
        a.add(new FileNode("f2", 2));
        a.add(b);
        b.add(new FileNode("f3", 3));

        StatisticsVisitor stats = new StatisticsVisitor();
        stats.collect(root);

        assertEquals(3, stats.getFileCount());
        assertEquals(3, stats.getDirectoryCount());
        assertEquals(3, stats.getMaxDepth());
    }

    @Test
    void flatDirectoryHasDepthOne() {
        DirectoryNode root = new DirectoryNode("root");
        root.add(new FileNode("x", 1));
        root.add(new FileNode("y", 1));

        StatisticsVisitor stats = new StatisticsVisitor();
        stats.collect(root);

        assertEquals(2, stats.getFileCount());
        assertEquals(1, stats.getDirectoryCount());
        assertEquals(1, stats.getMaxDepth());
    }

    @Test
    void emptyRootDirectoryIsCountedWithDepthZero() {
        StatisticsVisitor stats = new StatisticsVisitor();
        stats.collect(new DirectoryNode("empty"));

        assertEquals(0, stats.getFileCount());
        assertEquals(1, stats.getDirectoryCount());
        assertEquals(0, stats.getMaxDepth());
    }

    @Test
    void emptyNestedDirectoriesAreCountedAndAffectDepth() {
        DirectoryNode root = new DirectoryNode("root");
        DirectoryNode a = new DirectoryNode("a");
        root.add(a);
        a.add(new DirectoryNode("b"));

        StatisticsVisitor stats = new StatisticsVisitor();
        stats.collect(root);

        assertEquals(0, stats.getFileCount());
        assertEquals(3, stats.getDirectoryCount());
        assertEquals(2, stats.getMaxDepth());
    }

    @Test
    void fileRootHasOneFileNoDirectoriesDepthZero() {
        StatisticsVisitor stats = new StatisticsVisitor();
        stats.collect(new FileNode("only.txt", 7));

        assertEquals(1, stats.getFileCount());
        assertEquals(0, stats.getDirectoryCount());
        assertEquals(0, stats.getMaxDepth());
    }

    @Test
    void repeatedCollectionDoesNotMultiplyOrLeakState() {
        DirectoryNode big = new DirectoryNode("big");
        DirectoryNode inner = new DirectoryNode("inner");
        big.add(inner);
        inner.add(new FileNode("f", 1));
        DirectoryNode small = new DirectoryNode("small");

        StatisticsVisitor stats = new StatisticsVisitor();
        stats.collect(big);
        stats.collect(big);
        assertEquals(1, stats.getFileCount());
        assertEquals(2, stats.getDirectoryCount());
        assertEquals(2, stats.getMaxDepth());

        stats.collect(small);
        assertEquals(0, stats.getFileCount());
        assertEquals(1, stats.getDirectoryCount());
        assertEquals(0, stats.getMaxDepth());
    }
}