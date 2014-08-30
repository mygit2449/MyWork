package com.skeyedex.EmailDataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class EmailDataBaseHelper extends SQLiteOpenHelper{

	private static EmailDataBaseHelper mDBConnection;
	public static String Lock = "dblock";
	
	public EmailDataBaseHelper(Context context)
	{
		super(context, "Skeyedex.db", null, 1);
	}

	public static synchronized EmailDataBaseHelper getDBAdapterInstance(Context context)
	{
		
		synchronized(Lock)
		{
			if (mDBConnection == null) {
				mDBConnection = new EmailDataBaseHelper(context);
	    }
	    return mDBConnection;
		}

	}

	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		db.execSQL("create table ReceivedMails (_Id  integer primary key autoincrement, Email_Id varchar(50), Uid integer , EmailDate date, EmailTime varchar(10), EmailFrom varchar(50) , Subject varchar(200), EmailStatus integer(1), EnF_Category integer(1), BnD_Category integer(1), MnP_Category integer(1), GnF_Category integer(1), Email_dateTime varchar(100) UNIQUE NOT NULL, MessageBody varchar(500), Attachment  varchar(100), TimeZone varchar(20));");
		
		db.execSQL("create table ServerSettings (_Id integer primary key autoincrement, Username  varchar(50), Password varchar(50), IMAP_Server varchar(70), Port integer, Security_Type varchar(20), IMAP_Path varchar(30), Email_Id varchar(100) unique, Email_Type varchar(20));");
		
		db.execSQL("create table  ContactsList(_Id integer primary key autoincrement, contact_name varchar(70), phone_number varchar(20), email_id varchar(100)  UNIQUE, contact_id varchar(20) UNIQUE);");
		
		Log.v("DataBaseHelper Class", "Table Created");
	}



	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) 
	{
		// TODO Auto-generated method stub
		
	}

}
