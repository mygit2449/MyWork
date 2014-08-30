package com.skeyedex.GroupMessagesAndEmails;

import java.text.ParseException;
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

public class ThirdDayMessagesReader 
{

	private static final String LOG_TAG = ThirdDayMessagesReader.class.getSimpleName();


	Context context;
   
	
	public ThirdDayMessagesReader(Context context) 
	{
		this.context = context;
	}
	

	
	 public ArrayList get_Third_messages()  throws  Exception
	 {
		 
		 ArrayList<EmailModel> arr3DayEmails = null;
		 ArrayList<SMSmodel>  arr3Day_Sms = null;
		 ArrayList arrAllMessages =  new ArrayList();
		 
		 int indexArr3DayEmails = 0;
		 int indexArr3DaySMS = 0;
		 
		 String seperator = "--";
		 arrAllMessages.add(seperator);

		 Calendar calender = Calendar.getInstance();
		 Date threeDay_date = calender.getTime();
		 SimpleDateFormat checkDateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		 checkDateFormatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
		 String st_calender = checkDateFormatter.format(threeDay_date);
		 
		 Date Thdate = null;
		  try
		  {
			  Thdate = checkDateFormatter.parse(st_calender);
		  }catch(Exception ex){
			  ex.printStackTrace();
		  }
		 
		 Calendar thirdDayCalendar = Calendar.getInstance();
		 thirdDayCalendar.clear();
		 thirdDayCalendar.setTime(Thdate);
		 
		 Calendar thirdDay = Calendar.getInstance();
		 thirdDay.clear();
		 thirdDay.set(thirdDayCalendar.get(Calendar.YEAR), thirdDayCalendar.get(Calendar.MONTH), thirdDayCalendar.get(Calendar.DAY_OF_MONTH));
		 thirdDay.add(Calendar.DAY_OF_MONTH, -2);
		 
	     
	     SMSreader mSMSreader = new SMSreader(context);
	     arr3Day_Sms = mSMSreader.getTxtMessages(4, 0,-1); // only gets Third day sms
		    
	     ThirdDayEmailsList mThirdDayEmailsList = new ThirdDayEmailsList(context, thirdDay);
	     arr3DayEmails = mThirdDayEmailsList.get_3Day_Emails();
	     
         Log.i(LOG_TAG , " The mail and sms fetched for the today sizes are Email Size " + arr3DayEmails.size()+"  and sms size "+ arr3Day_Sms.size());
         SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
         
         while (indexArr3DayEmails < arr3DayEmails.size())
		   {
        	 
		    	Calendar emailTime = Calendar.getInstance();
		    	emailTime.clear();
		    	Date dateObj = (Date)timeFormat.parse(arr3DayEmails.get(indexArr3DayEmails).getEmail_Time());
		    	emailTime.setTime(dateObj);
		    	emailTime.set(0, 0, 0);
		    	
		    	if(indexArr3DaySMS < arr3Day_Sms.size())
		    	{
		    		Calendar smsDateTime = Calendar.getInstance();
			    	smsDateTime.clear();
		    		smsDateTime.setTimeInMillis(arr3Day_Sms.get(indexArr3DaySMS).getDate());
			    	
		    		
		    	 	Calendar smsTime = Calendar.getInstance();
			    	smsTime.clear();
			    	smsTime.set(0, 0, 0,smsDateTime.get(Calendar.HOUR_OF_DAY), smsDateTime.get(Calendar.MINUTE), smsDateTime.get(Calendar.SECOND));
			   	   // Log.v(LOG_TAG, "Sms Time "+smsTime.getTime()+" Email Time "+emailTime.getTime());
			    	/* Log.v(LOG_TAG, " The Email  and Sms time are " +  emailTime.get(Calendar.HOUR_OF_DAY) + ":" + emailTime.get(Calendar.MINUTE) + ":" + emailTime.get(Calendar.SECOND)
			    									+  " " + smsTime.get(Calendar.HOUR_OF_DAY) + ":" + smsTime.get(Calendar.MINUTE) + ":" + smsTime.get(Calendar.SECOND) + "  milli " + arr3Day_Sms.get(indexArr3DaySMS).getDate());
			    	*/
			    	if(emailTime.after(smsTime))
			    	{
			    			//Log.v(LOG_TAG, " The email time is greater then sms time");
			    			arr3DayEmails.get(indexArr3DayEmails).setImage_resourceId(R.drawable.threed_icon);
			    			arrAllMessages.add(arr3DayEmails.get(indexArr3DayEmails));
			    			indexArr3DayEmails++;
			    			
			    	}else {
			    		
			    			//Log.v(LOG_TAG, " The email time is lesser  then sms time");;
			    			arrAllMessages.add(arr3Day_Sms.get(indexArr3DaySMS));
			    			indexArr3DaySMS++;
			    		
			    	}
		    		
		    		
		    	}else{ // if sms are not available just add the email to allArrayList
		    		
		    			arr3DayEmails.get(indexArr3DayEmails).setImage_resourceId(R.drawable.threed_icon);
		    			arrAllMessages.add(arr3DayEmails.get(indexArr3DayEmails));
		    			indexArr3DayEmails++;
		    		
		    	}

		    	
		  }
		    
		    //Log.v(LOG_TAG," The index Sms and size " + indexArr3DaySMS + "  "+ arr3Day_Sms.size());
		    while(indexArr3DaySMS < arr3Day_Sms.size())
		    {
		    	arrAllMessages.add(arr3Day_Sms.get(indexArr3DaySMS));
		    	indexArr3DaySMS ++;
		    }
		
			return arrAllMessages;
	 }
}
