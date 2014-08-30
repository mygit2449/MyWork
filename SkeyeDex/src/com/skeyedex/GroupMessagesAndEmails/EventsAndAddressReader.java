package com.skeyedex.GroupMessagesAndEmails;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.util.Log;

import com.skeyedex.Model.EmailModel;
import com.skeyedex.Model.SMSmodel;
import com.skeyedex.TextMessages.SMSreader;

public class EventsAndAddressReader {

	
	private static final String LOG_TAG = EventsAndAddressReader.class.getSimpleName();


	Context context;
   
	
	public EventsAndAddressReader(Context context) 
	{
		this.context = context;
	}
	
	 public ArrayList get_Events_messages(StringBuffer strbuf)  throws  Exception
	 {
		 
			 ArrayList<EmailModel> arrEventsEmails = null;
			 ArrayList<SMSmodel>  arrEventsSms = null;
			 ArrayList arrAllMessages =  new ArrayList();
			 int indexArr3DayEmails = 0;
			 int indexArr3DaySMS = 0;
		     int count = 0;
		    	
			 arrEventsEmails = new ArrayList<EmailModel>();
		     SMSreader mSMSreader = new SMSreader(context);
		     arrEventsSms = mSMSreader.getTxtMessages(7, 0, 0); // only gets yellow category sms's and emails
			    
		     EventsAndAddressList mEventsAndAddressList = new EventsAndAddressList(context);
		     arrEventsEmails = mEventsAndAddressList.getEventsAndAddressMails();
		    

	         count = arrEventsEmails.size();
		     //Log.i(LOG_TAG , " The mail and sms fetched for the today sizes are Email Size " + arrEventsEmails.size()+"  and sms size "+ arrEventsSms.size());
	         SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
	          
	         while (indexArr3DayEmails < arrEventsEmails.size())
	         {
	        	 
				    	Calendar emailTime = Calendar.getInstance();
				    	emailTime.clear();
				    	//Log.v(LOG_TAG, "  date check "+arrEventsEmails.get(indexArr3DayEmails).getEmail_Time());
				    	Date dateObj = (Date)timeFormat.parse(arrEventsEmails.get(indexArr3DayEmails).getEmail_Time());
				    	emailTime.setTime(dateObj);
				    	emailTime.set(0, 0, 0);
				    	
				    	if(indexArr3DaySMS < arrEventsSms.size())
				    	{
				    		Calendar smsDateTime = Calendar.getInstance();
					    	smsDateTime.clear();
				    		smsDateTime.setTimeInMillis(arrEventsSms.get(indexArr3DaySMS).getDate());
					    	
				    		
				    	 	Calendar smsTime = Calendar.getInstance();
					    	smsTime.clear();
					    	smsTime.set(0, 0, 0,smsDateTime.get(Calendar.HOUR_OF_DAY), smsDateTime.get(Calendar.MINUTE), smsDateTime.get(Calendar.SECOND));
					   	   // Log.v(LOG_TAG, "Sms Time "+smsTime.getTime()+" Email Time "+emailTime.getTime());
					    	/*Log.v(LOG_TAG, " The Email  and Sms time are " +  emailTime.get(Calendar.HOUR_OF_DAY) + ":" + emailTime.get(Calendar.MINUTE) + ":" + emailTime.get(Calendar.SECOND)
					    									+  " " + smsTime.get(Calendar.HOUR_OF_DAY) + ":" + smsTime.get(Calendar.MINUTE) + ":" + smsTime.get(Calendar.SECOND) + "  milli " + arrEventsSms.get(indexArr3DaySMS).getDate());
					    	*/
					    	if(emailTime.after(smsTime))
					    	{
					    			//Log.v(LOG_TAG, " The email time is greater then sms time");
					    			arrAllMessages.add(arrEventsEmails.get(indexArr3DayEmails));
					    			indexArr3DayEmails++;
					    			
					    	}else {
					    		
					    			//Log.v(LOG_TAG, " The email time is lesser  then sms time");;
					    			arrAllMessages.add(arrEventsSms.get(indexArr3DaySMS));
					    			indexArr3DaySMS++;
					    		
					    	}
				    		
				    		
				    	}else{ // if sms are not available just add the email to allArrayList
				    		
				    			arrAllMessages.add(arrEventsEmails.get(indexArr3DayEmails));
				    			indexArr3DayEmails++;
				    		
				    	}
	
			    	
			  }
		    
		    //Log.v(LOG_TAG," The index Sms and size " + indexArr3DaySMS + "  "+ arrEventsSms.size());
		    while(indexArr3DaySMS < arrEventsSms.size())
		    {
		    	arrAllMessages.add(arrEventsSms.get(indexArr3DaySMS));
		    	indexArr3DaySMS ++;
		    }
		    //Log.v(LOG_TAG, "Events Array Size"+arrAllMessages.size());
		    strbuf.append(count);
		return arrAllMessages;
		
	 }

	
}
