
package com.thaivun01.beans;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Bean containing Email information about its database id, the sender of the email, the subject of the emai
 * and the date of when the email was received
 * 
 * @author Thai-Vu Nguyen
 */
public class EmailPreview {
    
    private IntegerProperty id;
    private StringProperty from;
    private StringProperty subject;
    private StringProperty dateRecvd;

   /**
    * Contructor
    * @param id String
    * @param from String
    * @param subject String 
    * @param date Date
    */
    public EmailPreview(int id, String from, String subject, Date date) {
        this.id = new SimpleIntegerProperty(id);
        this.subject = new SimpleStringProperty(subject);
        this.from = new SimpleStringProperty(from);
        
        //Converting from Date to LocalDateTime to String
        if (date == null){
            this.dateRecvd = new SimpleStringProperty("");
        }else{
            LocalDateTime ldt = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
            this.dateRecvd = new SimpleStringProperty(ldt.toString());
            
        }
        
    }

    /**
     * Getter for the email ID's Property
     * @return StringProperty
     */
    public IntegerProperty getId() {
        return id;
    }
    
    /**
     * Getter for the email ID
     * @return int
     */
    public int getIdValue(){
        return id.get();
    }

    /**
     * Setter for the email ID
     * @param id int 
     */
    public void setId(int id) {
        this.id.set(id);
    }

    /**
     * Getter for the Property of the sender of the email
     * @return StringProperty
     */
    public StringProperty getFrom() {
        return from;
    }
    
    /**
     * Getter for the Email Sender 
     * @return String
     */
    public String getFromValue(){
        return from.get();
    }

    /**
     * Setter for the Email Sender
     * @param from String
     */
    public void setFrom(String from) {
        this.from.set(from);
    }

    /**
     * Getter for the Email subject
     * @return StringProperty
     */
    public StringProperty getSubject() {
        return subject;
    }
    
    /**
     * Getter for the email subject Property
     * @return StringProperty
     */
    public String getSubjectValue(){
        return subject.get();
    }

    /**
     * Setter for the email subject
     * @param subject String
     */
    public void setSubject(String subject) {
        this.subject.set(subject);
    }

    /**
     * Getter for the received date's Property
     * @return StringProperty
     */
    public StringProperty getDateRecvd() {
        return dateRecvd;
    }
    
    /**
     * Getter for the received date
     * @return String
     */
    public String getDateRecvdValue(){
        return dateRecvd.get();
    }

    /**
     * Setter for the received date
     * @param dateRecvd Date
     */
    public void setDateRecvd(Date dateRecvd) {
        //Converting from Date to LocalDateTime to String
        if (dateRecvd == null){
            this.dateRecvd = new SimpleStringProperty("");
        }else{
            LocalDateTime ldt = LocalDateTime.ofInstant(dateRecvd.toInstant(), ZoneId.systemDefault());
            this.dateRecvd = new SimpleStringProperty(ldt.toString());
            
        }
    }
    
    
     
    

}
