package com.thaivun01.manager;

import com.thaivun01.beans.ConfigurationBean;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import static java.nio.file.Files.newInputStream; 
import static java.nio.file.Files.newOutputStream;
import static java.nio.file.Paths.get;

/**
 * Action class used to load or write a Configuration Bean
 * 
 * @author Thai-Vu Nguyen
 */
public class PropertiesManager {
    
    public final boolean loadPropertiesTxtFile (final ConfigurationBean configBean, final String path, final String filename) throws IOException{
        boolean found = false;
        
        Properties properties = new Properties();
        
        Path file = get(path, filename + ".properties");
        if (Files.exists(file)){
            try(InputStream fileStream = newInputStream(file);){
                properties.load(fileStream);
            }
            
            //LOAD configuration
            configBean.setUsername(properties.getProperty("username"));
            configBean.setSenderEmail(properties.getProperty("emailaddress"));
            configBean.setSenderPass(properties.getProperty("emailpassword"));
            configBean.setDbUrl(properties.getProperty("databaseurl"));
            configBean.setDbUser(properties.getProperty("databaseuser"));
            configBean.setDbPwd(properties.getProperty("databasepassword"));
            configBean.setDbPortNumber(properties.getProperty("databaseport"));
            configBean.setImapServerName(properties.getProperty("imapserver"));
            configBean.setImapPortNumber(properties.getProperty("imapport"));
            configBean.setServerSMTP(properties.getProperty("smtpserver"));
            configBean.setSmtpPortNumber(properties.getProperty("smtpport"));
            
            found = true;
        }
        
        return found;
    }
    
    public final void writePropertiesTxtFile(final ConfigurationBean configBean, final String path, final String filename) throws IOException{
        Properties properties = new Properties();
        
        //LOAD PROPERTIES
        properties.setProperty("username", configBean.getUsername());
        properties.setProperty("emailaddress", configBean.getSenderEmail());
        properties.setProperty("emailpassword", configBean.getSenderPass());
        properties.setProperty("databaseurl", configBean.getDbUrl());
        properties.setProperty("databaseuser", configBean.getDbUser());
        properties.setProperty("databasepassword", configBean.getDbPwd());
        properties.setProperty("databaseport", configBean.getDbPortNumber());
        properties.setProperty("imapserver", configBean.getImapServerName());
        properties.setProperty("imapport", configBean.getImapPortNumber());
        properties.setProperty("smtpserver", configBean.getServerSMTP());
        properties.setProperty("smtpport", configBean.getSmtpPortNumber());

        
        Path file = get(path, filename + ".properties");
        
        try(OutputStream fileStream = newOutputStream(file)){
            properties.store(fileStream, "Configuration Properties");
        }
    }
    
}
