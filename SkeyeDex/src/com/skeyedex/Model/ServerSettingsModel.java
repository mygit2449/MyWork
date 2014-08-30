package com.skeyedex.Model;

public class ServerSettingsModel {
	
	String username;
	String password;
	String imap_server;
	String port;
	String security_type;
	String imap_path_prifix;
	String email_id;
	String email_type;
	String contact_name;
	
	
	public String getContact_name() {
		return contact_name;
	}
	public void setContact_name(String contact_name) {
		this.contact_name = contact_name;
	}
	public String getEmail_type() {
		return email_type;
	}
	public void setEmail_type(String email_type) {
		this.email_type = email_type;
	}
	public String getEmail_id() {
		return email_id;
	}
	public void setEmail_id(String email_id) {
		this.email_id = email_id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getImap_server() {
		return imap_server;
	}
	public void setImap_server(String imap_server) {
		this.imap_server = imap_server;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getSecurity_type() {
		return security_type;
	}
	public void setSecurity_type(String security_type) {
		this.security_type = security_type;
	}
	public String getImap_path_prifix() {
		return imap_path_prifix;
	}
	public void setImap_path_prifix(String imap_path_prifix) {
		this.imap_path_prifix = imap_path_prifix;
	}

}
