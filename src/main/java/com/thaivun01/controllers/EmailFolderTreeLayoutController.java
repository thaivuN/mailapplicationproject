/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

    private EmailDAO mailDAO;
    private ConfigurationBean configBean;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.info("Resources " + resources);

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

    /**
     *
     */
    public void setUpTree() {
        //Create root Folder
        FolderBean rootFolder = new FolderBean();
        rootFolder.setFolderName("Root");

        folderTreeView.setRoot(new TreeItem<FolderBean>(rootFolder));

        /**
         * folderTreeView.setCellFactory((x) -> new TreeCell<FolderBean>(){
         *
         * @Override protected void updateItem(FolderBean folder, boolean
         * isEmpty){ super.updateItem(folder, isEmpty); if (folder != null){
         * setText(folder.getFolderName());
         * setGraphic(getTreeItem().getGraphic()); } else{ setText("");
         * setGraphic(null); } }
         *
         * });
         */
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
            
            tc.setOnDragOver(ev -> {
                log.info("Drag Over");
                if(ev.getDragboard().hasString()){
                    log.info("Drag Value " + ev.getDragboard().getString());
                    ev.acceptTransferModes(TransferMode.MOVE);
                }
                 ev.consume();
            
            });
            
            tc.setOnDragDropped(ev ->{
                
                log.info("Drop Detected");
                Dragboard dragBoard = ev.getDragboard();
                
                
                if (dragBoard.hasString())
                {
                    try {
                        log.info("DRAG & DROP DEBUG: DROP VALUE - " + dragBoard.getString());
                        log.info("Dropped into FolderBean of ID " + tc.treeItemProperty().get().getValue().getFolderID());
                        mailDAO.updateEmailFolder(Integer.parseInt(dragBoard.getString()), tc.treeItemProperty().get().getValue().getFolderID());
                        folderTreeView.getSelectionModel().select(tc.getTreeItem());
                    } catch (SQLException ex) {
                        log.error(ex.getMessage());
                    }
                    catch (NumberFormatException nfe){
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
     * Build the tree from the database
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

    }

    /**
     * Updates the Email Table based on the selected TreeItem
     *
     * @param folder
     */
    public void updateEmailView(TreeItem<FolderBean> folder) {

        FolderBean folderSelected = folder.getValue();
        try {
            emailTableLayoutController.updateEmailTable(folderSelected.getFolderID());
        } catch (SQLException ex) {
            log.error(ex.getMessage());
        }

    }

    public void createNewFolder(String newFolder) throws SQLException {
        mailDAO.createFolder(newFolder);
        setUpTree();
        displayTree();

    }
}
