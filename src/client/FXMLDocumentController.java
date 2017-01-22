
package client;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.DosFileAttributes;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.DirectoryChooser;
import javafx.util.Callback;

/**
 *
 * @author Mikołaj
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private TreeView<Path> treeView;
    @FXML
    private TextField serverField;
    @FXML
    private TextField portField;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Label label;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        treeView.setCellFactory(new PathCellFactory());
    }

    @FXML
    protected void changeRootButtonAction(ActionEvent event) {
        
        try {
            DirectoryChooser dir = new DirectoryChooser();
            File file = dir.showDialog(null);
            Files.walkFileTree(file.toPath(), new Display(treeView));
        } catch (Exception e) {
            System.err.println("Not selected folder. Error: " + e);
        }
        
    }
    
    @FXML
    protected void sendButtonAction(ActionEvent event) {
        
        try{
            TreeItem<Path> selected = treeView.getSelectionModel().getSelectedItem();
            
            if(selected.getValue().toFile().isFile()){
                String serverName = serverField.textProperty().get();
                int portNumber = Integer.parseInt(portField.textProperty().get());
                
                if (!serverName.isEmpty() && portNumber != 0) {
                    Sending sending = new Sending(selected.getValue().toFile(), serverName, portNumber);
                    label.textProperty().bind(sending.messageProperty());
                    progressBar.progressProperty().bind(sending.progressProperty());
                    Thread thread = new Thread(sending);
                    thread.setDaemon(true);
                    thread.start();
                }
                
            }
            else
                System.err.println("You have to select a file, not a directory.");
        }
        catch(Exception e){
            System.err.println("Send a file failed. Error: " + e);
        }
        
    }

    private class PathCell extends TreeCell<Path> {
        
        @Override
        protected void updateItem(Path file, boolean empty) {
            super.updateItem(file, empty);
            if (file != null)
                setText(file.getFileName().toString());
            else
                setText(null);
        }

    }

    private class PathCellFactory implements Callback<TreeView<Path>, TreeCell<Path>> {
        @Override
        public TreeCell<Path> call(TreeView<Path> p) {
            return new PathCell();
        }
    }  
    
}
