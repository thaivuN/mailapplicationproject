/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thaivun01.business;

import com.thaivun01.beans.ConfigurationBean;
import com.thaivun01.beans.BoostedEmail;

/**
 * Interface for our bean action class that sends and receives email
 * 
 * @author Thai-Vu Nguyen
 * @version 9/20/2016
 */
public interface EmailClientFace {
    
    /**
     * Send a BoostedEmail object
     * 
     * @param tos String[]
     * @param subject String
     * @param messages String[]
     * @param htmlMessages String[]
     * @param ccs String[]
     * @param bccs String[]
     * @param filenames String[]
     * @param embedFiles String[]
     * @return The BoostedEmail sent 
     */
    BoostedEmail sendMail (String [] tos, String subject, String [] messages, 
            String [] htmlMessages, String [] ccs, String [] bccs, 
            String []filenames, String[] embedFiles);
    
    
    /**
     * Receives an array of BoostedEmail objects
     * 
     * @return An array of BoostedEmail received
     */
    BoostedEmail [] receiveEmails ();
    
    
}
