package validator;

import com.thaivun01.beans.ConfigurationBean;
import java.util.ArrayList;
import java.util.List;
import jodd.mail.EmailAddress;

/**
 *
 * @author MDThai
 */
public class SuperValidator {

    private SuperValidator() {

    }

    /**
     * Check if the string is empty or null
     * @param input String
     * @return true or false
     */
    private static boolean isEmpty(String input) {
        if (input == null || input.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Check if the string input is numeric
     * @param num String
     * @return true if numeric, false if not
     */
    private static boolean isNumeric(String num) {

        try {
            int value = Integer.parseInt(num);
        } catch (NumberFormatException nfe) {
            return false;
        }

        return true;
    }

    /**
     * Check if the email input is a valid email message
     * @param email String
     * @return true if valid, false if not valid
     */
    public static boolean isValidEmail(String email) {

        EmailAddress address = new EmailAddress(email);

        return address.isValid();

    }

    /**
     * Validate the fields of the ConfigurationBean
     * @param configBean ConfigurationBean
     * @return true if valid, false if invalid
     */
    public static boolean validateConfiguration(ConfigurationBean configBean) {
        String[] keys = new String[]{configBean.getUsername(),
            configBean.getDbUrl(), configBean.getDbUser(), configBean.getDbPwd(),
            configBean.getDbPortNumber(), configBean.getSenderEmail(),
            configBean.getSenderPass(), configBean.getImapServerName(),
            configBean.getImapPortNumber(), configBean.getServerSMTP(),
            configBean.getSmtpPortNumber()
        };

        for (int i = 0; i < keys.length; i++) {
            if (isEmpty(keys[i])) {
                return false;
            }
        }

        if (isValidEmail(configBean.getSenderEmail()) == false) {
            return false;
        }

        String[] portsToValidate = new String[]{
            configBean.getDbPortNumber(), configBean.getImapPortNumber(),
            configBean.getSmtpPortNumber()
        };

        for (int i = 0; i < portsToValidate.length; i++) {
            if (isNumeric(portsToValidate[i]) == false) {
                return false;
            }
        }

        return true;
    }
    
    /**
     * Validate the fields needed to send an email
     * @param tos
     * @param subject
     * @param messages
     * @param htmlMessages
     * @param ccs
     * @param bccs
     * @param filenames
     * @param embedFiles
     * @return 
     */
    public static boolean validateEmailFields (String [] tos, String subject, String [] messages, 
            String [] htmlMessages, String [] ccs, String [] bccs, 
            String []filenames, String[] embedFiles){
        
        if (tos == null || tos.length == 0)
            return false;
        
        if (subject == null || subject.isEmpty())
            return false;
        
        if (validateEmails(tos) == false){
            return false;
        }
        
        if (ccs != null){
            if (validateEmails(ccs) == false)
                return false;
        }
        
        if (bccs != null){
            if (validateEmails(bccs) == false)
                return false;
        }
       
        return true;
    }
    
    /**
     * Validate an array of emails
     * @param emails String[]
     * @return true if valid, false if invalid
     */
    private static boolean validateEmails(String[] emails){
        for (int i = 0; i < emails.length;i++){
            if (isValidEmail(emails[i]) == false)
                return false;
            
        }
        return true;
    }
}
