package com.parsinganddatabase.exp;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class MyExpDAO {

	DbHelperClass mDbHelperClass;
	SQLiteDatabase mDatabase;
	
	
	public MyExpDAO(Context context){
		mDbHelperClass = new DbHelperClass(context);
	}
	
	
	public void saveDetailsIndatabase(String title, String description, String pubdate){
		
		try 
		{
			mDatabase = mDbHelperClass.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put("Title", title);
			values.put("Description", description);
			values.put("Pubdate", pubdate);
			mDatabase.insert("BBCNews", null, values);
			Log.v("CheckDataBase", "Inserted");

		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	
	public ArrayList<MyExampleModel> getDetails(){
		
		mDatabase = mDbHelperClass.getReadableDatabase();
		ArrayList<MyExampleModel> allDetails = new ArrayList<MyExampleModel>();
		MyExampleModel myExampleModel;
		
		try 
		{
			
			Cursor cursor = mDatabase.rawQuery("select * from BBCNews", null);
			
			if (cursor.getCount() > 0) {
				int iCtr = 0;
				cursor.moveToPosition(iCtr);
				do 
				{
					
				 myExampleModel = new MyExampleModel();
				 myExampleModel.setTitle(cursor.getString(0));
				 myExampleModel.setDescription(cursor.getString(1));
				 myExampleModel.setTitle(cursor.getString(2));
				 allDetails.add(myExampleModel);
				 myExampleModel = null;	
				 iCtr++;
				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return allDetails;
		
	}
}
