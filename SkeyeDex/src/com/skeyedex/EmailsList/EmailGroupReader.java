package com.skeyedex.EmailsList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.skeyedex.R;
import com.skeyedex.EmailDataBase.EmailDataBaseHelper;
import com.skeyedex.Model.EmailModel;

public class EmailGroupReader {

	
	EmailDataBaseHelper mEmailDataBase;
	SQLiteDatabase mDatabase;
	ArrayList<EmailModel> mGroup_emails = null;
    Context context;	
    
    public EmailGroupReader(Context context) {
		this.context = context;
	}
    
    public ArrayList<EmailModel>  get_group_emails() 
    {
		
    	    mEmailDataBase = new EmailDataBaseHelper(context);
    	    mGroup_emails = new ArrayList<EmailModel>();
			mDatabase = mEmailDataBase.getWritableDatabase();
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
 	        Date currentDate = new Date();
 	        Date toDay = new Date(currentDate.getYear(),currentDate.getMonth(),currentDate.getDate());
 	        Date yestarDay = new Date(currentDate.getYear(),currentDate.getMonth(),(currentDate.getDate()-1));
 	        Date threeDays = new Date(currentDate.getYear(),currentDate.getMonth(),(currentDate.getDate()-2));
 	        
 	        ArrayList<EmailModel> arrT=new ArrayList<EmailModel>(); 
 	        ArrayList<EmailModel> arrY=new ArrayList<EmailModel>(); 
		    ArrayList<EmailModel> arr3Y=new ArrayList<EmailModel>();
		   
		   Cursor cursor = null;
		   
		   try {
			         cursor = mDatabase.rawQuery("select * from ReceivedMails ORDER BY Email_dateTime ASC", null);
			         Log.i("Query Here",""+mDatabase.rawQuery("select * from ReceivedMails ORDER BY Email_dateTime ASC", null));
			         
			         if (cursor.moveToFirst()) 
			         {
				  			
			  			 	do 
			  			 	{              
	  			 		
		                    	   EmailModel mCurrentEmail = new EmailModel();
		                    	   mCurrentEmail.setSubject(cursor.getString(cursor.getColumnIndex("Subject")));
		                    	   mCurrentEmail.setEmail_Sender(cursor.getString(cursor.getColumnIndex("EmailFrom")));
		                    	   String txtEmailDate = cursor.getString(cursor.getColumnIndex("EmailDate"));
		                    	   mCurrentEmail.setDate(cursor.getLong(cursor.getColumnIndex("EmailDate")));
		                    	   mCurrentEmail.setEmail_Date(cursor.getString(cursor.getColumnIndex("EmailDate")));
		                    	   mCurrentEmail.setEmail_Time(cursor.getString(cursor.getColumnIndex("EmailDate")));
		                    	   mCurrentEmail.setStatus(cursor.getInt(cursor.getColumnIndex("EmailStatus")));
		                    	   mCurrentEmail.setEvents_status(cursor.getInt(cursor.getColumnIndex("EnF_Category")));
		                    	   mCurrentEmail.setEmail_id(cursor.getString(cursor.getColumnIndex("Email_Id")));
		                    	   mCurrentEmail.setBusiness_status(cursor.getInt(cursor.getColumnIndex("BnD_Category")));
		                    	   mCurrentEmail.setMedia_status(cursor.getInt(cursor.getColumnIndex("MnP_Category")));
		                    	   mCurrentEmail.setGeneral_status(cursor.getInt(cursor.getColumnIndex("GnF_Category")));
		                    	   mCurrentEmail.setDate_time(cursor.getString(cursor.getColumnIndex("Email_dateTime")));
		                    	   Log.i("Email Reader"," "+cursor.getString(cursor.getColumnIndex("Email_dateTime")));
		                    	   mCurrentEmail.seperator = "";
		                  
		                    	   Date emailDate = null;
		                    	   
		                    	   try{ 
		                    		   
		                    		   	emailDate = (Date)dateFormat.parse(txtEmailDate);
		                    		   	
		                    	   		}catch(Exception ex){
		                    		   
		                    	   }
		                    	  
		                    	  if(emailDate.equals(toDay))
	                        	   {
		                    		    
	                        		    arrT.add(mCurrentEmail);
	                        		   
	                        	   }else if(emailDate.equals(yestarDay)){
	                        		   arrY.add(mCurrentEmail);
	                        		
	                        	   } 
	                        	   else if(emailDate.equals(threeDays))
	                        	   {
	                        		   arr3Y.add(mCurrentEmail);
	                        		  
	                        	   }
		                    	   mCurrentEmail=null;

		  			 	}while(cursor.moveToNext());
			  			 	
			        }
			         
		} catch (Exception ex) {
			  ex.printStackTrace();
		}finally {
			mDatabase.close();
			if(cursor !=null)
				cursor.close();
		}
		   if(arrT.size() >0)
	    	 {
			   
	    		   Log.i("today", "" + arrT.size());
	    		   EmailModel mEmailModel = new EmailModel();
	    		   mEmailModel.seperator = "--t";
	    		   mGroup_emails.add(mEmailModel);
	        	   mEmailModel = null;

	        	   for(int ictr = 0; ictr<arrT.size(); ictr++ )
	        	   {
	        		   
	        		   mEmailModel = arrT.get(ictr);
	        		   mEmailModel.setImage_resourceId(R.drawable.t_icon);
	        		   mEmailModel.setBackground_color(R.drawable.gl_red_bar);
	        		   mGroup_emails.add(mEmailModel);
	        		   mEmailModel = null;
	        		   
	        	   }
	        	   
	    	 }
	    	 
	    	 if(arrY.size() >0)
	    	 {
		    		 Log.i("yesterday", "" + arrY.size());
			    	 EmailModel mEmailModel = new EmailModel();
			    	 mEmailModel.seperator = "--y";
			    	 mGroup_emails.add(mEmailModel);
			      	 mEmailModel = null;
			      	 
		      	   for(int ictr = 0; ictr<arrY.size(); ictr++ )
		      	   {
		      		 
		      		 mEmailModel = arrY.get(ictr);
		      		 mEmailModel.setImage_resourceId(R.drawable.y_icon);
		      		 mEmailModel.setBackground_color(R.drawable.gl_purple_bar);
		      		 mGroup_emails.add(mEmailModel);
		      		 mEmailModel = null;
		      		
		      	   }
		      	   
		      }
	    	 
	    	 if(arr3Y.size() >0)
	    	 {
	    		 
	    		 	Log.i("3 days", "" + arr3Y.size());
	    		 	EmailModel mEmailModel=new EmailModel();
	    		 	mEmailModel.seperator = "--3y";
	    		 	mGroup_emails.add(mEmailModel);
	        	    mEmailModel = null;
	        	   
	        	    for(int ictr = 0; ictr<arr3Y.size(); ictr++ )
	        	    {
	        	    	
	        	    	mEmailModel = arr3Y.get(ictr);
	        	    	mEmailModel.setImage_resourceId(R.drawable.threed_icon);
	        	    	mEmailModel.setBackground_color(R.drawable.gl_brown_bar);
	        	    	mGroup_emails.add(mEmailModel);
	         		    mEmailModel = null;
	         		    
	        	    }
	        	    
	    	 }
	
		return mGroup_emails;
    }
}
