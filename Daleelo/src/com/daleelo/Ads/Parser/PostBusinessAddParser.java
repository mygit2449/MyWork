package com.daleelo.Ads.Parser;

import org.xmlpull.v1.XmlPullParser;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;

import com.daleelo.Utilities.BaseHttpLoader;

public class PostBusinessAddParser extends BaseHttpLoader{

	private final int BUSINESS_POST_SUCCESS = 7;
	private final int NO_RESULT_FOUND_BUSINESS = 1;
	private final int ERROR_WHILE_GETTING_RESULT = 2;
	boolean requiredTagFound = false;
	
	Handler parentHandler;

	public PostBusinessAddParser(String feedUrl, Handler handler) {
		super(feedUrl);
		parentHandler = handler;
	}

	public void getBusinessResponce(){
		
		XmlPullParser parser = Xml.newPullParser();
		try
		{
		
			parser.setInput(this.getInputStream(), null);
			int eventType = parser.getEventType();
			boolean done = false;
			String satatus_flag = null;
			String satatus_message = null;
			
			while (eventType != XmlPullParser.END_DOCUMENT && !done) {
				String name = null;

				switch (eventType) 
				{
					case XmlPullParser.START_DOCUMENT:
						
						break;
	
					case XmlPullParser.START_TAG:
						name = parser.getName();
						
						if (name.equalsIgnoreCase("BusinessResponse")) {
							Log.v(getClass().getSimpleName(), "required found ");
							requiredTagFound = true;
						}else if (name.equalsIgnoreCase("StatusFlag")) {
							satatus_flag = parser.nextText();
							Log.v(getClass().getSimpleName(), "required flag "+satatus_flag);

						}else if (name.equalsIgnoreCase("StatusMessage")) {
							satatus_message = parser.nextText();
							Log.v(getClass().getSimpleName(), "required flag "+satatus_message);

						}else if (name.equalsIgnoreCase("BusinessID")) {
							Log.v(getClass().getSimpleName(), "required flag "+satatus_message);

						}
						break;
					
					case XmlPullParser.END_TAG:
						name = parser.getName();
						if (name.equalsIgnoreCase("BusinessResponse") && requiredTagFound) {
							Message messageToparent = new Message();
							Bundle messageData = new Bundle();
							messageToparent.what = BUSINESS_POST_SUCCESS;
							messageData.putSerializable("businesspost_responce_flag", satatus_flag);
							messageData.putSerializable("businesspost_responce_messsage", satatus_message);
							messageToparent.setData(messageData);
							parentHandler.sendMessage(messageToparent);
							done = true;
						}else if (name.equalsIgnoreCase("BusinessResponse") && !requiredTagFound) {
						
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
