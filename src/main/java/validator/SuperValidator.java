/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

    private static boolean isEmpty(String input) {
        if (input == null || input.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean isNumeric(String num) {

        try {
            int value = Integer.parseInt(num);
        } catch (NumberFormatException nfe) {
            return false;
        }

        return true;
    }

    public static boolean isValidEmail(String email) {

        EmailAddress address = new EmailAddress(email);

        return address.isValid();

    }

    public static boolean isValidCommaSeparatedEmails(String csEmails) {
        if (csEmails == null || csEmails.isEmpty()) {
            return false;
        }

        String[] emails = csEmails.split(",");

        for (String email : emails) {
            if (isValidEmail(email) != true) {
                return false;
            }
        }

        return true;
    }

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
}
