package com.daleelo.Ads.Parser;

import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;

import com.daleelo.Ads.Model.StatesModel;
import com.daleelo.Utilities.BaseHttpLoader;

public class StatesFeedParser extends BaseHttpLoader{

	private final int FOUND_RESULT = 0;
	private final int NO_RESULT = 1;
	private final int ERROR_WHILE_GETTING_RESULT = 3;
	
	boolean resuiredTagFound = false;
	private Handler parentHandler;
	
	public  StatesFeedParser(String feedUrl, Handler handler) {
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

			
			ArrayList<StatesModel> mTotalStates = null;
			StatesModel mStatesModel = null;
			
			while (eventType != XmlPullParser.END_DOCUMENT && !done) {
				String name = null;
				
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					
					break;

				case XmlPullParser.START_TAG:
					
					name = parser.getName();
					if (name.equalsIgnoreCase("ArrayOfState")) {
						mTotalStates = new ArrayList<StatesModel>();
					}else if (name.equalsIgnoreCase("State")) {
						mStatesModel = new StatesModel();
					}else if (name.equalsIgnoreCase("StateID")) {
						resuiredTagFound = true;
						mStatesModel.setState_id(parser.nextText());
					}else if (name.equalsIgnoreCase("StateName")) {
						mStatesModel.setState_name(parser.nextText());
					}else if (name.equalsIgnoreCase("StateCode")) {
						mStatesModel.setState_code(parser.nextText());
					}else if (name.equalsIgnoreCase("CountryCode")) {
						mStatesModel.setCountry_code(parser.nextText());
					}
					
					break;
					
					
				case XmlPullParser.END_TAG:
					
					name = parser.getName();
					if (name.equalsIgnoreCase("State") && mStatesModel != null && mTotalStates != null) {
						mTotalStates.add(mStatesModel);
						mStatesModel = null;
//						Log.e("LogCat", "END_TAG States");

					}else if (name.equalsIgnoreCase("ArrayOfState") && resuiredTagFound) {
						Message messageToparent = new Message();
						Bundle messageData = new Bundle();
						messageToparent.what = FOUND_RESULT;
						messageData.putSerializable("statesFeeds", mTotalStates);
						messageToparent.setData(messageData);
						parentHandler.sendMessage(messageToparent);
						done = true;
					}else if (name.equalsIgnoreCase("ArrayOfState") && !resuiredTagFound) {
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
