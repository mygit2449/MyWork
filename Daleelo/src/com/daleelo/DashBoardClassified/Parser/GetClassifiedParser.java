package com.daleelo.DashBoardClassified.Parser;

import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;

import com.daleelo.DashBoardClassified.Model.GetClassifiedModel;
import com.daleelo.Utilities.BaseHttpLoader;

public class GetClassifiedParser extends BaseHttpLoader {

	private final int FOUND_RESULT = 0;
	private final int NO_RESULT = 1;
	private final int ERROR_WHILE_GETTING_RESULT = 2;

	private Handler parentHandler;
	boolean requiredTagFound = false;
	
	ArrayList<GetClassifiedModel> mClassifiedFeeds = null;

	public GetClassifiedParser(String feedUrl, Handler handler) {
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
			

			GetClassifiedModel mGetClassifiedModel = null;
			

			while (eventType != XmlPullParser.END_DOCUMENT && !done) {
				String name = null;
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					

					break;
				case XmlPullParser.START_TAG:
					
					name = parser.getName();
					
					if (name.equalsIgnoreCase("ArrayOfCategory")) {

						mClassifiedFeeds = new ArrayList<GetClassifiedModel>();

					}else if (name.equalsIgnoreCase("category")) {
						
						requiredTagFound = true;
						mGetClassifiedModel = new GetClassifiedModel();
						Log.e("LogCat", "mGetClassifiedModel");

					} 

					if (mGetClassifiedModel != null) { //  ********************GetSpotLightDetails 

						if (name.equalsIgnoreCase("CategoryId")) {

							mGetClassifiedModel.setCategoryId(parser.nextText());

						} else if (name.equalsIgnoreCase("CategoryName")) {

							mGetClassifiedModel.setCategoryName(parser.nextText());

						} else if (name.equalsIgnoreCase("Imageurl")) {

							mGetClassifiedModel.setImageurl(parser.nextText());

						} else if (name.equalsIgnoreCase("Imageurl1")) {

							mGetClassifiedModel.setImageurl1(parser.nextText());

						} else if (name.equalsIgnoreCase("Imageurl2")) {

							mGetClassifiedModel.setImageurl2(parser.nextText());

						} else if (name.equalsIgnoreCase("MatsterCategoryId")) {

							mGetClassifiedModel.setMatsterCategoryId(parser.nextText());

						} 
										
					}


					break;
					
				case XmlPullParser.END_TAG:
					
					name = parser.getName();

					if (name.equalsIgnoreCase("category") && mGetClassifiedModel != null) {

						mClassifiedFeeds.add(mGetClassifiedModel);
						mGetClassifiedModel = null;

					} 

					if (name.equalsIgnoreCase("ArrayOfCategory") && requiredTagFound) {
						sortData();
						Message messageToParent = new Message();
						Bundle messageData = new Bundle();
						messageToParent.what = FOUND_RESULT;
						messageData.putSerializable("datafeeds", mClassifiedFeeds);
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



	private void sortData(){
	
		GetClassifiedModel temp , tempPlus;
		
		for(int i = 0 ; i < mClassifiedFeeds.size() ; i++){
			
			for(int j = 0   ; j < mClassifiedFeeds.size()-1  ; j++){
				
				if(Integer.parseInt(mClassifiedFeeds.get(j).getCategoryId())  > Integer.parseInt(mClassifiedFeeds.get(j+1).getCategoryId())){
					
					Log.e("", "************ID "+mClassifiedFeeds.get(j).getCategoryId()+" :: "+mClassifiedFeeds.get(j+1).getCategoryId());
					
					temp = mClassifiedFeeds.get(j);
					tempPlus = mClassifiedFeeds.get(j+1);
					
					mClassifiedFeeds.remove(j);
					mClassifiedFeeds.add(j, tempPlus);
					mClassifiedFeeds.remove(j+1);
					mClassifiedFeeds.add(j+1, temp);
					
					temp = null;
					tempPlus =null;
					
					Log.e("", "************ID "+mClassifiedFeeds.get(j).getCategoryId()+" :: "+mClassifiedFeeds.get(j+1).getCategoryId());
					
				}
				
			}
			
		}
	
	}


}