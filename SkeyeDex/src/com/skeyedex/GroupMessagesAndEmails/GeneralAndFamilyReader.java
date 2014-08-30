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

public class GeneralAndFamilyReader {

	private static final String LOG_TAG = GeneralAndFamilyReader.class.getSimpleName();


	Context context;
   
	
	public GeneralAndFamilyReader(Context context) 
	{
		this.context = context;
	}
	
	 public ArrayList get_General_messages(StringBuffer strBuff)  throws  Exception
	 {
		 
			 ArrayList<EmailModel> arrGeneralEmails = null;
			 ArrayList<SMSmodel>  arrGeneralSms = null;
			 ArrayList arrAllMessages =  new ArrayList();
			 
			 int count = 0;
			 int indexArrGeneralEmails = 0;
			 int indexArrGeneralSMS = 0;
		
		     SMSreader mSMSreader = new SMSreader(context);
		     arrGeneralSms = mSMSreader.getTxtMessages(7, 0, 3); // only gets Third day sms
			    
		     GeneralAndFamilyList mGeneralAndFamilyList = new GeneralAndFamilyList(context);
		     arrGeneralEmails = mGeneralAndFamilyList.getGeneralAndFamilyMails();
		     
		     count = arrGeneralEmails.size();
	         Log.i(LOG_TAG , " The mail and sms fetched for the today sizes are Email Size " + arrGeneralEmails.size()+"  and sms size "+ arrGeneralSms.size());
	         SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
         
	         while (indexArrGeneralEmails < arrGeneralEmails.size())
			 {
	        	 
				    	Calendar emailTime = Calendar.getInstance();
				    	emailTime.clear();
				    	Date dateObj = (Date)timeFormat.parse(arrGeneralEmails.get(indexArrGeneralEmails).getEmail_Time());
				    	emailTime.setTime(dateObj);
				    	emailTime.set(0, 0, 0);
				    	
				    	if(indexArrGeneralSMS < arrGeneralSms.size())
				    	{
				    		Calendar smsDateTime = Calendar.getInstance();
					    	smsDateTime.clear();
				    		smsDateTime.setTimeInMillis(arrGeneralSms.get(indexArrGeneralSMS).getDate());
					    	
				    		
				    	 	Calendar smsTime = Calendar.getInstance();
					    	smsTime.clear();
					    	smsTime.set(0, 0, 0,smsDateTime.get(Calendar.HOUR_OF_DAY), smsDateTime.get(Calendar.MINUTE), smsDateTime.get(Calendar.SECOND));
					   	    //Log.v(LOG_TAG, "Sms Time "+smsTime.getTime()+" Email Time "+emailTime.getTime());
					    /*	Log.v(LOG_TAG, " The Email  and Sms time are " +  emailTime.get(Calendar.HOUR_OF_DAY) + ":" + emailTime.get(Calendar.MINUTE) + ":" + emailTime.get(Calendar.SECOND)
					    									+  " " + smsTime.get(Calendar.HOUR_OF_DAY) + ":" + smsTime.get(Calendar.MINUTE) + ":" + smsTime.get(Calendar.SECOND) + "  milli " + arrGeneralSms.get(indexArrGeneralSMS).getDate());
					    	*/
					    	if(emailTime.after(smsTime))
					    	{
					    			//Log.v(LOG_TAG, " The email time is greater then sms time");
					    			arrAllMessages.add(arrGeneralEmails.get(indexArrGeneralEmails));
					    			indexArrGeneralEmails++;
					    			
					    	}else {
					    		
					    			//Log.v(LOG_TAG, " The email time is lesser  then sms time");;
					    			arrAllMessages.add(arrGeneralSms.get(indexArrGeneralSMS));
					    			indexArrGeneralSMS++;
					    		
					    	}
				    		
				    		
				    	}else{ // if sms are not available just add the email to allArrayList
				    		
				    			arrAllMessages.add(arrGeneralEmails.get(indexArrGeneralEmails));
				    			indexArrGeneralEmails++;
				    		
				    	}
	
			    	
			  }
		    
		    //Log.v(LOG_TAG," The index Sms and size " + indexArrGeneralSMS + "  "+ arrGeneralSms.size());
		    while(indexArrGeneralSMS < arrGeneralSms.size())
		    {
		    	arrAllMessages.add(arrGeneralSms.get(indexArrGeneralSMS));
		    	indexArrGeneralSMS ++;
		    }
		    
		    strBuff.append(count);
		return arrAllMessages;
	 }
}
