package com.daleelo.Qiblah.Activites;

import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;

import com.daleelo.Utilities.BaseHttpLoader;

public class PrayerTimeParser extends BaseHttpLoader{

	public  static final int FOUND_RESULT = 3;
	public  static final int NO_RESULT = 1;
	public  static final int ERROR_WHILE_GETTING_RESULT = 2;
	private ArrayList<PrayerTimeModel> monthlyPrayerTimings;
	private Handler parentHandler;
	boolean requiredTagFound = false;

	public PrayerTimeParser(String feedUrl, Handler handler) {
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
						monthlyPrayerTimings = new ArrayList<PrayerTimeModel>();
						requiredTagFound = true;
					}else if(name.equalsIgnoreCase("date")){
						mTimeModel = new PrayerTimeModel();		
						mTimeModel.setDate(Integer.parseInt(parser.getAttributeValue(0)));
						mTimeModel.setMonth(Integer.parseInt(parser.getAttributeValue(1)));
						mTimeModel.setYear(Integer.parseInt(parser.getAttributeValue(2)));
						mTimeModel.prayerTimings = new ArrayList<String>();
					}else if (name.equalsIgnoreCase("fajr")) {
						mTimeModel.prayerTimings.add(parser.nextText().trim()+" am");
					} else if (name.equalsIgnoreCase("sunrise")) {
						mTimeModel.prayerTimings.add(parser.nextText().trim()+" am");

					} else if (name.equalsIgnoreCase("dhuhr")) {	
						mTimeModel.prayerTimings.add(parser.nextText().trim()+" pm");

					} else if (name.equalsIgnoreCase("asr")) {
						mTimeModel.prayerTimings.add(parser.nextText().trim()+" pm");

					}else if (name.equalsIgnoreCase("maghrib")) {
						mTimeModel.prayerTimings.add(parser.nextText().trim()+" pm");

					} else if (name.equalsIgnoreCase("isha")) {
						mTimeModel.prayerTimings.add(parser.nextText().trim()+" pm");

					}


					break;
					
				case XmlPullParser.END_TAG:
					
					name = parser.getName();
					if (name.equalsIgnoreCase("date")){
						monthlyPrayerTimings.add(mTimeModel);
						mTimeModel = null;
					}else if (name.equalsIgnoreCase("prayer") && requiredTagFound) {

						if(requiredTagFound){
							Message messageToParent = new Message();
							Bundle messageData = new Bundle();
							messageToParent.what = FOUND_RESULT;
							messageData.putSerializable("data", monthlyPrayerTimings);
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
			e.printStackTrace();
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
