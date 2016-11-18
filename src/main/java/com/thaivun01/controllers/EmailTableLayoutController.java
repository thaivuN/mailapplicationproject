package com.thaivun01.controllers;


import com.thaivun01.beans.BoostedEmail;
import com.thaivun01.beans.ConfigurationBean;
import com.thaivun01.beans.EmailPreview;
import com.thaivun01.database.EmailDAO;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FXML Controller class
 *
 * @author MDThai
 */
public class EmailTableLayoutController implements Initializable {
    
    private final Logger log = LoggerFactory.getLogger(getClass().getName());
    
    @FXML
    private AnchorPane emailTableFxLayout;
    
    @FXML
    private TableView<EmailPreview> emailTableView;
    
    @FXML
    private TableColumn<EmailPreview, String> subjectColumnView;
    
    @FXML
    private TableColumn<EmailPreview, String> fromColumnView;
    
    @FXML
    private TableColumn<EmailPreview, String> dateColumnView;
    
    private EmailDAO mailDAO;
    
    private EmailHtmlLayoutController emailHtmlLayoutController;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        emailTableView.setRowFactory(cb -> {
            TableRow<EmailPreview> row = new TableRow<>();
            row.setOnDragDetected(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    
                    log.info("Drag Detected ");
                    Dragboard db = row.startDragAndDrop(TransferMode.ANY);
                    ClipboardContent content = new ClipboardContent();
                    content.putString(row.getItem().getId().get() + "");
                    
                    log.info("Drag Value - " + content.getString());
                    
                    db.setContent(content);
                    
                    event.consume();
                }
            });
            return row;
        });
        
        subjectColumnView.setCellValueFactory(cellData -> cellData.getValue().getSubject());
        fromColumnView.setCellValueFactory(cellData -> cellData.getValue().getFrom());
        dateColumnView.setCellValueFactory(cellData -> cellData.getValue().getDateRecvd());
        
        
        
        emailTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> updateEmailLayout(newValue));
        
    }
    
    /**
     * Receive the DAO action bean
     * @param mailDAO 
     */
    public void setDaoObject(EmailDAO mailDAO) {
        this.mailDAO = mailDAO;
    }
    
    /**
     * Receive the Email view controller 
     * @param emailHtmlLayoutController 
     */
    public void setEmailHtmlController(EmailHtmlLayoutController emailHtmlLayoutController) {
        this.emailHtmlLayoutController = emailHtmlLayoutController;
    }

    /**
     * Update the Table
     *
     * @param folder_id
     * @throws SQLException
     */
    public void updateEmailTable(int folder_id) throws SQLException {
        emailTableView.setItems(mailDAO.getEmailPreviewByFolder(folder_id));
    }
    
    /**
     * Will update the Email View
     * @param email 
     */
    public void updateEmailLayout(EmailPreview email) {
        
        if (email != null) {
            log.info("Email id " + email.getIdValue());
            log.info("Email Subject " + email.getSubjectValue());
            
            try {
                BoostedEmail fetchMail = mailDAO.getEmailById(email.getIdValue());
                emailHtmlLayoutController.displayEmailDetails(fetchMail);
                
            } catch (SQLException ex) {
                log.error("Cannot fetch the email");
                log.error(ex.getMessage());
            }
            
        }
    }
    
    /**
     * Deselects the currently selected row in the Email table.
     */
    public void unselectEmailPreview() {
        
        emailTableView.getSelectionModel().clearSelection();
    }
    
  
    
}
