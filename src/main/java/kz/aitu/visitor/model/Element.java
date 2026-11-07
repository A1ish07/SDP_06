package kz.aitu.visitor.model;

import kz.aitu.visitor.visitor.Visitor;

public interface Element {

    String getName();

    DirectoryNode getParent();

    void accept(Visitor visitor);
    void assignParent(DirectoryNode parent);
}