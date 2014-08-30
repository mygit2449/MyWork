package com.daleelo.Hasanat.Parser;

import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;

import com.daleelo.Hasanat.Model.CategoryModel;
import com.daleelo.Utilities.BaseHttpLoader;

public class HasanatCategoryParser extends BaseHttpLoader {

	private final int FOUND_RESULT = 0;
	private final int NO_RESULT = 1;
	private final int ERROR_WHILE_GETTING_RESULT = 2;

	private Handler parentHandler;
	private boolean TagFound = false;

	public HasanatCategoryParser(String feedUrl, Handler handler) {
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

			ArrayList<CategoryModel> mCategoryList = null;
			CategoryModel mcCategoryModel = null;
			
			while (eventType != XmlPullParser.END_DOCUMENT && !done) {
				String name = null;
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					
					mCategoryList = new ArrayList<CategoryModel>();

					break;
				case XmlPullParser.START_TAG:
					
					name = parser.getName();
					
					if (name.equalsIgnoreCase("category")) {
						
						TagFound = true;
						mcCategoryModel  = new CategoryModel();

					} else if(mcCategoryModel != null ){
						if (name.equalsIgnoreCase("CategoryId")) {
							mcCategoryModel.setCategoryId(parser.nextText());
						} else if (name.equalsIgnoreCase("CategoryName")) {
							mcCategoryModel.setCategoryName(parser.nextText());
						} else if (name.equalsIgnoreCase("MatsterCategoryId")) {
							mcCategoryModel.setCategoryMasterId(parser.nextText());
						}else if (name.equalsIgnoreCase("Imageurl")) {
							mcCategoryModel.setImgUrl(parser.nextText());
						}

					}
					break;
					
				case XmlPullParser.END_TAG:
					
					name = parser.getName();
					

					if (name.equalsIgnoreCase("category") && mcCategoryModel != null) {

						mCategoryList.add(mcCategoryModel);
						mcCategoryModel = null;					
						
					} else if (name.equalsIgnoreCase("ArrayOfCategory") && TagFound) {

						Log.v("required tag found result",""+TagFound);
						Message messageToParent = new Message();
						Bundle messageData = new Bundle();
						messageToParent.what = FOUND_RESULT;
						messageData.putSerializable("datafeeds", mCategoryList);
						messageToParent.setData(messageData);
						// send message to mainThread
						parentHandler.sendMessage(messageToParent);
						done = true;

					}else if (name.equalsIgnoreCase("ArrayOfCategory") && !TagFound) {
						Log.v("required tag  not found",""+TagFound);
						
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
