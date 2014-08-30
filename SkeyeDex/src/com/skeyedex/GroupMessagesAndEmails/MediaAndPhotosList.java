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
import com.skeyedex.EmailDataBase.EmailDataBaseHelper;
import com.skeyedex.Model.EmailModel;

public class MediaAndPhotosList {

	
private static final String LOG_TAG = MediaAndPhotosList.class.getSimpleName();
	
	EmailDataBaseHelper mEmailDataBase;
	SQLiteDatabase mDatabase;
	ArrayList<EmailModel> mMedia_mails = null;
    Context context;	
    
    public MediaAndPhotosList(Context context)
	{
		this.context = context;
	}
    
		    public ArrayList<EmailModel> getMediaAndPhotosMails() throws Exception
			{
    	            int serialNumber = 299;
		            int mediaStatus = 1;
		            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
			        mEmailDataBase =  EmailDataBaseHelper.getDBAdapterInstance(context);
			        mMedia_mails = new ArrayList<EmailModel>();
					mDatabase = mEmailDataBase.getReadableDatabase();
					
					Calendar calendar=Calendar.getInstance();
				    Date dby_date = calendar.getTime();
				    SimpleDateFormat checkDateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				    checkDateFormatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
					String st_calender = checkDateFormatter.format(dby_date);
					
					Date media_date = null;
					try{
						media_date = checkDateFormatter.parse(st_calender);
					}catch(Exception ex) {}
				    
					Calendar mediaCalender = Calendar.getInstance();
					mediaCalender.clear();
					mediaCalender.setTime(media_date);
			        
			        Calendar toDay = Calendar.getInstance();
			        toDay.clear();
			        Calendar yestarDay = Calendar.getInstance();
			        yestarDay.clear();
			        Calendar threeDays = Calendar.getInstance();
			        threeDays.clear();
			         
			        toDay.set(mediaCalender.get(Calendar.YEAR), mediaCalender.get(Calendar.MONTH), mediaCalender.get(Calendar.DAY_OF_MONTH));
			        
			        yestarDay.set(mediaCalender.get(Calendar.YEAR), mediaCalender.get(Calendar.MONTH), mediaCalender.get(Calendar.DAY_OF_MONTH));
			        yestarDay.add(Calendar.DAY_OF_MONTH, -1);
		
			        threeDays.set(mediaCalender.get(Calendar.YEAR), mediaCalender.get(Calendar.MONTH), mediaCalender.get(Calendar.DAY_OF_MONTH));
			        threeDays.add(Calendar.DAY_OF_MONTH,-2);
			        
			        Cursor cursor  = null;
			        
			        String st_toDay = (toDay.get(Calendar.YEAR)) + "-" + ((toDay.get(Calendar.MONTH)+1>9) ?  (toDay.get(Calendar.MONTH)+1) : "0" + (toDay.get(Calendar.MONTH)+1) )  + "-" + ((toDay.get(Calendar.DAY_OF_MONTH)>9) ?  (toDay.get(Calendar.DAY_OF_MONTH)) : "0" + (toDay.get(Calendar.DAY_OF_MONTH)));
		 	        String st_3yDay = (threeDays.get(Calendar.YEAR)) + "-" + ((threeDays.get(Calendar.MONTH)+1>9) ?  (threeDays.get(Calendar.MONTH)+1) : "0" + (threeDays.get(Calendar.MONTH)+1) )  + "-" + ((threeDays.get(Calendar.DAY_OF_MONTH)>9) ?  (threeDays.get(Calendar.DAY_OF_MONTH)) : "0" + (threeDays.get(Calendar.DAY_OF_MONTH)));
			        
		 	        ArrayList<EmailModel> arrT=new ArrayList<EmailModel>(); 
		 	        ArrayList<EmailModel> arrY=new ArrayList<EmailModel>(); 
				    ArrayList<EmailModel> arr3Y=new ArrayList<EmailModel>(); 
			        
		 	      try
					{
						
						cursor =	mDatabase.rawQuery("select * from ReceivedMails Where EmailDate >='" +st_3yDay + "' AND EmailDate <= '" + st_toDay + "' AND MnP_Category ="+mediaStatus, null);
				  		Log.i("Query","select * from ReceivedMails Where EmailDate >='" +st_3yDay + "' AND EmailDate <= '" + st_toDay + "' AND MnP_Category ="+mediaStatus); 
				  		
				  		 if (cursor.moveToFirst()) 
				         {
				  			
				  			 	do 
				  			 	{             
				  			 		   EmailModel mCurrentEmail = new EmailModel();
				  			 		   
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
			                    	   mCurrentEmail.setMedia_status(cursor.getInt(cursor.getColumnIndex("GnF_Category")));
			                    	   mCurrentEmail.setDate_time(cursor.getString(cursor.getColumnIndex("Email_dateTime")));
			                    	   mCurrentEmail.setFetched_id(cursor.getLong(cursor.getColumnIndex("Uid")));
			                    	   mCurrentEmail.setMessageBody(cursor.getString(cursor.getColumnIndex("MessageBody")));
			                    	   mCurrentEmail.setAttachmentName(cursor.getString(cursor.getColumnIndex("Attachment")));
			                    	   mCurrentEmail.seperator = "";
			                    	   Log.i("Emails Date",""+cursor.getString(cursor.getColumnIndex("Email_dateTime")));
			                    	   
			                    	   
			                    	   //mMedia_mails.add(mCurrentEmail);
			                    	   
			                    	  	
		                  	            Calendar emailDateTemp = Calendar.getInstance();
		                  	            emailDateTemp.clear();
		                  	            Calendar emailDate = Calendar.getInstance();
		                  	            emailDate.clear();
		                  	            
		                  	            
		                  	          try{ 
			                    		   
				                    		 
			                        	   
				                    		 emailDateTemp.setTime(dateFormat.parse( mCurrentEmail.getEmail_Date()));
				                    		 emailDate.set(emailDateTemp.get(Calendar.YEAR), emailDateTemp.get(Calendar.MONTH), emailDateTemp.get(Calendar.DAY_OF_MONTH));
				                    		 
				                    	   	}catch(Exception ex){
				                    		   throw ex;
				                    	   }
				                    	  Log.i("Emails Date",""+emailDate);
				                    	  if(emailDate.equals(toDay))
			                        	   {
				                    		  Log.i("Emails Date",""+emailDate);
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
		        		   mMedia_mails.add(mEmailModel);
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
			      		 mMedia_mails.add(mEmailModel);
			      		 mEmailModel = null;
			      		
			      	   }
			      }
		    	 
		    	 if(arr3Y.size() >0)
		    	 {
		        	    for(int ictr = 0; ictr<arr3Y.size(); ictr++ )
		        	    {
		        	    	
		        	    	EmailModel mEmailModel = new EmailModel();
		        	    	mEmailModel = arr3Y.get(ictr);
		        	    	mEmailModel.setImage_resourceId(R.drawable.threed_icon);
		        	    	mEmailModel.setSerial_no(serialNumber);
		                    serialNumber--;
		        	    	mMedia_mails.add(mEmailModel);
		         		    mEmailModel = null;
		         		    
		        	    }
		    	 }
		 	         Log.v(LOG_TAG, ""+mMedia_mails.size());
					return mMedia_mails;
				
			}
}
