/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
 *
 * @author MDThai
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

    public IntegerProperty getId() {
        return id;
    }
    
    public int getIdValue(){
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public StringProperty getFrom() {
        return from;
    }
    
    public String getFromValue(){
        return from.get();
    }

    public void setFrom(String from) {
        this.from.set(from);
    }

    public StringProperty getSubject() {
        return subject;
    }
    
    public String getSubjectValue(){
        return subject.get();
    }

    public void setSubject(String subject) {
        this.subject.set(subject);
    }

    public StringProperty getDateRecvd() {
        return dateRecvd;
    }
    
    public String getDateRecvdValue(){
        return dateRecvd.get();
    }

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
