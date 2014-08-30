package com.daleelo.Dashboard.Parser;

import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;

import com.daleelo.Dashboard.Model.GetSpotLightModel;
import com.daleelo.Utilities.BaseHttpLoader;

public class SpotlightListParser extends BaseHttpLoader {

	private final int FOUND_RESULT = 0;
	private final int NO_RESULT = 1;
	private final int ERROR_WHILE_GETTING_RESULT = 2;

	private Handler parentHandler;
	boolean requiredTagFound = false;

	public SpotlightListParser(String feedUrl, Handler handler) {
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
			ArrayList<GetSpotLightModel> mSpotlightFeeds = null;

			GetSpotLightModel mGetSpotLightModel = null;
			

			while (eventType != XmlPullParser.END_DOCUMENT && !done) {
				String name = null;
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					

					break;
				case XmlPullParser.START_TAG:
					
					name = parser.getName();
					
					if (name.equalsIgnoreCase("ArrayOfSpotlightitem") ) {

						mSpotlightFeeds = new ArrayList<GetSpotLightModel>();

					}else if (name.equalsIgnoreCase("Spotlightitem")) {
						
						requiredTagFound = true;
						mGetSpotLightModel = new GetSpotLightModel();
						Log.e("LogCat", "mGetSpotLightModel");

					} 

					if (mGetSpotLightModel != null) { //  ********************GetSpotLightDetails 

						if (name.equalsIgnoreCase("SpotLightID")) {

							mGetSpotLightModel.setSpotLightID(parser.nextText());

							Log.e("LogCat", "SpotLightID");

						} else if (name.equalsIgnoreCase("SpotLightName")) {

							mGetSpotLightModel.setSpotLightName(parser.nextText());

						} else if (name.equalsIgnoreCase("Spotlightsubname")) {

							mGetSpotLightModel.setSpotlightsubname(parser.nextText());

						} else if (name.equalsIgnoreCase("Description")) {

							mGetSpotLightModel.setDescription(parser.nextText());

						} else if (name.equalsIgnoreCase("BusinessID")) {

							mGetSpotLightModel.setBusinessID(parser.nextText());

						} else if (name.equalsIgnoreCase("Type")) {

							mGetSpotLightModel.setType(parser.nextText());

						} else if (name.equalsIgnoreCase("CreatedDate")) {

							mGetSpotLightModel.setCreatedDate(parser.nextText());

						} else if (name.equalsIgnoreCase("BusinessTitle")) {

							mGetSpotLightModel.setBusinessTitle(parser.nextText());

						} else if (name.equalsIgnoreCase("BusinessDescription")) {

							mGetSpotLightModel.setBusinessDescription(parser.nextText());

						} else if (name.equalsIgnoreCase("BusinessAddress")) {

							mGetSpotLightModel.setBusinessAddress(parser.nextText());

						} else if (name.equalsIgnoreCase("AddressLine1")) {

							mGetSpotLightModel.setAddressLine1(parser.nextText());

						} else if (name.equalsIgnoreCase("AddressLine2")) {

							mGetSpotLightModel.setAddressLine2(parser.nextText());

						} else if (name.equalsIgnoreCase("CityName")) {

							mGetSpotLightModel.setCityName(parser.nextText());

						} else if (name.equalsIgnoreCase("StateCode")) {

							mGetSpotLightModel.setStateCode(parser.nextText());

						} else if (name.equalsIgnoreCase("AddressZipcode")) {

							mGetSpotLightModel.setAddressZipcode(parser.nextText());

						} else if (name.equalsIgnoreCase("AddressPhone")) {

							mGetSpotLightModel.setAddressPhone(parser.nextText());

						} else if (name.equalsIgnoreCase("AddressLat")) {

							mGetSpotLightModel.setAddressLat(parser.nextText());

						} else if (name.equalsIgnoreCase("AddressLong")) {

							mGetSpotLightModel.setAddressLong(parser.nextText());

						} else if (name.equalsIgnoreCase("ImageUrl")) {

							mGetSpotLightModel.setImageUrl(parser.nextText());

						} else if (name.equalsIgnoreCase("Distance")) {

							mGetSpotLightModel.setDistance(parser.nextText());

						} 
										
					}


					break;
					
				case XmlPullParser.END_TAG:
					
					name = parser.getName();

					if (name.equalsIgnoreCase("Spotlightitem") && mGetSpotLightModel != null && mSpotlightFeeds != null) {

						mSpotlightFeeds.add(mGetSpotLightModel);
						mGetSpotLightModel = null;

					} else if (name.equalsIgnoreCase("Spotlightitem") && mGetSpotLightModel != null && mSpotlightFeeds == null) {
						
//						This is use to get the single SpotLight item by its ID.

						Message messageToParent = new Message();
						Bundle messageData = new Bundle();
						messageToParent.what = FOUND_RESULT;
						messageData.putSerializable("datafeeds", mGetSpotLightModel);
						messageToParent.setData(messageData);
						// send message to mainThread
						parentHandler.sendMessage(messageToParent);
						done = true;

					} else	if (name.equalsIgnoreCase("ArrayOfSpotlightitem") && requiredTagFound) {

						Message messageToParent = new Message();
						Bundle messageData = new Bundle();
						messageToParent.what = FOUND_RESULT;
						messageData.putSerializable("datafeeds", mSpotlightFeeds);
						messageToParent.setData(messageData);
						// send message to mainThread
						parentHandler.sendMessage(messageToParent);
						done = true;

					} else if (name.equalsIgnoreCase("ArrayOfSpotlightitem") && !requiredTagFound) {
						
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
