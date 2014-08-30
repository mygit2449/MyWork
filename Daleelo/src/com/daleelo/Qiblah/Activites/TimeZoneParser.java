package com.daleelo.Qiblah.Activites;

import org.xmlpull.v1.XmlPullParser;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;

import com.daleelo.Utilities.BaseHttpLoader;
import com.daleelo.Utilities.CurrentLocationData;

public class TimeZoneParser extends BaseHttpLoader{

	public  static final int FOUND_RESULT = 0;
	public  static final int NO_RESULT = 1;
	public  static final int ERROR_WHILE_GETTING_RESULT = 2;

	private Handler parentHandler;
	boolean requiredTagFound = false;

	public TimeZoneParser(String feedUrl, Handler handler) {
		super(feedUrl);
		parentHandler = handler;
		Log.e("URL", feedUrl);
		// TODO Auto-generated constructor stub
	}

	public void parser() {
		// TODO Auto-generated method stub

		XmlPullParser parser = Xml.newPullParser();
		try {

			parser.setInput(this.getInputStream(), null);
			int eventType = parser.getEventType();
			boolean done = false;			

			TimeZoneModel mTimeZoneModel = null;
			
			
			while (eventType != XmlPullParser.END_DOCUMENT && !done) {
				String name = null;
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					

					break;
				case XmlPullParser.START_TAG:
					
					name = parser.getName();					
					
					
					if (name.equalsIgnoreCase("timezone")) {

						requiredTagFound = true;
						mTimeZoneModel = new TimeZoneModel();

					}else if (name.equalsIgnoreCase("gmtOffset")) {
						
						mTimeZoneModel.setOffset(parser.nextText());
						

					} 


					break;
					
				case XmlPullParser.END_TAG:
					
					name = parser.getName();

					if (name.equalsIgnoreCase("timezone") && requiredTagFound) {

						if(requiredTagFound){
							Message messageToParent = new Message();
							Bundle messageData = new Bundle();
							messageToParent.what = FOUND_RESULT;
							messageData.putSerializable("data", mTimeZoneModel);
							messageToParent.setData(messageData);
							// send message to mainThread
							parentHandler.sendMessage(messageToParent);							
						}else{
							Message messageToParent = new Message();
							Bundle messageData = new Bundle();
							messageToParent.what = NO_RESULT;
							messageData.putString("notfound", "notfound");
							messageToParent.setData(messageData);
							// send message to mainThread
							parentHandler.sendMessage(messageToParent);						
							done = true;
						}					

					} 
					break;
				}
				eventType = parser.next();
			}
		} catch (Exception e) {
			Message messageToParent = new Message();
			Bundle messageData = new Bundle();
			messageToParent.what = ERROR_WHILE_GETTING_RESULT;
			messageData.putString("connectionTimeOut","Internet connection gone");
			messageToParent.setData(messageData);
			// send message to mainThread
			parentHandler.sendMessage(messageToParent);
//			throw new RuntimeException(e);
		}
		// return citiesFeeds;

	}

}
