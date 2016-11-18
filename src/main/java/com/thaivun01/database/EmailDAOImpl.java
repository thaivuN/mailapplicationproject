package com.thaivun01.database;

import com.thaivun01.beans.BoostedEmail;
import com.thaivun01.beans.ConfigurationBean;
import com.thaivun01.beans.EmailPreview;
import com.thaivun01.beans.FolderBean;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jodd.mail.EmailAddress;
import jodd.mail.EmailAttachment;
import jodd.mail.EmailMessage;
import jodd.mail.MailAddress;

/**
 *  DAO Action class for the actions related to BoostedEmail and FolderBean
 * 
 * @author Thai-vu Nguyen
 */
public class EmailDAOImpl implements EmailDAO {

    private ConfigurationBean config;

    /**
     * Constructor
     * @param config ConfigurationBean
     */
    public EmailDAOImpl(ConfigurationBean config) {
        this.config = config;
    }

    /**
     * Get an observable list of all FolderBeans
     * @return Observable list of FolderBean objects
     * @throws SQLException 
     */
    @Override
    public ObservableList<FolderBean> getAllFoldersTree() throws SQLException {
        ArrayList<FolderBean> fetchedFolders = getAllFolders();
        
        ObservableList<FolderBean> folders = FXCollections.observableArrayList();
        
        for(FolderBean folder : fetchedFolders){
            folders.add(folder);
        }
        
        return folders;
    }
    
    

    
    
    /**
     * Creates a folder into the database
     * 
     * @param folder
     * @return number of rows affected
     * @throws SQLException 
     */
    @Override
    public int createFolder(String folder) throws SQLException {

        String query = "INSERT INTO FOLDER (FOLDER_NAME) VALUES (?)";

        try (Connection c = DriverManager.getConnection(config.getDbUrl(),
                config.getDbUser(), config.getDbPwd());
                PreparedStatement ps = c.prepareStatement(query)) {

            ps.setString(1, folder);

            int row = ps.executeUpdate();

            return row;
        }
    }

    /**
     * Removes a folder from the database
     * 
     * @param folder_id
     * @return number of rows affected
     * @throws SQLException 
     */
    @Override
    public int removeFolder(int folder_id) throws SQLException {
        String query = "DELETE FROM FOLDER WHERE FOLDER_ID = ?";

        try (Connection c = DriverManager.getConnection(config.getDbUrl(),
                config.getDbUser(), config.getDbPwd());
                PreparedStatement ps = c.prepareStatement(query);) {
            ps.setInt(1, folder_id);

            int rows = ps.executeUpdate();

            return rows;
        }

    }

    /**
     * Retrieves all the folders in the database in the form of FolderBean inside of an ArrayList
     * @return ArrayList of FolderBean
     * @throws SQLException 
     */
    @Override
    public ArrayList<FolderBean> getAllFolders() throws SQLException {
        ArrayList<FolderBean> folders = new ArrayList<>();
        String query = "SELECT FOLDER_ID, FOLDER_NAME FROM FOLDER ORDER BY FOLDER_ID ASC";
        try (Connection c = DriverManager.getConnection(config.getDbUrl(),
                config.getDbUser(), config.getDbPwd());
                PreparedStatement ps = c.prepareStatement(query);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                FolderBean folder = new FolderBean(name, id);
                folders.add(folder);

            }

            return folders;

        }
    }

    /**
     * Updates the Email's Folder
     * 
     * @param be BoostedEmail
     * @return number of rows affected
     * @throws SQLException 
     */
    @Override
    public int updateEmailFolder(BoostedEmail be) throws SQLException {
        
        int result = 0;
        
        int folder_id = -1;
        List<FolderBean> folders = getAllFolders();
        boolean found = false;
        for (int i = 0;i < folders.size() && found == false;i++){
            if (folders.get(i).getFolderName().equalsIgnoreCase(be.getFolder())){
                folder_id = folders.get(i).getFolderID();
                found = true;
            }
        }
        
        if (found == false)
            throw new SQLException("No Folder exists");
        
        String query = "UPDATE EMAIL SET FOLDER_ID = ? WHERE EMAIL_ID = ?";
        
        try(Connection c = DriverManager.getConnection(config.getDbUrl(),
                config.getDbUser(), config.getDbPwd());
                PreparedStatement ps = c.prepareStatement(query);){
            
            ps.setInt(1, folder_id);
            ps.setInt(2, be.getId());
            
            result = ps.executeUpdate();
            
        }
        
        return result;
    }

    /**
     * Updates the Email's Folder
     * 
     * @param email_id int
     * @param folder_id int
     * @return number of rows affects
     * @throws SQLException 
     */
    @Override
    public int updateEmailFolder(int email_id, int folder_id) throws SQLException {
        int result = 0;
        
        String query = "UPDATE EMAIL SET FOLDER_ID = ? WHERE EMAIL_ID = ?";
        
        try(Connection c = DriverManager.getConnection(config.getDbUrl(),
                config.getDbUser(), config.getDbPwd());
                PreparedStatement ps = c.prepareStatement(query);){
            
            ps.setInt(1, folder_id);
            ps.setInt(2, email_id);
            
            result = ps.executeUpdate();
            
        }
        
        return result;
    }
    
    
    
    /**
     * Saves a BoostedEmail into the database
     * @param be BoostedEmail
     * @return number of rows affected
     * @throws SQLException 
     */

    @Override
    public int saveEmail(BoostedEmail be) throws SQLException {
        String createQuery
                = "INSERT INTO EMAIL (EMAIL_FROM, EMAIL_SUBJECT, "
                + "FOLDER_ID, SENT_DATE, RECEIVED_DATE) "
                + "VALUES (?,?,?,?,?)";

        //Resolve between Inbox and Sent emails
        int folder_id;
        
        if (config.getSenderEmail().equals(be.getFrom().getEmail()))
        {
            folder_id = 2; //Default: Sent
            if (be.getReceivedDate() != null)
                folder_id= 1; //Inbox if the Email has a received date
        }
        else
            folder_id = 1; //Inbox

        //Convert java.util.Date to Timestamp
        Timestamp sent = (be.getSentDate() != null) ? new Timestamp(be.getSentDate().getTime()) : null;
        Timestamp rcved = (be.getReceivedDate() != null) ? new Timestamp(be.getReceivedDate().getTime()) : null;

        try (Connection c = DriverManager.getConnection(config.getDbUrl(),
                config.getDbUser(), config.getDbPwd());
                PreparedStatement ps = c.prepareStatement(createQuery,
                        Statement.RETURN_GENERATED_KEYS);) {
            ps.setString(1, be.getFrom().getEmail());
            ps.setString(2, be.getSubject());
            ps.setInt(3, folder_id);
            ps.setTimestamp(4, sent);
            ps.setTimestamp(5, rcved);

            int updatedRows = ps.executeUpdate();
            if (updatedRows != 1) {
                throw new SQLException("COULD NOT ADD EMAIL TO DATABASE");
            }

            try (ResultSet generatedKey = ps.getGeneratedKeys()) {

                if (generatedKey.next()) {
                    int key = generatedKey.getInt(1);
                    

                    for (EmailAttachment ea : be.getAttachments()) {
                        storeAttachment(c, ea, key);
                    }

                    for (EmailMessage messages : be.getAllMessages()) {
                        storeMessage(c, messages, key);
                    }

                    storeDestinationEmails(c, be, key);

                    return updatedRows;
                } else {
                    throw new SQLException("COULD NOT FIND KEY");
                }

            }

        }

    }

    /**
     * Retrives all BoostedEmail by folder_id from the database
     * @param folder_id int
     * @return ArrayList of BoostedEmail
     * @throws SQLException 
     */
    @Override
    public ArrayList<BoostedEmail> getAllEmailsByFolder(int folder_id) throws SQLException {
        ArrayList<BoostedEmail> bmails = new ArrayList<>();

        String query = "SELECT EMAIL_ID FROM EMAIL WHERE FOLDER_ID = ?";

        try (Connection c = DriverManager.getConnection(config.getDbUrl(),
                config.getDbUser(), config.getDbPwd());
                PreparedStatement ps = c.prepareStatement(query);) {

            ps.setInt(1, folder_id);

            try (ResultSet rs = ps.executeQuery();) {
                while (rs.next()) {
                    int email_id = rs.getInt(1);
                    BoostedEmail be = assembleEmail(c, email_id);
                    bmails.add(be);
                }

                return bmails;
            }
        }

    }

    /**
     * Deletes an email from the database
     * @param id int
     * @return rows affected
     * @throws SQLException 
     */
    @Override
    public int deleteEmail(int id) throws SQLException {
        String query = "DELETE FROM EMAIL WHERE EMAIL_ID = ?";

        try (Connection c = DriverManager.getConnection(config.getDbUrl(),
                config.getDbUser(), config.getDbPwd());
                PreparedStatement ps = c.prepareStatement(query);) {

            ps.setInt(1, id);
            int row = ps.executeUpdate();
            return row;
        }
    }

    /**
     * Store an Attachment into the database
     * @param c Connection
     * @param ea EmailAttachment
     * @param email_id int
     * @return rows affected
     * @throws SQLException 
     */
    private int storeAttachment(Connection c, EmailAttachment ea, int email_id) throws SQLException {

        String query = "INSERT INTO BINARYFILES "
                + "(FILE_NAME, FILE_DATA, CID, EMAIL_ID) "
                + "VALUES(?,?,?,?)";
        try (PreparedStatement ps = c.prepareStatement(query);) {

            ps.setString(1, ea.getName());
            ps.setBytes(2, ea.toByteArray());
            ps.setString(3, ea.getContentId());
            ps.setInt(4, email_id);

            int row = ps.executeUpdate();

            return row;
        }
    }

    /**
     * Store an EmailMessage into the database
     * @param c Connection
     * @param message EmailMessage
     * @param email_id int
     * @return rows affected
     * @throws SQLException 
     */
    private int storeMessage(Connection c, EmailMessage message, int email_id) throws SQLException {
        String query = "INSERT INTO EMAILMESSAGE (CONTENT, ENCODING, MIME, EMAIL_ID) VALUES(?,?,?,?) ";
        try (PreparedStatement ps = c.prepareStatement(query);) {

            ps.setString(1, message.getContent());
            ps.setString(2, message.getEncoding());
            ps.setString(3, message.getMimeType());
            ps.setInt(4, email_id);

            int row = ps.executeUpdate();

            return row;
        }
    }
    

    /**
     * Stores the TOs, CCs and BCCs of a BoostedEmail into the database
     * @param c Connection
     * @param be BoostedEmail
     * @param email_id int
     * @throws SQLException 
     */
    private void storeDestinationEmails(Connection c, BoostedEmail be, int email_id) throws SQLException {
        for (MailAddress to : be.getTo()) {
            int toKey = storeEmailAddress(c, to);
            bridgeAddress(c, email_id, toKey, "TO_BRIDGE");

        }

        for (MailAddress cc : be.getCc()) {
            int ccKey = storeEmailAddress(c, cc);
            bridgeAddress(c, email_id, ccKey, "CC_BRIDGE");
        }

        for (MailAddress bcc : be.getBcc()) {
            int bccKey = storeEmailAddress(c, bcc);
            bridgeAddress(c, email_id, bccKey, "BCC_BRIDGE");
        }

    }

    /**
     * Will store an Email address to the database and return its ID If the
     * email address already exists, the ID of that existing email will simply
     * be returned.
     *
     * @param c Connection
     * @param address MailAddress
     * @return the ID of an email address
     * @throws SQLException
     */
    private int storeEmailAddress(Connection c, MailAddress address) throws SQLException {
        //Find if email already exists
        String query = "SELECT EA_ID FROM EMAILADDRESS WHERE EA_NAME = ?";

        try (PreparedStatement ps = c.prepareStatement(query);) {
            ps.setString(1, address.getEmail());

            try (ResultSet result = ps.executeQuery()) {
                if (result.next()) {
                    //Email address already exists in database
                    //Return the ID
                    return result.getInt(1);
                } else {
                    String insert_query
                            = "INSERT INTO EMAILADDRESS (EA_NAME) VALUES (?)";
                    //Store the Email Address
                    int key = storeEAAndGetID(c, address.getEmail());
                    return key;

                }
            }
        }
    }

    /**
     * Store the string email address into the database 
     * @param c Connection
     * @param address String
     * @return the ID of an email Address;
     * @throws SQLException 
     */
    private int storeEAAndGetID(Connection c, String address) throws SQLException {
        String insert_query = "INSERT INTO EMAILADDRESS (EA_NAME) VALUES (?)";

        try (PreparedStatement ps
                = c.prepareStatement(insert_query,
                        Statement.RETURN_GENERATED_KEYS);) {

            ps.setString(1, address);
            ps.executeUpdate();
            try (ResultSet generatedKey = ps.getGeneratedKeys()) {
                if (generatedKey.next()) {
                    int recordNum = generatedKey.getInt(1);
                    return recordNum;
                } else {
                    throw new SQLException("Couldn't not get Email Address ID");
                }

            }

        }

    }

    /**
     * Creates a connection between an email and an email_address
     *
     * @param email_id
     * @param address_id
     * @param bridge_name
     * @return
     * @throws SQLException
     */
    private int bridgeAddress(Connection c, int email_id, int address_id, String bridge_name) throws SQLException {
        String bridge_query = "INSERT INTO " + bridge_name + " (EMAIL_ID, EA_ID) VALUES (?,?)";

        try (PreparedStatement ps = c.prepareStatement(bridge_query);) {
            ps.setInt(1, email_id);
            ps.setInt(2, address_id);

            int rows = ps.executeUpdate();

            return rows;

        }

    }

    /**
     * Assembles a BoostedEmail based on the Email ID
     * @param c Connection
     * @param id int
     * @return BoostedEmail
     * @throws SQLException 
     */
    private BoostedEmail assembleEmail(Connection c, int id) throws SQLException {
        BoostedEmail be = new BoostedEmail();
        
        String query = "SELECT EMAIL_FROM, EMAIL_SUBJECT, "
                + "SENT_DATE, RECEIVED_DATE, FOLDER_ID "
                + "FROM EMAIL WHERE EMAIL_ID = ?";
        try (PreparedStatement ps = c.prepareStatement(query)) {
            ps.setInt(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    
                    be.setId(id);

                    be = (BoostedEmail) be.from(rs.getString(1))
                            .subject(rs.getString(2));

                    be.setSentDate(rs.getTimestamp(3));
                    be.setReceivedDate(rs.getTimestamp(4));

                    //Get Tos
                    String[] tos = getAddresses(c, id, "TO_BRIDGE");
                    //Get ccs
                    String[] ccs = getAddresses(c, id, "CC_BRIDGE");
                    //Get bccs
                    String[] bccs = getAddresses(c, id, "BCC_BRIDGE");

                    be.setTo(MailAddress.createFrom(tos));
                    be.setCc(MailAddress.createFrom(ccs));
                    be.setBcc(MailAddress.createFrom(bccs));
                    //Messages
                    List<EmailMessage> messages = getMessages(c, id);
                    be.setMessages(messages);
                    //Attachments
                    ArrayList<EmailAttachment> attachs = getAttachments(c, id);
                    be.setAttachments(attachs);
                    
                    be.setFolder(getFolderName(c, rs.getInt(5)));

                    return be;
                } else {
                    throw new SQLException("Could not find email ID");
                }
            }

        }
    }

    /**
     * Retrives all messages of an Email based on the ID of the Email
     * @param c Connection
     * @param email_id int
     * @return ArrayList of EmailMessage
     * @throws SQLException 
     */
    private ArrayList<EmailMessage> getMessages(Connection c, int email_id) throws SQLException {
        ArrayList<EmailMessage> messages = new ArrayList<>();

        String query = "SELECT CONTENT, ENCODING, MIME "
                + "FROM EMAILMESSAGE WHERE EMAIL_ID = ?";

        try (PreparedStatement ps = c.prepareStatement(query)) {
            ps.setInt(1, email_id);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    //Constructor EmailMessage(Content, MIME, ENCODING)
                    EmailMessage eMess
                            = new EmailMessage(rs.getString(1),
                                    rs.getString(3),
                                    rs.getString(2));
                    messages.add(eMess);
                }
            }

        }

        return messages;
    }

    /**
     * Retrives all Addresses based on the Email's ID and on 
     * the name of the bridge table between TO_BRIDGE, CC_BRIDGE and BCC_BRIDGE
     * 
     * @param c Connection
     * @param email_id int
     * @param bridge_table String
     * @return String [] of email addresses
     * @throws SQLException 
     */
    private String[] getAddresses(Connection c, int email_id, String bridge_table) throws SQLException {
        List<String> emailAddresses = new ArrayList<>();
        String query = "SELECT EA_NAME FROM EMAILADDRESS "
                + "INNER JOIN " + bridge_table + " USING (EA_ID) "
                + "INNER JOIN EMAIL USING (EMAIL_ID) "
                + "WHERE EMAIL.EMAIL_ID = ?";

        try (PreparedStatement ps = c.prepareStatement(query);) {
            ps.setInt(1, email_id);

            try (ResultSet rs = ps.executeQuery();) {
                while (rs.next()) {
                    MailAddress ma = new MailAddress("");
                    emailAddresses.add(rs.getString(1));
                }
                String[] eas = new String[emailAddresses.size()];
                for (int i = 0; i < eas.length; i++) {
                    eas[i] = emailAddresses.get(i);
                }
                return eas;
            }
        }

    }

    /**
     * Retrieves all the attachments based on an Email's ID
     * @param c
     * @param email_id
     * @return
     * @throws SQLException 
     */
    private ArrayList<EmailAttachment> getAttachments(Connection c, int email_id) throws SQLException {
        ArrayList<EmailAttachment> atts = new ArrayList<>();
        String query = "SELECT FILE_ID, FILE_NAME, FILE_DATA, CID "
                + "FROM BINARYFILES WHERE EMAIL_ID = ?";
        try (PreparedStatement ps = c.prepareStatement(query)) {
            ps.setInt(1, email_id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    
                    String file_name = rs.getString(2);
                    byte[] data = rs.getBytes(3);
                    String cid = rs.getString(4);
                    

                    EmailAttachment ea
                            = EmailAttachment.attachment()
                            .setName(file_name)
                            .setInline(cid)
                            .bytes(data)
                            .create();
                    
                    atts.add(ea);

                }

                return atts;
            }
        }

    }
    
    /**
     * Retrives a folder's name based on the id of a Folder
     * @param c Connection
     * @param folder_id int
     * @return the name of a Folder in the database
     * @throws SQLException 
     */
    private String getFolderName (Connection c, int folder_id) throws SQLException{
        String query = "SELECT FOLDER_NAME FROM FOLDER WHERE FOLDER_ID = ?";
        
        try(PreparedStatement ps = c.prepareStatement(query)){
            
            ps.setInt(1, folder_id);
            try(ResultSet rs = ps.executeQuery();){
                if (rs.next())
                    return rs.getString(1);
                else
                    throw new SQLException("Could not find folder ID");
            }
        }
    }

    @Override
    public ObservableList<EmailPreview> getEmailPreviewByFolder(int folder_id) throws SQLException {
        String query = "SELECT EMAIL_ID, EMAIL_FROM, EMAIL_SUBJECT, "
                + "RECEIVED_DATE "
                + "FROM EMAIL WHERE FOLDER_ID = ?";
        
        ObservableList<EmailPreview> previews = FXCollections.observableArrayList();
        
        try(Connection c = DriverManager.getConnection(config.getDbUrl(),
                config.getDbUser(), config.getDbPwd());
                PreparedStatement ps = c.prepareStatement(query);){
            
            ps.setInt(1, folder_id);
            
            try(ResultSet rs = ps.executeQuery();){
                
                while(rs.next()){
                    
                    int email_id = rs.getInt(1);
                    String email_from = rs.getString(2);
                    String email_subject = rs.getString(3);
                    Date date = rs.getTimestamp(4);
                    
                    EmailPreview mailPreview = new EmailPreview(email_id, email_from, email_subject, date);
                    previews.add(mailPreview);
                    
                }
                
                return previews;
            }
        }
        
    }

    @Override
    public BoostedEmail getEmailById(int email_id) throws SQLException {
        try(Connection c = DriverManager.getConnection(config.getDbUrl(),
                config.getDbUser(), config.getDbPwd());){
            
            BoostedEmail email = assembleEmail(c, email_id);
            return email;
        }
    }
    
    

}
