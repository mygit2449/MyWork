package com.skeyedex.Model;

import java.sql.Date;

import android.graphics.drawable.Drawable;

public class SMSmodel 
{
	private long 	messageID;
	private long 	threadID;
	private String 	sender;
	private String 	body;
	private int serial_No;
	private int image_resourceId;
	
	private String smsDate;
    private String type = "";
	
    private boolean selected = false ;
    
    

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSmsDate() {
		return smsDate;
	}
	public void setSmsDate(String smsDate) {
		this.smsDate = smsDate;
	}
	public int getSerial_No() {
		return serial_No;
	}
	public int getImage_resourceId() {
		return image_resourceId;
	}
	public void setImage_resourceId(int image_resourceId) {
		this.image_resourceId = image_resourceId;
	}
	public void setSerial_No(int serial_No) {
		this.serial_No = serial_No;
	}
	private long 	timeinmills;
	private int 	status;  //read or unread
	
	public void setmessageID(long messageID)
	{
		this.messageID=messageID;
	}
	public long getmessageID()
	{
		return messageID;
	}
	public void setthreadID(long threadID)
	{
		this.threadID=threadID;
	}
	public long getthreadID()
	{
		return threadID;
	}
	public void setSender(String sender)
	{
		this.sender=sender;
	}
	public String getSender()
	{
		return sender;
	}
	
	public void setBody(String body)
	{
		this.body=body;
	}
	public String getBody()
	{
		return body;
	}
	
	public void setDate(long timeinmills)
	{
		this.timeinmills=timeinmills;
	}
	public long getDate()
	{
		return timeinmills;
	}
	
	public void setStatus(int status)
	{
		this.status=status;
	}
	public int getStatus()
	{
		return status;
	}
}
