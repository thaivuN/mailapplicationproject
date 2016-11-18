package com.thaivun01.mailapplicationproject;

import com.thaivun01.beans.ConfigurationBean;
import com.thaivun01.controllers.ConfigFormController;
import com.thaivun01.controllers.TopLevelContainerLayoutController;
import com.thaivun01.manager.PropertiesManager;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainApp extends Application {

    private final Logger log = LoggerFactory.getLogger(getClass().getName());
    private Stage stage;
    private ConfigFormController formController;
    private TopLevelContainerLayoutController topLevelController;
    
    
    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        

        Scene formScene = createConfigForm();
        Scene mainScene = createMainLayout();
        
        topLevelController.setFormScene(formScene);
        topLevelController.setFormController(formController);
        
        if (propertiesExists()){
            
            this.stage.setScene(mainScene);
            this.stage.setTitle("E-Hub");
            topLevelController.setStage(stage);
            topLevelController.setTopScene(mainScene);
            topLevelController.loadConfigBean();
            topLevelController.loadRootLayout();
            
            
        }
        else{
            this.stage.setScene(formScene);
            this.stage.setTitle("Configuration Form");
            formController.setMainScene(mainScene, stage, topLevelController);
            
        }
        log.info("getClass() "+ getClass());
        log.info("getClass().getResource(filename) " + getClass().getResource("/images/appicon.png"));
        stage.getIcons().add(new Image(getClass().getResource("/images/appicon.png").toExternalForm())); 
        stage.show();
         
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Prepares the configuration form scene
     * @return Scene
     * @throws Exception 
     */
    private Scene createConfigForm() throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("/fxml/ConfigForm.fxml"));
        log.info("Default locale " + Locale.getDefault());
        
        loader.setResources(ResourceBundle.getBundle("BundleResources", Locale.getDefault()));
        
        
        log.info("Resource " + loader.getResources());
        
        Parent root = (AnchorPane) loader.load();
        formController = loader.getController();
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/configform.css");
        return scene;
    }
    
    /**
     * Prepares the main application scene
     * 
     * @return Scene
     * @throws Exception 
     */
    private Scene createMainLayout () throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("/fxml/TopLevelContainerLayout.fxml"));
        
        loader.setResources(ResourceBundle.getBundle("BundleResources", Locale.getDefault()));
        log.info("Resource " + loader.getResources());
        
        Parent root = (BorderPane) loader.load();
        
        topLevelController = loader.getController();
        
        Scene scene = new Scene (root);
        scene.getStylesheets().add("/styles/toplevelcontainerlayout.css");
        return scene;
    }
    
    /**
     * Look at if properties file of the ConfigurationBean exists
     * @return 
     */
    private boolean propertiesExists(){
        boolean found = false;
        
        ConfigurationBean configBean = new ConfigurationBean();
        PropertiesManager manager = new PropertiesManager();
        
        try{
            if (manager.loadPropertiesTxtFile(configBean, "", "ConfigurationEmail")){
                found = true;
            }
        }
        catch (IOException ex){
            
        }
        return found;
    }

}
