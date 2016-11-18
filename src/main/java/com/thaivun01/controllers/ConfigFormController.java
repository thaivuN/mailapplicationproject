package com.thaivun01.controllers;

import com.thaivun01.beans.ConfigurationBean;
import com.thaivun01.manager.PropertiesManager;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import static java.nio.file.Paths.get;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import validator.SuperValidator;
import static java.nio.file.Paths.get;

public class ConfigFormController implements Initializable {

    private final Logger log = LoggerFactory.getLogger(getClass().getName());

    @FXML
    private ResourceBundle resources;

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
    private TopLevelContainerLayoutController mainProgramController;

    public ConfigFormController() {
        super();
        configBean = new ConfigurationBean();
    }

    @Override
    @FXML
    public void initialize(URL location, ResourceBundle resources) {

        log.info("Resources " + resources);
        this.resources = resources;

        Bindings.bindBidirectional(usernameConfig.textProperty(), configBean.getUsernameProperty());
        Bindings.bindBidirectional(emailAddressConfig.textProperty(), configBean.getEmailAddressProperty());
        Bindings.bindBidirectional(emailPwdConfig.textProperty(), configBean.getEmailPwdProperty());
        Bindings.bindBidirectional(smtpServerConfig.textProperty(), configBean.getServerSMTPProperty());
        Bindings.bindBidirectional(smtpPortConfig.textProperty(), configBean.getSmtpPortProperty());
        Bindings.bindBidirectional(imapServerConfig.textProperty(), configBean.getServerIMAPProperty());
        Bindings.bindBidirectional(imapPortConfig.textProperty(), configBean.getImapPortProperty());
        Bindings.bindBidirectional(databaseUrlConfig.textProperty(), configBean.getDbUrlProperty());
        Bindings.bindBidirectional(databaseUserConfig.textProperty(), configBean.getDbUserProperty());
        Bindings.bindBidirectional(databasePwdConfig.textProperty(), configBean.getDbPwdProperty());
        Bindings.bindBidirectional(databasePortConfig.textProperty(), configBean.getDbPortProperty());

    }

    /**
     * Set the handle on the scene, stage and main controller
     *
     * @param scene
     * @param stage
     * @param mainProgramController
     */
    public void setMainScene(Scene scene, Stage stage, TopLevelContainerLayoutController mainProgramController) {
        this.scene = scene;
        this.stage = stage;
        this.mainProgramController = mainProgramController;

    }

    /**
     * Exists the entire program
     *
     * @param event
     */
    @FXML
    void onCancel(ActionEvent event) {
        Path path = get("", "ConfigurationEmail.properties");
        if (path.toFile().exists()) {
            log.info("Configuration Properties file already exists");

            try {
                loadMainScene();
            } catch (IOException ex) {
                log.error("Failed load main scene.");
                log.error(ex.getMessage());
            }
        } else {
            log.info("Configuration Properties file does not exists");
            Platform.exit();
        }

    }

    /**
     * Checks if the input is valid If it is, create a properties file and
     * launch the
     *
     * @param event ActionEvent
     */
    @FXML
    void onSubmit(ActionEvent event) {

        PropertiesManager propManager = new PropertiesManager();

        if (SuperValidator.validateConfiguration(configBean)) {
            try {
                propManager.writePropertiesTxtFile(configBean, "", "ConfigurationEmail");

                loadMainScene();

            } catch (IOException ex) {
                log.error("Failed load main scene.");
                log.error(ex.getMessage());
            }
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle(resources.getString("errorTitleConfig"));
            alert.setHeaderText(resources.getString("errorHeaderConfig"));
            alert.setContentText(resources.getString("errorContentConfig"));
            alert.showAndWait();

        }

    }

    /**
     * Reloading the ConfigurationBean instance from an existing Properties file
     *
     * @param propManager PropertiesManager
     * @throws IOException
     */
    public void reloadConfigBean(PropertiesManager propManager) throws IOException {
        propManager.loadPropertiesTxtFile(configBean, "", "ConfigurationEmail");
    }

    private void loadMainScene() throws IOException {
        stage.setScene(scene);
        stage.setTitle("E-Hub");
        mainProgramController.setStage(stage);
        mainProgramController.setTopScene(scene);
        mainProgramController.loadConfigBean();
        mainProgramController.loadRootLayout();
    }

}
