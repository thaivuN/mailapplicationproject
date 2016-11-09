package com.thaivun01.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.mail.Flags;
import jodd.mail.Email;
import jodd.mail.EmailAttachment;
import jodd.mail.EmailMessage;
import jodd.mail.MailAddress;
import jodd.mail.ReceivedEmail;

/**
 * Class extending Jodd's Email class. The class contains important properties
 * for an Email.
 *
 * @author Thai-Vu Nguyen
 * @version 9/20/2016
 */
public class BoostedEmail extends Email implements Serializable {

    private static final long serialVersionUID = 420517692095142206L;
    private int id;
    private String folder;
    private Flags flags;
    private List<ReceivedEmail> attachedMessages;
    private Date receivedDate;
    private int messageNumber;

    /**
     * Constructor
     */
    public BoostedEmail() {
        super();
        this.attachments = new ArrayList<>();
        id = -1;

    }

    /**
     * Getting the primary key of the email
     * @return 
     */
    public int getId() {
        return id;
    }

    /**
     * Setting the primary key of the email
     * @param id 
     */
    public void setId(int id) {
        this.id = id;
    }
    
    

    /**
     * Sets the folder of the email
     *
     * @param folder String
     */
    public void setFolder(String folder) {
        this.folder = folder;
    }

    /**
     * Gets the folder name of the email
     *
     * @return String
     */
    public String getFolder() {
        return folder;
    }

    /**
     * Sets the ArrayList of attachments
     *
     * @param attachments
     */
    public void setAttachments(ArrayList<EmailAttachment> attachments) {
        this.attachments = attachments;
    }

    /**
     * Sets the list of messages
     *
     * @param messages
     */
    public void setMessages(List<EmailMessage> messages) {
        this.messages = messages;
    }

    /**
     * Sets the Flags
     *
     * @param flags Flags
     */
    public void setFlags(Flags flags) {
        this.flags = flags;
    }

    /**
     * Gets the Flags
     *
     * @return Flags
     */
    public Flags getFlags() {
        return flags;
    }

    /**
     * Gets a list of Attached Messages
     *
     * @param attachedMessages List<ReceivedEmail>
     */
    public void setAttachedMessages(List<ReceivedEmail> attachedMessages) {
        this.attachedMessages = attachedMessages;
    }

    /**
     * Gets the list of Attached Mssages
     *
     * @return a list of Attached Messages
     */
    public List<ReceivedEmail> getAttachedMessages() {
        return attachedMessages;
    }

    /**
     * Sets the received Date of the email
     *
     * @param receivedDate Date
     */
    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }

    /**
     * Gets the received date of the email
     *
     * @return Received Date
     */
    public Date getReceivedDate() {
        return receivedDate;
    }

    /**
     * Sets the message ID of the email
     *
     * @param messageNumber int
     */
    public void setMessageNumber(int messageNumber) {
        this.messageNumber = messageNumber;
    }

    /**
     * gets the message id of the email
     *
     * @return Message ID
     */
    public int getMessageNumber() {
        return messageNumber;
    }

    /**
     * Equals method that checks if two BoostedEmail are equal or not
     *
     * @param obj Object
     * @return True or Falser
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final BoostedEmail anEmail = (BoostedEmail) obj;

        if (!(this.getFrom().getEmail().equals(anEmail.getFrom().getEmail()))) {
            return false;
        }

        if (!(this.getSubject().equals(anEmail.getSubject()))) {
            return false;
        }

        if (compareEmailMessagesArrays(this.getAllMessages(), anEmail.getAllMessages()) == false) {
            return false;
        }

        if (compareMailAddresses(this.getTo(), anEmail.getTo()) == false) {
            return false;
        }

        if (compareAttachmentsArrays(this.attachments, anEmail.attachments) == false) {
            return false;
        }

        return true;
    }

    /**
     * Returns the hash code of the BoostedEmail object
     *
     * @return Hash Code integer
     */
    @Override
    public int hashCode() {
        int hash = 3;
        String[] addr = new String[to.length];
        for (int i = 0; i < addr.length; i++) {
            addr[i] = to[i].getEmail();
        }

        String[] encoding = new String[messages.size()];
        String[] content = new String[messages.size()];
        String[] mime = new String[messages.size()];
        for (int j = 0; j < content.length; j++) {
            content[j] = messages.get(j).getContent();
            encoding[j] = messages.get(j).getEncoding();
            mime[j] = messages.get(j).getMimeType();
        }

        String[] attachNames = new String[(attachments != null) ? attachments.size() : 0];

        for (int k = 0; k < attachNames.length; k++) {
            attachNames[k] = attachments.get(k).getName();

        }

        hash = 69 * hash + Objects.hash(this.subject) + Objects.hash(this.getFrom().getEmail())
                + Arrays.hashCode(addr)
                + Arrays.hashCode(mime)
                + Arrays.hashCode(encoding)
                + Arrays.hashCode(content)
                + Arrays.hashCode(attachNames);

        return hash;
    }

    /**
     * Checks if two lists of EmailMessage are equal
     *
     * @param list1 List<EmailMessage>
     * @param list2 List<EmailMessage>
     * @return boolean value
     */
    private boolean compareEmailMessagesArrays(List<EmailMessage> list1, List<EmailMessage> list2) {

        if (list1 == null || list2 == null) {
            return (list1 == null && list2 == null);
        }

        if (list1 == list2) {
            return true;
        }

        if (list1.size() != list2.size()) {
            return false;
        }

        for (int i = 0; i < list1.size(); i++) {
            if (compareEmailMessages(list1.get(i), list2.get(i)) == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if two EmailMessage objects are equal
     *
     * @param email1 EmailMessage
     * @param email2 EmailMessage
     * @return boolean value
     */
    private boolean compareEmailMessages(EmailMessage email1, EmailMessage email2) {
        if (email1 == null || email2 == null) {
            return (email1 == null && email2 == null);
        }

        if (!email1.getMimeType().trim().toLowerCase().equals(email2.getMimeType().trim().toLowerCase())) {
            return false;
        }
        if (!email1.getEncoding().trim().toLowerCase().equals(email2.getEncoding().trim().toLowerCase())) {
            return false;
        }
        String content1 = email1.getContent().trim();
        String content2 = email2.getContent().trim();

        return content1.equals(content2);
    }

    /**
     * Checks if two arrays of MailAddress objects are equal
     *
     * @param arr1 MailAddress[]
     * @param arr2 MailAddress[]
     * @return boolean value
     */
    private boolean compareMailAddresses(MailAddress[] arr1, MailAddress[] arr2) {
        if (arr1 == null || arr2 == null) {
            return (arr1 == null && arr2 == null);
        }
        if (arr1.length != arr2.length) {
            return false;
        }
        
        String[] strArr1 = new String[arr1.length];
        String[] strArr2 = new String[arr2.length];
        for (int i = 0; i< strArr1.length;i++)
        {
            strArr1[i] = arr1[i].getEmail();
            strArr2[i] = arr2[i].getEmail();
        }
        
        Arrays.sort(strArr1);
        Arrays.sort(strArr2);

        for (int i = 0; i < strArr1.length; i++) {
            if (strArr1[i].trim().equals(strArr2[i].trim()) == false) {
                return false;
            }
        }
        return true;

    }

    /**
     * Checks if two ArrayList of EmailAttachment objects are equal
     *
     * @param att1 ArrayList<EmailAttachment>
     * @param att2 ArrayList<EmailAttachment>
     * @return boolean value
     */
    private boolean compareAttachmentsArrays(ArrayList<EmailAttachment> att1, ArrayList<EmailAttachment> att2) {
        if (att1 == null || att2 == null) {
            return (att1 == null && att2 == null);
        }

        if (att1.size() != att2.size()) {
            return false;
        }

        att1 = sort(att1);
        att2 = sort(att2);

        for (int i = 0; i < att1.size(); i++) {
            if (compareAttachments(att1.get(i), att2.get(i)) == false) {
                return false;
            }
        }

        return true;
    }

    /**
     * Checks if two EmailAttachment objects are equal
     *
     * @param ea1 EmailAttachment
     * @param ea2 EmailAttachment
     * @return boolean value
     */
    private boolean compareAttachments(EmailAttachment ea1, EmailAttachment ea2) {
        if (ea1 == null || ea2 == null) {
            return (ea1 == null && ea2 == null);
        }

        return ea1.getName().trim().equals(ea2.getName().trim());

    }

    /**
     * Sorts an ArrayList of EmailAttachment
     *
     * @param att ArrayList<EmailAttachment>
     * @return An ArrayList of EmailAttachment objects
     */
    private ArrayList<EmailAttachment> sort(ArrayList<EmailAttachment> att) {
        if (att == null || att.isEmpty()) {
            return new ArrayList<>();
        }

        for (int i = 0; i < att.size() - 1; i++) {
            int smallest = i;
            for (int j = i + 1; j < att.size(); j++) {
                if (att.get(j).getName().compareTo(att.get(smallest).getName()) < 0) {
                    smallest = j;

                    EmailAttachment temp = att.get(smallest);
                    att.set(smallest, att.get(i));
                    att.set(i, temp);
                }
            }

        }
        return att;
    }

}
