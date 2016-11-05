/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thaivun01.controllers;

import com.thaivun01.beans.ConfigurationBean;
import com.thaivun01.business.EmailClient;
import com.thaivun01.business.EmailClientFace;
import com.thaivun01.database.EmailDAO;
import com.thaivun01.database.EmailDAOImpl;
import com.thaivun01.mailapplicationproject.MainApp;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FXML Controller class
 *
 * @author MDThai
 */
public class RootClientLayoutController implements Initializable {

    private final Logger log = LoggerFactory.getLogger(getClass().getName());

    @FXML
    private AnchorPane leftContainer;

    @FXML
    private AnchorPane rightTopContainer;

    @FXML
    private AnchorPane rightBottomContainer;
   
   
   @FXML 
    private ResourceBundle resource;
   
   
    //Handle on the controllers
    private EmailTableLayoutController emailTableLayoutController;
    private EmailFolderTreeLayoutController emailFolderTreeLayoutController;
    private EmailEditorLayoutController emailEditorLayoutController;
   
   private ConfigurationBean configBean;
   private EmailClientFace  emailClient;
   private EmailDAO dao;
   
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
           
    }    
    
    public void loadComponents(){

        
        loadLeft();
           //loadUpperRight();
           
           //Pass the Email Table controller to the Folder Tree controller
           //emailFolderTreeLayoutController.setEmailTableLayoutController(emailTableLayoutController);
           
           try{
               emailFolderTreeLayoutController.displayTree();
               
           }catch(SQLException e){
               log.error(e.getMessage());
           }
           
    }
    
    /**
     * Sets the configuration bean
     * @param configBean 
     */
    public void setUpActionBeans (ConfigurationBean configBean){
        this.configBean = configBean;
        this.dao = new EmailDAOImpl(configBean);
        this.emailClient = new EmailClient(configBean);
    }
    
    private void loadLeft(){
        try {
            
            //Load the controller responsible for the Email Folder Tree
            FXMLLoader loader = new FXMLLoader();
            loader.setResources(resource);
            loader.setLocation(MainApp.class.getResource("/fxml/EmailFolderTreeLayout.fxml"));
            AnchorPane folderTreeView = (AnchorPane) loader.load();
            emailFolderTreeLayoutController = loader.getController();
            
            //Pass the configuration bean and the DAO class to the controller
            emailFolderTreeLayoutController.setConfigBean(configBean);
            emailFolderTreeLayoutController.setDaoObject(dao);
            
            //Load the container with the tree view
            leftContainer.getChildren().add(folderTreeView);
            
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
    }
    
    private void loadUpperRight(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setResources(resource);
            loader.setLocation(MainApp.class.getResource("/fxml/EmailTableLayoutController.fxml"));
            AnchorPane emailTableView = (AnchorPane) loader.load();
            emailTableLayoutController = loader.getController();
            
            emailTableLayoutController.setConfigBean(configBean);
            emailTableLayoutController.setDaoObject(dao);
            
            rightTopContainer.getChildren().add(emailTableView);
            
            
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(RootClientLayoutController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    private void loadLowerRight(){
        //TO DO LATER
    }
    
    
    public EmailFolderTreeLayoutController getTreeController(){
        return emailFolderTreeLayoutController;
    }
    
    public EmailTableLayoutController getTableController(){
        return emailTableLayoutController;
    }
    
}
