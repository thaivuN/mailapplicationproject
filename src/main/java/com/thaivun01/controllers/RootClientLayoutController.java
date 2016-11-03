/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thaivun01.controllers;

import com.thaivun01.beans.ConfigurationBean;
import com.thaivun01.database.EmailDAO;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author MDThai
 */
public class RootClientLayoutController implements Initializable {

    @FXML
    private AnchorPane rootAnchPane;

    @FXML
    private AnchorPane upperLeftPane;

    @FXML
    private AnchorPane upperRightPane;

    @FXML
    private AnchorPane lowerLeftPane;

    @FXML
    private AnchorPane lowerRightPane;
   
   
   @FXML 
    private ResourceBundle resource;
    private ConfigurationBean configBean;
    private EmailDAO dao;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
           
    }    
    
    /**
     * Sets the configuration bean
     * @param configBean 
     */
    public void setConfigBean (ConfigurationBean configBean){
        this.configBean = configBean;
    }
    
    private void loadUpperLeft(){
        
    }
    
    private void loadUpperRight(){
        
    }
    
    private void loadLowerLeft(){
        
    }
    
    private void loadLowerRight(){
        
    }
    
}
