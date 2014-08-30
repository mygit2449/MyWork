package com.daleelo.Business.Parser;

import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Xml;

import com.daleelo.Business.Model.BusinessMoreCategoriesModel;
import com.daleelo.Utilities.BaseHttpLoader;

public class BusinessMoreCatParser extends BaseHttpLoader {

	private final int FOUND_RESULT = 0;
	private final int NO_RESULT = 1;
	private final int ERROR_WHILE_GETTING_RESULT = 2;

	private Handler parentHandler;
	boolean requiredTagFound = false;

	public BusinessMoreCatParser(String feedUrl, Handler handler) {
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
			
			
			ArrayList<BusinessMoreCategoriesModel> mBusinessFeeds = null;			
			BusinessMoreCategoriesModel mBusinessMoreCategoriesModel = null;
			

			while (eventType != XmlPullParser.END_DOCUMENT && !done) {
				String name = null;
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					

					break;
				case XmlPullParser.START_TAG:
					
					name = parser.getName();
					
					if (name.equalsIgnoreCase("ArrayOfCategory")) {

						mBusinessFeeds = new ArrayList<BusinessMoreCategoriesModel>();				
						

					}else if (name.equalsIgnoreCase("category")) {

						requiredTagFound = true;
						mBusinessMoreCategoriesModel = new BusinessMoreCategoriesModel();					

					}else if (name.equalsIgnoreCase("CategoryId")) {

							mBusinessMoreCategoriesModel.setCategoryId(parser.nextText());						

						} else if (name.equalsIgnoreCase("CategoryName")) {

							mBusinessMoreCategoriesModel.setCategoryName(parser.nextText());

						} else if (name.equalsIgnoreCase("Imageurl")) {
							
							mBusinessMoreCategoriesModel.setImageurl(parser.nextText());
							
						}else if (name.equalsIgnoreCase("Imageurl1")) {
							
							mBusinessMoreCategoriesModel.setImageurl1(parser.nextText());

						}else if (name.equalsIgnoreCase("Imageurl2")) {
							
							mBusinessMoreCategoriesModel.setImageurl2(parser.nextText());

						}else if (name.equalsIgnoreCase("MatsterCategoryId")) {
							
							mBusinessMoreCategoriesModel.setMatsterCategoryId(parser.nextText());

						}


					break;
					
				case XmlPullParser.END_TAG:
					
					name = parser.getName();

					if (name.equalsIgnoreCase("category") && mBusinessMoreCategoriesModel != null && mBusinessFeeds != null) {

						mBusinessFeeds.add(mBusinessMoreCategoriesModel);
						mBusinessMoreCategoriesModel = null;

					}else if (name.equalsIgnoreCase("ArrayOfCategory") && requiredTagFound) {
						
						Message messageToParent = new Message();
						Bundle messageData = new Bundle();
						messageToParent.what = FOUND_RESULT;
						messageData.putSerializable("datafeeds", mBusinessFeeds);
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
