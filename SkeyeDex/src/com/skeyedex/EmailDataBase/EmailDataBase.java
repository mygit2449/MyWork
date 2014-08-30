package com.skeyedex.EmailDataBase;

import java.util.ArrayList;

import com.skeyedex.Model.EmailModel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class EmailDataBase {

		EmailDataBaseHelper mDatabase_helper;
	    SQLiteDatabase mDatabase;
	    EmailModel mEmailDataBaseModel;
	    
	    ArrayList<EmailModel> mTotal_mails = null;
	    String mResult;
    
		public EmailDataBase(Context context) 
		{
			mDatabase_helper = new EmailDataBaseHelper(context);
	    }
		
		
		public String getEmailIds_From_ServerDataBase(String email_Id)
		{
			
			
			Cursor cursor = null;
			try
			{
		
				mDatabase = mDatabase_helper.getWritableDatabase();
				cursor  = mDatabase.rawQuery("SELECT Email_Id FROM ServerSettings WHERE Email_Id='"+email_Id+"'", null);
				if (cursor != null && cursor.moveToFirst()) {
					return cursor.getString(0);
				}
				
				return null;
				
			}
			finally
			{
//				cursor.close();
//		        if (mDatabase != null)
//		        	mDatabase.close();
				
			}
	}
		
		public String getEmailIds_From_DataBase(long msgId)
		{
			
			
			Cursor cursor = null;
			try
			{
		
				mDatabase = mDatabase_helper.getWritableDatabase();
				cursor  = mDatabase.rawQuery("SELECT Email_Id FROM ReceivedMails WHERE Uid='"+msgId+"'", null);
				if (cursor != null && cursor.moveToFirst()) {
					return cursor.getString(0);
				}
				
				return null;
				
			}
			finally
			{
				cursor.close();
//		        if (mDatabase != null)
//		        	mDatabase.close();
				
			}
	}
		
		
			// update status read and unread
			public void updateEmailStatus(int read_message, int unread_message, long uid)
			{
	
				
				ContentValues update_cv = new ContentValues();
				update_cv.put("EmailStatus",read_message);
				Log.v("Read",""+read_message);
				
				try
				{
					
					mDatabase = mDatabase_helper.getWritableDatabase();
					mDatabase.execSQL("UPDATE ReceivedMails SET EmailStatus='"+read_message+"'WHERE EmailStatus='"+unread_message+"' AND Uid = '"+uid+"'");
					Log.i("Update Query","UPDATE ReceivedMails SET EmailStatus='"+read_message+"'WHERE EmailStatus='"+unread_message+"' AND Uid = '"+uid+"'"); 
					
				}
				catch (Exception e) {
					// TODO: handle exception
					Log.e("updating not done", e.toString());
		    		e.printStackTrace();
				}finally
				{
//			        if (mDatabase != null)
//			        	mDatabase.close();
				}
			}
		
			//deleting mails from database
			public void deleteMails(String email_id)
			{
				try
				{
					
					mDatabase = mDatabase_helper.getWritableDatabase();
					mDatabase.execSQL("DELETE FROM ReceivedMails WHERE Email_Id='"+email_id+"'");
					Log.i("Delete mails Query", "DELETE FROM ReceivedMails WHERE Email_Id='"+email_id+"'");
					
				 	}catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}finally
				 {
			        if (mDatabase != null)
			        	mDatabase.close();
				 }
			
				
			}
			
			//deleting mails from database
			public void deleteEmails(long minMsgId, long maxMsgId)
			{
				try
				{
					
					mDatabase = mDatabase_helper.getWritableDatabase();
					mDatabase.execSQL("DELETE FROM ReceivedMails WHERE Uid >='"+minMsgId+"' AND Uid <='"+maxMsgId+"'");
					Log.i("Delete Emails Query","DELETE FROM ReceivedMails WHERE Uid >='"+minMsgId+"' AND Uid <='"+maxMsgId+"'");
					
				 }catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				 }finally
				 {
			        if (mDatabase != null)
			        	mDatabase.close();
				 }
				
			}
		
			
			//deleting mails from database
			public void deleteEmails(long msgId)
			{
				try
				{
					
					mDatabase = mDatabase_helper.getWritableDatabase();
					mDatabase.execSQL("DELETE FROM ReceivedMails WHERE Uid='"+msgId+"'");
					
					Log.i("Delete mails Query", "DELETE FROM ReceivedMails WHERE Uid='"+msgId+"'");
					
				 }catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				 }finally
				 {
			        if (mDatabase != null)
			        	mDatabase.close();
				 }
				
			}
		
}
