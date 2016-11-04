/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package validator;

import jodd.mail.EmailAddress;

/**
 *
 * @author MDThai
 */
public class SuperValidator {
    private SuperValidator(){
        
    }
    
    public static boolean isEmpty(String input){
        if (input == null || input.isEmpty())
            return true;
        else
            return false;
    }
    
    public static boolean isNumeric(String num){
            if (num == null || num.isEmpty())
                return false;
            
            try{
                int value = Integer.parseInt(num);
            }
            catch(NumberFormatException nfe){
                return false;
            }
            
            return true;
    }
    
    public static boolean isValidEmail(String email){
        if (email == null || email.isEmpty())
            return false;
        
        EmailAddress address = new EmailAddress(email);
        
        return address.isValid();
        
    }
    
    public static boolean isValidCommaSeparatedEmails(String csEmails){
        if (csEmails == null || csEmails.isEmpty())
            return false;
        
        String[] emails = csEmails.split(",");
        
        for (String email : emails)
            if (isValidEmail(email) != true)
                return false;
        
        return true;
    }
}
