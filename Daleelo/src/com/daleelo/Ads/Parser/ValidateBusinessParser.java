package com.daleelo.Ads.Parser;

import org.xmlpull.v1.XmlPullParser;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;

import com.daleelo.Utilities.BaseHttpLoader;

public class ValidateBusinessParser extends BaseHttpLoader{

	private final int FOUND_BUSINESS = 6;
	private final int NO_RESULT_FOUND_BUSINESS = 1;
	private final int ERROR_WHILE_GETTING_RESULT = 2;
	boolean requiredTagFound = false;
	
	Handler parentHandler;
	
	public ValidateBusinessParser(String feedUrl, Handler handler) {
		super(feedUrl);
		parentHandler = handler;
	}
	
	
	public void getServerResponce(){
		
		XmlPullParser parser = Xml.newPullParser();
		try
		{
		
			parser.setInput(this.getInputStream(), null);
			int eventType = parser.getEventType();
			boolean done = false;
			String responce_flag = null;
			String responce_message = null;
			
			while (eventType != XmlPullParser.END_DOCUMENT && !done) {
				String name = null;

				switch (eventType) 
				{
					case XmlPullParser.START_DOCUMENT:
						
						break;
	
					case XmlPullParser.START_TAG:
						name = parser.getName();
						
						if (name.equalsIgnoreCase("validationbusiness")) {
							Log.v(getClass().getSimpleName(), "required found ");
							requiredTagFound = true;
						}else if (name.equalsIgnoreCase("Flag")) {
							responce_flag = parser.nextText();
							Log.v(getClass().getSimpleName(), "required flag "+responce_flag);

						}else if (name.equalsIgnoreCase("Message")) {
							responce_message = parser.nextText();
							Log.v(getClass().getSimpleName(), "required flag "+responce_message);

						}
						break;
					
					case XmlPullParser.END_TAG:
						name = parser.getName();
						if (name.equalsIgnoreCase("validationbusiness") && requiredTagFound) {
							Message messageToparent = new Message();
							Bundle messageData = new Bundle();
							messageToparent.what = FOUND_BUSINESS;
							messageData.putSerializable("flag", responce_flag);
							messageData.putSerializable("responceData", responce_message);
							messageToparent.setData(messageData);
							parentHandler.sendMessage(messageToparent);
							done = true;
						}else if (name.equalsIgnoreCase("validationbusiness") && !requiredTagFound) {
						
							Message messageToParent = new Message();
							Bundle messageData = new Bundle();
							messageToParent.what = NO_RESULT_FOUND_BUSINESS;
							messageData.putString("notfound", "notfound");
							messageToParent.setData(messageData);
							// send message to mainThread
							parentHandler.sendMessage(messageToParent);						
							done = true;
					}
					break;
						
				}
				eventType = parser.next();
			}
		}catch (Exception ex) {
			
			Message messageToParent = new Message();
			Bundle messageData = new Bundle();
			messageToParent.what = ERROR_WHILE_GETTING_RESULT;
			messageData.putString("connectionTimeOut","Internet connection gone");
			messageToParent.setData(messageData);
			// send message to mainThread
			parentHandler.sendMessage(messageToParent);
			throw new RuntimeException(ex);
		}
	}

}
