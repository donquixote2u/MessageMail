package com.bvw.messagemail;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.ContentType;
import javax.mail.internet.ParseException;

public class EmailParse {
	static boolean showStructure = false;
	static boolean showMessage = false;
	static boolean showAlert = false;
	static boolean saveAttachments = false;
	static int attnum = 1;
	static String s = null;
	static String indentStr = "     ";
	static int level = 0;

	public String getText(Message message) throws Exception 	{
		return s = s + dumpPart((Part) message);
	}
	
	public String dumpPart(Part p) throws Exception {
	String ct = p.getContentType();
	try {
	    pr("CONTENT-TYPE: " + (new ContentType(ct)).toString());
	} catch (ParseException pex) {
	    pr("BAD CONTENT-TYPE: " + ct);
	}
	 /*
	 * Using isMimeType to determine the content type avoids
	 * fetching the actual content data until we need it.
	 */
	if (p.isMimeType("text/plain")) {	// plain text content
		pr((String)p.getContent());
	} else if (p.isMimeType("multipart/*")) {	// multipart 
	    Multipart mp = (Multipart)p.getContent();
	    level++;
	    int count = mp.getCount();
	    for (int i = 0; i < count; i++)
		dumpPart(mp.getBodyPart(i));
	    level--;
	} else if (p.isMimeType("message/rfc822")) {	
		// nested mime message, so recurse
	    level++;
	    dumpPart((Part)p.getContent());
	    level--;
	} else {
		/*
		 * If we actually want to see the data, and it's not a
		 * MIME type we know, fetch it and check its Java type.
		 */
		Object o = p.getContent();
		if (o instanceof String) {
		    pr((String)o);
		/* } 
		 	else if (o instanceof InputStream) {
		    pr("This is just an input stream");
		    pr("---------------------------");
		    InputStream is = (InputStream)o;
		    int c;
		    while ((c = is.read()) != -1)
			pr(c);
		*/	
		} else {
		    pr("This is an unknown type");
		    pr("---------------------------");
		    pr(o.toString());
			}
		} return s;
	}  
	
	/*  Print a, possibly indented, string.     */
	public static void pr(String c) {
	// if (showStructure)
	s=(indentStr.substring(0, level * 2)+c);
	}
}
