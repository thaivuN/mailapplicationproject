/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
 *
 * @author MDThai
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
            
            //TO DO: LOAD configuration
            
            found = true;
        }
        
        return found;
    }
    
    public final void writePropertiesTxtFile(final ConfigurationBean configBean, final String path, final String filename) throws IOException{
        Properties properties = new Properties();
        
        //TO DO: LOAD PROP
        
        Path file = get(path, filename + ".properties");
        
        try(OutputStream fileStream = newOutputStream(file)){
            properties.store(fileStream, "Configuration Properties");
        }
    }
    
}
