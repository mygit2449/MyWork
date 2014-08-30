package com.skeyedex.EmailDataBase;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.skeyedex.Model.ContactsModel;

public class ContactsDataBase {

	EmailDataBaseHelper mDatabase_helper;
	SQLiteDatabase mDatabase;
	ContactsModel mContactsModel;

	public ContactsDataBase(Context context) {
		mDatabase_helper = EmailDataBaseHelper.getDBAdapterInstance(context);
	}

	public long saveContactsInDataBase( String Contact_name,String Email_Id, String phone_number, String contact_id)
	{
		
			mDatabase = mDatabase_helper.getReadableDatabase();
	        long iInsetResult=0;
	        
			try 
			{
				
				ContentValues values = new ContentValues();
				values.put("contact_name", Contact_name);
				values.put("email_id", Email_Id);
				values.put("phone_number", phone_number);
				values.put("contact_id", contact_id);
				iInsetResult=mDatabase.insert("ContactsList", null, values);
				
		    } catch (Exception e) {
		    	iInsetResult = 0;
				// TODO: handle exception
			} finally {
				if (mDatabase != null) {
					//mDatabase.close();
				}
			}
		return iInsetResult;
	}
	
	public ArrayList<ContactsModel> getContacts_From_DataBase()
	{
		
		ArrayList<ContactsModel> total_contacts = new ArrayList<ContactsModel>();;
		mDatabase = mDatabase_helper.getWritableDatabase();
		
		try 
		{
			
				Cursor cursor  = mDatabase.rawQuery("SELECT contact_name,phone_number, email_id from  ContactsList", null);
				Log.e("Contacts db", " Contacts Count "+cursor.getCount());
				if (cursor.getCount() > 0) 
				{
					
					int ictr = 0;
					cursor.moveToPosition(ictr);
					do 
					{
						mContactsModel = new ContactsModel();
						mContactsModel.setContact_name(cursor.getString(0));
						mContactsModel.setEmail_id(cursor.getString(2));
						mContactsModel.setPhone_number(cursor.getString(1));
						total_contacts.add(mContactsModel);
						mContactsModel = null;
						ictr++;
						
					 } while (cursor.moveToPosition(ictr));
					
				}
				cursor.close();
				return total_contacts;
		}
		finally
		{
	        //if (mDatabase != null)
	        	//mDatabase.close();
	        
		}
   }
	
	public void deleteSelectedContact(String selected_name)
	{
		try 
		{
			
				mDatabase = mDatabase_helper.getWritableDatabase();
				mDatabase.execSQL("DELETE FROM ContactsList WHERE contact_name='"+selected_name+"'");
				Log.i("Query","DELETE FROM ContactsList WHERE contact_name='"+selected_name+"'"); 
				Log.e("selected name", selected_name+"deleted");
				
				
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally
		{
//	        if (mDatabase != null)
//	         //mDatabase.close();
	        
		}
	
  }
	
}
