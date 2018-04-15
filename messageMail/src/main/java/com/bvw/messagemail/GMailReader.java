package com.bvw.messagemail;

import java.util.Calendar;
import java.util.Properties;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.search.AndTerm;
import javax.mail.search.ComparisonTerm;
import javax.mail.search.FlagTerm;
import javax.mail.search.ReceivedDateTerm;
import javax.mail.search.SearchTerm;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;

import android.util.Log;

public class GMailReader extends javax.mail.Authenticator { 
    private static final String TAG = "GMailReader";
    private String mailhost = "imap.gmail.com";  
    private Session session;
    private IMAPStore store;

    public GMailReader(String user, String password) {

        Properties props = System.getProperties();
        props.setProperty("mail.store.protocol", "imaps");            
        try {
        session = Session.getInstance(props, null);
        store = (IMAPStore) session.getStore("imaps");
        store.connect(mailhost, user, password);
        // Log.i(TAG, "Store: "+store.toString());
    	} 	catch (NoSuchProviderException e) { e.printStackTrace(); } 
    		catch (MessagingException e) { e.printStackTrace(); }
    }

public Message[] readMail() throws Exception { 
    try { 
        IMAPFolder folder = (IMAPFolder) this.store.getFolder("Inbox"); 
        // must open readwrite so emails flagged as SEEN
        folder.open(Folder.READ_WRITE);
        // Message[] msgs = folder.getMessages();
        // sample filter to get all of last week's mail
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -7);
        SearchTerm newer = new ReceivedDateTerm(ComparisonTerm.GT,cal.getTime());
        // Message msgs[] = folder.search(newer);
        // search for all "unseen" messages
        FlagTerm unseenFlagTerm = new FlagTerm(new Flags(Flags.Flag.SEEN), false);
        SearchTerm combined = new AndTerm(newer, unseenFlagTerm);
        // combine search = last weeks unseen
        Message msgs[] = folder.search(combined);
        return msgs; 
    }  catch (Exception e) { Log.e(TAG, e.getMessage(), e);  return null;} 
    finally {}
}
}