package kz.aitu.visitor;

import kz.aitu.visitor.model.DirectoryNode;
import kz.aitu.visitor.model.FileNode;
import kz.aitu.visitor.visitor.SearchVisitor;
import kz.aitu.visitor.visitor.StatisticsVisitor;
import kz.aitu.visitor.visitor.TotalSizeVisitor;

public class Main {

    public static void main(String[] args) {
        DirectoryNode root = buildSampleTree();

        printTotalSize(root);
        printStatistics(root);
        printSearch("*.txt, size >= 500", root,
                new SearchVisitor(name -> name.endsWith(".txt"), 500));
        printSearch("*.png, size >= 20000", root,
                new SearchVisitor(name -> name.endsWith(".png"), 20_000));
    }

    private static DirectoryNode buildSampleTree() {
        DirectoryNode root = new DirectoryNode("project");

        DirectoryNode docs = new DirectoryNode("docs");
        DirectoryNode images = new DirectoryNode("images");
        DirectoryNode src = new DirectoryNode("src");
        DirectoryNode util = new DirectoryNode("util");
        DirectoryNode deep = new DirectoryNode("deep");

        root.add(new FileNode("README.md", 1_200));
        root.add(docs);
        root.add(src);
        root.add(new DirectoryNode("empty"));

        docs.add(new FileNode("guide.txt", 3_500));
        docs.add(new FileNode("notes.txt", 800));
        docs.add(images);

        images.add(new FileNode("logo.png", 15_000));
        images.add(new FileNode("banner.png", 42_000));

        src.add(new FileNode("Main.java", 2_500));
        src.add(util);

        util.add(new FileNode("Helper.java", 1_800));
        util.add(deep);

        deep.add(new FileNode("Config.txt", 400));

        return root;
    }

    private static void printTotalSize(DirectoryNode root) {
        long total = new TotalSizeVisitor().calculate(root);
        System.out.println("Total size: " + total + " bytes");
    }

    private static void printStatistics(DirectoryNode root) {
        StatisticsVisitor statistics = new StatisticsVisitor();
        statistics.collect(root);
        System.out.println("Files: " + statistics.getFileCount());
        System.out.println("Directories: " + statistics.getDirectoryCount());
        System.out.println("Max depth: " + statistics.getMaxDepth());
    }

    private static void printSearch(String title, DirectoryNode root, SearchVisitor search) {
        System.out.println("Search [" + title + "]:");
        for (String path : search.search(root)) {
            System.out.println("  " + path);
        }
    }
}