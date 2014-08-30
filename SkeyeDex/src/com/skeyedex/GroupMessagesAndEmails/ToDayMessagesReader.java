package com.skeyedex.GroupMessagesAndEmails;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.content.Context;
import android.util.Log;

import com.skeyedex.R;
import com.skeyedex.Model.EmailModel;
import com.skeyedex.Model.SMSmodel;
import com.skeyedex.TextMessages.SMSreader;

public class ToDayMessagesReader {
	private static final String LOG_TAG = ToDayMessagesReader.class.getSimpleName();
	
	
	Context context;
	public ToDayMessagesReader(Context context)
	{
	   
		this.context = context;
		
	}
	 
	public ArrayList getTodayMessages() throws Exception 
	 {
		 
			ArrayList<EmailModel> arrTodayEmails = null;
			ArrayList<SMSmodel>  arrToday_Sms = null;
			ArrayList arrAllMessages =  new ArrayList();
			
		    int indexArrTodayEmails = 0;
			int indexArrTodaySMS = 0;
			
			String seperator = "--";
			arrAllMessages.add(seperator);
			
			
			Calendar calendar=Calendar.getInstance();
		    Date dby_date = calendar.getTime();
		    SimpleDateFormat checkDateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		    checkDateFormatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
			String st_calender = checkDateFormatter.format(dby_date);
			
			Date email_date = null;
			try{
				email_date = checkDateFormatter.parse(st_calender);
			}catch(Exception ex) {}
		    
			Calendar todayCalender = Calendar.getInstance();
			todayCalender.clear();
			todayCalender.setTime(email_date);
			
		    Calendar toDay = Calendar.getInstance();
	        toDay.clear();
		    toDay.set(todayCalender.get(Calendar.YEAR), todayCalender.get(Calendar.MONTH), todayCalender.get(Calendar.DAY_OF_MONTH));
	    	Log.v(LOG_TAG, "Current Date " + toDay.getTime());
			
			SMSreader mSMSreader = new SMSreader(context);
			arrToday_Sms = mSMSreader.getTxtMessages(1, 0, -1); // only gets current days sms
		    
			TodayEmailsList todayEmailList = new TodayEmailsList(context, toDay);
			arrTodayEmails =todayEmailList.get_Today_Mails();
		    
		    
            Log.i(LOG_TAG , " The mail and sms fetched for the today sizes are Email Size " + arrTodayEmails.size()+"  and sms size "+ arrToday_Sms.size());
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		    
		    while (indexArrTodayEmails < arrTodayEmails.size())
		    {
		    	Calendar emailTime = Calendar.getInstance();
		    	emailTime.clear();
		    	Date dateObj = (Date)timeFormat.parse(arrTodayEmails.get(indexArrTodayEmails).getEmail_Time());
		    	emailTime.setTime(dateObj);
		    	emailTime.set(0, 0, 0);
		    	
		    	//Log.v(LOG_TAG, " The Email & Sms index and sizes  smsIndex " + indexArrTodaySMS + " array size " +  arrToday_Sms.size() + " email index " + indexArrTodayEmails + " email size " + arrTodayEmails.size());
		    	if(indexArrTodaySMS < arrToday_Sms.size())
		    	{
		    		Calendar smsDateTime = Calendar.getInstance();
			    	smsDateTime.clear();
		    		smsDateTime.setTimeInMillis(arrToday_Sms.get(indexArrTodaySMS).getDate());
			    	
		    		
		    	 	Calendar smsTime = Calendar.getInstance();
			    	smsTime.clear();
			    	smsTime.set(0, 0, 0,smsDateTime.get(Calendar.HOUR_OF_DAY), smsDateTime.get(Calendar.MINUTE), smsDateTime.get(Calendar.SECOND));
			   	    //Log.v(LOG_TAG, "Sms Time "+smsTime.getTime()+" Email Time "+emailTime.getTime());
			    	/* Log.v(LOG_TAG, " The Email  and Sms time are " +  emailTime.get(Calendar.HOUR_OF_DAY) + ":" + emailTime.get(Calendar.MINUTE) + ":" + emailTime.get(Calendar.SECOND)
			    									+  " " + smsTime.get(Calendar.HOUR_OF_DAY) + ":" + smsTime.get(Calendar.MINUTE) + ":" + smsTime.get(Calendar.SECOND) + "  milli " + arrToday_Sms.get(indexArrTodaySMS).getDate());
			    	*/
			    	if(emailTime.after(smsTime))
			    	{
			    		//Log.v(LOG_TAG, " The email time is greater then sms time");
			    		arrTodayEmails.get(indexArrTodayEmails).setImage_resourceId(R.drawable.t_icon);
			    		arrAllMessages.add(arrTodayEmails.get(indexArrTodayEmails));
			    		indexArrTodayEmails++;
			    	}else {
			    		//Log.v(LOG_TAG, " The email time is lesser  then sms time");;
			    		arrAllMessages.add(arrToday_Sms.get(indexArrTodaySMS));
			    		indexArrTodaySMS++;
			    		
			    	}
		    		
		    		
		    	}else{ // if sms are not available just add the email to allArrayList
		    		arrTodayEmails.get(indexArrTodayEmails).setImage_resourceId(R.drawable.t_icon);
		    		arrAllMessages.add(arrTodayEmails.get(indexArrTodayEmails));
		    		indexArrTodayEmails++;
		    	}

		    	
		    }
		    
		    //Log.v(LOG_TAG," The index Sms and size " + indexArrTodaySMS + "  "+ arrToday_Sms.size());
		    while(indexArrTodaySMS < arrToday_Sms.size())
		    {
		    	arrAllMessages.add(arrToday_Sms.get(indexArrTodaySMS));
		    	indexArrTodaySMS ++;
		    }
		
			return arrAllMessages;
	 }
	
}
