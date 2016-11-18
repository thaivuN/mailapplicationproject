
package com.thaivun01.controllers;

import com.thaivun01.beans.ConfigurationBean;
import com.thaivun01.mailapplicationproject.MainApp;
import com.thaivun01.manager.PropertiesManager;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FXML Controller class
 *
 * @author thai-vunguyen
 */
public class TopLevelContainerLayoutController implements Initializable {

    private final Logger log = LoggerFactory.getLogger(getClass().getName());
    
    @FXML
    private ResourceBundle resources;
    
    @FXML // fx:id="mainBodyAnchorPane"
    private AnchorPane mainBodyAnchorPane; // Value injected by FXMLLoader
    
    private Scene formScene;
    private ConfigFormController formController;
    private RootClientLayoutController rootController;
    private ConfigurationBean config;
    private Scene mainScene;
    private Stage stage;
    
    public TopLevelContainerLayoutController(){
        this.config = new ConfigurationBean();
    }
    
    /**
     * Initializes the controller class.
     * @param location URL
     * @param resources ResourceBundle
     */
    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.info("Resources " + resources );
        this.resources = resources;
        
    }    
    
    
    /**
     * Action to close the program
     * @param event ActionEvent
     */
    @FXML
    void onClose(ActionEvent event) {
        Platform.exit();

    }

    /**
     * Action to launch the About dialog
     * @param event ActionEvent
     */
    @FXML
    void onHelp(ActionEvent event) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(resources.getString("aboutHelpTitle"));
            alert.setHeaderText(resources.getString("aboutHelpHeader"));
            alert.setContentText(resources.getString("aboutHelpContent"));
            
            
            alert.showAndWait();

    }
    
    /**
     * Action to request to compose a new email
     * @param event ActionEvent
     */
    @FXML
    void onNewEmail(ActionEvent event){
        
        rootController.getEmailController().requestNewMessage();
        
    }
    
    /**
     * Action to request to compose an email reply
     * @param event ActionEvent
     */
    @FXML
    void onReplyEmail(ActionEvent event){
        rootController.getEmailController().replyMessage();
    }
    
    /**
     * Action to request to compose an email forward
     * @param event ActionEvent
     */
    @FXML
    void onForwardEmail(ActionEvent event){
        rootController.getEmailController().forwardMessage();
    }
    
    /**
     * Action to create a new Folder
     * @param event ActionEvent
     */
    @FXML
    void onNewFolder(ActionEvent event){
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Create Folder");
        
        dialog.setHeaderText("Create a new folder");
        dialog.setContentText("Please write the name of the folder:");
        
        Optional<String> result = dialog.showAndWait();
        
        if (result.isPresent())
        {
            try {
                rootController.getTreeController().createNewFolder(result.get());
            } catch (SQLException ex) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle(resources.getString("errorTitle"));
                alert.setHeaderText(resources.getString("errorHeaderFolderAction"));
                alert.setContentText(resources.getString("errorContentFolderAdd"));
                alert.showAndWait();
            }
        }
        
    }
    
    /**
     * Action to load the Configuration form.
     * @param event ActionEvent
     */
    @FXML
    void onSettingsCalled(ActionEvent event){
        try {
            PropertiesManager prop = new PropertiesManager();
            formController.setMainScene(mainScene, stage, this);
            formController.reloadConfigBean(prop);
            stage.setScene(formScene);
        } catch (IOException ex) {
            log.error("Could not reload config bean in form controller");
        }
    }
    
    /**
     * Action to delete a folder.
     * @param event ActionEvent
     */
    @FXML
    void onDeleteFolder(ActionEvent event){
        try {
            rootController.getTreeController().deleteFolder();
            
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle(resources.getString("errorTitle"));
                alert.setHeaderText(resources.getString("errorHeaderFolderAction"));
                alert.setContentText(resources.getString("errorContentFolderDelete"));
                alert.showAndWait();
        }
    }
    
    /**
     * Action to receive emails
     * @param event ActionEvent
     */
    @FXML
    void onRefreshEmails(ActionEvent event){
        try {
            rootController.getEmailController().updateInbox();
            
        } catch (SQLException ex) {
            log.error("Could not update emails");
        }
    }
    
    
    /**
     * Loads the RootClientLayout view.
     */
    public void loadRootLayout(){
        FXMLLoader loader = new FXMLLoader();
       
       
        
        log.info("Resources  " + resources);
        loader.setResources(resources);
        
        loader.setLocation(MainApp.class.getResource("/fxml/RootClientLayout.fxml"));
        
        try {
            AnchorPane rootPane = (AnchorPane) loader.load();
            rootController = loader.getController();
            
            rootController.setStage(stage);
            rootController.setUpActionBeans(config);
            rootController.loadComponents();
            mainBodyAnchorPane.getChildren().add(rootPane);
            rootPane.prefWidthProperty().bind(mainBodyAnchorPane.widthProperty());
            rootPane.prefHeightProperty().bind(mainBodyAnchorPane.heightProperty());
            
            
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
    }
    
    /**
     * Load the ConfigurationBean object
     * @throws IOException 
     */
    public void loadConfigBean() throws IOException{
        PropertiesManager prop = new PropertiesManager();
        prop.loadPropertiesTxtFile(config, "", "ConfigurationEmail");
    }
    
    /**
     * Receive the stage
     * @param stage Stage
     */
    public void setStage(Stage stage){
        this.stage = stage;
    }

    /**
     * Receive the controller for the Configuration form 
     * @param formController ConfigFormController
     */
    public void setFormController(ConfigFormController formController) {
        this.formController = formController;
    }

    /**
     * Receive the Form scene
     * @param formScene 
     */
    public void setFormScene(Scene formScene) {
        this.formScene = formScene;
    }

    /**
     * Receive the scene of this controller
     * @param mainScene 
     */
    public void setTopScene(Scene mainScene) {
        this.mainScene = mainScene;
    }
    
}
