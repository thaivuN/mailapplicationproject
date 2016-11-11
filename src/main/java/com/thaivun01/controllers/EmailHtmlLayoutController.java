package com.thaivun01.controllers;

import com.thaivun01.beans.BoostedEmail;
import com.thaivun01.beans.ConfigurationBean;
import com.thaivun01.business.EmailClientFace;
import com.thaivun01.database.EmailDAO;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import static java.nio.file.Files.newOutputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jodd.mail.EmailAttachment;
import jodd.mail.EmailMessage;
import jodd.mail.MailAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FXML Controller class
 *
 * @author MDThai
 */
public class EmailHtmlLayoutController implements Initializable {

    private final Logger log = LoggerFactory.getLogger(getClass().getName());
    private EmailClientFace client;
    private ConfigurationBean configBean;
    private EmailDAO emailDAO;
    private EmailTableLayoutController emailTableController;
    private BoostedEmail bmail;

    private Stage stage;

    @FXML
    private ResourceBundle resources;

    @FXML
    private HTMLEditor messageEmailView;

    @FXML
    private Label fromEmailView;

    @FXML
    private TextField toEmailView;

    @FXML
    private TextField ccEmailView;

    @FXML
    private TextField bccEmailView;

    @FXML
    private TextField subjectEmailView;

    @FXML
    private Button clearEmailBtn;

    @FXML
    private Button submitEmailBtn;

    @FXML
    private HBox attachmentView;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
    }

    /**
     * Set the Email Client action bean
     *
     * @param client EmailClientFace
     */
    public void setClientBean(EmailClientFace client) {
        this.client = client;
    }

    /**
     * Set the ConfigurationBean
     *
     * @param configBean
     */
    public void setConfigBean(ConfigurationBean configBean) {
        this.configBean = configBean;
    }

    /**
     * Set the DAO Action bean
     *
     * @param emailDAO
     */
    public void setDAOActionBean(EmailDAO emailDAO) {
        this.emailDAO = emailDAO;
    }

    /**
     * Set the Email Table controller
     *
     * @param emailTableController
     */
    public void setEmailTableController(EmailTableLayoutController emailTableController) {
        this.emailTableController = emailTableController;
    }

    /**
     * Handler to attach a file to the attachment view
     * @param event 
     */
    @FXML
    void onAttachFiles(ActionEvent event){
        log.info("Attach Files reached");
        
        
        
    }
    
    /**
     * Display Email Details
     *
     * @param email BoostedEmail
     */
    public void displayEmailDetails(BoostedEmail email) {

        setReadOnly();
        
        fromEmailView.setText(email.getFrom().getEmail());
        toEmailView.setText(buildEmailAddressesString(email.getTo()));
        ccEmailView.setText(buildEmailAddressesString(email.getCc()));
        bccEmailView.setText(buildEmailAddressesString(email.getBcc()));
        subjectEmailView.setText(email.getSubject());
        messageEmailView.setHtmlText(buildMessage(email.getAllMessages()));

        //Clearing the HBox before adding attachments
        attachmentView.getChildren().clear();
        //Adding the attachments to the HBox
        List<EmailAttachment> atts = email.getAttachments();
        displayAttachments(atts);

    }

    /**
     * Format the array of MailAddress into one string
     *
     * @param addresses MailAddress[]
     * @return String
     */
    private String buildEmailAddressesString(MailAddress[] addresses) {
        String compactAddresses = "";
        for (int i = 0; i < addresses.length; i++) {
            compactAddresses += addresses[i].getEmail();
            if (i < addresses.length - 1) {
                compactAddresses += "; ";
            }
        }
        return compactAddresses;
    }

    private String buildMessage(List<EmailMessage> messages) {
        String message = "";
        for (int i = 0; i < messages.size() && i < 1; i++) {
            message = messages.get(i).getContent();
        }

        return message;
    }

    public void requestNewMessage() {
        fromEmailView.setText(configBean.getSenderEmail());
        toEmailView.setText("");
        ccEmailView.setText("");
        bccEmailView.setText("");
        subjectEmailView.setText("");
        messageEmailView.setHtmlText("");

        attachmentView.getChildren().clear();
        
        setReadWrite();
        emailTableController.unselectEmailPreview();
    }

    public void sendMessage() {
        for (Node node : attachmentView.getChildren()) {
            if (node instanceof Label) {
                Label label = (Label) node;
                String fileName = label.getText();
            }
        }
    }

    public void forwardMessage() {
        if (messageEmailView.isDisabled()) {
            String message = messageEmailView.getHtmlText();
            String from = fromEmailView.getText();
            String to = toEmailView.getText();
            String subject = subjectEmailView.getText();

            String forward = buildForwardMessage(message, from, to, subject);

            requestNewMessage();

            messageEmailView.setHtmlText(forward);
        }
    }

    public void replyMessage() {

        if (messageEmailView.isDisabled()) {
            String from = fromEmailView.getText();
            String subject = subjectEmailView.getText();
            requestNewMessage();
            toEmailView.setText(from);
            subjectEmailView.setText(subject);
        }

    }

    private void setReadOnly() {
        toEmailView.setEditable(false);
        ccEmailView.setEditable(false);
        bccEmailView.setEditable(false);
        subjectEmailView.setEditable(false);
        messageEmailView.setDisable(true);

        clearEmailBtn.setDisable(true);
        clearEmailBtn.setVisible(false);
        submitEmailBtn.setDisable(true);
        submitEmailBtn.setVisible(false);
    }

    private void setReadWrite() {
        toEmailView.setEditable(true);
        ccEmailView.setEditable(true);
        bccEmailView.setEditable(true);
        subjectEmailView.setEditable(true);
        messageEmailView.setDisable(false);
        clearEmailBtn.setDisable(false);
        clearEmailBtn.setVisible(true);
        submitEmailBtn.setDisable(false);
        submitEmailBtn.setVisible(true);
    }

    private String buildForwardMessage(String message, String from, String to, String subject) {
        StringBuilder newMessage = new StringBuilder();

        newMessage.append("<br />").append("<br />").append("<hr>").append("<br />")
                .append("<p>").append("From: ").append(from).append("<br />")
                .append("To: ").append(to).append("<br />")
                .append("Subject: ").append(subject)
                .append("</p>")
                .append(message);

        return newMessage.toString();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void displayAttachments(List<EmailAttachment> attachments) {
        for (EmailAttachment ea : attachments) {
            Hyperlink link = new Hyperlink();
            link.setText(ea.getName());
            link.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    try {
                        saveFile(ea.getName(), ea.toByteArray());
                    } catch (IOException ex) {
                        log.error(ex.getMessage());
                    }
                }
            });
            attachmentView.getChildren().add(link);

        }
    }
    
    private void saveFile(String filename, byte[] data) throws IOException{
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save file");
        chooser.setInitialFileName(filename);
        
        File file = chooser.showSaveDialog(stage);
        Path path = file.toPath();
        if (path != null){
            try(OutputStream out = newOutputStream(path)){
                out.write(data);
                
            }
        }
        
    }
}
