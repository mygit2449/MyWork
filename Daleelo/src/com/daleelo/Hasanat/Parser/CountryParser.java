package com.daleelo.Hasanat.Parser;

import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Xml;

import com.daleelo.Hasanat.Model.CountryModel;
import com.daleelo.Utilities.BaseHttpLoader;

public class CountryParser extends BaseHttpLoader {

	private final int FOUND_RESULT = 0;
	private final int NO_RESULT = 1;
	private final int ERROR_WHILE_GETTING_RESULT = 2;

	private Handler parentHandler;
	boolean requiredTagFound = false;

	public CountryParser(String feedUrl, Handler handler) {
		super(feedUrl);
		parentHandler = handler;
	}

	public void parser() {
		// TODO Auto-generated method stub

		XmlPullParser parser = Xml.newPullParser();
		try {

			parser.setInput(this.getInputStream(), null);
			int eventType = parser.getEventType();
			boolean done = false;

			ArrayList<CountryModel> mCountryList = null;
			CountryModel mCountryModel = null;
			
			while (eventType != XmlPullParser.END_DOCUMENT && !done) {
				String name = null;
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					
					mCountryList = new ArrayList<CountryModel>();

					break;
				case XmlPullParser.START_TAG:
					
					name = parser.getName();
					
					if (name.equalsIgnoreCase("Country")) {
						
						requiredTagFound = true;
						mCountryModel  = new CountryModel();

					} else if(mCountryModel != null ){
						if (name.equalsIgnoreCase("CountryID")) {
							mCountryModel.setCountryId(parser.nextText());
						} else if (name.equalsIgnoreCase("CountryName")) {
							mCountryModel.setCountryName(parser.nextText());
						} else if (name.equalsIgnoreCase("CountryCode")) {
							mCountryModel.setCountryCode(parser.nextText());
						}else if (name.equalsIgnoreCase("IsActive")) {
							mCountryModel.setIsActive(parser.nextText());
						}else if (name.equalsIgnoreCase("CountryFlag")) {
							mCountryModel.setCountryFlag(parser.nextText());
						}else if (name.equalsIgnoreCase("InActiveSince")) {
							mCountryModel.setInActiveSince(parser.nextText());
						}else if (name.equalsIgnoreCase("CurrencyFlag")) {
							mCountryModel.setCurrencyFlag(parser.nextText());
						}else if (name.equalsIgnoreCase("CurrencyCode")) {
							mCountryModel.setCurrencyCode(parser.nextText());
						}

					}
					break;
					
				case XmlPullParser.END_TAG:
					
					name = parser.getName();

					if (name.equalsIgnoreCase("Country") && mCountryModel != null) {

						mCountryList.add(mCountryModel);
						mCountryModel = null;

					} else if (name.equalsIgnoreCase("ArrayOfCountry") && requiredTagFound) {

						Message messageToParent = new Message();
						Bundle messageData = new Bundle();
						messageToParent.what = FOUND_RESULT;
						messageData.putSerializable("datafeeds", mCountryList);
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
			throw new RuntimeException(e);
		}
		// return citiesFeeds;

	}

}
