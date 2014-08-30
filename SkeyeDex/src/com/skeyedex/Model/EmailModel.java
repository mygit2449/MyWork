package com.skeyedex.Model;

import java.io.Serializable;

public class EmailModel implements Serializable{

	
		private String email_Sender="";
		private int image_resourceId=0;
		private int background_color;
		private int events_status = 0;
		private int media_status = 0;
		private int business_status = 0;
		private int general_status = 0;
		private String subject="";
	    private String email_Date = "";
	    private String email_Time = "";
	    private int Status ;
	    private String date_time = "";
	    private String type = "";
	    private String email_id = "";
	    private String messageBody = "";
	    private String attachmentName = "";
	    private String timezone = "";
	    private String attachment_name = "";
	    public String getAttachment_name() {
			return attachment_name;
		}

		public void setAttachment_name(String attachment_name) {
			this.attachment_name = attachment_name;
		}

		public String getTimezone() {
			return timezone;
		}

		public void setTimezone(String timezone) {
			this.timezone = timezone;
		}

		public String getAttachmentName() {
			return attachmentName;
		}

		public void setAttachmentName(String attachmentName) {
			this.attachmentName = attachmentName;
		}

		public String getMessageBody() {
			return messageBody;
		}

		public void setMessageBody(String messageBody) {
			this.messageBody = messageBody;
		}
		private long fetched_id = 0;
	    
        public long getFetched_id() {
			return fetched_id;
		}

		public void setFetched_id(long fetched_id) {
			this.fetched_id = fetched_id;
		}

		public String getEmail_id() {
			return email_id;
		}

		public void setEmail_id(String email_id) {
			this.email_id = email_id;
		}
		private int serial_no = 0;
        private boolean selected = false ;
        
      

		public boolean isSelected() {
			return selected;
		}

		public void setSelected(boolean selected) {
			this.selected = selected;
		}

		public int getSerial_no() {
			return serial_no;
		}

		public void setSerial_no(int serial_no) {
			this.serial_no = serial_no;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getDate_time() {
			return date_time;
		}

		public void setDate_time(String date_time) {
			this.date_time = date_time;
		}

		public int getStatus() {
			return Status;
		}

		public void setStatus(int status) {
			Status = status;
		}

		public String getEmail_Date() {
			return email_Date;
		}

		public void setEmail_Date(String email_Date) {
			this.email_Date = email_Date;
		}

		public String getEmail_Time() {
			return email_Time;
		}

		public void setEmail_Time(String email_Time) {
			this.email_Time = email_Time;
		}
		public String seperator="";
	    private long 	timeinmills;
		private int message_no;
		int status;


		public int getEvents_status() {
			return events_status;
		}

		public void setEvents_status(int events_status) {
			this.events_status = events_status;
		}

		public int getMedia_status() {
			return media_status;
		}

		public void setMedia_status(int media_status) {
			this.media_status = media_status;
		}

		public int getBusiness_status() {
			return business_status;
		}

		public void setBusiness_status(int business_status) {
			this.business_status = business_status;
		}

		public int getGeneral_status() {
			return general_status;
		}

		public void setGeneral_status(int general_status) {
			this.general_status = general_status;
		}


	

		public int getMessage_no() {
			return message_no;
		}

		public void setMessage_no(int message_no) {
			this.message_no = message_no;
		}

		public int getBackground_color() {
			return background_color;
		}

		public void setBackground_color(int background_color) {
			this.background_color = background_color;
		}

	 
		public long getTimeinmills() {
			return timeinmills;
		}

		public void setTimeinmills(long timeinmills) {
			this.timeinmills = timeinmills;
		}

		public int getImage_resourceId() {
			return image_resourceId;
		}

		public void setImage_resourceId(int image_resourceId) {
			this.image_resourceId = image_resourceId;
		}

		public String getEmail_Sender() {
			return email_Sender;
		}

		public void setEmail_Sender(String email_Sender) {
			this.email_Sender = email_Sender;
		}

		public String getSubject() {
			return subject;
		}
		
		public void setSubject(String subject) {
			this.subject = subject;
		}
	
		public void setDate(long timeinmills)
		{
			this.timeinmills=timeinmills;
		}
		public long getDate()
		{
			return timeinmills;
		}
		public void setFrom(String string) {
			// TODO Auto-generated method stub
			
		}
}
