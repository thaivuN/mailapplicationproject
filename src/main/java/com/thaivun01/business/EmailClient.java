package com.thaivun01.business;

import com.thaivun01.beans.ConfigurationBean;
import com.thaivun01.beans.BoostedEmail;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.mail.Flags;
import jodd.mail.Email;
import jodd.mail.EmailAttachment;
import jodd.mail.EmailFilter;
import jodd.mail.EmailMessage;
import jodd.mail.ImapSslServer;
import jodd.mail.MailAddress;
import jodd.mail.ReceiveMailSession;
import jodd.mail.ReceivedEmail;
import jodd.mail.SendMailSession;
import jodd.mail.SmtpServer;
import jodd.mail.SmtpSslServer;

/**
 *
 * Class that sends and receives Emails
 * 
 * @author Thai-Vu Nguyen
 * @version 9/20/2016
 */
public class EmailClient implements EmailClientFace, Serializable {

    private ConfigurationBean configBean;
    private static final long serialVersionUID = 420517692095142206L;

    /**
     * Constructor
     * @param configBean 
     */
    public EmailClient(ConfigurationBean configBean) {
        this.configBean = configBean;
    }
    
/**
     * Send a BoostedEmail object
     * 
     * @param tos String[]
     * @param subject String
     * @param messages String[]
     * @param htmlMessages String[]
     * @param ccs String[]
     * @param bccs String[]
     * @param filenames String[]
     * @param embedFiles String[]
     * @return The BoostedEmail sent 
     */
    @Override
    public BoostedEmail sendMail(String[] tos, String subject,
            String[] messages, String[] htmlMessages, String[] ccs,
            String[] bccs, String[] filenames, String[] embedFiles) {

        //Create the email object
        BoostedEmail mail
                = createEmail(tos, subject, messages, htmlMessages, ccs, bccs, filenames, embedFiles);

        //Make a copy of the attachmenets
        //Because sending the email will cause the embedded message to disapear from the Email objects
        List<EmailAttachment> atts = mail.getAttachments();
        ArrayList<EmailAttachment> newAtts = new ArrayList<>();
        if (atts != null)
        {
           for (int i=0 ; i < atts.size(); i++)
           {
               newAtts.add(atts.get(i));
           }
        }
        
        //Sending email to the SMTP Session
        sendEmailToSMTPSession(mail);
        
        //Putting back the copy of attachments into the BoostedEmail
        mail.setAttachments(newAtts);
        
        
        return mail;
    }

    /**
     * Receives an array of BoostedEmail objects
     * 
     * @return An array of BoostedEmail received
     */
    @Override
    public BoostedEmail[] receiveEmails() {
        
        //Login to the IMAP server with credentials
        ImapSslServer imapServer = new ImapSslServer(this.configBean.getImapServerName(),
                this.configBean.getSenderEmail(), this.configBean.getSenderPass());

        //Setting debug to true
        imapServer.setProperty("mail.debug", "true");

        //Create a session
        ReceiveMailSession session = imapServer.createSession();

        //Open the session
        session.open();

        //Receive the array of emails
        ReceivedEmail[] emails = session.receiveEmailAndMarkSeen(EmailFilter
                .filter().flag(Flags.Flag.SEEN, false));

        //Convert into array of BoostedEmail
        BoostedEmail[] receivedBoostedEmail
                = convertToBoostedEmailArray(emails);
        //Close the session
        session.close();

        return receivedBoostedEmail;
    }


    /**
     * Converting an array of ReceivedEmail into array of BoostedEmail
     * 
     * @param email ReceivedEmail[]
     * @return BoostedEmail[]
     */
    private BoostedEmail[] convertToBoostedEmailArray(ReceivedEmail[] email) {
        if (email == null || email.length == 0) {
            return new BoostedEmail[0];
        }

        BoostedEmail[] bmails = new BoostedEmail[email.length];

        for (int i = 0; i < bmails.length; i++) {
            bmails[i] = convertToBoostedEmail(email[i]);
        }

        return bmails;
    }

    /**
     * Converting a ReceivedEmail object into a BoostedEmail objects
     * @param email
     * @return 
     */
    private BoostedEmail convertToBoostedEmail(ReceivedEmail email) {
        BoostedEmail theMail = new BoostedEmail();

        //Getting the properties unique to ReceivedEmail
        Flags flags = email.getFlags();
        List<ReceivedEmail> attachedMessages = email.getAttachedMessages();
        Date rcvDate = email.getReceiveDate();
        int messNumber = email.getMessageNumber();

        //Getting the regular Email properties
        MailAddress from = email.getFrom();
        MailAddress[] tos = email.getTo();
        String sub = email.getSubject();
        List<EmailMessage> messages = email.getAllMessages();
        List<EmailAttachment> attachments = email.getAttachments();
        MailAddress[] ccs = email.getCc();
        MailAddress[] bccs = email.getBcc();
        Date sent = email.getSentDate();

        //Converting List<EmailAttachment> into ArrayList<EmailAttachment>
        //Since setAttachments method takes in ArrayList<EmailAttachment> as a parameter
        ArrayList<EmailAttachment> newAttach = new ArrayList<>();
        if (attachments != null) {

            for (EmailAttachment ea : attachments) {
                newAttach.add(ea);
            }
        }

        //Copying properties of ReceivedEmail into BoostedEmail
        theMail.setTo(tos);
        theMail.setFrom(from);
        theMail.setMessages(messages);
        theMail.setAttachments(newAttach);
        theMail.setBcc(bccs);
        theMail.setCc(ccs);
        theMail.setSentDate(sent);
        theMail.setSubject(sub);
        theMail.setAttachedMessages(attachedMessages);
        theMail.setFlags(flags);
        theMail.setReceivedDate(rcvDate);
        theMail.setMessageNumber(messNumber);
        theMail.setFolder("inbox");

        return theMail;
    }

    /**
     * Creates a BoostedEmail object
     *
     * @param tos String[]
     * @param subject String
     * @param messages String[]
     * @param htmlMessages String[]
     * @param ccs String[]
     * @param bccs String[]
     * @param filenames String[]
     * @param embedFiles String[]
     * @return
     */
    private BoostedEmail createEmail(String[] tos, String subject,
            String[] messages, String[] htmlMessages, String[] ccs,
            String[] bccs, String[] filenames, String[] embedFiles) {
 
        //Validation are checked in the private methods that adds to the BoostedEmail Properties
        
        //Initiate BoostedEmail
        BoostedEmail mail = createBasicEmail(tos);

        //Add Subject
        mail = addSubject(mail, subject);

        //Add messages
        mail = addMessages(mail, messages, htmlMessages);

        //Add CCs
        mail = addCC(mail, ccs);
        //Add BCCs
        mail = addBCC(mail, bccs);
        //Add Attachments
        mail = addAttachments(mail, filenames);
        //Add Embedded files
        mail = attachEmbedFiles(mail, embedFiles);

        return mail;
    }

    /**
     * Sending a BoostedEmail object to a SMTP session
     *
     * @param mailObj Email
     */
    private void sendEmailToSMTPSession(BoostedEmail mailObj) {

        //Login to stmp server with the credentials
        SmtpServer<SmtpSslServer> smtpServer = SmtpSslServer
                .create(configBean.getServerSMTP())
                .authenticateWith(configBean.getSenderEmail(),
                        configBean.getSenderPass());

        //debug to true
        smtpServer.debug(true);

        //create a sending session
        SendMailSession session = smtpServer.createSession();

        mailObj.setFolder("Inbox");

        //Open the session and send the mail
        session.open();
        session.sendMail(mailObj);
        session.close();
    }

    /**
     * Instantiate the BoostedEmail object and add the sender and receiver
     *
     * @param receivers
     * @return
     */
    private BoostedEmail createBasicEmail(String[] receivers) {
        //Validation Block
        //Take out all null and empty strings in the array
        //Check the array
        String[] filteredReceivers = filterOutEmptyStrings(receivers);
        if (filteredReceivers.length == 0) {
            throw new IllegalArgumentException("INPUT ERROR - Receiver email is not valid");
        }

        BoostedEmail mail = new BoostedEmail();

        //Add basic sender-destination to Email
        return (BoostedEmail) mail.from(configBean.getSenderEmail()).to(filteredReceivers);
    }

    /**
     * Add to the Subject property of the email
     *
     * @param mail BoostedEmail
     * @param subject String
     * @return BoostedEmail
     */
    private BoostedEmail addSubject(BoostedEmail mail, String subject) {
        if (mail == null) {
            throw new IllegalArgumentException("INPUT ERROR - BoostedEmail points to NULL");
        }
        if (subject == null || subject.isEmpty()) {
            throw new IllegalArgumentException("INPUT ERROR - Subject field is empty");
        }

        //Add Subject to BoostedEmail
        mail.setSubject(subject);
        return mail;
    }

    /**
     * Add messages to the email
     * @param mail BoostedEmail
     * @param textMessages String[]
     * @param htmlMessages String[]
     * @return BoostedEmail
     */
    private BoostedEmail addMessages(BoostedEmail mail, String[] textMessages,
            String[] htmlMessages) {
        String[] txtFiltered = filterOutEmptyStrings(textMessages);
        String[] htmlFiltered = filterOutEmptyStrings(htmlMessages);
        if (mail == null) {
            throw new IllegalArgumentException("INPUT ERROR - BoostedEmail points to NULL");
        }

        if (txtFiltered.length == 0 && htmlFiltered.length == 0) {
            throw new IllegalArgumentException("INPUT ERROR - Both Text and HTML fields are empty");
        }

        for (String txt : txtFiltered) {
            mail = addTextMessage(mail, txt);
        }

        for (String html : htmlFiltered) {
            mail = addHTML(mail, html);
        }

        return mail;
    }

    /**
     * Add a text message to a BoostedEmail
     *
     * @param mail BoostedEmail
     * @param subject String
     * @param message String
     * @return BoostedEmail
     */
    private BoostedEmail addTextMessage(BoostedEmail mail, String message) {
        //Validation Block
        if (mail == null) {
            throw new IllegalArgumentException("INPUT ERROR - BoostedEmail points to NULL");
        }

        if (message == null || message.isEmpty()) {
            throw new IllegalArgumentException("INPUT ERROR - Message field is empty");
        }

        //Add text Message to BoostedEmail
        return (BoostedEmail) mail.addText(message);
    }

    /**
     * Add an HTML message to a BoostedEmail
     *
     * @param mail BoostedEmail
     * @param htmlMessage String
     * @return BoostedEmail
     */
    private BoostedEmail addHTML(BoostedEmail mail, String htmlMessage) {
        //Validation Block
        if (mail == null) {
            throw new IllegalArgumentException("INPUT ERROR - BoostedEmail points to NULL");
        }

        if (htmlMessage == null || htmlMessage.isEmpty()) {
            throw new IllegalArgumentException("INPUT ERROR - Message field is empty");
        }

        //Add  HTML Message to BoostedEmail
        return (BoostedEmail) mail.addHtml(htmlMessage);
    }

     /**
     * Add an array of email Strings to the BCC of a BoostedEmail
     *
     * @param mail BoostedEmail
     * @param bccs String []
     * @return BoostedEmail
     */
    private BoostedEmail addBCC(BoostedEmail mail, String[] bccs) {
        String[] filteredBccs = filterOutEmptyStrings(bccs);

        if (mail == null) {
            throw new IllegalArgumentException("INPUT ERROR - BoostedEmail points to NULL");
        }

        //if there's no non-empty values in the array, return the original BoostedEmail 
        if (filteredBccs.length == 0) {
            return mail;
        }

        //Add emails to the Bcc
        return (BoostedEmail) mail.bcc(filteredBccs);
    }

    /**
     * Add an array of email Strings to the CC of a BoostedEmail
     *
     * @param mail BoostedEmail
     * @param ccs String []
     * @return BoostedEmail
     */
    private BoostedEmail addCC(BoostedEmail mail, String[] ccs) {
        String[] filteredCcs = filterOutEmptyStrings(ccs);
       //Validation
        if (mail == null) {
            throw new IllegalArgumentException("INPUT ERROR - BoostedEmail points to NULL");
        }

        if (filteredCcs.length == 0) {
            return mail;
        }

        return (BoostedEmail) mail.cc(filteredCcs);
    }

    /**
     * Add an array of file attachments to an email
     *
     * @param mail BoostedEmail;
     * @param filenames String[]
     * @return BoostedEmail
     */
    private BoostedEmail addAttachments(BoostedEmail mail, String[] filenames) {
        String[] filteredFiles = filterOutEmptyStrings(filenames);
        //Validation
        if (mail == null) {
            throw new IllegalArgumentException("INPUT ERROR - BoostedEmail points to NULL");
        }
        if (filteredFiles.length == 0) {
            return mail;
        }

        for (int i = 0; i < filteredFiles.length; i++) {
            mail = addAttachment(mail, filteredFiles[i]);
        }

        return mail;

    }

    /**
     * Add an attachment file to an email
     *
     * @param mail BoostedEmail
     * @param attachment String
     * @return BoostedEmail
     */
    private BoostedEmail addAttachment(BoostedEmail mail, String attachment) {
        //Validation
        if (mail == null) {
            throw new IllegalArgumentException("INPUT ERROR - BoostedEmail points to NULL");
        }
        if (attachment == null || attachment.isEmpty()) {
            throw new IllegalArgumentException("INPUT ERROR - Filename is empty");
        }

        return (BoostedEmail) mail.attach(EmailAttachment.attachment().file(attachment));
    }

    /**
     * Add an array of embedded files to an email
     *
     * @param mail BoostedEmail
     * @param embeds String[]
     * @return BoostedEmail
     */
    private BoostedEmail attachEmbedFiles(BoostedEmail mail, String[] embeds) {
        
        //Will make sure the array will at least won't be null
        String[] filteredFiles = filterOutEmptyStrings(embeds);
        //Validation
        if (mail == null) {
            throw new IllegalArgumentException("INPUT ERROR - BoostedEmail points to NULL");
        }
        if (filteredFiles.length == 0) {
            return mail;
        }
        for (int i = 0; i < filteredFiles.length; i++) {
            mail = attachEmbedFile(mail, filteredFiles[i]);
        }

        return mail;

    }

    /**
     * Add an embedded file to an email
     *
     * @param mail BoostedEmail
     * @param embed String
     * @return BoostedEmail
     */
    private BoostedEmail attachEmbedFile(BoostedEmail mail, String embed) {
        
        //Validation
        if (mail == null) {
            throw new IllegalArgumentException("INPUT ERROR - BoostedEmail points to NULL");
        }
        if (embed == null || embed.isEmpty()) {
            throw new IllegalArgumentException("INPUT ERROR - Embedded Filename is empty");
        }

        return (BoostedEmail) mail.embed(EmailAttachment.attachment().bytes(new File(embed)));
    }

    /**
     * Remove NULL and empty string entries of a String array
     *
     * @param arr String []
     * @return String []
     */
    private String[] filterOutEmptyStrings(String[] arr) {
        
        //Check if null or empty
        if (arr == null || arr.length == 0) {
            return new String[0];
        }

        //Cut out null or empty elements of the array
        List<String> list = new ArrayList();
        for (String element : arr) {
            if (element != null && element.isEmpty() == false) {
                list.add(element);
            }
        }

        String[] newArr = new String[list.size()];

        for (int i = 0; i < newArr.length; i++) {
            newArr[i] = list.get(i);
        }

        return newArr;

    }

}
