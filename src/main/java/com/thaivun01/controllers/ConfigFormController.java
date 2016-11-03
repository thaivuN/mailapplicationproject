package com.thaivun01.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class ConfigFormController implements Initializable {

    @FXML
    private TextField emailAddressConfig;

    @FXML
    private TextField databaseUserConfig;

    @FXML
    private PasswordField emailPwdConfig;

    @FXML
    private PasswordField databasePwdConfig;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }

}
