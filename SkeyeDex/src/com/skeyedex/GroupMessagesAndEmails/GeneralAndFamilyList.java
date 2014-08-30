package com.skeyedex.GroupMessagesAndEmails;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.skeyedex.R;
import com.skeyedex.EmailDataBase.ContactsDataBase;
import com.skeyedex.EmailDataBase.EmailDataBaseHelper;
import com.skeyedex.Model.ContactsModel;
import com.skeyedex.Model.EmailModel;

public class GeneralAndFamilyList {

private static final String LOG_TAG = GeneralAndFamilyList.class.getSimpleName();
	
	EmailDataBaseHelper mEmailDataBase;
	SQLiteDatabase mDatabase;
	ArrayList<EmailModel> mGeneral_mails = null;
    Context context;	
    
    public GeneralAndFamilyList(Context context)
	{
		this.context = context;
	}
    
    public ArrayList<EmailModel> getGeneralAndFamilyMails() throws Exception
	{
    	    int serialNumber = 500;
    	    
    	    ContactsDataBase mContactsDataBase =null;
			mContactsDataBase = new ContactsDataBase(context);
            ArrayList<ContactsModel> phone_Contacts = new ArrayList<ContactsModel>();
 	    	phone_Contacts = mContactsDataBase.getContacts_From_DataBase(); // getting contacts from database
 	    	int generalAndFamily;
 	    	
	        mEmailDataBase =  EmailDataBaseHelper.getDBAdapterInstance(context);
	        mGeneral_mails = new ArrayList<EmailModel>();
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
			mDatabase = mEmailDataBase.getReadableDatabase();
			
			Calendar calendar=Calendar.getInstance();
		    Date dby_date = calendar.getTime();
		    SimpleDateFormat checkDateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		    checkDateFormatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
			String st_calender = checkDateFormatter.format(dby_date);
			
			Date business_date = null;
			try{
				business_date = checkDateFormatter.parse(st_calender);
			}catch(Exception ex) {}
		    
			Calendar businessCalender = Calendar.getInstance();
			businessCalender.clear();
			businessCalender.setTime(business_date);	
			
	        Calendar toDay = Calendar.getInstance();
	        toDay.clear();
	        Calendar yestarDay = Calendar.getInstance();
	        yestarDay.clear();
	        Calendar threeDays = Calendar.getInstance();
	        threeDays.clear();
	         
	        toDay.set(businessCalender.get(Calendar.YEAR), businessCalender.get(Calendar.MONTH), businessCalender.get(Calendar.DAY_OF_MONTH));
	        
	        yestarDay.set(businessCalender.get(Calendar.YEAR), businessCalender.get(Calendar.MONTH), businessCalender.get(Calendar.DAY_OF_MONTH));
	        yestarDay.add(Calendar.DAY_OF_MONTH, -1);

	        threeDays.set(businessCalender.get(Calendar.YEAR), businessCalender.get(Calendar.MONTH), businessCalender.get(Calendar.DAY_OF_MONTH));
	        threeDays.add(Calendar.DAY_OF_MONTH,-2);
	        
	        Cursor cursor  = null;
	        
	        String st_toDay = (toDay.get(Calendar.YEAR)) + "-" + ((toDay.get(Calendar.MONTH)+1>9) ?  (toDay.get(Calendar.MONTH)+1) : "0" + (toDay.get(Calendar.MONTH)+1) )  + "-" + ((toDay.get(Calendar.DAY_OF_MONTH)>9) ?  (toDay.get(Calendar.DAY_OF_MONTH)) : "0" + (toDay.get(Calendar.DAY_OF_MONTH)));
 	        String st_3yDay = (threeDays.get(Calendar.YEAR)) + "-" + ((threeDays.get(Calendar.MONTH)+1>9) ?  (threeDays.get(Calendar.MONTH)+1) : "0" + (threeDays.get(Calendar.MONTH)+1) )  + "-" + ((threeDays.get(Calendar.DAY_OF_MONTH)>9) ?  (threeDays.get(Calendar.DAY_OF_MONTH)) : "0" + (threeDays.get(Calendar.DAY_OF_MONTH)));
	        
	 	    ArrayList<EmailModel> arrT=new ArrayList<EmailModel>(); 
	 	    ArrayList<EmailModel> arrY=new ArrayList<EmailModel>(); 
			ArrayList<EmailModel> arr3Y=new ArrayList<EmailModel>(); 
	        
 	      try
			{
				
				cursor =	mDatabase.rawQuery("select * from ReceivedMails Where EmailDate >='" +st_3yDay + "' AND EmailDate <= '" + st_toDay + "' AND GnF_Category =0", null);
		  		//Log.i("Query","select * from ReceivedMails Where EmailDate >='" +st_3yDay + "' AND EmailDate <= '" + st_toDay + "' AND GnF_Category =0"); 
		  		
		  		 if (cursor.moveToFirst()) 
		         {
		  			
		  			 	do 
		  			 	{             
		  			 		  
	                    	   EmailModel mCurrentEmail = new EmailModel();
	                    	   
	                    	   String email_from = cursor.getString(cursor.getColumnIndex("EmailFrom"));
	                    	   
	                    	   //Log.v("size "," "+phone_Contacts.size());

	                    	   generalAndFamily = 0 ;
							   for (int ifamilyCtr = 0; ifamilyCtr < phone_Contacts.size(); ifamilyCtr++) 
							   {
								   //Log.v("contact address"," "+phone_Contacts.get(ifamilyCtr).getEmail_id());
								 if (email_from.equalsIgnoreCase(phone_Contacts.get(ifamilyCtr).getEmail_id())) 
								 {
									//Log.v("General Reader", "Email From"+phone_Contacts.get(ifamilyCtr).getEmail_id());
									generalAndFamily = 1;
									break;
								 }
									
							   }
	                    	 
	                    	   mCurrentEmail.setSubject(cursor.getString(cursor.getColumnIndex("Subject")));
	                    	   mCurrentEmail.setEmail_Sender(cursor.getString(cursor.getColumnIndex("EmailFrom")));
	                    	   mCurrentEmail.setDate(cursor.getLong(cursor.getColumnIndex("EmailDate")));
	                    	   mCurrentEmail.setEmail_Date(cursor.getString(cursor.getColumnIndex("EmailDate")));
	                    	   mCurrentEmail.setEmail_Time(cursor.getString(cursor.getColumnIndex("EmailTime")));
	                    	   mCurrentEmail.setStatus(cursor.getInt(cursor.getColumnIndex("EmailStatus")));
	                    	  
	                    	   mCurrentEmail.setEmail_id(cursor.getString(cursor.getColumnIndex("Email_Id")));
	                    	   mCurrentEmail.setEvents_status(cursor.getInt(cursor.getColumnIndex("EnF_Category")));
	                    	   mCurrentEmail.setBusiness_status(cursor.getInt(cursor.getColumnIndex("BnD_Category")));
	                    	   mCurrentEmail.setMedia_status(cursor.getInt(cursor.getColumnIndex("MnP_Category")));
	                    	   mCurrentEmail.setGeneral_status(generalAndFamily);
	                    	   mCurrentEmail.setDate_time(cursor.getString(cursor.getColumnIndex("Email_dateTime")));
	                    	   mCurrentEmail.setFetched_id(cursor.getLong(cursor.getColumnIndex("Uid")));
	                    	   mCurrentEmail.setMessageBody(cursor.getString(cursor.getColumnIndex("MessageBody")));
	                    	   mCurrentEmail.setAttachmentName(cursor.getString(cursor.getColumnIndex("Attachment")));
	                    	   mCurrentEmail.seperator = "";
	                    	   Log.i("Emails Date",""+cursor.getString(cursor.getColumnIndex("Email_dateTime")));
	                    	   
	                    	   
	                    	   //mGeneral_mails.add(mCurrentEmail);
	                    	   
	                    	   Calendar emailDateTemp = Calendar.getInstance();
                 	           emailDateTemp.clear();
                 	           Calendar emailDate = Calendar.getInstance();
                 	           emailDate.clear();
                 	            
	                    	   try
	                    	   { 
	                    		 emailDateTemp.setTime(dateFormat.parse( mCurrentEmail.getEmail_Date()));
	                    		 emailDate.set(emailDateTemp.get(Calendar.YEAR), emailDateTemp.get(Calendar.MONTH), emailDateTemp.get(Calendar.DAY_OF_MONTH));
	                    		 
	                    	   	}catch(Exception ex){
	                    		   throw ex;
	                    	   }
	                    	  //Log.i("Emails Date",""+cursor.getString(cursor.getColumnIndex("EmailTime")));
	                    	  
	                    	  if(emailDate.equals(toDay) && generalAndFamily == 1)
                      	   		{
	                    		  //Log.i("Emails Date",""+emailDate);
                      		     arrT.add(mCurrentEmail);
                      		   
                      	   		}else if(emailDate.equals(yestarDay) && generalAndFamily == 1){
                      		    arrY.add(mCurrentEmail);
                      		
                      	   		} 
                      	   		else if(emailDate.equals(threeDays) && generalAndFamily == 1){
                      	   		arr3Y.add(mCurrentEmail);
                      		  
                      	   		}
	                    	   mCurrentEmail=null;
		                        		   
		  			 	}while(cursor.moveToNext());
		         }
			   
			}catch (Exception e) {
				// TODO: handle exception
			}finally {
				
				if(cursor !=null)
					cursor.close();
			}
 	      
 	      if(arrT.size() >0)
	    	 {
	        	   for(int ictr = 0; ictr<arrT.size(); ictr++ )
	        	   {
	        		   EmailModel mEmailModel = new EmailModel();
	        		   mEmailModel = arrT.get(ictr);
	        		   mEmailModel.setImage_resourceId(R.drawable.t_icon);
	        		   mEmailModel.setSerial_no(serialNumber);
                	   serialNumber--;	
	        		   mGeneral_mails.add(mEmailModel);
	        		   mEmailModel = null;
	        	   }
	    	 }
	    	 
	    	 if(arrY.size() >0)
	    	 {
		      	   for(int ictr = 0; ictr<arrY.size(); ictr++ )
		      	   {
		      		 EmailModel mEmailModel = new EmailModel();
		      		 mEmailModel = arrY.get(ictr);
		      		 mEmailModel.setImage_resourceId(R.drawable.y_icon);
		      		 mEmailModel.setSerial_no(serialNumber);
		      		 serialNumber--;	
		      		 mGeneral_mails.add(mEmailModel);
		      		 mEmailModel = null;
		      	   }
		      }
	    	 
	    	 if(arr3Y.size() >0)
	    	 {
	    		 	Log.i("3 days", "" + arr3Y.size());

	        	    for(int ictr = 0; ictr<arr3Y.size(); ictr++ )
	        	    {
	        	    	EmailModel mEmailModel=new EmailModel();
	        	    	mEmailModel = arr3Y.get(ictr);
	        	    	mEmailModel.setImage_resourceId(R.drawable.threed_icon);
	        	    	mEmailModel.setSerial_no(serialNumber);
	                	serialNumber--;	
	        	    	mGeneral_mails.add(mEmailModel);
	         		    mEmailModel = null;
	        	    }
	    	 }
 	         Log.v(LOG_TAG, ""+mGeneral_mails.size());
			return mGeneral_mails;
		
	}
}
