package com.copperlabs.newxml;



import android.app.Application;
import android.content.Context;

public class MyNewXmlApp extends Application{

	public static Context context;
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		MyNewXmlApp.context=getApplicationContext();
	
	}
}
