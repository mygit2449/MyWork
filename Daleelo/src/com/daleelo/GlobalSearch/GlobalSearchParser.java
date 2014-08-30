package com.daleelo.GlobalSearch;

import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;

import com.daleelo.Masjid.Model.MosqueinfoModel;
import com.daleelo.Utilities.BaseHttpLoader;

public class GlobalSearchParser extends BaseHttpLoader {

	private final int FOUND_RESULT = 0;
	private final int NO_RESULT = 1;
	private final int ERROR_WHILE_GETTING_RESULT = 2;

	private Handler parentHandler;
	boolean requiredTagFound = false;

	public GlobalSearchParser(String feedUrl, Handler handler) {
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
			
			
			ArrayList<GlobalSearchModel> mSearchFeeds = null;		
			GlobalSearchModel mGlobalSearchModel = null;
			ArrayList<MosqueinfoModel> mMosqueinfoModelFeeds = null;
			MosqueinfoModel mMosqueinfoModel = null;
			

			while (eventType != XmlPullParser.END_DOCUMENT && !done) {
				String name = null;
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					

					break;
				case XmlPullParser.START_TAG:
					
					name = parser.getName();
					
					if (name.equalsIgnoreCase("ArrayOfGolobalsearch")) {

						mSearchFeeds = new ArrayList<GlobalSearchModel>();				
						

					}else if(name.equalsIgnoreCase("Golobalsearch")){
				    	
				    	requiredTagFound = true;							
						mGlobalSearchModel=new GlobalSearchModel();
						
				    }else if (name.equalsIgnoreCase("BusinessID")) {
						 
						mGlobalSearchModel.setBusinessID(parser.nextText());
						
					}else if(name.equalsIgnoreCase("BusinessTitle")) {
						
						mGlobalSearchModel.setBusinessTitle(parser.nextText());
						
					}else if (name.equalsIgnoreCase("Descinfo")) {
						
						mGlobalSearchModel.setDescinfo(parser.nextText());
						
					}else if (name.equalsIgnoreCase("OtherDescinfo")) {
						
						mGlobalSearchModel.setOtherDescinfo(parser.nextText());
						
					}else if (name.equalsIgnoreCase("AddressInfo")) {
						
						mGlobalSearchModel.setAddressInfo(parser.nextText());
						
					}else if (name.equalsIgnoreCase("CityName")) {
						
						mGlobalSearchModel.setCityName(parser.nextText());
						
					}else if (name.equalsIgnoreCase("StateCode")) {
						
						mGlobalSearchModel.setStateCode(parser.nextText());
						
					}else if (name.equalsIgnoreCase("Typeinfo")) {
						
						mGlobalSearchModel.setTypeinfo(parser.nextText());
						
					}else if (name.equalsIgnoreCase("Mosqueinfo")){
						
						mMosqueinfoModelFeeds = new ArrayList<MosqueinfoModel>();
					
						}else if(name.equalsIgnoreCase("Mosque")){
							
							mMosqueinfoModel=new MosqueinfoModel();
							
							}else if(mMosqueinfoModel!=null){
								
								if(name.equalsIgnoreCase("MosqueId")){
									
								mMosqueinfoModel.setMosqueId(parser.nextText());	
								
								}else if(name.equalsIgnoreCase("MosqueImam")){
									
									mMosqueinfoModel.setMosqueImam(parser.nextText());
									
								}else if (name.equalsIgnoreCase("MosqueDemographic")) {
									
									mMosqueinfoModel.setMosqueDemographic(parser.nextText());
		
								}else if (name.equalsIgnoreCase("MosqueDenomination")) {
									
									mMosqueinfoModel.setMosqueDenomination(parser.nextText());
		
								}else if (name.equalsIgnoreCase("MosqueLanguages")) {
									
									mMosqueinfoModel.setMosqueLanguages(parser.nextText());
		
								}else if (name.equalsIgnoreCase("PrayerType")) {
									
									mMosqueinfoModel.setPrayerType(parser.nextText());
		
								}else if (name.equalsIgnoreCase("MerchentId")) {
									
									mMosqueinfoModel.setMerchentId(parser.nextText());
		
								}
							}		 
		

					
					break;
					
				case XmlPullParser.END_TAG:
					
					name = parser.getName();

					if (name.equalsIgnoreCase("Mosque") && mMosqueinfoModel != null && mMosqueinfoModelFeeds != null) {
						
						mMosqueinfoModelFeeds.add(mMosqueinfoModel);
						mMosqueinfoModel = null;								

					}else if (name.equalsIgnoreCase("Mosqueinfo") && mMosqueinfoModelFeeds != null && mGlobalSearchModel != null) {

						mGlobalSearchModel.setMosqueDataFeeds(mMosqueinfoModelFeeds);
						mMosqueinfoModelFeeds = null;

					}else if (name.equalsIgnoreCase("Golobalsearch") && requiredTagFound) {

						mSearchFeeds.add(mGlobalSearchModel);
						mGlobalSearchModel= null;								

					}else if (name.equalsIgnoreCase("ArrayOfGolobalsearch") && requiredTagFound) {
						
						Log.e("LogCat", "END_TAG ArrayOfBusinessDetail");
						Message messageToParent = new Message();
						Bundle messageData = new Bundle();
						messageToParent.what = FOUND_RESULT;
						messageData.putSerializable("datafeeds", mSearchFeeds);
						messageToParent.setData(messageData);
						// send message to mainThread
						parentHandler.sendMessage(messageToParent);
						done = true;

					} else if (name.equalsIgnoreCase("ArrayOfGolobalsearch") && !requiredTagFound) {
						
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
