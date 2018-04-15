package com.bvw.messagemail;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
// import android.util.Log;

public class GMailWriter {
	// private static final String TAG = "GMailWriter";
    // private String host = "smtp.gmail.com";
    public String user;
    public String password;
    public String from;
    public String subject;
    public String recipients;
    public Object content;
    // public Transport t;
    // MimeMessage message;
    public Session mailSession;
    
	public GMailWriter( String user, String password) {
	// smtp parameters   
		Properties props = new Properties();
	//	props.put("mail.debug", "true");
		props.put("mail.smtps.auth", "true");
	//	props.put("mail.smtp.starttls.enable", "true");
	//	props.put("mail.smtp.host", "smtp.gmail.com");
	//	props.put("mail.smtp.port", "587");
	    mailSession = Session.getInstance(props, null);
	    mailSession.setDebug(true);
 }
	
	public void  write() {
    try{
    MimeMessage message = new MimeMessage(mailSession);
    message.setSubject(this.subject);
    message.setFrom(new InternetAddress(this.user));
    // sample: message.setContent("<h1>Hello world</h1>", "text/html");
    message.setContent(this.content, "text/html");
    message.addRecipient(Message.RecipientType.TO,new InternetAddress(this.recipients));
    // Send message
    Transport t = this.mailSession.getTransport("smtps");
    try {
	t.connect("smtp.gmail.com", this.user,this.password);
	t.sendMessage(message, message.getAllRecipients());
    } catch(MessagingException e)  { e.printStackTrace(); }
    finally {
	t.close();
    }
     }
    catch(NoSuchProviderException e)  { e.printStackTrace(); }
    catch(AddressException e)  { e.printStackTrace(); }
    catch(Exception e)  { e.printStackTrace(); }
	}
}

