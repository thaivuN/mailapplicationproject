/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thaivun01.database;
import com.thaivun01.beans.BoostedEmail;
import com.thaivun01.beans.ConfigurationBean;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import jodd.mail.EmailAttachment;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 *Test class for the Email-related DAO actions of the EmailDAO class.
 * 
 * @author Thai-Vu Nguyen
 * @version 10/19/2016
 */

public class EmailDAOImplTest {
    
    
    private BoostedEmail be;
    private ConfigurationBean config;
    private EmailDAO dao;
    
    /**
     * Constructor
     */
    public EmailDAOImplTest() {
        
        this.config = new ConfigurationBean("smtp.gmail.com","imap.gmail.com","deltathaivun@gmail.com","evilhipster");
        this.dao = new EmailDAOImpl (config);
        
        
        
        
    }

    /**
     * Case 1: Email with an attachment with the user being the sender.
     * Email is supposed to be saved in the Sent folder
     */
    
    /**
     * Very Basic test of saveEmail method.
     * Checks only the row added
     */
    @Test
    public void testSaveEmailCase1() throws Exception {
        BoostedEmail be = new BoostedEmail();
        be =(BoostedEmail) be.from(config.getSenderEmail()).subject("Test Messages").addText("Hello World")
                .to(new String[] {"trump@gmail.com", "hipster@gmail.com"})
                .cc("hello@gmail.com")
                .bcc("bye@gmail.com");
        
        
        Path p = FileSystems.getDefault().getPath("", "me_picture_test.png");
        
        
        be.attach(EmailAttachment.attachment().setName("me_picture_test.png").bytes(Files.readAllBytes(p)).create());
        
        int row = dao.saveEmail(be);
        
        assertEquals(1, row);
        
    }
    
    

    /**
     * Test of getAllEmailsByFolder method, of class EmailDAOImpl.
     */
    @Test
    public void testGetAllEmailsByFolderCase1() throws Exception {
        BoostedEmail be = new BoostedEmail();
        be =(BoostedEmail) be.from(config.getSenderEmail()).subject("Test Messages").addText("Hello World")
                .to(new String[] {"trump@gmail.com", "hipster@gmail.com"})
                .cc("hello@gmail.com")
                .bcc("bye@gmail.com");
        
        Path p = FileSystems.getDefault().getPath("", "me_picture_test.png");
        
        
        be.attach(EmailAttachment.attachment().setName("me_picture_test.png").bytes(Files.readAllBytes(p)).create());
        
        int row = dao.saveEmail(be);
        
        List<BoostedEmail>emails = dao.getAllEmailsByFolder(2);
        boolean found = false;
        for (int i = 0; i < emails.size() && found == false;i++){
            if (be.equals(emails.get(i)))
                found = true;
        }
        
        assertTrue(found);
    }

    /**
     * Test of deleteEmail method, of class EmailDAOImpl.
     * Checks only the rows deleted
     */
    @Test
    public void testDeleteEmailCase1() throws Exception {
        BoostedEmail be = new BoostedEmail();
        be =(BoostedEmail) be.from(config.getSenderEmail()).subject("Test Messages").addText("Hello World")
                .to(new String[] {"trump@gmail.com", "hipster@gmail.com"})
                .cc("hello@gmail.com")
                .bcc("bye@gmail.com");
        
        Path p = FileSystems.getDefault().getPath("", "me_picture_test.png");
        be.attach(EmailAttachment.attachment().setName("me_picture_test.png").bytes(Files.readAllBytes(p)).create());
        
        dao.saveEmail(be);
        
        
        int rowDel = dao.deleteEmail(1);
        
        assertEquals(1, rowDel);
        
        
    }
    
    /**
     * Case 2: Email with an attachment with the user being a receiver.
     * Email is supposed to be saved in the Inbox folder
     */
    
    
    /**
     * Very Basic test of saveEmail method.
     * Checks only the row added
     */
    @Test
    public void testSaveEmailCase2() throws Exception {
        BoostedEmail be = new BoostedEmail();
        be =(BoostedEmail) be.from("trump@gmail.com").subject("Test Messages").addText("Hello World")
                .to(new String[] {config.getSenderEmail(), "nyan11@gmail.com"})
                .cc("hello@gmail.com")
                .bcc("bye@gmail.com");
        
        Path p = FileSystems.getDefault().getPath("", "me_picture_test.png");
        be.attach(EmailAttachment.attachment().setName("me_picture_test.png").bytes(Files.readAllBytes(p)).create());
        
        int row = dao.saveEmail(be);
        
        assertEquals(1, row);
        
    }
    
    /**
     * Test of getAllEmailsByFolder method, of class EmailDAOImpl.
     */
    @Test
    public void testGetAllEmailsByFolderCase2() throws Exception {
        BoostedEmail be = new BoostedEmail();
        be =(BoostedEmail) be.from("trump@gmail.com").subject("Test Messages").addText("Hello World")
                .to(new String[] {config.getSenderEmail(), "nyan11@gmail.com"})
                .cc("hello@gmail.com")
                .bcc("bye@gmail.com");
        
        Path p = FileSystems.getDefault().getPath("", "me_picture_test.png");
        be.attach(EmailAttachment.attachment().setName("me_picture_test.png").bytes(Files.readAllBytes(p)).create());
        
        int row = dao.saveEmail(be);
        
        List<BoostedEmail>emails = dao.getAllEmailsByFolder(1);
        boolean found = false;
        for (int i = 0; i < emails.size() && found == false;i++){
            if (be.equals(emails.get(i)))
                found = true;
        }
        
        assertTrue(found);
    }
    
    /**
     * Test of deleteEmail method, of class EmailDAOImpl.
     * Checks only the rows deleted
     */
    @Test
    public void testDeleteEmailCase2() throws Exception {
        BoostedEmail be = new BoostedEmail();
        be =(BoostedEmail) be.from("trump@gmail.com").subject("Test Messages").addText("Hello World")
                .to(new String[] {config.getSenderEmail(), "nyan11@gmail.com"})
                .cc("hello@gmail.com")
                .bcc("bye@gmail.com");
        
        Path p = FileSystems.getDefault().getPath("", "me_picture_test.png");
        be.attach(EmailAttachment.attachment().setName("me_picture_test.png").bytes(Files.readAllBytes(p)).create());
        
        dao.saveEmail(be);
        
        
        int rowDel = dao.deleteEmail(1);
        
        assertEquals(1, rowDel);
        
        
    }
    
    
    /**
     * Case 3: Email with an embed attachment with the user being a receiver.
     * Email is supposed to be saved in the Inbox folder
     */
    
     /**
     * Very Basic test of saveEmail method.
     * Checks only the row added
     */
    @Test
    public void testSaveEmailCase3() throws Exception {
        BoostedEmail be = new BoostedEmail();
        be =(BoostedEmail) be.from("trump@gmail.com").subject("Test Messages").addHtml("<h1>Hello World</h1> <img src='cid:me_picture_test.png' />")
                .to(new String[] {config.getSenderEmail(), "nyan11@gmail.com"})
                .cc("hello@gmail.com")
                .bcc("bye@gmail.com");
        
        
        be.embed(EmailAttachment.attachment().bytes(new File("me_picture_test.png")));
        
        
        int row = dao.saveEmail(be);
        
        assertEquals(1, row);
        
    }
    
    /**
     * Test of getAllEmailsByFolder method, of class EmailDAOImpl.
     */
    @Test
    public void testGetAllEmailsByFolderCase3() throws Exception {
        BoostedEmail be = new BoostedEmail();
        be =(BoostedEmail) be.from("trump@gmail.com").subject("Test Messages").addHtml("<h1>Hello World</h1> <img src='cid:me_picture_test.png' />")
                .to(new String[] {config.getSenderEmail(), "nyan11@gmail.com"})
                .cc("hello@gmail.com")
                .bcc("bye@gmail.com");
        
        
        be.embed(EmailAttachment.attachment().bytes(new File("me_picture_test.png")));
        
        dao.saveEmail(be);
        
        List<BoostedEmail>emails = dao.getAllEmailsByFolder(1);
        boolean found = false;
        for (int i = 0; i < emails.size() && found == false;i++){
            if (be.equals(emails.get(i)))
                found = true;
        }
        
        assertTrue(found);
    }
    
     /**
     * Test of deleteEmail method, of class EmailDAOImpl.
     * Checks only the rows deleted
     */
    @Test
    public void testDeleteEmailCase3() throws Exception {
        BoostedEmail be = new BoostedEmail();
        be =(BoostedEmail) be.from("trump@gmail.com").subject("Test Messages").addHtml("<h1>Hello World</h1> <img src='cid:me_picture_test.png' />")
                .to(new String[] {config.getSenderEmail(), "nyan11@gmail.com"})
                .cc("hello@gmail.com")
                .bcc("bye@gmail.com");
        
        
        be.embed(EmailAttachment.attachment().bytes(new File("me_picture_test.png")));
        
        dao.saveEmail(be);
        
        
        int rowDel = dao.deleteEmail(1);
        
        assertEquals(1, rowDel);
        
        
    }
    
    /**
     * Case 4: Email with an embed attachment with the user sending to himself.
     * Email is supposed to be saved in the sent folder
     */
    
     /**
     * Very Basic test of saveEmail method.
     * Checks only the row added
     */
    @Test
    public void testSaveEmailCase4() throws Exception {
        BoostedEmail be = new BoostedEmail();
        be =(BoostedEmail) be.from(config.getSenderEmail()).subject("Test Messages").addHtml("<h1>Hello World</h1> <img src='cid:me_picture_test.png' />")
                .to(new String[] {config.getSenderEmail(), "nyan11@gmail.com"})
                .cc("hello@gmail.com")
                .bcc("bye@gmail.com");
        
        
        be.embed(EmailAttachment.attachment().bytes(new File("me_picture_test.png")));
        
        
        int row = dao.saveEmail(be);
        
        assertEquals(1, row);
        
    }
    
    /**
     * Test of getAllEmailsByFolder method, of class EmailDAOImpl.
     */
    @Test
    public void testGetAllEmailsByFolderCase4() throws Exception {
        BoostedEmail be = new BoostedEmail();
        be =(BoostedEmail) be.from(config.getSenderEmail()).subject("Test Messages").addHtml("<h1>Hello World</h1> <img src='cid:me_picture_test.png' />")
                .to(new String[] {config.getSenderEmail(), "nyan11@gmail.com"})
                .cc("hello@gmail.com")
                .bcc("bye@gmail.com");
        
        
        be.embed(EmailAttachment.attachment().bytes(new File("me_picture_test.png")));
        
        dao.saveEmail(be);
        
        List<BoostedEmail>emails = dao.getAllEmailsByFolder(2);
        boolean found = false;
        for (int i = 0; i < emails.size() && found == false;i++){
            if (be.equals(emails.get(i)))
                found = true;
        }
        
        assertTrue(found);
    }
    
     /**
     * Test of deleteEmail method, of class EmailDAOImpl.
     * Checks only the rows deleted
     */
    @Test
    public void testDeleteEmailCase4() throws Exception {
        BoostedEmail be = new BoostedEmail();
        be =(BoostedEmail) be.from(config.getSenderEmail()).subject("Test Messages").addHtml("<h1>Hello World</h1> <img src='cid:me_picture_test.png' />")
                .to(new String[] {config.getSenderEmail(), "nyan11@gmail.com"})
                .cc("hello@gmail.com")
                .bcc("bye@gmail.com");
        
        
        be.embed(EmailAttachment.attachment().bytes(new File("me_picture_test.png")));
        
        dao.saveEmail(be);
        
        
        
        int rowDel = dao.deleteEmail(1);
        
        assertEquals(1, rowDel);
        
        
    }
    
    /**
     * Case 5: Email with an embed attachment with the user receiving an email from himself.
     * Email is supposed to be saved in the inbox folder
     */
    
    /**
     * Very Basic test of saveEmail method.
     * Checks only the row added
     */
    @Test
    public void testSaveEmailCase5() throws Exception {
        BoostedEmail be = new BoostedEmail();
        be =(BoostedEmail) be.from(config.getSenderEmail()).subject("Test Messages").addHtml("<h1>Hello World</h1> <img src='cid:me_picture_test.png' />")
                .to(new String[] {config.getSenderEmail(), "nyan11@gmail.com"})
                .cc("hello@gmail.com")
                .bcc("bye@gmail.com");
        
        
        be.embed(EmailAttachment.attachment().bytes(new File("me_picture_test.png")));
        be.setReceivedDate(Timestamp.valueOf(LocalDateTime.now()));
        
        int row = dao.saveEmail(be);
        
        assertEquals(1, row);
        
    }
    
    /**
     * Test of getAllEmailsByFolder method, of class EmailDAOImpl.
     */
    @Test
    public void testGetAllEmailsByFolderCase5() throws Exception {
        BoostedEmail be = new BoostedEmail();
        be =(BoostedEmail) be.from(config.getSenderEmail()).subject("Test Messages").addHtml("<h1>Hello World</h1> <img src='cid:me_picture_test.png' />")
                .to(new String[] {config.getSenderEmail(), "nyan11@gmail.com"})
                .cc("hello@gmail.com")
                .bcc("bye@gmail.com");
        
        
        be.embed(EmailAttachment.attachment().bytes(new File("me_picture_test.png")));
        be.setReceivedDate(Timestamp.valueOf(LocalDateTime.now()));
        
        dao.saveEmail(be);
        
        List<BoostedEmail>emails = dao.getAllEmailsByFolder(1);
        boolean found = false;
        for (int i = 0; i < emails.size() && found == false;i++){
            if (be.equals(emails.get(i)))
                found = true;
        }
        
        assertTrue(found);
    }
    
    /**
     * Test of deleteEmail method, of class EmailDAOImpl.
     * Checks only the rows deleted
     */
    @Test
    public void testDeleteEmailCase5() throws Exception {
        BoostedEmail be = new BoostedEmail();
        be =(BoostedEmail) be.from(config.getSenderEmail()).subject("Test Messages").addHtml("<h1>Hello World</h1> <img src='cid:me_picture_test.png' />")
                .to(new String[] {config.getSenderEmail(), "nyan11@gmail.com"})
                .cc("hello@gmail.com")
                .bcc("bye@gmail.com");
        
        
        be.embed(EmailAttachment.attachment().bytes(new File("me_picture_test.png")));
        be.setReceivedDate(Timestamp.valueOf(LocalDateTime.now()));
        
        dao.saveEmail(be);
        
        
        
        int rowDel = dao.deleteEmail(1);
        
        assertEquals(1, rowDel);
        
        
    }
    

    /**
     * This routine recreates the database for every test. This makes sure that
     * a destructive test will not interfere with any other test.
     *
     * @author Bartosz Majsak, Ken Fogel
     */
    @Before
    public void seedDatabase() {
        final String seedDataScript = loadAsString("createEmailMySQL.sql");
        try (Connection connection = DriverManager.getConnection(config.getDbUrl(), config.getDbUser(), config.getDbPwd());) {
            for (String statement : splitStatements(new StringReader(seedDataScript), ";")) {
                connection.prepareStatement(statement).execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed seeding database", e);
        }
    }

    /**
     * The following methods support the seedDatabse method
     */
    private String loadAsString(final String path) {
        try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
                Scanner scanner = new Scanner(inputStream)) {
            return scanner.useDelimiter("\\A").next();
        } catch (IOException e) {
            throw new RuntimeException("Unable to close input stream.", e);
        }
    }

    private List<String> splitStatements(Reader reader, String statementDelimiter) {
        final BufferedReader bufferedReader = new BufferedReader(reader);
        final StringBuilder sqlStatement = new StringBuilder();
        final List<String> statements = new LinkedList<String>();
        try {
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || isComment(line)) {
                    continue;
                }
                sqlStatement.append(line);
                if (line.endsWith(statementDelimiter)) {
                    statements.add(sqlStatement.toString());
                    sqlStatement.setLength(0);
                }
            }
            return statements;
        } catch (IOException e) {
            throw new RuntimeException("Failed parsing sql", e);
        }
    }

    private boolean isComment(final String line) {
        return line.startsWith("--") || line.startsWith("//") || line.startsWith("/*");
    }

}
