package com.daleelo.Ads.Parser;

import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;

import com.daleelo.Ads.Model.SubCategoriesModel;
import com.daleelo.Utilities.BaseHttpLoader;

public class SubCategoriesParser extends BaseHttpLoader{

	private final int FOUND_SUB_CATEGORIES = 5;
	private final int NO_RESULT = 1;
	private final int ERROR_WHILE_GETTING_RESULT = 2;

	private Handler parentHandler;
	boolean requiredTagFound = false;

	public SubCategoriesParser(String feedUrl, Handler handler) {
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

			ArrayList<SubCategoriesModel> mSubCategoriesFeeds = null;

			SubCategoriesModel mSubCategoriesModel = null;
			
			while (eventType != XmlPullParser.END_DOCUMENT && !done) {
				String name = null;
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					

					break;
				case XmlPullParser.START_TAG:
					
					name = parser.getName();
					
					if (name.equalsIgnoreCase("ArrayOfCategory")) {

						mSubCategoriesFeeds = new ArrayList<SubCategoriesModel>();

					}else if (name.equalsIgnoreCase("category")) {
						
						requiredTagFound = true;
						mSubCategoriesModel  = new SubCategoriesModel();

					} else if (name.equalsIgnoreCase("CategoryId")) {
						
						mSubCategoriesModel.setSub_CategoryId(parser.nextText());

					} else if (name.equalsIgnoreCase("CategoryName")) {
						
						mSubCategoriesModel.setSub_CategoryName(parser.nextText());

   					} else if (name.equalsIgnoreCase("Imageurl")) {
						
						mSubCategoriesModel.setSub_Imageurl(parser.nextText());

					}else if (name.equalsIgnoreCase("Imageurl1")) {
						
						mSubCategoriesModel.setSub_Imageurl1(parser.nextText());

					}else if (name.equalsIgnoreCase("Imageurl2")) {
						
						mSubCategoriesModel.setSub_Imageurl2(parser.nextText());

					}else if (name.equalsIgnoreCase("MatsterCategoryId")) {
						
						mSubCategoriesModel.setSub_MatsterCategoryId(parser.nextText());

					}


					break;
					
				case XmlPullParser.END_TAG:
					
					name = parser.getName();

					if (name.equalsIgnoreCase("category") && mSubCategoriesModel != null) {

						mSubCategoriesFeeds.add(mSubCategoriesModel);
						mSubCategoriesModel = null;

					} 

					if (name.equalsIgnoreCase("ArrayOfCategory") && requiredTagFound) {

						Message messageToParent = new Message();
						Bundle messageData = new Bundle();
						messageToParent.what = FOUND_SUB_CATEGORIES;
						messageData.putSerializable("subcategoriesfeed", mSubCategoriesFeeds);
						messageToParent.setData(messageData);
						// send message to mainThread
						parentHandler.sendMessage(messageToParent);
						done = true;
						mSubCategoriesFeeds = null;

					} else if (name.equalsIgnoreCase("ArrayOfCategory") && !requiredTagFound) {
						
							Message messageToParent = new Message();
							Bundle messageData = new Bundle();
							messageToParent.what = NO_RESULT;
							messageData.putString("notfound", "notfound");
							Log.v("data not found ", "category not found");
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
