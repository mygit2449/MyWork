package com.skeyedex.GroupMessagesAndEmails;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.skeyedex.EmailDataBase.ContactsDataBase;
import com.skeyedex.EmailDataBase.EmailDataBaseHelper;
import com.skeyedex.Model.ContactsModel;
import com.skeyedex.Model.EmailModel;

public class ThirdDayEmailsList 
{
	
		 private static final String LOG_TAG = ThirdDayEmailsList.class.getSimpleName();
		 
		EmailDataBaseHelper mEmailDataBase;
		SQLiteDatabase mDatabase;
		ArrayList<EmailModel> m3Day_emails = null;
		Context context;
		Calendar currentDate;

		public ThirdDayEmailsList(Context context,  Calendar _currentDate) 
		{
			this.context = context;
			this.currentDate = _currentDate;
		}

		public ArrayList<EmailModel> get_3Day_Emails() 
		{
			
				mEmailDataBase = EmailDataBaseHelper.getDBAdapterInstance(context);
				mDatabase = mEmailDataBase.getReadableDatabase();
			
				ContactsDataBase mContactsDataBase =null;
				mContactsDataBase = new ContactsDataBase(context);
	            ArrayList<ContactsModel> phone_Contacts = new ArrayList<ContactsModel>();
	 	    	phone_Contacts = mContactsDataBase.getContacts_From_DataBase(); // getting contacts from database
	 	    	int generalAndFamily;
	 	    	
				m3Day_emails = new ArrayList<EmailModel>();
				
				Cursor cursor  = null;
				int month = currentDate.get(Calendar.MONTH);
				int day = currentDate.get(Calendar.DATE);
			  
				String st_yestarDay = currentDate.get(Calendar.YEAR) + "-" + ((month+1>9) ?  (month+1) : "0" + (month+1))  + "-" + ((day>9) ?  (day) : "0" + (day));
			    Log.v(LOG_TAG, "Fetching the email for the date " + st_yestarDay);  
						
			    try 
			    {
		       	  cursor =	mDatabase.rawQuery("select * from ReceivedMails Where EmailDate ='" +st_yestarDay + "' ORDER BY Email_dateTime DESC", null);
		       	 
		       	 if (cursor.moveToFirst()) 
			         {
				  			
			  			 	do 
			  			 	{              
		                    	   EmailModel mCurrentEmail = new EmailModel();
		                    	   
		                    	   Log.v("size "," "+phone_Contacts.size());

			  			 		   String email_from = cursor.getString(cursor.getColumnIndex("EmailFrom"));
		                    	   generalAndFamily = 0 ;
								   for (int ifamilyCtr = 0; ifamilyCtr < phone_Contacts.size(); ifamilyCtr++) 
								   {
									   Log.v("contact address"," "+phone_Contacts.get(ifamilyCtr).getEmail_id());
									 if (email_from.equalsIgnoreCase(phone_Contacts.get(ifamilyCtr).getEmail_id())) 
									 {
										Log.v("General Reader", "Email From"+phone_Contacts.get(ifamilyCtr).getEmail_id());
										generalAndFamily = 1;
										break;
									 }
										
								   }
								   
		                    	   mCurrentEmail.setSubject(cursor.getString(cursor.getColumnIndex("Subject")));
		                    	   mCurrentEmail.setEmail_Sender(cursor.getString(cursor.getColumnIndex("EmailFrom")));
		                    	  
		                    	   mCurrentEmail.setStatus(cursor.getInt(cursor.getColumnIndex("EmailStatus")));
		                    	   mCurrentEmail.setEvents_status(cursor.getInt(cursor.getColumnIndex("EnF_Category")));
		                    	   mCurrentEmail.setBusiness_status(cursor.getInt(cursor.getColumnIndex("BnD_Category")));
		                    	   mCurrentEmail.setMedia_status(cursor.getInt(cursor.getColumnIndex("MnP_Category")));
		                    	   mCurrentEmail.setGeneral_status(generalAndFamily);
		                    	   
		                    	   mCurrentEmail.setEmail_id(cursor.getString(cursor.getColumnIndex("Email_Id")));
		                    	   mCurrentEmail.setDate_time(cursor.getString(cursor.getColumnIndex("Email_dateTime")));
		                    	   mCurrentEmail.setEmail_Time(cursor.getString(cursor.getColumnIndex("EmailTime")));
		                    	   mCurrentEmail.setFetched_id(cursor.getLong(cursor.getColumnIndex("Uid")));
		                    	   mCurrentEmail.setMessageBody(cursor.getString(cursor.getColumnIndex("MessageBody")));
		                    	   mCurrentEmail.setAttachmentName(cursor.getString(cursor.getColumnIndex("Attachment")));
		                    	   mCurrentEmail.seperator = "";
		                    	   Log.i("Emails Date",""+cursor.getString(cursor.getColumnIndex("Email_dateTime")));
		                    	   m3Day_emails.add(mCurrentEmail);
				                    	  
				                    	  
			  			 	}while(cursor.moveToNext());
			         }
				                    	 
				} catch (Exception e) {
					// TODO: handle exception
			}finally {
				
				if(cursor !=null)
					cursor.close();
			}
	   return m3Day_emails;
	}
		
}
