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

public class BussinessAndDocumentsReader {

	private static final String LOG_TAG = BussinessAndDocumentsReader.class.getSimpleName();


	Context context;
   
	
	public BussinessAndDocumentsReader(Context context) 
	{
		this.context = context;
	}
	
	 public ArrayList get_Business_messages(StringBuffer strBuff)  throws  Exception
	 {
		 
			 ArrayList<EmailModel> arrBusinessEmails = null;
			 ArrayList<SMSmodel>  arrBusinessSms = null;
			 ArrayList arrAllMessages =  new ArrayList();
			 
			 int count = 0;
			 int indexArrBusinessEmails = 0;
			 int indexArrBusinessSMS = 0;
		
		     SMSreader mSMSreader = new SMSreader(context);
		     arrBusinessSms = mSMSreader.getTxtMessages(7, 0, 2); 
			    
		     BusinessAndDocumentsList mBusinessAndDocumentsList = new BusinessAndDocumentsList(context);
		     arrBusinessEmails = mBusinessAndDocumentsList.getBusinessAndDocuments();
		     
		     count = arrBusinessEmails.size();
	         Log.i(LOG_TAG , " The mail and sms fetched for the today sizes are Email Size " + arrBusinessEmails.size()+"  and sms size "+ arrBusinessSms.size());
	         SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
         
	         while (indexArrBusinessEmails < arrBusinessEmails.size())
			  {
	        	 
				    	Calendar emailTime = Calendar.getInstance();
				    	emailTime.clear();
				    	Date dateObj = (Date)timeFormat.parse(arrBusinessEmails.get(indexArrBusinessEmails).getEmail_Time());
				    	emailTime.setTime(dateObj);
				    	emailTime.set(0, 0, 0);
				    	
				    	if(indexArrBusinessSMS < arrBusinessSms.size())
				    	{
				    		Calendar smsDateTime = Calendar.getInstance();
					    	smsDateTime.clear();
				    		smsDateTime.setTimeInMillis(arrBusinessSms.get(indexArrBusinessSMS).getDate());
					    	
				    		
				    	 	Calendar smsTime = Calendar.getInstance();
					    	smsTime.clear();
					    	smsTime.set(0, 0, 0,smsDateTime.get(Calendar.HOUR_OF_DAY), smsDateTime.get(Calendar.MINUTE), smsDateTime.get(Calendar.SECOND));
					   	    //Log.v(LOG_TAG, "Sms Time "+smsTime.getTime()+" Email Time "+emailTime.getTime());
					    	/* Log.v(LOG_TAG, " The Email  and Sms time are " +  emailTime.get(Calendar.HOUR_OF_DAY) + ":" + emailTime.get(Calendar.MINUTE) + ":" + emailTime.get(Calendar.SECOND)
					    									+  " " + smsTime.get(Calendar.HOUR_OF_DAY) + ":" + smsTime.get(Calendar.MINUTE) + ":" + smsTime.get(Calendar.SECOND) + "  milli " + arrBusinessSms.get(indexArrBusinessSMS).getDate());
					    	*/
					    	if(emailTime.after(smsTime))
					    	{
					    			//Log.v(LOG_TAG, " The email time is greater then sms time");
					    			arrAllMessages.add(arrBusinessEmails.get(indexArrBusinessEmails));
					    			indexArrBusinessEmails++;
					    			
					    	}else {
					    		
					    			//Log.v(LOG_TAG, " The email time is lesser  then sms time");;
					    			arrAllMessages.add(arrBusinessSms.get(indexArrBusinessSMS));
					    			indexArrBusinessSMS++;
					    		
					    	}
				    		
				    		
				    	}else{ // if sms are not available just add the email to allArrayList
				    		
				    			arrAllMessages.add(arrBusinessEmails.get(indexArrBusinessEmails));
				    			indexArrBusinessEmails++;
				    		
				    	}
	
			    	
			  }
		    
		    //Log.v(LOG_TAG," The index Sms and size " + indexArrBusinessSMS + "  "+ arrBusinessSms.size());
		    while(indexArrBusinessSMS < arrBusinessSms.size())
		    {
		    	arrAllMessages.add(arrBusinessSms.get(indexArrBusinessSMS));
		    	indexArrBusinessSMS ++;
		    }
		    
		    strBuff.append(count);
		return arrAllMessages;
	 }
}
