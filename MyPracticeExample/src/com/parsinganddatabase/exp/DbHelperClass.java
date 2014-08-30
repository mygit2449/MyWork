package com.parsinganddatabase.exp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelperClass extends SQLiteOpenHelper{

	public DbHelperClass(Context context) {
		super(context, "MyDataBase.db", null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("create table BBCNews (_Id integer primary key autoincrement, Title varchar(500), Description varchar(100), Pubdate varchar(50));");
		Log.v(getClass().getSimpleName(), " table created ");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
