package com.thaivun01.mailapplicationproject;

import com.thaivun01.beans.ConfigurationBean;
import com.thaivun01.controllers.ConfigFormController;
import com.thaivun01.manager.PropertiesManager;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MainApp extends Application {

    private Stage stage;
    
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Scene.fxml"));
        this.stage = stage;
        //Scene scene = new Scene(root);
        //scene.getStylesheets().add("/styles/Styles.css");

        Scene formScene = createConfigForm();
        Scene mainScene = createRootLayout();
        
        if (propertiesExists()){
            this.stage.setScene(mainScene);
            this.stage.setTitle("Mail Application");
        }
        else{
            this.stage.setScene(formScene);
            this.stage.setTitle("Configuration Form");
        }
       
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

    private Scene createConfigForm() throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("/fxml/ConfigForm.fxml"));
        
        loader.setResources(ResourceBundle.getBundle("BundleResources"));
        
       
        
        Parent root = (AnchorPane) loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/configform.css");
        return scene;
    }
    
    private Scene createRootLayout () throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("/fxml/RootClientLayout.fxml"));
        
        loader.setResources(ResourceBundle.getBundle("BundleResources"));
        
        
        Parent root = (AnchorPane) loader.load();
        
        Scene scene = new Scene (root);
        scene.getStylesheets().add("/styles/rootclientlayout.css");
        return scene;
    }
    
    private boolean propertiesExists(){
        boolean found = false;
        
        ConfigurationBean configBean = new ConfigurationBean();
        PropertiesManager manager = new PropertiesManager();
        
        try{
            if (manager.loadPropertiesTxtFile(configBean, "", "MailConfiguration")){
                found = true;
            }
        }
        catch (IOException ex){
            
        }
        return found;
    }

}
