
package com.thaivun01.controllers;

import com.thaivun01.beans.ConfigurationBean;
import com.thaivun01.beans.FolderBean;
import com.thaivun01.database.EmailDAO;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FXML Controller class
 *
 * @author MDThai
 */
public class EmailFolderTreeLayoutController implements Initializable {

    private final Logger log = LoggerFactory.getLogger(getClass().getName());

    @FXML
    private AnchorPane folderTreeFxLayout;

    @FXML
    private TreeView<FolderBean> folderTreeView;

    @FXML
    private ResourceBundle resources;

    private EmailTableLayoutController emailTableLayoutController;
    private EmailHtmlLayoutController emailHtmlLayoutController;

    private EmailDAO mailDAO;
    private ConfigurationBean configBean;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.info("Resources " + resources);
        this.resources = resources;

        setUpTree();

    }

    /**
     * Set the DAO action class
     *
     * @param mailDAO EmailDAO
     */
    public void setDaoObject(EmailDAO mailDAO) {
        this.mailDAO = mailDAO;
    }

    /**
     * Set the user configuration bean
     *
     * @param configBean ConfigurationBean
     */
    public void setConfigBean(ConfigurationBean configBean) {
        this.configBean = configBean;
    }

    /**
     * Set a handle on the Email Table controller.
     *
     * @param emailTableLayoutController EmailTableLayoutController
     */
    public void setEmailTableLayoutController(EmailTableLayoutController emailTableLayoutController) {
        this.emailTableLayoutController = emailTableLayoutController;
    }
    
    public void setEmailHtmlLayoutController(EmailHtmlLayoutController emailHtmlLayoutController){
        this.emailHtmlLayoutController = emailHtmlLayoutController;
    }

    /**
     *  Set up the Tree view of Email Folders.
     */
    public void setUpTree() {
        //Create root Folder
        FolderBean rootFolder = new FolderBean();
        rootFolder.setFolderName("Root");

        folderTreeView.setRoot(new TreeItem<FolderBean>(rootFolder));

        folderTreeView.setCellFactory(x -> {
            TreeCell<FolderBean> tc = new TreeCell<FolderBean>() {
                @Override
                protected void updateItem(FolderBean folder, boolean isEmpty) {
                    super.updateItem(folder, isEmpty);
                    if (folder != null) {
                        setText(folder.getFolderName());
                        setGraphic(getTreeItem().getGraphic());
                    } else {
                        setText("");
                        setGraphic(null);
                    }
                }

            };

            //Set the TreeCell to accept a drop event
            tc.setOnDragOver(ev -> {
                log.info("Drag Over");
                if (ev.getDragboard().hasString()) {
                    log.info("Drag Value " + ev.getDragboard().getString());
                    ev.acceptTransferModes(TransferMode.MOVE);
                }
                ev.consume();

            });

            //Set the TreeCell to process the drop event
            tc.setOnDragDropped(ev -> {

                log.info("Drop Detected");
                Dragboard dragBoard = ev.getDragboard();

                if (dragBoard.hasString()) {
                    try {
                        log.info("DRAG & DROP DEBUG: DROP VALUE - " + dragBoard.getString());
                        log.info("Dropped into FolderBean of ID " + tc.treeItemProperty().get().getValue().getFolderID());
                        mailDAO.updateEmailFolder(Integer.parseInt(dragBoard.getString()), tc.treeItemProperty().get().getValue().getFolderID());
                        folderTreeView.getSelectionModel().select(tc.getTreeItem());
                    } catch (SQLException ex) {
                        log.error(ex.getMessage());
                    } catch (NumberFormatException nfe) {
                        log.error(nfe.getMessage());
                    }

                }

                ev.setDropCompleted(true);
                ev.consume();
            });

            return tc;
        });
    }

    /**
     * Build the tree from the database.
     *
     * @throws SQLException
     */
    public void displayTree() throws SQLException {
        //Get the list of Folders
        ObservableList<FolderBean> folders = mailDAO.getAllFoldersTree();

        // Build an item for each fish and add it to the root
        if (folders != null) {
            for (FolderBean folder : folders) {
                TreeItem<FolderBean> item = new TreeItem<>(folder);
                item.setGraphic(new ImageView(getClass().getResource("/images/folder_icon.png").toExternalForm()));
                folderTreeView.getRoot().getChildren().add(item);
            }
        }

        // Expand tree
        folderTreeView.getRoot().setExpanded(true);

        // Add a listener for when the user selects a TreeItem
        // Update the Email Table when user selects a folder
        folderTreeView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> updateEmailView(newValue));
        folderTreeView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

    }

    /**
     * Updates the Email Table based on the selected TreeItem
     *
     * @param folder
     */
    public void updateEmailView(TreeItem<FolderBean> folder) {

        if (folder != null) {
            FolderBean folderSelected = folder.getValue();
            try {
                emailTableLayoutController.updateEmailTable(folderSelected.getFolderID());
            } catch (SQLException ex) {
                log.error(ex.getMessage());
            }
        }

    }

    /**
     * Creates a new Folder and reloads the Tree 
     * @param newFolder
     * @throws SQLException 
     */
    public void createNewFolder(String newFolder) throws SQLException {
        mailDAO.createFolder(newFolder);
        setUpTree();
        displayTree();

    }

    /**
     * Deletes a Folder and reloads the Tree.
     * Will not delete the Root, Inbox, Sent folders.
     * 
     * @throws SQLException 
     */
    public void deleteFolder() throws SQLException {
        TreeItem<FolderBean> folderSelected = folderTreeView.getSelectionModel().getSelectedItem();
        log.info("FolderBean attempt to delete " + folderSelected);

        if (folderSelected != null) {
            String f_name = folderSelected.getValue().getFolderName();
            if (f_name.equalsIgnoreCase("Inbox") == false && f_name.equalsIgnoreCase("Sent") == false && f_name.equalsIgnoreCase("Root") == false) {
                log.info("Delete folder attempt: Not Inbox or Sent folder");
                mailDAO.removeFolder(folderSelected.getValue().getFolderID());
                log.info("Folder Deleted: " + f_name);
                setUpTree();
                displayTree();
                folderTreeView.getSelectionModel().selectFirst();
                emailHtmlLayoutController.requestNewMessage();
                
            } else {
                log.info("Can't delete that folder");
            }
        }
    }
    
    /**
     * Refresh the current table view
     */
    public void refreshEmailTableView(){
        updateEmailView(folderTreeView.getSelectionModel().getSelectedItem());
        
    }
}
