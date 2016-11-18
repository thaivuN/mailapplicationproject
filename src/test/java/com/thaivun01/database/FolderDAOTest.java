package com.thaivun01.database;

import com.thaivun01.beans.BoostedEmail;
import com.thaivun01.beans.ConfigurationBean;
import com.thaivun01.beans.FolderBean;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Test class serving to test Folder related routines of the EmailDAO class
 * 
 * @author Thai-Vu Nguyen
 */
public class FolderDAOTest {
    
    private ConfigurationBean config;
    private EmailDAO dao;
    public FolderDAOTest(){
        this.config = new ConfigurationBean("smtp.gmail.com","imap.gmail.com","deltathaivun@gmail.com","evilhipster");
        config.setDbUser("CS1412998");
        config.setDbPwd("ecluiste");
        this.dao = new EmailDAOImpl (config);
        
    }
    
   
    
    /**
     * Test of createFolder method, of class EmailDAOImpl.
     */
    @Test
    public void testCreateFolder() throws Exception {
        int row = dao.createFolder("Custom");
        
        List<FolderBean> folders = dao.getAllFolders();
        
        assertTrue(row == 1 && folders.size() == 5);
    }

    /**
     * Test of removeFolder method, of class EmailDAOImpl.
     */
    @Test
    public void testRemoveFolder() throws Exception {
        List<FolderBean> originals = dao.getAllFolders();

        //Delete Spam folder
        //ID number 3
        int row = dao.removeFolder(3);
        
        List<FolderBean> newFolders = dao.getAllFolders();
        
        int difference = originals.size() - newFolders.size();
        
        assertTrue(row == 1 && difference == 1);
        
    }

    /**
     * Test of getAllFolders method, of class EmailDAOImpl.
     */
    @Test
    public void testGetAllFolders() throws Exception {
        //Get all folders in the database
        //SQL file creates 4, should get 4 from DAO
        
        List<FolderBean> folders = dao.getAllFolders();
        
        assertEquals(4, folders.size());
    }
    
    /**
     * Test of getAllFolders method, of class EmailDAOImpl.
     */
    @Test
    public void testGetAllFoldersName() throws Exception {
        //Get all folders in the database
        //SQL file creates 3, should get 3 from DAO
        
        String[] expected = new String[]{"Inbox", "Sent", "Spam", "Fun Stuff"};
        
        List<FolderBean> folders = dao.getAllFolders();
        
        String[] real = new String[folders.size()];  
        
        for (int i=0; i< real.length;i++)
            real[i] = folders.get(i).getFolderName();
        
       Arrays.sort(real);
        Arrays.sort(expected);
        
        assertArrayEquals(expected, real);
    }
    
    /**
     * Test of updateEmailFolder method, of class EmailDAOImpl.
     */
    @Test
    public void testUpdateEmailFolder() throws Exception{
        BoostedEmail be = new BoostedEmail();
        be =(BoostedEmail) be.from("trump@gmail.com").subject("Test Messages").addText("Hello World")
                .to(new String[] {config.getSenderEmail(), "nyan11@gmail.com"});
        
        dao.saveEmail(be);
        
        List<BoostedEmail> bmails = dao.getAllEmailsByFolder(1);
        List<BoostedEmail> spamBox = dao.getAllEmailsByFolder(3);
        
        boolean found = false;
        for (int i = 0; i <bmails.size() && found == false; i++)
        {
            if (be.equals(bmails.get(i)))
            {
                found = true;
                be.setId(bmails.get(i).getId());
            }
        }
        
        if (found == false)
            fail("Could not find the Email");
        
        be.setFolder("Spam");
        
        int row = dao.updateEmailFolder(be);
        List<BoostedEmail> newInbox = dao.getAllEmailsByFolder(1);
        List<BoostedEmail> newSpambox = dao.getAllEmailsByFolder(3);
        
        int inboxDiff = bmails.size() - newInbox.size();
        int spamDiff = newSpambox.size() - spamBox.size();
        
        assertTrue (row == 1 && inboxDiff == 1 && spamDiff == 1);
        
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
