package ai.smarthome.util;

import java.util.Map;
import java.util.Properties; 

import javax.activation.CommandMap; 
import javax.activation.MailcapCommandMap; 
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication; 
import javax.mail.Session; 
import javax.mail.Transport; 
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress; 
import javax.mail.internet.MimeMessage; 

import ai.smarthome.util.exception.PasswordReminderException;


public class PasswordReminderMail extends javax.mail.Authenticator { 
  
	public static boolean sendPasswordReminderMail(final String to, final String username, final String password, final Map<String,String> parametri) throws AddressException, MessagingException, PasswordReminderException { 
	
		if (to == null)
			throw new PasswordReminderException("PasswordReminderExcpetion: variable \"to\" undefined");
		if (username == null)
			throw new PasswordReminderException("PasswordReminderExcpetion: variable \"username\" undefined");
		if (password == null)
			throw new PasswordReminderException("PasswordReminderExcpetion: variable \"password\" undefined");
		if (parametri == null)
			throw new PasswordReminderException("PasswordReminderExcpetion: variable \"parametri\" undefined");
		
		final String EMAIL_FROM = "email_from";
		final String USERNAME = "username";
		final String PASSWORD = "password";
		final String SMTP = "smtp";
		final String PORTA_SMTP = "porta_smtp";
		final String PORTA_SF = "porta_sf";
		
		final String MAIL_INTESTAZIONE = "Recupero credenziali Smart Home Environment - C@SA";
		final String MAIL_BODY = "Username: "+username+"\nPassword: "+password;
		
		String _host = parametri.get(SMTP);
		String _port = parametri.get(PORTA_SMTP);
		String _sport = parametri.get(PORTA_SF);
    
		final String _user = parametri.get(USERNAME); // username 
		final String _pass = parametri.get(PASSWORD); // password
    
		String _from = parametri.get(EMAIL_FROM);; // email del mittente
		String _to = to;
		String _subject = MAIL_INTESTAZIONE; // oggetto della mail
		String _body = MAIL_BODY; // corpo della mail
 
		boolean _debuggable = false; // debug mode (on / off) - default off 
		boolean _auth = true; // smtp authentication - default on 
 
		MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap(); 
		mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html"); 
		mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml"); 
		mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain"); 
		mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed"); 
		mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822"); 
		CommandMap.setDefaultCommandMap(mc); 
		
		Properties props = new Properties(); 
 		props.put("mail.smtp.host", _host); 
 		if(_debuggable)
			props.put("mail.debug", "true"); 
		if(_auth)  
			props.put("mail.smtp.auth", "true"); 
 		props.put("mail.smtp.port", _port); 
		props.put("mail.smtp.socketFactory.port", _sport); 
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); 
		props.put("mail.smtp.socketFactory.fallback", "false"); 
	
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			@Override
	        protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(_user, _pass);
	        }
	    });
	    MimeMessage message = new MimeMessage(session);
	    message.setFrom(new InternetAddress(_from));
	    message.setSubject(_subject);
	    message.setText(_body);                
	    message.setRecipient(Message.RecipientType.TO,  new InternetAddress(_to));                    
	    Transport.send(message);
	    return true;
	} 
   	
} 