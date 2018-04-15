package com.bvw.messagemail;
import javax.mail.Message;
import javax.mail.MessagingException;
import com.bvw.android_library.Message_Listview.MessageAdapter.MessageDetails;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
// import android.os.Message;
import android.os.Messenger;
import android.telephony.SmsManager;
import android.util.Log;

public class MailService extends IntentService {
// mail receiving service, started from main activity via intent
// don't confuse the two message types
// one is the javamail email message,
// the other is for	status messages passed back for display	
  // private String User2 = "bruce.woolmore@gmail.com";
  // private String Password2 = "cervantes";
  private String User = "donquixote2u@gmail.com";
  private String Password = "Datsun260z";
  public int result2;
  String debugg;
  Message[] messages;
  Messenger messenger;
  msgObject msgobject;

  public MailService() {
    super("MailService");
  }

  @Override
  protected void onHandleIntent(Intent intent) {
	  // first setup messaging
	   Bundle extras = intent.getExtras();
	    if (extras != null) {
	      Messenger messenger = (Messenger) extras.get("MESSENGER");
      
		GMailReader reader = new GMailReader(User, Password);
		GMailWriter sendit = new GMailWriter(User, Password);
		//try {
		//	Log.d("Session", sendit.mailSession.getTransport().getURLName().toString());
		//} catch (NoSuchProviderException e) {Log.e("Writer:init", e.getMessage(), e);	}
	    try {
	      messages = reader.readMail();
	      Log.d("messages:", Integer.toString(messages.length));
	   	  MessageDetails displ = new MessageDetails();
		  if(messages.length < 1) // no emails?
		    {
		  	displ.setSubj("(no messages)");
		  	displayMessage(messenger,displ);
		    }
          for (int i = 0; i < messages.length; i++) {
	    	if(i>10) {break;}
	    	Message message = messages[i];
	    	// create display message object to pass back to main activity
       		displ.setFrom(message.getFrom()[0].toString());
			displ.setSubj(message.getSubject());
	    	debugg="From: " + displ.getFrom() + ":" + displ.getSubj();
	    	Log.d("messagelist", debugg);
	    	// pass email back to main activity for display
	    	// displayMessage(messenger,message);
	    	displayMessage(messenger,displ);
	    	String msgText=null;
	    	msgText = new EmailParse().getText(message);
	    	if ( msgText != null )
	    	  { 
	    		String smsnum = null; // change to get recipient phone #
	    		String smsmsg = null; // change to get message
	    		// SmsManager.getDefault().sendTextMessage( smsnum, null, smsmsg, null,null );
	    	  }
	    	sendit.user = User;
	    	sendit.password = Password;
	    	sendit.from = User;
	    	sendit.recipients = message.getFrom()[0].toString();
	    	sendit.subject = debugg;
	    	sendit.content = message.getContent();
	    	sendit.write();
            }
          } catch (Exception e) {Log.e("readMail", e.getMessage(), e);}
    }
  }
  	// protected void displayMessage(Messenger messenger, Message message) throws MessagingException { 
    protected void displayMessage(Messenger messenger, MessageDetails displ) throws MessagingException { 
   	// send a message object back to UI containing data to display
    android.os.Message msg = android.os.Message.obtain();
  	// load mail message into system message for passing
    msg.obj = displ; 
    try {
        messenger.send(msg);
    } catch (android.os.RemoteException e1) {
        Log.w(getClass().getName(), "Exception sending message", e1);
    }
	  
  }
  
public static class msgObject { 
//sets up structure for a message object to pass back to main
	// String msgcontent;
	MessageDetails msgcontent = new MessageDetails();
	// public void setmsg(String txt)
	public void setmsg(Message message) throws MessagingException
								{
								// msgcontent = txt;
								msgcontent.setFrom(message.getFrom()[0].toString());
								msgcontent.setSubj(message.getSubject());
								}
	// public String getmsg()		{
	public MessageDetails getmsg()	{
									return msgcontent;
									}
} 

} 
