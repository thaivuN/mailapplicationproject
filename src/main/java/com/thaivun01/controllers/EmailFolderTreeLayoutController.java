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
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author MDThai
 */
public class EmailFolderTreeLayoutController implements Initializable {

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
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    /**
     * Set the DAO action class
     * @param mailDAO EmailDAO
     */
    public void setDaoObject(EmailDAO mailDAO) {
        this.mailDAO = mailDAO;
    }

    /**
     * Set the user configuration bean
     * @param configBean ConfigurationBean
     */
    public void setConfigBean(ConfigurationBean configBean) {
        this.configBean = configBean;
    }
    
    /**
     * Set a handle on the Email Table controller.
     * @param emailTableLayoutController EmailTableLayoutController
     */
    public void setEmailTableLayoutController(EmailTableLayoutController emailTableLayoutController){
        this.emailTableLayoutController = emailTableLayoutController;
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
     * @param folder 
     */
    public void updateEmailView(TreeItem<FolderBean> folder){
        
    }
}
