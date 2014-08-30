package com.skeyedex.EmailDataBase;

import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.skeyedex.Model.ServerSettingsModel;

public class ServerSettingsDataBase {

	EmailDataBaseHelper mDatabase_helper;
    SQLiteDatabase mDatabase;
    ServerSettingsModel mseServerSettingsModel;
    
    
    
	public ServerSettingsDataBase(Context context)
	{
		mDatabase_helper =  EmailDataBaseHelper.getDBAdapterInstance(context);
	}
    
	public long saveIncomingSettings(String username, String password, String imap_server, String port, String security_type, String imap_path_prifix, String email_id, String email_type)
	{
		
		mDatabase = mDatabase_helper.getReadableDatabase();
		long iInsetResult = 0;
		
		try 
			{
			
				ContentValues values = new ContentValues();
		        values.put("Username", username);
		        values.put("Password", password);
		        values.put("IMAP_Server", imap_server);
		        values.put("Port", port);
		        values.put("Security_Type", security_type);
		        values.put("IMAP_Path", imap_path_prifix);
		        values.put("Email_Id", email_id);
		        values.put("Email_Type", email_type);
		        iInsetResult = mDatabase.insert("ServerSettings", null, values);
    
			} catch (Exception e) {
			
				iInsetResult = 0;
			}
			finally  
			{
//				if (mDatabase != null) {
//					mDatabase.close();
//				}
			}
		 
		 return iInsetResult;
	}
	
	public ArrayList<ServerSettingsModel> getServerDetailsFromDb()
	{
		
		mDatabase = mDatabase_helper.getReadableDatabase();
		ArrayList<ServerSettingsModel> mtotal_EmailIds = new ArrayList<ServerSettingsModel>();
		
		try 
		{
			
			Cursor cursor  = mDatabase.rawQuery("SELECT  Username, password ,IMAP_Server , Port, Email_Type, Email_Id  from ServerSettings", null);
			if (cursor.getCount() > 0)
			{
				
					int ictr = 0;
					cursor.moveToPosition(ictr);
					
					do 
					{
						
						mseServerSettingsModel = new ServerSettingsModel();
						mseServerSettingsModel.setUsername(cursor.getString(0));
						mseServerSettingsModel.setPassword(cursor.getString(1));
						mseServerSettingsModel.setImap_server(cursor.getString(2));
						mseServerSettingsModel.setPort(cursor.getString(3));
						mseServerSettingsModel.setEmail_type(cursor.getString(4));
						mseServerSettingsModel.setEmail_id(cursor.getString(5));
						mtotal_EmailIds.add(mseServerSettingsModel);
						mseServerSettingsModel = null;
						ictr++;
						
					 } while (cursor.moveToPosition(ictr));
					
			}
			
			cursor.close();
			
		}catch(Exception ex){}
		
		finally{}
		
		return mtotal_EmailIds;
		
	}
	
	//deletion of selected email account from database.
	public void deleteSelectedContact(String email_id)
	{
		try 
		{
			
			mDatabase = mDatabase_helper.getWritableDatabase();
			mDatabase.execSQL("DELETE FROM ServerSettings WHERE Email_Id='"+email_id+"'");
			Log.i("Query","DELETE FROM ServerSettings WHERE Email_Id='"+email_id+"'"); 
			Log.e("selected contact ", email_id+"deleted");
				
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally
		{
//	        if (mDatabase != null)
//	         mDatabase.close();
	        
		}
	
  }
	
	public String getImapServerFromDatabase(String email_Id)
	{
		
		
		Cursor cursor = null;
		try
		{
	
			mDatabase = mDatabase_helper.getWritableDatabase();
			cursor  = mDatabase.rawQuery("SELECT IMAP_Server FROM ServerSettings WHERE Email_Id='"+email_Id+"'", null);
			Log.i("Query","SELECT IMAP_Server FROM ServerSettings WHERE Email_Id='"+email_Id+"'");
			if (cursor != null && cursor.moveToFirst()) {
				return cursor.getString(0);
			}
			
			return null;
			
		}
		finally
		{
			cursor.close();
//	        if (mDatabase != null)
//	        	mDatabase.close();
			
		}
}

}
