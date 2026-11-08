## Assignment #6 Alimbayev Alisher


## Structure

```
SDP_06
├── pom.xml
├── README.md
├── .gitignore
└── src
    ├── main/java/kz/aitu/visitor
    │   ├── Main.java                   demonstration (deterministic sample tree)
    │   ├── model
    │   │   ├── Element.java            common interface: accept(Visitor)
    │   │   ├── FileNode.java           leaf: name + non-negative size
    │   │   ├── DirectoryNode.java      composite: named children, tree invariants
    │   │   └── NameValidator.java      shared name validation
    │   └── visitor
    │       ├── Visitor.java            visit(FileNode), visit(DirectoryNode)
    │       ├── TotalSizeVisitor.java   sum of file sizes in a subtree
    │       ├── SearchVisitor.java      name criterion + min size, sorted relative paths
    │       └── StatisticsVisitor.java  file count, directory count, max depth
    └── test/java/kz/aitu/visitor
        ├── DoubleDispatchTest.java     T1
        ├── TotalSizeVisitorTest.java   T2, T6, T7
        ├── SearchVisitorTest.java      T3, T4, T6, T7
        ├── StatisticsVisitorTest.java  T5, T6, T7
        └── TreeIntegrityTest.java      T8
```

# UML
<img width="946" height="309" alt="VLDDRzGm4BtdLrWz9PIDIe-hgYhQPIKaMbLQn7tgp6v6zKUm9uKM-E_4gMtYLSNHxysyUVFX6y_WUDIgyeza1d2WXLJW_KTcHmyZusTfpPvUK5miGdmFdVr1vYWEe3mk66Cxz3Y83vSBq85v3E3b4kpm0AFYdJKSz4D6JnqAr0C_2utw0PslAZACxW0InIU56WsBNvKGvqVaEz1OD" src="https://github.com/user-attachments/assets/97eb6964-0c96-4165-81f1-eab5a7d41ff7" />

