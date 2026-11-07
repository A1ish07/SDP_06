package kz.aitu.visitor;

import kz.aitu.visitor.model.DirectoryNode;
import kz.aitu.visitor.model.FileNode;
import kz.aitu.visitor.visitor.SearchVisitor;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SearchVisitorTest {

    private DirectoryNode buildTree() {
        DirectoryNode root = new DirectoryNode("root");
        DirectoryNode sub = new DirectoryNode("sub");
        DirectoryNode deep = new DirectoryNode("deep");
        // children are added in "wrong" order on purpose to test sorting
        root.add(sub);
        root.add(new FileNode("b.txt", 1000));
        root.add(new FileNode("a.txt", 100));
        sub.add(deep);
        sub.add(new FileNode("d.log", 5000));
        sub.add(new FileNode("c.txt", 1000));
        deep.add(new FileNode("e.txt", 1000));
        return root;
    }

    @Test
    void combinesNameAndSizeConditions() {
        SearchVisitor search = new SearchVisitor(name -> name.endsWith(".txt"), 1000);

        assertEquals(List.of("b.txt", "sub/c.txt", "sub/deep/e.txt"), search.search(buildTree()));
    }

    @Test
    void nameConditionAloneWithZeroThreshold() {
        SearchVisitor search = new SearchVisitor(name -> name.endsWith(".txt"), 0);

        assertEquals(List.of("a.txt", "b.txt", "sub/c.txt", "sub/deep/e.txt"), search.search(buildTree()));
    }

    @Test
    void sizeConditionAloneKeepsEveryName() {
        SearchVisitor search = new SearchVisitor(name -> true, 1000);

        assertEquals(List.of("b.txt", "sub/c.txt", "sub/d.log", "sub/deep/e.txt"), search.search(buildTree()));
    }

    @Test
    void thresholdIsInclusive() {
        DirectoryNode tree = buildTree();

        assertEquals(List.of("sub/d.log"), new SearchVisitor(name -> true, 5000).search(tree));
        assertTrue(new SearchVisitor(name -> true, 5001).search(tree).isEmpty());
    }

    @Test
    void pathsAreRootRelativeSlashSeparatedAndSorted() {
        List<String> paths = new SearchVisitor(name -> true, 0).search(buildTree());

        assertEquals(
                List.of("a.txt", "b.txt", "sub/c.txt", "sub/d.log", "sub/deep/e.txt"),
                paths);
    }

    @Test
    void noMatchReturnsEmptyList() {
        SearchVisitor search = new SearchVisitor(name -> name.endsWith(".exe"), 0);

        assertTrue(search.search(buildTree()).isEmpty());
    }

    @Test
    void emptyDirectoryReturnsEmptyList() {
        SearchVisitor search = new SearchVisitor(name -> true, 0);

        assertTrue(search.search(new DirectoryNode("empty")).isEmpty());
    }

    @Test
    void fileRootProducesItsOwnName() {
        SearchVisitor search = new SearchVisitor(name -> true, 0);

        assertEquals(List.of("solo.txt"), search.search(new FileNode("solo.txt", 5)));
    }

    @Test
    void repeatedSearchesDoNotLeakResultsOrPath() {
        SearchVisitor search = new SearchVisitor(name -> name.endsWith(".txt"), 1000);
        DirectoryNode tree = buildTree();

        List<String> first = search.search(tree);
        List<String> second = search.search(tree);
        List<String> other = search.search(new DirectoryNode("empty"));

        assertEquals(first, second);
        assertEquals(3, second.size());
        assertTrue(other.isEmpty());
    }

    @Test
    void invalidConfigurationIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new SearchVisitor(null, 0));
        assertThrows(IllegalArgumentException.class, () -> new SearchVisitor(name -> true, -1));
    }
}