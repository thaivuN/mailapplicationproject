
package com.thaivun01.controllers;

import com.thaivun01.beans.BoostedEmail;
import com.thaivun01.beans.ConfigurationBean;
import com.thaivun01.business.EmailClientFace;
import com.thaivun01.database.EmailDAO;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;
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
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
    }    
    
    public void setClientBean(EmailClientFace client){
        this.client = client;
    }
    
    public void setConfigBean(ConfigurationBean configBean){
        this.configBean = configBean;
    }
    
    public void setDAOActionBean(EmailDAO emailDAO){
        this.emailDAO = emailDAO;
    }
    
    public void setEmailTableController(EmailTableLayoutController emailTableController){
        this.emailTableController = emailTableController;
    }
    
    public void displayEmailDetails(BoostedEmail email){
        
        setReadOnly();
        fromEmailView.setText(email.getFrom().getEmail());
        toEmailView.setText(buildEmailAddressesString(email.getTo()));
        ccEmailView.setText(buildEmailAddressesString(email.getCc()));
        bccEmailView.setText(buildEmailAddressesString(email.getBcc()));
        subjectEmailView.setText(email.getSubject());
        messageEmailView.setHtmlText(buildMessage(email.getAllMessages()));
        
    }
    
    private String buildEmailAddressesString(MailAddress[] addresses){
        String compactAddresses = "";
        for (int i = 0; i < addresses.length; i++){
            compactAddresses += addresses[i].getEmail();
            if (i < addresses.length - 1)
                compactAddresses += "; ";
        }
        return compactAddresses;
    }
    
    private String buildMessage(List<EmailMessage> messages){
        String message = "";
        for(int i = 0; i < messages.size() && i < 1; i++){
            message = messages.get(i).getContent();
        }
        
        return message;
    }
    
    
    public void requestNewMessage(){
        fromEmailView.setText("");
        toEmailView.setText("");
        ccEmailView.setText("");
        bccEmailView.setText("");
        subjectEmailView.setText("");
        messageEmailView.setHtmlText("");
        
        setReadWrite();
        emailTableController.unselectEmailPreview();
    }
    
    
    public void sendMessage(){
        
    }
    
    
    public void forwardMessage(){
        
    }
    
    
    public void replyMessage(){
        
        
        if (messageEmailView.isDisabled())
        {
            String from = fromEmailView.getText();
            String subject = subjectEmailView.getText();
            requestNewMessage();
            toEmailView.setText(from);
            subjectEmailView.setText(subject);
        }
        
    }
    
    private void setReadOnly(){
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
    
    private void setReadWrite(){
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
}
