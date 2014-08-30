package com.daleelo.Main.Parser;

import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;

import com.daleelo.Main.Model.GetCitiesModel;
import com.daleelo.Utilities.BaseHttpLoader;

public class GetCitiesParser extends BaseHttpLoader {

	private final int FOUND_RESULT = 0;
	private final int NO_RESULT = 1;
	private final int ERROR_WHILE_GETTING_RESULT = 2;
	private final int RESULT_FINISH = 3;

	private Handler parentHandler;
	boolean requiredTagFound = false;

	public GetCitiesParser(String feedUrl, Handler handler) {
		super(feedUrl);
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

			ArrayList<GetCitiesModel> mCategoryFeeds = null;

			GetCitiesModel mMainGetCitiesModel = null;
			
			int cnt = 1;
			
			while (eventType != XmlPullParser.END_DOCUMENT && !done) {
				String name = null;
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					

					break;
				case XmlPullParser.START_TAG:
					
					name = parser.getName();					
					
					
					if (name.equalsIgnoreCase("ArrayOfCity")) {

						mCategoryFeeds = new ArrayList<GetCitiesModel>();

					}else if (name.equalsIgnoreCase("City")) {
						
						requiredTagFound = true;
						mMainGetCitiesModel  = new GetCitiesModel();

					} else if (name.equalsIgnoreCase("CityID")) {
						
						mMainGetCitiesModel.setCityID(parser.nextText());

					} else if (name.equalsIgnoreCase("CityName")) {
						cnt++;
						String city = parser.nextText();
						Log.e("", "city found "+city+" "+cnt);						
						mMainGetCitiesModel.setCityName(city);

					} else if (name.equalsIgnoreCase("StateCode")) {
						
						mMainGetCitiesModel.setStateCode(parser.nextText());

					}else if (name.equalsIgnoreCase("Latitude")) {
						
						mMainGetCitiesModel.setLatitude(parser.nextText());

					} else if (name.equalsIgnoreCase("Longitude")) {
						
						mMainGetCitiesModel.setLongitude(parser.nextText());

					} else if( name.equalsIgnoreCase("CountryCode")){
						mMainGetCitiesModel.setCountry_code(parser.nextText());
					}


					break;
					
				case XmlPullParser.END_TAG:
					
					name = parser.getName();

					if (name.equalsIgnoreCase("City") && mMainGetCitiesModel != null) {

						mCategoryFeeds.add(mMainGetCitiesModel);
						
						
						Message messageToParent = new Message();
						Bundle messageData = new Bundle();
						messageToParent.what = FOUND_RESULT;
						messageData.putSerializable("datafeed", mMainGetCitiesModel);
						messageToParent.setData(messageData);
						// send message to mainThread
						parentHandler.sendMessage(messageToParent);
						
						mMainGetCitiesModel = null;

					} 

					if (name.equalsIgnoreCase("ArrayOfCity") && requiredTagFound) {

						Message messageToParent = new Message();
						Bundle messageData = new Bundle();
						messageToParent.what = RESULT_FINISH;
						messageData.putSerializable("datafeeds", mCategoryFeeds);
						messageToParent.setData(messageData);
						// send message to mainThread
						parentHandler.sendMessage(messageToParent);
						done = true;

					} else if (name.equalsIgnoreCase("ArrayOfCategory") && !requiredTagFound) {
						
							Message messageToParent = new Message();
							Bundle messageData = new Bundle();
							messageToParent.what = NO_RESULT;
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
