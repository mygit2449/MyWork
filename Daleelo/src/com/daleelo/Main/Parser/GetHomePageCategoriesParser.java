package com.daleelo.Main.Parser;

import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Xml;

import com.daleelo.Dashboard.Model.GetHomePageCategoriesModel;
import com.daleelo.Utilities.BaseHttpLoader;

public class GetHomePageCategoriesParser extends BaseHttpLoader {

	private final int FOUND_RESULT = 0;
	private final int NO_RESULT = 1;
	private final int ERROR_WHILE_GETTING_RESULT = 2;

	private Handler parentHandler;
	boolean requiredTagFound = false;

	public GetHomePageCategoriesParser(String feedUrl, Handler handler) {
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

			ArrayList<GetHomePageCategoriesModel> mCategoryFeeds = null;

			GetHomePageCategoriesModel mMainGetHomePageCategoriesModel = null;
			
			while (eventType != XmlPullParser.END_DOCUMENT && !done) {
				String name = null;
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					

					break;
				case XmlPullParser.START_TAG:
					
					name = parser.getName();
					
					if (name.equalsIgnoreCase("ArrayOfCategory")) {

						mCategoryFeeds = new ArrayList<GetHomePageCategoriesModel>();

					}else if (name.equalsIgnoreCase("category")) {
						
						requiredTagFound = true;
						mMainGetHomePageCategoriesModel  = new GetHomePageCategoriesModel();

					} else if (name.equalsIgnoreCase("CategoryId")) {
						
						mMainGetHomePageCategoriesModel.setCategoryId(parser.nextText());

					} else if (name.equalsIgnoreCase("CategoryName")) {
						
						mMainGetHomePageCategoriesModel.setCategoryName(parser.nextText());

					} else if (name.equalsIgnoreCase("Imageurl")) {
						
						mMainGetHomePageCategoriesModel.setImageurl(parser.nextText());

					}else if (name.equalsIgnoreCase("Imageurl1")) {
						
						mMainGetHomePageCategoriesModel.setImageurl1(parser.nextText());

					}else if (name.equalsIgnoreCase("Imageurl2")) {
						
						mMainGetHomePageCategoriesModel.setImageurl2(parser.nextText());

					}


					break;
					
				case XmlPullParser.END_TAG:
					
					name = parser.getName();

					if (name.equalsIgnoreCase("category") && mMainGetHomePageCategoriesModel != null) {

						mCategoryFeeds.add(mMainGetHomePageCategoriesModel);
						mMainGetHomePageCategoriesModel = null;

					} 

					if (name.equalsIgnoreCase("ArrayOfCategory") && requiredTagFound) {

						Message messageToParent = new Message();
						Bundle messageData = new Bundle();
						messageToParent.what = FOUND_RESULT;
						messageData.putSerializable("datafeeds", mCategoryFeeds);
						messageToParent.setData(messageData);
						// send message to mainThread
						parentHandler.sendMessage(messageToParent);
						done = true;
						mCategoryFeeds = null;

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
