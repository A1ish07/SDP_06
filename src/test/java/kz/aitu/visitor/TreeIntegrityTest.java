package kz.aitu.visitor;

import kz.aitu.visitor.model.DirectoryNode;
import kz.aitu.visitor.model.FileNode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TreeIntegrityTest {

    @Test
    void negativeFileSizeIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new FileNode("bad.txt", -1));
    }

    @Test
    void zeroSizeFileIsAllowed() {
        assertEquals(0L, new FileNode("empty.txt", 0).getSize());
    }

    @Test
    void invalidNamesAreRejected() {
        assertThrows(IllegalArgumentException.class, () -> new FileNode("", 1));
        assertThrows(IllegalArgumentException.class, () -> new FileNode("a/b", 1));
        assertThrows(IllegalArgumentException.class, () -> new DirectoryNode(" "));
        assertThrows(IllegalArgumentException.class, () -> new DirectoryNode("x/y"));
    }

    @Test
    void duplicateSiblingNameIsRejectedAndLeavesRejectedElementDetached() {
        DirectoryNode dir = new DirectoryNode("dir");
        dir.add(new FileNode("same.txt", 1));
        FileNode duplicate = new FileNode("same.txt", 2);

        assertThrows(IllegalArgumentException.class, () -> dir.add(duplicate));

        assertNull(duplicate.getParent());
        assertEquals(1, dir.getChildren().size());
    }

    @Test
    void sameNameInDifferentDirectoriesIsAllowed() {
        DirectoryNode root = new DirectoryNode("root");
        DirectoryNode a = new DirectoryNode("a");
        DirectoryNode b = new DirectoryNode("b");
        root.add(a);
        root.add(b);

        a.add(new FileNode("same.txt", 1));
        b.add(new FileNode("same.txt", 1));

        assertEquals(1, a.getChildren().size());
        assertEquals(1, b.getChildren().size());
    }

    @Test
    void elementCannotHaveTwoParents() {
        DirectoryNode first = new DirectoryNode("first");
        DirectoryNode second = new DirectoryNode("second");
        FileNode file = new FileNode("f.txt", 1);
        first.add(file);

        assertThrows(IllegalStateException.class, () -> second.add(file));

        assertSame(first, file.getParent());
    }

    @Test
    void directoryCannotContainItself() {
        DirectoryNode dir = new DirectoryNode("dir");

        assertThrows(IllegalArgumentException.class, () -> dir.add(dir));
    }

    @Test
    void cyclesAreRejected() {
        DirectoryNode a = new DirectoryNode("a");
        DirectoryNode b = new DirectoryNode("b");
        DirectoryNode c = new DirectoryNode("c");
        a.add(b);
        b.add(c);

        assertThrows(IllegalArgumentException.class, () -> b.add(a));
        assertThrows(IllegalArgumentException.class, () -> c.add(a));
    }

    @Test
    void nullChildIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new DirectoryNode("d").add(null));
    }

    @Test
    void childrenViewIsNotWritable() {
        DirectoryNode dir = new DirectoryNode("dir");
        dir.add(new FileNode("f.txt", 1));

        assertThrows(UnsupportedOperationException.class,
                () -> dir.getChildren().add(new FileNode("hack.txt", 1)));
    }

    @Test
    void deepValidNestingIsSupported() {
        DirectoryNode root = new DirectoryNode("root");
        DirectoryNode current = root;
        for (int i = 0; i < 50; i++) {
            DirectoryNode next = new DirectoryNode("level" + i);
            current.add(next);
            current = next;
        }
        current.add(new FileNode("leaf.txt", 1));

        assertEquals(1, current.getChildren().size());
        assertSame(current, current.getChildren().get(0).getParent());
    }
}