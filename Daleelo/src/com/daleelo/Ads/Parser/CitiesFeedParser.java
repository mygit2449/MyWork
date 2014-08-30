package com.daleelo.Ads.Parser;

import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Xml;

import com.daleelo.Ads.Model.CitiesModel;
import com.daleelo.Utilities.BaseHttpLoader;

public class CitiesFeedParser extends BaseHttpLoader{

	private final int FOUND_CITIES = 4;
	private final int NO_RESULT = 1;
	private final int ERROR_WHILE_GETTING_RESULT = 2;
	
	boolean resuiredTagFound = false;
	private Handler parentHandler;
	
	public CitiesFeedParser(String feedUrl, Handler handler) {
		super(feedUrl);
		parentHandler = handler;
	}

	
public void parser(){
		
		XmlPullParser parser = Xml.newPullParser();
		
		try 
		{
			parser.setInput(this.getInputStream(), null);
			int eventType = parser.getEventType();
			boolean done = false;

			
			ArrayList<CitiesModel> mTotalCities = null;
			CitiesModel mCitiesModel = null;
			
			while (eventType != XmlPullParser.END_DOCUMENT && !done) {
				String name = null;
				
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					
					break;

				case XmlPullParser.START_TAG:
					
					name = parser.getName();
					if (name.equalsIgnoreCase("ArrayOfCity")) {
						mTotalCities = new ArrayList<CitiesModel>();
					}else if (name.equalsIgnoreCase("City")) {
						mCitiesModel = new CitiesModel();
					}else if (name.equalsIgnoreCase("CityID")) {
						resuiredTagFound = true;
						mCitiesModel.setCity_id(parser.nextText());
					}else if (name.equalsIgnoreCase("CityName")) {
						mCitiesModel.setCity_name(parser.nextText());
					}else if (name.equalsIgnoreCase("StateCode")) {
						mCitiesModel.setState_code(parser.nextText());
					}else if (name.equalsIgnoreCase("CountryCode")) {
						mCitiesModel.setCountry_code(parser.nextText());
					}else if (name.equalsIgnoreCase("Latitude")) {
						mCitiesModel.setLatitude(parser.nextText());
					}else if (name.equalsIgnoreCase("Longitude")) {
						mCitiesModel.setLongitude(parser.nextText());
					}
					
					break;
					
					
				case XmlPullParser.END_TAG:
					
					name = parser.getName();
					if (name.equalsIgnoreCase("City") && mCitiesModel != null && mTotalCities != null) {
						mTotalCities.add(mCitiesModel);
						mCitiesModel = null;
//						Log.e("LogCat", "END_TAG CITIES");

					}else if (name.equalsIgnoreCase("ArrayOfCity") && resuiredTagFound) {
						Message messageToparent = new Message();
						Bundle messageData = new Bundle();
						messageToparent.what = FOUND_CITIES;
						messageData.putSerializable("citiesFeeds", mTotalCities);
						messageToparent.setData(messageData);
						parentHandler.sendMessage(messageToparent);
						done = true;
					}else if (name.equalsIgnoreCase("ArrayOfCity") && !resuiredTagFound) {
						Message messageToparent = new Message();
						Bundle messageData = new Bundle();
						messageToparent.what = NO_RESULT;
						messageData.putString("no date", "no data");
						messageToparent.setData(messageData);
						parentHandler.sendMessage(messageToparent);
						done = true;
					}
					break;
				}
				eventType = parser.next();
			}

		} catch (Exception e) {
			Message messageToparent = new Message();
			Bundle messageData = new Bundle();
			messageToparent.what = ERROR_WHILE_GETTING_RESULT;
			messageData.putString("connectionTimeOut","Internet connection gone");
			messageToparent.setData(messageData);
			parentHandler.sendMessage(messageToparent);
		}
	}
}
