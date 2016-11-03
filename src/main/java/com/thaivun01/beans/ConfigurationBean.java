package com.thaivun01.beans;

import java.io.Serializable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *  Bean containing configuration values
 *
 * @author Thai-Vu Nguyen
 * @version 9/20/2016
 */
public class ConfigurationBean implements Serializable {
    
    private static final long serialVersionUID = 420517692095142206L;    
    //smtp.gmail.com
    //imap.gmail.com
    private StringProperty serverSMTP;
    private StringProperty imapServerName;
    private StringProperty senderEmail;
    private StringProperty senderPass;
    private StringProperty dbUrl;
    private StringProperty dbUser;
    private StringProperty dbPwd;
    private StringProperty username;
    private StringProperty dbPortNumber;
    private StringProperty imapPortNumber;
    private StringProperty smtpPortNumber;

    
    
    /**
     * No parameter constructor for ConfigurationBean
     * 
     * 
     */
    public ConfigurationBean(){
        this("", "", "", "");
        
        //this.serverSMTP = "smtp.gmail.com";
        //this.imapServerName = "imap.gmail.com";
        
    }
    
    /**
     * 4-parameters constructor for ConfigurationBean
     * @param serverSMTP String
     * @param imapServerName String
     * @param senderEmail String
     * @param senderPass  String
     */
    public ConfigurationBean (String serverSMTP, String imapServerName, 
            String senderEmail, String senderPass)
    {   
        this(serverSMTP,imapServerName,senderEmail, senderPass, 
                "jdbc:mysql://waldo2.dawsoncollege.qc.ca/CS1412998", 
                "CS1412998", "ecluiste");
        
    }
    
    public ConfigurationBean(String serverSMTP, String imapServerName, 
            String senderEmail, String senderPass, 
            String dbUrl, String dbUser, String dbPwd){
        this(serverSMTP, imapServerName, senderEmail, senderPass, dbUrl, dbUser,dbPwd, "");
        
    }
    
    public ConfigurationBean(String serverSMTP, String imapServerName, 
            String senderEmail, String senderPass, 
            String dbUrl, String dbUser, String dbPwd, String username){
        this(serverSMTP, imapServerName, senderEmail, senderPass, dbUrl, dbUser,dbPwd, username, "3306", "993", "465");
        
    }

    public ConfigurationBean(String serverSMTP, String imapServerName, String senderEmail, String senderPass, 
            String dbUrl, String dbUser, String dbPwd, String username, 
            String dbPortNumber, String imapPortNumber, String smtpPortNumber) {
        this.serverSMTP = new SimpleStringProperty(serverSMTP);
        this.imapServerName = new SimpleStringProperty(imapServerName);
        this.senderEmail = new SimpleStringProperty(senderEmail);
        this.senderPass = new SimpleStringProperty(senderPass);
        this.dbUrl = new SimpleStringProperty(dbUrl);
        this.dbUser = new SimpleStringProperty(dbUser);
        this.dbPwd = new SimpleStringProperty(dbPwd);
        this.username = new SimpleStringProperty(username);
        this.dbPortNumber = new SimpleStringProperty(dbPortNumber);
        this.imapPortNumber = new SimpleStringProperty(imapPortNumber);
        this.smtpPortNumber = new SimpleStringProperty(smtpPortNumber);
    }
    
    

    /**
     * Returns the SMTP server name
     * 
     * @return The SMTP server name
     */
    public String getServerSMTP() {
        return serverSMTP.get();
    }

    /**
     * Returns the IMAP server name
     * @return The IMAP server name
     */
    public String getImapServerName() {
        return imapServerName.get();
    }

    /**
     * Returns the email address
     * @return address of an address 
     */
    public String getSenderEmail() {
        return senderEmail.get();
    }

    /**
     * returns the email's password
     * @return password of an email address
     */
    public String getSenderPass() {
        return senderPass.get();
    }

    /**
     * Gets the Database URL
     * @return Database URL in String
     */
    public String getDbUrl() {
        return dbUrl.get();
    }

    /**
     * Gets the Database User
     * @return Database user in String
     */
    public String getDbUser() {
        return dbUser.get();
    }

    /**
     * Gets the DB user password
     * 
     * @return DB User Password
     */
    public String getDbPwd() {
        return dbPwd.get();
    }

    
    
    /**
     * Sets the STMP server name
     * @param serverSMTP String
     */
    public void setServerSMTP(String serverSMTP) {
        this.serverSMTP.set(serverSMTP);
    }

    /**
     * Sets the IMAP server name
     * @param imapServerName String
     */
    public void setImapServerName(String imapServerName) {
        this.imapServerName.set(imapServerName);
    }

    /**
     * Sets the email address
     * @param senderEmail String 
     */
    public void setSenderEmail(String senderEmail) {
        this.senderEmail.set(senderEmail);
    }

    /**
     * Sets the password of the email address
     * @param senderPass String
     */
    public void setSenderPass(String senderPass) {
        this.senderPass.set(senderPass);
    }

    /**
     * Sets the DB URL
     * @param dbUrl String of Database URL
     */
    public void setDbUrl(String dbUrl) {
        this.dbUrl.set(dbUrl);
    }

    /**
     * Sets the DB user
     * @param dbUser String
     */
    public void setDbUser(String dbUser) {
        this.dbUser.set(dbUser);
    }

    /**
     * Sets the DB User Password
     * @param dbPwd String
     */
    public void setDbPwd(String dbPwd) {
        this.dbPwd.set(dbPwd);
    }
    
    public String getUsername() {
        return username.get();
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public String getDbPortNumber() {
        return dbPortNumber.get();
    }

    public void setDbPortNumber(String dbPortNumber) {
        this.dbPortNumber.set(dbPortNumber);
    }

    public String getImapPortNumber() {
        return imapPortNumber.get();
    }

    public void setImapPortNumber(String imapPortNumber) {
        this.imapPortNumber.set(imapPortNumber);
    }

    public String getSmtpPortNumber() {
        return smtpPortNumber.get();
    }

    public void setSmtpPortNumber(String smtpPortNumber) {
        this.smtpPortNumber.set(smtpPortNumber);
    }
    
    public StringProperty getServerSMTPProperty(){
        return serverSMTP;
    }
    
    public StringProperty getServerIMAPProperty(){
        return imapServerName;
    }
    
    public StringProperty getEmailAddressProperty()
    {
        return senderEmail;
    }
    
    public StringProperty getEmailPwdProperty(){
        return senderPass;
    }
    
    public StringProperty getDbUrlProperty(){
        return dbUrl;
    }
    
    public StringProperty getDbUserProperty(){
        return dbUser;
    }
    
    public StringProperty getDbPwdProperty(){
        return dbPwd;
    }
    
    public StringProperty getUsernameProperty(){
        return username;
    }
    
    public StringProperty getDbPortProperty(){
        return dbPortNumber;
    }
    
    public StringProperty getImapPortProperty(){
        return imapPortNumber;
    }
    
    public StringProperty getSmtpPortProperty(){
        return smtpPortNumber;
    }
    
    
}
