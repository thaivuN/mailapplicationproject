package com.thaivun01.controllers;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.thaivun01.beans.ConfigurationBean;
import com.thaivun01.beans.EmailPreview;
import com.thaivun01.database.EmailDAO;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author MDThai
 */
public class EmailTableLayoutController implements Initializable {

    @FXML
    private AnchorPane emailTableFxLayout;

    @FXML
    private TableView<EmailPreview> emailTableView;
    
    private EmailDAO mailDAO;
    private ConfigurationBean configBean;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void setDaoObject(EmailDAO mailDAO){
        this.mailDAO = mailDAO;
    }
    
    public void setConfigBean (ConfigurationBean configBean){
        this.configBean = configBean;
    }
    
}
