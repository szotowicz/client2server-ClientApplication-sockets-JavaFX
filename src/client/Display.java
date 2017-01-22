
package client;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

/**
 *
 * @author Mikołaj
 */
public class Display extends SimpleFileVisitor<Path> {

    private TreeView<Path> treeView;
    private TreeItem<Path> currentItem;

    public Display(TreeView<Path> tree) {
        treeView = tree;
        currentItem = null;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {
        currentItem.getChildren().add(new TreeItem<Path>(file));
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
        TreeItem<Path> pre = new TreeItem<Path>(dir);
        if (currentItem == null)
            treeView.setRoot(pre);
        else 
            currentItem.getChildren().add(pre);
        
        currentItem = pre;
        return FileVisitResult.CONTINUE;
    }
    
}
