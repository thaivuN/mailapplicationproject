/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thaivun01.controllers;

import com.thaivun01.beans.ConfigurationBean;
import com.thaivun01.mailapplicationproject.MainApp;
import com.thaivun01.manager.PropertiesManager;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import org.slf4j.LoggerFactory;

/**
 * FXML Controller class
 *
 * @author thai-vunguyen
 */
public class TopLevelContainerLayoutController implements Initializable {

    private final org.slf4j.Logger log = LoggerFactory.getLogger(getClass().getName());
    
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;


    @FXML // fx:id="mainBodyAnchorPane"
    private AnchorPane mainBodyAnchorPane; // Value injected by FXMLLoader
    
    
    private RootClientLayoutController rootController;
    private ConfigurationBean config;
    
    
    public TopLevelContainerLayoutController(){
        this.config = new ConfigurationBean();
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML
    void onClose(ActionEvent event) {
        Platform.exit();

    }

    @FXML
    void onHelp(ActionEvent event) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("TO DO");
            alert.setHeaderText("Will Implement Later");
            alert.setContentText("Will Implement the Help functionality at the later time");
            alert.showAndWait();

    }
    
    void onNewEmail(ActionEvent event){
        
    }
    
    void onReplyEmail(ActionEvent event){
        
    }
    
    void onForwardEmail(ActionEvent event){
        
    }
    
    public void loadRootLayout(){
        FXMLLoader loader = new FXMLLoader();
        loader.setResources(resources);
        
        loader.setLocation(MainApp.class.getResource("/fxml/RootClientLayout.fxml"));
        
        try {
            AnchorPane rootPane = (AnchorPane) loader.load();
            rootController = loader.getController();
            
            rootController.setUpActionBeans(config);
            rootController.loadComponents();
            mainBodyAnchorPane.getChildren().add(rootPane);
            
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
    }
    
    public void loadConfigBean() throws IOException{
        PropertiesManager prop = new PropertiesManager();
        prop.loadPropertiesTxtFile(config, "", "ConfigurationEmail");
    }
    
}
