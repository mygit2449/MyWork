package com.skeyedex.EmailsList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.skeyedex.R;
import com.skeyedex.EmailDataBase.ContactsDataBase;
import com.skeyedex.EmailDataBase.EmailDataBaseHelper;
import com.skeyedex.Model.ContactsModel;
import com.skeyedex.Model.EmailModel;

public class EmailReader {
	
		EmailDataBaseHelper mEmailDataBase;
		SQLiteDatabase mDatabase;
		ArrayList<EmailModel> mTotal_mails = null;
	    Context context;	
	    
	    
		public EmailReader(Context context)
		{
			this.context = context;
		}
		
		
		
		public ArrayList<EmailModel> getMailsFromWeb(int withSeparator) throws Exception
		{
				
			        mEmailDataBase =  EmailDataBaseHelper.getDBAdapterInstance(context);
					mTotal_mails = new ArrayList<EmailModel>();
					mDatabase = mEmailDataBase.getReadableDatabase();
					
					ContactsDataBase mContactsDataBase =null;
					mContactsDataBase = new ContactsDataBase(context);
			
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
				    
				    Calendar calendar=Calendar.getInstance();
				    Date dby_date = calendar.getTime();
				    SimpleDateFormat checkDateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				    checkDateFormatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
					String st_calender = checkDateFormatter.format(dby_date);
					
					Date email_date = null;
					try{
						email_date = checkDateFormatter.parse(st_calender);
					}catch(Exception ex) {}
				    
					Calendar emailCalender = Calendar.getInstance();
					emailCalender.clear();
					emailCalender.setTime(email_date);
					
		 	        Calendar toDay = Calendar.getInstance();
		 	        toDay.clear();
		 	        Calendar yestarDay = Calendar.getInstance();
		 	        yestarDay.clear();
		 	        Calendar threeDays = Calendar.getInstance();
		 	        threeDays.clear();
		 	         
		 	        toDay.set(emailCalender.get(Calendar.YEAR), emailCalender.get(Calendar.MONTH), emailCalender.get(Calendar.DAY_OF_MONTH));
		 	        
		 	        yestarDay.set(emailCalender.get(Calendar.YEAR), emailCalender.get(Calendar.MONTH), emailCalender.get(Calendar.DAY_OF_MONTH));
		 	        yestarDay.add(Calendar.DAY_OF_MONTH, -1);

		 	        threeDays.set(emailCalender.get(Calendar.YEAR), emailCalender.get(Calendar.MONTH), emailCalender.get(Calendar.DAY_OF_MONTH));
		 	        threeDays.add(Calendar.DAY_OF_MONTH,-2);
		 	        
		 	      		 	        
		 	        String st_toDay = (toDay.get(Calendar.YEAR)) + "-" + ((toDay.get(Calendar.MONTH)+1>9) ?  (toDay.get(Calendar.MONTH)+1) : "0" + (toDay.get(Calendar.MONTH)+1) )  + "-" + ((toDay.get(Calendar.DAY_OF_MONTH)>9) ?  (toDay.get(Calendar.DAY_OF_MONTH)) : "0" + (toDay.get(Calendar.DAY_OF_MONTH)));
		 	        String st_3yDay = (threeDays.get(Calendar.YEAR)) + "-" + ((threeDays.get(Calendar.MONTH)+1>9) ?  (threeDays.get(Calendar.MONTH)+1) : "0" + (threeDays.get(Calendar.MONTH)+1) )  + "-" + ((threeDays.get(Calendar.DAY_OF_MONTH)>9) ?  (threeDays.get(Calendar.DAY_OF_MONTH)) : "0" + (threeDays.get(Calendar.DAY_OF_MONTH)));
		 	        
		 	       //String st_yesDay = yestarDay.getYear() + "-" + ((yestarDay.getMonth()+1>9) ?  (yestarDay.getMonth()+1) : "0" + (yestarDay.getMonth()+1) )  + "-" + ((yestarDay.getDate()>9) ?  (yestarDay.getDate()) : "0" + (yestarDay.getDate()));
		 	    
		 	    	Cursor cursor  = null;
		 	    	ArrayList<ContactsModel> phone_Contacts = new ArrayList<ContactsModel>();
		 	    	phone_Contacts = mContactsDataBase.getContacts_From_DataBase();
		 	    	int generalAndFamily;
					ArrayList<EmailModel> arrT=new ArrayList<EmailModel>(); 
					ArrayList<EmailModel> arrY=new ArrayList<EmailModel>(); 
					ArrayList<EmailModel> arr3Y=new ArrayList<EmailModel>(); 
					
					try
					{
						
						cursor =	mDatabase.rawQuery("select * from ReceivedMails Where EmailDate >='" +st_3yDay + "' AND EmailDate <= '" + st_toDay + "' ORDER BY Email_dateTime DESC", null);
						if (cursor.moveToFirst()) 
				         {
					  			
				  			 	do 
				  			 	{

				  			 		   String email_from = cursor.getString(cursor.getColumnIndex("EmailFrom"));
				  			 		  
				  			 		   
					  			 	   generalAndFamily = 0;
					  			 	   
									   for (int ifamilyCtr = 0; ifamilyCtr < phone_Contacts.size(); ifamilyCtr++) 
									   {
										 if (email_from.equalsIgnoreCase(phone_Contacts.get(ifamilyCtr).getEmail_id())) 
										 {
											generalAndFamily = 1;
											break;
										 }
											
									   }
				  			 		
									   EmailModel mCurrentEmail = new EmailModel();
			                    	   mCurrentEmail.setSubject(cursor.getString(cursor.getColumnIndex("Subject")));
			                    	   mCurrentEmail.setEmail_Sender(cursor.getString(cursor.getColumnIndex("EmailFrom")));
			                    	   mCurrentEmail.setTimezone(cursor.getString(cursor.getColumnIndex("TimeZone")));
			                    	   mCurrentEmail.setStatus(cursor.getInt(cursor.getColumnIndex("EmailStatus")));
			                    	   mCurrentEmail.setEvents_status(cursor.getInt(cursor.getColumnIndex("EnF_Category")));
			                    	   mCurrentEmail.setBusiness_status(cursor.getInt(cursor.getColumnIndex("BnD_Category")));
			                    	   mCurrentEmail.setMedia_status(cursor.getInt(cursor.getColumnIndex("MnP_Category")));
			                    	   mCurrentEmail.setGeneral_status(generalAndFamily);
			                    	   mCurrentEmail.setEmail_id(cursor.getString(cursor.getColumnIndex("Email_Id")));
			                    	   mCurrentEmail.setDate_time(cursor.getString(cursor.getColumnIndex("Email_dateTime")));
			                    	   mCurrentEmail.setEmail_Date(cursor.getString(cursor.getColumnIndex("EmailDate")));
			                    	   mCurrentEmail.setFetched_id(cursor.getLong(cursor.getColumnIndex("Uid")));
			                    	   mCurrentEmail.setMessageBody(cursor.getString(cursor.getColumnIndex("MessageBody")));
			                    	   mCurrentEmail.setAttachmentName(cursor.getString(cursor.getColumnIndex("Attachment")));
			                    	   mCurrentEmail.seperator = "";
			                	
			                    	   Calendar emailDateTemp = Calendar.getInstance();
		                   	           emailDateTemp.clear();
		                   	           Calendar emailDate = Calendar.getInstance();
		                   	           emailDate.clear();
		                   	           Calendar timeZone = Calendar.getInstance();
		                   	           timeZone.clear();
		                   	            
			                    	   try
			                    	   { 
			                    		 
//			                    		 long email_timeStamp = DateFormater.parseDate(mCurrentEmail.getTimezone(), "EEE MMM dd HH:mm:ss z yyyy");
//			                    		 Date email_timeZone_date = new Date(email_timeStamp);
//			                    		 emailDateTemp.setTime(email_timeZone_date);
			                    		 emailDateTemp.setTime(dateFormat.parse(mCurrentEmail.getEmail_Date()));
			                    		 emailDate.set(emailDateTemp.get(Calendar.YEAR), emailDateTemp.get(Calendar.MONTH), emailDateTemp.get(Calendar.DAY_OF_MONTH));
			                    		 
			                    	   }catch(Exception ex){
			                    		   throw ex;
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

						    }while (cursor.moveToNext());
				        }
					}catch(Exception ex){
						ex.printStackTrace();
						throw ex;
					}
					finally {
						
						if(cursor !=null)
							cursor.close();
					}
	  	
			    	 if(arrT.size() >0 )
			    	 {
			    		 
			    		   EmailModel mEmailModel = new EmailModel();
			    		   if(withSeparator ==1 )
			    		   {
				    		   mEmailModel.seperator = "--t";
				    		   mTotal_mails.add(mEmailModel);
				        	   mEmailModel = null;
			    		   }
			        	   for(int ictr = 0; ictr<arrT.size(); ictr++ )
			        	   {
			        		   
			        		   mEmailModel = arrT.get(ictr);
			        		   mEmailModel.setImage_resourceId(R.drawable.t_icon);
			        		   mEmailModel.setBackground_color(R.drawable.gl_red_bar);
			        		   mTotal_mails.add(mEmailModel);
			        		   mEmailModel = null;
			        		   
			        	   }
			    	 }
			    	 
			    	 if(arrY.size() >0)
			    	 {
			    		 
				    		//Log.i("yesterday", "" + arrY.size());
					    	EmailModel mEmailModel = new EmailModel();
					    	if(withSeparator ==1)
					    	{
						    	mEmailModel.seperator = "--y";
						      	mTotal_mails.add(mEmailModel);
						      	mEmailModel = null;
					    	}
				      	   for(int ictr = 0; ictr<arrY.size(); ictr++ )
				      	   {
				      		   
				      		 mEmailModel = arrY.get(ictr);
				      		 mEmailModel.setImage_resourceId(R.drawable.y_icon);
				      		 mEmailModel.setBackground_color(R.drawable.gl_purple_bar);
				      		 mTotal_mails.add(mEmailModel);
				      		 mEmailModel = null;
				      		
				      	   }
				      }
			    	 
			    	 if(arr3Y.size() >0)
			    	 {
			    		 
			    		 	 EmailModel mEmailModel=new EmailModel();
			    		 	if(withSeparator ==1 )
				    		{
				    		 	 mEmailModel.seperator = "--3y";
				    		     mTotal_mails.add(mEmailModel);
				        	     mEmailModel = null;
				    		}
			        	    for(int ictr = 0; ictr<arr3Y.size(); ictr++ )
			        	    {
			        	    	
			        	    	mEmailModel = arr3Y.get(ictr);
			        	    	mEmailModel.setImage_resourceId(R.drawable.threed_icon);
			        	    	mEmailModel.setBackground_color(R.drawable.gl_brown_bar);
			         		   	mTotal_mails.add(mEmailModel);
			         		    mEmailModel = null;
			         		    
			        	    }
			    	 }
	    	 return mTotal_mails;

		 }


		private void backupDb() throws IOException {
		    File sd = Environment.getExternalStorageDirectory();
		    File data = Environment.getDataDirectory();

		    if (sd.canWrite()) {

		        String currentDBPath = "/data/com.skeyedex/databases/Skeyedex.db";
		        String backupDBPath = "/Skeyedex_logs/Skeyedex.db";

		        File currentDB = new File(data, currentDBPath);
		        File backupDB = new File(sd, backupDBPath);

		        if (backupDB.exists())
		            backupDB.delete();

		        if (currentDB.exists()) {
		            makeLogsFolder();

		            //copy(currentDB, backupDB);
		       }
		        
		        
		     // Local database
		        InputStream input = new FileInputStream(data + currentDBPath);

		        // create directory for backup
		       // File dir = new File(backupDBPath);
		        //dir.mkdir();

		        // Path to the external backup
		        OutputStream output = new FileOutputStream(sd + backupDBPath);

		        // transfer bytes from the Input File to the Output File
		        byte[] buffer = new byte[1024];
		        int length;
		        while ((length = input.read(buffer))>0) {
		            output.write(buffer, 0, length);
		        }

		        output.flush();
		        output.close();
		        input.close();


		      
		   }
		}

		 private void makeLogsFolder() {
		    try {
		        File sdFolder = new File(Environment.getExternalStorageDirectory(), "/Skeyedex_logs/");
		        sdFolder.mkdirs();
		    }
		    catch (Exception e) {}
		  }

		private void copy(File from, File to) throws FileNotFoundException, IOException {
		    FileChannel src = null;
		    FileChannel dst = null;
		    try {
		        src = new FileInputStream(from).getChannel();
		        dst = new FileOutputStream(to).getChannel();
		        dst.transferFrom(src, 0, src.size());
		    }
		    finally {
		        if (src != null)
		            src.close();
		        if (dst != null)
		            dst.close();
		    }
		}

}
