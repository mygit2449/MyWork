package com.skeyedex.TextMessages;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;

import com.skeyedex.R;
import com.skeyedex.EmailDataBase.ContactsDataBase;
import com.skeyedex.EmailDataBase.EmailDataBaseHelper;
import com.skeyedex.Model.ContactsModel;
import com.skeyedex.Model.SMSmodel;

public class SMSreader 
{
	private static String 					LOG_TAG=SMSreader.class.getSimpleName();
    private Uri 									sms=Uri.parse("content://sms");
    private ContentResolver 				mContentResolver;
  
    private PhoneContactsModel					mContactsModel;

    public static final Uri MMS_SMS_CONTENT_URI = Uri.parse("content://mms-sms/");
    public static final Uri THREAD_ID_CONTENT_URI =
                    Uri.withAppendedPath(MMS_SMS_CONTENT_URI, "threadID");
    public static final Uri CONVERSATION_CONTENT_URI =
                    Uri.withAppendedPath(MMS_SMS_CONTENT_URI, "conversations");
    
    public static final Uri SMS_CONTENT_URI = Uri.parse("content://sms");
    public static final Uri SMS_INBOX_CONTENT_URI = Uri.withAppendedPath(SMS_CONTENT_URI, "inbox");
    
    public static final Uri MMS_CONTENT_URI = Uri.parse("content://mms");
    public static final Uri MMS_INBOX_CONTENT_URI = Uri.withAppendedPath(MMS_CONTENT_URI, "inbox");
    
    public static final String SMS_ID = "_id";
    public static final String SMS_TO_URI = "smsto:/";
    public static final String SMS_MIME_TYPE = "vnd.android-dir/mms-sms";
    public static final int READ_THREAD = 1;
    public static final int MESSAGE_TYPE_SMS = 1;
    public static final int MESSAGE_TYPE_MMS = 2;
	    
	ArrayList<ContactsModel> phone_Contacts = null;
	ArrayList<PhoneContactsModel> 	contacts = null;
	
    Context context;
    
    public SMSreader(Context context)
    {
    	this.context = context;
    	 
    }
    
    
    public ArrayList<PhoneContactsModel> getContact(Context context) 
    {
    	ArrayList<PhoneContactsModel> contacts=new ArrayList<SMSreader.PhoneContactsModel>();
    	try{
    		
	    		

    			Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
    			String[] projection    = new String[] {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
    			                ContactsContract.CommonDataKinds.Phone.NUMBER};

    			Cursor people = context.getContentResolver().query(uri, projection, null, null, null);

    			int indexName = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
    			int indexNumber = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

    			if(people.moveToFirst())
    			{
	    			do {
		    				mContactsModel=new PhoneContactsModel();
		    			    mContactsModel.displayName  = people.getString(indexName);
		    			    mContactsModel.phoneNo = people.getString(indexNumber);
		    			    contacts.add(mContactsModel);
		    			    mContactsModel=null;
	    			  
	    				} while (people.moveToNext());
    			}
    			return contacts;
    			

    	}catch (Exception e) {
    		e.printStackTrace();

    	}
        return null;
    }
    
    public String getContactName(String contactNO)
    {
    	for(int i=0;i<contacts.size();i++)
    	{
    		if(PhoneNumberUtils.compare(contactNO, contacts.get(i).phoneNo ))
    		{
    			return contacts.get(i).displayName;
    		}
    
    	}
    	return contactNO;
    	
    }
    
    public String getContactNumber(String contactNO)
    {
    	for(int i=0;i<contacts.size();i++)
    	{
    		if(PhoneNumberUtils.compare(contactNO, contacts.get(i).phoneNo ))
    		{
    			return contacts.get(i).phoneNo;
    		}
    
    	}
    	return contactNO;
    	
    }
    
    public void deleteMessage(Context context, long messsageId)
    {
    	Uri messageUri;
    	messageUri=Uri.withAppendedPath(SMS_CONTENT_URI, String.valueOf(messsageId));
    	ContentResolver cr= context.getContentResolver();
    	int i=cr.delete(messageUri, null, null);
    	//Log.e("####",String.valueOf(i));
    }
     
    public static void setMessageRead(Context context, long messageId, int messageType) 
    {
   
        if (messageId > 0) {            
                ContentValues values = new ContentValues(1);
                values.put("read", READ_THREAD);

                Uri messageUri;

                if (SMSreader.MESSAGE_TYPE_MMS == messageType) {
                        messageUri = Uri.withAppendedPath(MMS_CONTENT_URI, String.valueOf(messageId));
                } else if (SMSreader.MESSAGE_TYPE_SMS == messageType) {
                        messageUri = Uri.withAppendedPath(SMS_CONTENT_URI, String.valueOf(messageId));
                } else {
                        return;
                }
                
                
                ContentResolver cr = context.getContentResolver(); 
                int result = 0;
                try {           
                        result = cr.update(messageUri, values, null, null);
                } catch (Exception e) {
                       e.printStackTrace();
                }
               
        }
    }
    
    public ArrayList<SMSmodel> getTxtMessages(int mailsOfDays, int withSeparator, int eventtype)
    {
    	
    	 
    	 String body = "";
    	 String from = "";
    	 ArrayList<SMSmodel>  txtMessages=new ArrayList<SMSmodel>(); 
    	 contacts=getContact(context);
    	 mContentResolver = context.getContentResolver();
    	 Cursor 	cursorSMS = mContentResolver.query(sms, null, null, null, null);
    	 ArrayList<SMSmodel> arrT=new ArrayList<SMSmodel>(); 
   		 ArrayList<SMSmodel> arrY=new ArrayList<SMSmodel>(); 
   		 ArrayList<SMSmodel> arr3Y=new ArrayList<SMSmodel>(); 
   		
    	 if (cursorSMS.moveToFirst()) 
         {
    		
    		Calendar calendar=Calendar.getInstance();
 	         
 	        Calendar currentDate = Calendar.getInstance();
 	        currentDate.clear();
 	        currentDate.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
 	        
 	        Calendar toDay = Calendar.getInstance();
 	        toDay.clear();
 	        Calendar yestarDay = Calendar.getInstance();
 	        yestarDay.clear();
 	        Calendar threeDays = Calendar.getInstance();
 	        threeDays.clear();
 	         
 	        toDay.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
 	        
 	        yestarDay.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
 	        yestarDay.add(Calendar.DAY_OF_MONTH, -1);

 	        threeDays.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
 	        threeDays.add(Calendar.DAY_OF_MONTH,-2);
 	        
 	       
 	         
 	         //Log.i("To day date"," "+currentDate+ " " +yestarDay+ " "+ threeDays);
             do{    
             	 
                     switch (Integer.parseInt(cursorSMS.getString(cursorSMS.getColumnIndex("type"))))
                     {
                           case 1: 
                        	  
                        	   SMSmodel currentSmSmodel=new SMSmodel();
                        	  
                        	   
                        	   currentSmSmodel.setthreadID(cursorSMS.getLong(cursorSMS.getColumnIndex("thread_id")));
                        	   currentSmSmodel.setmessageID(cursorSMS.getLong(cursorSMS.getColumnIndex("_id")));
                        	   currentSmSmodel.setDate(cursorSMS.getLong(cursorSMS.getColumnIndex("DATE")));
                        	   currentSmSmodel.setStatus(cursorSMS.getInt(cursorSMS.getColumnIndex("read")));
                        	  
                        	   currentSmSmodel.setBody(cursorSMS.getString(cursorSMS.getColumnIndex("body")));
                        	   currentSmSmodel.setSmsDate(cursorSMS.getString(cursorSMS.getColumnIndex("DATE")));
                        	   
                        	   currentSmSmodel.setSender(getContactName(cursorSMS.getString(cursorSMS.getColumnIndex("address"))));
                        	   
                   	           Calendar messageDate = Calendar.getInstance();
                   	           messageDate.clear();
                   	           messageDate.setTimeInMillis(currentSmSmodel.getDate());
                        	  
                        	   Calendar smsDate = Calendar.getInstance();
                        	   smsDate.clear();
                        	   
                        	   smsDate.set(messageDate.get(Calendar.YEAR), messageDate.get(Calendar.MONTH), messageDate.get(Calendar.DAY_OF_MONTH));
                        	   
                        	   //Log.i(LOG_TAG," The sms date time "+smsDate.getTime() + " toDay"+ toDay.getTime());
                        	  
                        	   body = cursorSMS.getString(cursorSMS.getColumnIndex("body"));
                        	   //Log.v(LOG_TAG, "Sender Addr "+getContactNumber(cursorSMS.getString(cursorSMS.getColumnIndex("address")))); 
                        	   from = getContactNumber(cursorSMS.getString(cursorSMS.getColumnIndex("address")));
                        	   if(smsDate.equals(toDay) && (mailsOfDays & 1) == 1) 
                        	   {
                        		   if (eventtype == -1 || eventtype == getEventtypefrombody(body, from)) // eventtype =0 is for events and address 
                        		   {
                        			   arrT.add(currentSmSmodel);
                        		   }
                        		    
                        	   }else if(smsDate.equals(yestarDay) && (mailsOfDays & 2) == 2){
                        		   if (eventtype == -1 || eventtype == getEventtypefrombody(body, from)) // eventtype =1 is for media
                        		   {
                        			   arrY.add(currentSmSmodel);
                        		   }
                        	   } 
                        	   else if(smsDate.equals(threeDays) && (mailsOfDays & 4) == 4)  // eventtype =1 is for business
                        	   {
                        		   if (eventtype == -1 || eventtype == getEventtypefrombody(body, from)) 
                        		   {
                        			   arr3Y.add(currentSmSmodel);
                        		   }

                        	   }

                        	  currentSmSmodel=null;
                              break;
                             
                     }
                   
             } while (cursorSMS.moveToNext());
          } 
    	 int serialNo =  arrT.size() + arrY.size() + arr3Y.size();
    	 if(arrT.size() >0)
    	 {
    		   //Log.i("today", "" + arrT.size());
 			   SMSmodel currentSmSmodel=new SMSmodel();
    		   if(withSeparator ==1 )
    		   {
    			   currentSmSmodel.setSender("--t");
    			   txtMessages.add(currentSmSmodel);
    			   currentSmSmodel = null;
    		   }	   
        	   for(int ictr = 0; ictr<arrT.size(); ictr++ )
        	   {
        		   currentSmSmodel = arrT.get(ictr);
        		   currentSmSmodel.setSerial_No(serialNo--);
        		   currentSmSmodel.setImage_resourceId(R.drawable.t_icon);
        		   txtMessages.add(currentSmSmodel);
        		   currentSmSmodel = null;
        	   }
    	 }
    	 
    	 if(arrY.size() >0)
    	 {
	    	//Log.i("yesterday", "" + arrY.size());
	    	  SMSmodel currentSmSmodel=new SMSmodel();
	    	  if(withSeparator ==1 )
   		   	 {
	    		  currentSmSmodel.setSender("--y");
	    		  txtMessages.add(currentSmSmodel);
	    		  currentSmSmodel = null;
   		   	 }
	      	   for(int ictr = 0; ictr<arrY.size(); ictr++ )
	      	   {
	      		 
	      		   currentSmSmodel = arrY.get(ictr);
	      		   currentSmSmodel.setSerial_No(serialNo--);
	      		   currentSmSmodel.setImage_resourceId(R.drawable.y_icon);
	      		   txtMessages.add(currentSmSmodel);
	      		   currentSmSmodel = null;
	      	   }
	       	 }
    	 if(arr3Y.size() >0)
    	 {
    		 	//Log.i("3 days", "" + arr3Y.size());
    		 	SMSmodel currentSmSmodel=new SMSmodel();
    		 	 if(withSeparator ==1 )
    		 	 {    		 
    		 		 currentSmSmodel.setSender("--3y");
    		 		 txtMessages.add(currentSmSmodel);
    		 		 currentSmSmodel = null;
    		 	 }
        	    for(int ictr = 0; ictr<arr3Y.size(); ictr++ )
        	    {
        	    	currentSmSmodel = arr3Y.get(ictr);
         		   	currentSmSmodel.setSerial_No(serialNo--);
         		   	currentSmSmodel.setImage_resourceId(R.drawable.threed_icon);
         		   	txtMessages.add(currentSmSmodel);
         		   	currentSmSmodel = null;
        	    	
        	    }
    	    }
    	 
    	 //Log.i("Total size ", "" + txtMessages.size());
    	return txtMessages;
    }
    
    public int getEventtypefrombody(String body, String from)
    {
	    	EmailDataBaseHelper mEmailDataBase;
			SQLiteDatabase mDatabase;
			mEmailDataBase = EmailDataBaseHelper.getDBAdapterInstance(context);
			ContactsDataBase mContactsDataBase =null;
			mContactsDataBase = new ContactsDataBase(context);
			
			phone_Contacts = mContactsDataBase.getContacts_From_DataBase();
			mDatabase = mEmailDataBase.getReadableDatabase();
			
    		int eventtype = -1;
    		if (body.contains("Events")) {
				eventtype = 0;
			}else if (body.contains("Business")){ 
				eventtype = 2;
			 }else if (body.contains("Media")) {
				eventtype = 1;
			 }else {
				 //Log.v(LOG_TAG, "Phone Contacts Size"+phone_Contacts.size());
				 
				 for (int ifamilyCtr = 0; ifamilyCtr < phone_Contacts.size(); ifamilyCtr++) 
					{
					 //Log.v(LOG_TAG, " phone number"+phone_Contacts.get(ifamilyCtr).getPhone_number());
					
					 try{
					 	if (from.contains(phone_Contacts.get(ifamilyCtr).getPhone_number())) 
						{
							//Log.v(LOG_TAG, "from address"+from);
							eventtype = 3;
							break;
						}
					}
					catch (Exception e) {
						// TODO: handle exception
					}
			 }
			 } 
			 return eventtype;
    }

    public class PhoneContactsModel
    {
    	String displayName,phoneNo;
    }
}
