/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package validator;

import com.thaivun01.beans.ConfigurationBean;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author MDThai
 */
public class SuperValidatorTest {
    private ConfigurationBean config;
    
    
    
    public SuperValidatorTest() {
    }
    
    @Before
    public void setUp() {
        config = new ConfigurationBean();
        config.setUsername("thaivuN");
        config.setSenderEmail("deltathaivun@gmail.com");
        config.setSenderPass("evilhipster");
        config.setDbUser("CS1412998");
        config.setDbPwd("ecluiste");
        config.setDbPortNumber("3306");
        
    }

    
    /**
     * Test of validateConfiguration method, of class SuperValidator.
     */
    @Test
    public void testValidateConfiguration() {
        boolean result = SuperValidator.validateConfiguration(config);
        
        assertTrue(result);
    }
    
   
    
}
