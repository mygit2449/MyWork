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

public class MediaAndPhotosReader {


	private static final String LOG_TAG = MediaAndPhotosReader.class.getSimpleName();


	Context context;
   
	
	public MediaAndPhotosReader(Context context) 
	{
		this.context = context;
	}
	
	 public ArrayList get_Media_messages(StringBuffer strBuff)  throws  Exception
	 {
		 
			 ArrayList<EmailModel> arrMediaEmails = null;
			 ArrayList<SMSmodel>  arrMediaSms = null;
			 ArrayList arrAllMessages =  new ArrayList();
			 
			 int count = 0;
			 int indexArrMediaEmails = 0;
			 int indexArrMediaSMS = 0;
		     
		     SMSreader mSMSreader = new SMSreader(context);
		     arrMediaSms = mSMSreader.getTxtMessages(7, 0, 1); // only gets Third day sms
			    
		     MediaAndPhotosList mMediaAndPhotosList = new MediaAndPhotosList(context);
		     arrMediaEmails = mMediaAndPhotosList.getMediaAndPhotosMails();
		     
		     count = arrMediaEmails.size();
	         Log.i(LOG_TAG , " The mail and sms fetched for the today sizes are Email Size " + arrMediaEmails.size()+"  and sms size "+ arrMediaSms.size());
	         SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
         
	         while (indexArrMediaEmails < arrMediaEmails.size())
			 {
	        	 
				    	Calendar emailTime = Calendar.getInstance();
				    	emailTime.clear();
//				    	Log.v(LOG_TAG, "  date check"+arrMediaEmails.get(indexArrMediaEmails).getEmail_Time());
				    	Date dateObj = (Date)timeFormat.parse(arrMediaEmails.get(indexArrMediaEmails).getEmail_Time());
				    	emailTime.setTime(dateObj);
				    	emailTime.set(0, 0, 0);
				    	
				    	if(indexArrMediaSMS < arrMediaSms.size())
				    	{
				    		Calendar smsDateTime = Calendar.getInstance();
					    	smsDateTime.clear();
				    		smsDateTime.setTimeInMillis(arrMediaSms.get(indexArrMediaSMS).getDate());
					    	
				    		
				    	 	Calendar smsTime = Calendar.getInstance();
					    	smsTime.clear();
					    	smsTime.set(0, 0, 0,smsDateTime.get(Calendar.HOUR_OF_DAY), smsDateTime.get(Calendar.MINUTE), smsDateTime.get(Calendar.SECOND));
					   	    //Log.v(LOG_TAG, "Sms Time "+smsTime.getTime()+" Email Time "+emailTime.getTime());
					    /*	Log.v(LOG_TAG, " The Email  and Sms time are " +  emailTime.get(Calendar.HOUR_OF_DAY) + ":" + emailTime.get(Calendar.MINUTE) + ":" + emailTime.get(Calendar.SECOND)
					    									+  " " + smsTime.get(Calendar.HOUR_OF_DAY) + ":" + smsTime.get(Calendar.MINUTE) + ":" + smsTime.get(Calendar.SECOND) + "  milli " + arrMediaSms.get(indexArrMediaSMS).getDate());
					    	*/
					    	if(emailTime.after(smsTime))
					    	{
					    			//Log.v(LOG_TAG, " The email time is greater then sms time");
					    			arrAllMessages.add(arrMediaEmails.get(indexArrMediaEmails));
					    			indexArrMediaEmails++;
					    			
					    	}else {
					    		
					    			//Log.v(LOG_TAG, " The email time is lesser  then sms time");;
					    			arrAllMessages.add(arrMediaSms.get(indexArrMediaSMS));
					    			indexArrMediaSMS++;
					    		
					    	}
				    		
				    		
				    	}else{ // if sms are not available just add the email to allArrayList
				    		
				    			arrAllMessages.add(arrMediaEmails.get(indexArrMediaEmails));
				    			indexArrMediaEmails++;
				    		
				    	}
	
			    	
			  }
		    
		   // Log.v(LOG_TAG," The index Sms and size " + indexArrMediaSMS + "  "+ arrMediaSms.size());
		    while(indexArrMediaSMS < arrMediaSms.size())
		    {
		    	arrAllMessages.add(arrMediaSms.get(indexArrMediaSMS));
		    	indexArrMediaSMS ++;
		    }
		    strBuff.append(count);
		return arrAllMessages;
	 }
}
