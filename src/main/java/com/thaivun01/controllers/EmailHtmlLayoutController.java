package com.thaivun01.controllers;

import com.thaivun01.beans.BoostedEmail;
import com.thaivun01.beans.ConfigurationBean;
import com.thaivun01.business.EmailClientFace;
import com.thaivun01.database.EmailDAO;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import static java.nio.file.Files.newInputStream;
import static java.nio.file.Files.newOutputStream;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
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
import validator.SuperValidator;

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
    private EmailFolderTreeLayoutController emailFolderController;
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
    private Button attachBtn;

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
    
    public void setEmailFolderController (EmailFolderTreeLayoutController emailFolderController){
        this.emailFolderController = emailFolderController;
    }

    /**
     * Handler to attach a file to the attachment view
     *
     * @param event
     */
    @FXML
    void onAttachFiles(ActionEvent event) {
        log.info("Attach Files reached");

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Load an attachments");
        File file = chooser.showOpenDialog(stage);

        if (file != null) {
            Path path = file.toPath();
            try (InputStream fileStream = newInputStream(path);) {

                byte[] data = getData(fileStream);

                Hyperlink link = new Hyperlink();
                link.setText(file.getAbsolutePath());
                link.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        try {
                            saveFile(file.getName(), data);
                        } catch (IOException ex) {
                            log.error("Could not save file");
                        }
                    }
                });

                attachmentView.getChildren().add(link);

            } catch (IOException ex) {
                log.error("Could not save file");
            }

        }
    }

    /**
     * Action dealing with sending an email
     * 
     * @param event ActionEvent
     */
    @FXML
    void onSendEmail(ActionEvent event) {
        log.info("Send Email reached");
        String from = fromEmailView.getText().trim();
        String subject = subjectEmailView.getText();
        String message = messageEmailView.getHtmlText();
        String[] tos = extractEmails(toEmailView.getText());
        
        log.info("Attempt to send message: " + message);
        
        String[] ccs = extractEmails(ccEmailView.getText());
        String[] bccs = extractEmails(bccEmailView.getText());
        String[] attaches = extractAttachment();

        if (SuperValidator.validateEmailFields(tos, subject, new String[]{}, new String[]{message}, ccs, bccs, attaches, new String[]{})) {
            log.info("Sending Attempt: This is valid");

            try {
                BoostedEmail emailSent = client.sendMail(tos, subject, new String[]{""}, new String[]{message}, ccs, bccs, attaches, new String[]{});
                log.info("Email sent "+emailSent);
                emailDAO.saveEmail(emailSent);
                requestNewMessage();

                }
             catch (SQLException ex) {
                log.error(ex.getMessage());
                
            } catch (Exception e) {
                log.error(e.getMessage());
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(resources.getString("errorTitle"));
                alert.setHeaderText(resources.getString("errorHeaderHtmlLayoutUnexpected"));
                alert.setContentText(resources.getString("errorContentHtmlLayoutUnexpeceted"));
                alert.showAndWait();
            }

        } else {
            log.info("Sending Attempt: This is not valid");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(resources.getString("errorTitle"));
                alert.setHeaderText(resources.getString("errorHeaderHtmlLayoutInvalid"));
                alert.setContentText(resources.getString("errorContentHtmlLayoutInvalid"));
                alert.showAndWait();
        }
        
        try {
            updateInbox();
        } catch (SQLException ex) {
            log.error(ex.getMessage());
        }
        

    }

    /**
     * Extracting the attachment filename
     * @return 
     */
    private String[] extractAttachment() {
        String[] atts = new String[attachmentView.getChildren().size()];

        for (int i = 0; i < atts.length; i++) {
            Node node = attachmentView.getChildren().get(i);
            if (node instanceof Hyperlink) {
                Hyperlink link = (Hyperlink) node;
                log.info(link.getText());
                atts[i] = link.getText();
            }
        }

        return atts;
    }

    /**
     * Extract all the emails from the compressed string of emails into an array
     * @param compressed String
     * @return String array of emails
     */
    private String[] extractEmails(String compressed) {
        if (compressed == null || compressed.isEmpty())
            return new String[0];
        
        String[] mails = compressed.trim().split(";");
        
        //Trim the every element of the array
        for (int i = 0; i < mails.length;i++){
            mails[i] = mails[i].trim();
        }
        return mails;
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
     * Format the array of MailAddress into one string.
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

    /**
     * Fetch the first message from the list of EmailMessage.
     * @param messages List<EmailMessage>
     * @return an Email message
     */
    private String buildMessage(List<EmailMessage> messages) {
        String message = "";
        for (int i = 0; i < messages.size() && i < 1; i++) {
            message = messages.get(i).getContent();
            log.info("Fetched message "+ message);
            log.info("Mime of fetched message " +  messages.get(i).getMimeType());
            log.info("Encoding of fetched message " + messages.get(i).getEncoding());
        }
        
        

        return message;
    }

    /**
     * Sets the fields for a new message.
     */
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


    /**
     * Sets the fields for an email forwarding.
     */
    public void forwardMessage() {
        if (messageEmailView.isDisabled()) {
            String message = messageEmailView.getHtmlText();
            
            
            
            String from = fromEmailView.getText();
            String to = toEmailView.getText();
            String subject = subjectEmailView.getText();

            String forward = buildForwardMessage(message, from, to, subject);
            
            requestNewMessage();
            
            subjectEmailView.setText(subject);
            messageEmailView.setHtmlText(forward);
            
            log.info("Attempt to forward message: "+ forward);
        }
    }

    /**
     * Sets the fields for an email reply.
     */
    public void replyMessage() {

        if (messageEmailView.isDisabled()) {
            String from = fromEmailView.getText();
            String subject = subjectEmailView.getText();
            requestNewMessage();
            toEmailView.setText(from);
            subjectEmailView.setText(subject);
        }

    }

    /**
     * Turn off all write functionalities.
     */
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
        attachBtn.setDisable(true);
        attachBtn.setVisible(false);
    }

    /**
     * Turn on write functionalities.
     * 
     */
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
        attachBtn.setDisable(false);
        attachBtn.setVisible(true);

    }

    /**
     * Build the message body for an email to be forwarded
     * @param message String
     * @param from String
     * @param to String
     * @param subject String
     * @return message body
     */
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

    /**
     * Display the attachments to the view
     * @param attachments 
     */
    private void displayAttachments(List<EmailAttachment> attachments) {
        for (EmailAttachment ea : attachments) {
            Hyperlink link = new Hyperlink();
            link.setText(ea.getName());
            File file = new File(ea.getName());
            link.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    try {
                        saveFile(file.getName(), ea.toByteArray());
                    } catch (IOException ex) {
                        log.error(ex.getMessage());
                    }
                }
            });
            attachmentView.getChildren().add(link);

        }
    }

    /**
     * Save file to disk
     * @param filename String
     * @param data byte[]
     * @throws IOException 
     */
    private void saveFile(String filename, byte[] data) throws IOException {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save file");
        chooser.setInitialFileName(filename);

        File file = chooser.showSaveDialog(stage);
        Path path = file.toPath();
        if (path != null) {
            try (OutputStream out = newOutputStream(path)) {
                out.write(data);

            }
        }

    }

    /**
     * Reads bytes from an InputStream
     * 
     * @param fileStream InputStream
     * @return byte array
     * @throws IOException 
     */
    private byte[] getData(InputStream fileStream) throws IOException {
        ArrayList<Byte> dataList = new ArrayList<Byte>();

        while (fileStream.available() > 0) {
            dataList.add((byte) fileStream.read());
        }

        byte[] data = new byte[dataList.size()];
        for (int i = 0; i < data.length; i++) {
            data[i] = dataList.get(i);
        }

        return data;
    }

    /**
     * Receive and store emails to the database, and reload the view. 
     * @throws SQLException 
     */
    public void updateInbox() throws SQLException {
        BoostedEmail[] newEmailsReceived = client.receiveEmails();

        for (int i = 0; i < newEmailsReceived.length; i++) {
            emailDAO.saveEmail(newEmailsReceived[i]);
        }
        
        emailFolderController.refreshEmailTableView();
       
    }
    
    
}
