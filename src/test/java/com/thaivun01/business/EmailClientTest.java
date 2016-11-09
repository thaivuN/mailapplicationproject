/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thaivun01.business;


import com.thaivun01.beans.ConfigurationBean;
import com.thaivun01.beans.BoostedEmail;
import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import jodd.mail.EmailAttachment;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 *  This is the test class for the EmailClient class
 * 
 * @author Thai-Vu Nguyen
 * @version 9/20/2016
 */
@Ignore
@RunWith(Parameterized.class)
public class EmailClientTest {
    
    
    /**
     * Setting up our parameterized testing
     * 
     */
    @Parameters(name="{index} plan[{0}]={1}]")
    public static Collection<Object[]> data() {
        return Arrays.asList(
                new Object[][]{
                    
                    {
                        new ConfigurationBean("smtp.gmail.com","imap.gmail.com","deltathaivun@gmail.com","evilhipster"),
                        new String[]{"superwalkurefan@gmail.com"}, "Test Message 1", new String[]{"The following is the first test message. It is very much plains"}, new String[]{}, 
                        new String[]{}, new String[]{}, new String [] {}, new String []{} 
                    }
                        
                   ,
                    {
                        new ConfigurationBean("smtp.gmail.com","imap.gmail.com","deltathaivun@gmail.com","evilhipster"),
                        new String[]{"superwalkurefan@gmail.com"}, "Test Message 2", new String[]{"The following is the second test message"}, new String[]{"<h3>Test</h3><p>Paragraph</p>"}, 
                        new String[]{"thaireceivecc1@gmail.com"}, new String[]{}, new String [] {"me_picture_test.png"}, new String []{} 
                    }
                   ,
                    {
                        new ConfigurationBean("smtp.gmail.com","imap.gmail.com","deltathaivun@gmail.com","evilhipster"),
                        new String[]{"superwalkurefan@gmail.com", "thaireceivecc1@gmail.com"}, "Test Message 3", new String[]{"This is the second test with two receivers"}, new String[]{}, 
                        new String[]{}, new String[]{}, new String [] {"me_picture_test.png"}, new String []{} 
                    }
                        ,
                    
                    {
                        new ConfigurationBean("smtp.gmail.com","imap.gmail.com","deltathaivun@gmail.com","evilhipster"),
                        new String[]{"superwalkurefan@gmail.com"}, "Test Message 4", new String[]{}, new String[]{"<h3>This is a header</h3><p>Here is a picture <img src='cid:me_picture_test.png' /></p>"}, 
                        new String[]{}, new String[]{}, new String [] {}, new String []{"me_picture_test.png"} 
                    }
                        ,
                        
                    {
                        new ConfigurationBean("smtp.gmail.com","imap.gmail.com","deltathaivun@gmail.com","evilhipster"),
                        new String[]{"superwalkurefan@gmail.com"}, "Test Message 5", new String[]{""}, new String[]{"<h1>Hello, this a header</h1><p>Here is a picture</p><img src='cid:mercats.jpg' /> <p>Goodbye</p> "}, 
                        new String[]{}, new String[]{"thaireceivecc1@gmail.com"}, new String [] {"me_picture_test.png"}, new String []{"mercats.jpg"} 
                    }
                    
                }
        );
        
    }
    
    private EmailClientFace ec;
    private ConfigurationBean config;
    private String[] expectedTos;
    private String expectedSubject;
    private String[] expectedMessages;
    private String[] expectedHTMLMessages;
    private String[] expectedCcs;
    private String[] expectedBccs;
    private String[] expectedAttachments;
    private String[] expectedEmbedFiles;
            
    /**
     * Testing class constructor
     * @param config ConfigurationBean
     * @param tos String[]
     * @param subject String
     * @param messages String[]
     * @param htmlMessages String[]
     * @param ccs String[]
     * @param bccs String[]
     * @param attachments String[]
     * @param embedFiles String[]
     */
    public EmailClientTest(ConfigurationBean config, String[] tos, String subject, String[] messages, String [] htmlMessages, 
            String[] ccs, String[] bccs, String [] attachments, String[] embedFiles) {
        this.config = config;
        this.ec = new EmailClient (this.config);
        this.expectedTos = tos;
        this.expectedSubject = subject;
        this.expectedMessages = messages;
        this.expectedHTMLMessages = htmlMessages;
        this.expectedCcs = ccs;
        this.expectedBccs = bccs;
        this.expectedAttachments = attachments;
        this.expectedEmbedFiles = embedFiles;
        
    }
    

    /**
     * Test of sendMail and receiveMail methods, of class EmailClient.
     */
    
    @Test
    public void testSendandReceiveMail() {
        
        BoostedEmail expectedEmail = ec.sendMail(expectedTos, expectedSubject, expectedMessages, 
                expectedHTMLMessages, expectedCcs, expectedBccs, 
                expectedAttachments, expectedEmbedFiles);
        
        //Wait 5 seconds to make sure the server receives the email sent
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
           //Weird thing happened
           //Fail the test
            fail("Interuption");

        }
        
        //Putting credential to to the receiver email
        config.setSenderEmail("superwalkurefan@gmail.com");
        config.setSenderPass("ichidodake");
        
        //Receiving the emails 
        BoostedEmail [] recvEmail = ec.receiveEmails();
        
        
        
        boolean found = false;
        
        //Check if the sent email matches one of the received emails
        for (int i = 0; i < recvEmail.length && found == false; i++)
            if (expectedEmail.equals(recvEmail[i]))
                found = true;
        
        //Assert if the email was found
        assertTrue(found);
        
        
        
    }
    


  

    
    
}
