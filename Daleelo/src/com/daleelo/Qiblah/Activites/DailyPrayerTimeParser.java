package com.daleelo.Qiblah.Activites;

import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;

import com.daleelo.Utilities.BaseHttpLoader;

public class DailyPrayerTimeParser extends BaseHttpLoader{

	public  static final int FOUND_RESULT = 4;
	public  static final int NO_RESULT = 1;
	public  static final int ERROR_WHILE_GETTING_RESULT = 2;
	private ArrayList<String> prayerTimings;
	private Handler parentHandler;
	boolean requiredTagFound = false;

	public DailyPrayerTimeParser(String feedUrl, Handler handler) {
		super(feedUrl);
		Log.e("URL", feedUrl);
		parentHandler = handler;
		// TODO Auto-generated constructor stub
	}

	public void parser() {
		// TODO Auto-generated method stub

		XmlPullParser parser = Xml.newPullParser();
		try {

			parser.setInput(this.getInputStream(), null);
			int eventType = parser.getEventType();
			boolean done = false;			

			PrayerTimeModel mTimeModel = null;
			
			
			while (eventType != XmlPullParser.END_DOCUMENT && !done) {
				String name = null;
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					

					break;
				case XmlPullParser.START_TAG:
					
					name = parser.getName();					
					
					
					if (name.equalsIgnoreCase("prayer")) {

						requiredTagFound = true;
						mTimeModel = new PrayerTimeModel();
						prayerTimings = new ArrayList<String>();
					}else if (name.equalsIgnoreCase("fajr")) {
						prayerTimings.add(parser.nextText().trim()+" am");
//						mTimeModel.setFajr_time(parser.nextText()+" am");

					} else if (name.equalsIgnoreCase("sunrise")) {
						prayerTimings.add(parser.nextText().trim()+" am");
//						mTimeModel.setSunrise_time(parser.nextText()+ " am");

					} else if (name.equalsIgnoreCase("dhuhr")) {	
						prayerTimings.add(parser.nextText().trim()+" pm");
//						mTimeModel.setDhuhr_time(parser.nextText()+ " pm");

					} else if (name.equalsIgnoreCase("asr")) {
						prayerTimings.add(parser.nextText().trim()+" pm");
//						mTimeModel.setAsr_time(parser.nextText()+ " pm");

					}else if (name.equalsIgnoreCase("maghrib")) {
						prayerTimings.add(parser.nextText().trim()+" pm");
//						mTimeModel.setAsr_time(parser.nextText()+ " pm");

					} else if (name.equalsIgnoreCase("isha")) {
						prayerTimings.add(parser.nextText().trim()+" pm");
//						mTimeModel.setIsha_time(parser.nextText()+ " pm");

					}


					break;
					
				case XmlPullParser.END_TAG:
					
					name = parser.getName();

					if (name.equalsIgnoreCase("prayer") && requiredTagFound) {

						if(requiredTagFound){
							Message messageToParent = new Message();
							Bundle messageData = new Bundle();
							messageToParent.what = FOUND_RESULT;
							messageData.putSerializable("data", prayerTimings);
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
