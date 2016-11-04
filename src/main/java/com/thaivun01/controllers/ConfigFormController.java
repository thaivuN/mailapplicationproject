package com.thaivun01.controllers;

import com.thaivun01.beans.ConfigurationBean;
import com.thaivun01.manager.PropertiesManager;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigFormController implements Initializable {
    
    private final Logger log = LoggerFactory.getLogger(getClass().getName());

    @FXML
    private TextField emailAddressConfig;

    @FXML
    private TextField databaseUserConfig;

    @FXML
    private PasswordField emailPwdConfig;

    @FXML
    private PasswordField databasePwdConfig;

    @FXML
    private TextField databaseUrlConfig;

    @FXML
    private TextField databasePortConfig;

    @FXML
    private TextField smtpServerConfig;

    @FXML
    private TextField smtpPortConfig;

    @FXML
    private TextField imapServerConfig;

    @FXML
    private TextField imapPortConfig;

    @FXML
    private TextField usernameConfig;
    
    private ConfigurationBean configBean;
    
    private Stage stage;
    private Scene scene;
    private RootClientLayoutController mainProgramController;
    
    public ConfigFormController(){
        super();
        configBean = new ConfigurationBean();
    }
    
    @Override
    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        Bindings.bindBidirectional(usernameConfig.textProperty(), configBean.getUsernameProperty());
        Bindings.bindBidirectional(emailAddressConfig.textProperty(), configBean.getEmailAddressProperty());
        Bindings.bindBidirectional(emailPwdConfig.textProperty(), configBean.getEmailPwdProperty());
        Bindings.bindBidirectional(smtpServerConfig.textProperty(), configBean.getServerSMTPProperty());
        Bindings.bindBidirectional(smtpPortConfig.textProperty(), configBean.getSmtpPortProperty());
        Bindings.bindBidirectional(imapServerConfig.textProperty(), configBean.getServerIMAPProperty());
        Bindings.bindBidirectional(imapPortConfig.textProperty(), configBean.getImapPortProperty());
        Bindings.bindBidirectional(databaseUrlConfig.textProperty(), configBean.getDbUrlProperty());
        Bindings.bindBidirectional(databaseUserConfig.textProperty(), configBean.getUsernameProperty());
        Bindings.bindBidirectional(databasePwdConfig.textProperty(), configBean.getDbPwdProperty());
        Bindings.bindBidirectional(databasePortConfig.textProperty(), configBean.getDbPortProperty());
        
        
    }
    
    /**
     * Set the handle on the scene, stage and main controller 
     * @param scene
     * @param stage
     * @param mainProgramController 
     */
    public void setRootController(Scene scene, Stage stage, RootClientLayoutController mainProgramController){
        this.scene = scene;
        this.stage = stage;
        this.mainProgramController = mainProgramController;
        
    }
    
    
    /**
     * Exists the entire program
     * @param event 
     */
    @FXML
    void onCancel(ActionEvent event){
        Platform.exit();
    }
    
    /**
     * Checks if the input is valid
     * If it is, create a properties file and launch the 
     * @param event 
     */
    @FXML
    void onSubmit(ActionEvent event){
        
        PropertiesManager propManager = new PropertiesManager();
        
        try{
            propManager.writePropertiesTxtFile(configBean, "", "ConfigurationEmail");
            
            stage.setScene(scene);
        }
        catch (IOException ex){
            
        }
        
    }

}
