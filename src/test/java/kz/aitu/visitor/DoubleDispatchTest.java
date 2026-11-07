package kz.aitu.visitor;

import kz.aitu.visitor.model.DirectoryNode;
import kz.aitu.visitor.model.Element;
import kz.aitu.visitor.model.FileNode;
import kz.aitu.visitor.visitor.Visitor;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DoubleDispatchTest {

    private static class RecordingVisitor implements Visitor {
        final List<String> calls = new ArrayList<>();

        @Override
        public void visit(FileNode file) {
            calls.add("file:" + file.getName());
        }

        @Override
        public void visit(DirectoryNode directory) {
            calls.add("dir:" + directory.getName());
        }
    }

    @Test
    void fileAcceptReachesFileOverload() {
        RecordingVisitor visitor = new RecordingVisitor();

        new FileNode("a.txt", 10).accept(visitor);

        assertEquals(List.of("file:a.txt"), visitor.calls);
    }

    @Test
    void directoryAcceptReachesDirectoryOverload() {
        RecordingVisitor visitor = new RecordingVisitor();

        new DirectoryNode("docs").accept(visitor);

        assertEquals(List.of("dir:docs"), visitor.calls);
    }

    @Test
    void dispatchDependsOnRuntimeTypeBehindElementReference() {
        RecordingVisitor visitor = new RecordingVisitor();
        Element file = new FileNode("x.bin", 1);
        Element directory = new DirectoryNode("d");

        file.accept(visitor);
        directory.accept(visitor);

        assertEquals(List.of("file:x.bin", "dir:d"), visitor.calls);
    }
}