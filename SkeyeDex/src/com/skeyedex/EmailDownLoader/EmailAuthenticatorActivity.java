package com.skeyedex.EmailDownLoader;

import java.util.Properties;

import javax.mail.Session;

public class EmailAuthenticatorActivity extends javax.mail.Authenticator {

	String username;
	String password;
	Session session;
	String email_host = "imap.gmail.com";

	public EmailAuthenticatorActivity(String username, String password)
	{
		
		this.username = username;
		this.password = password;
	
		
        Properties props = new Properties();
		props.setProperty("mail.transport.protocol", "imaps");
		props.setProperty("mail.host", email_host);
		props.put("mail.pop.auth", "true");
		props.put("mail.pop.port", "993");
		props.put("mail.pop.socketFactory.port", "993");
		props.put("mail.pop.socketFactory.class",	"javax.net.ssl.SSLSocketFactory");
		props.put("mail.pop.socketFactory.fallback", "false");
		props.setProperty("mail.pop.quitwait", "false");

		session = Session.getDefaultInstance(props, this);
	}

	protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
		return new javax.mail.PasswordAuthentication(username, password);
	}
}
