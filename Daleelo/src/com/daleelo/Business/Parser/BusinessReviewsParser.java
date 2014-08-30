package com.daleelo.Business.Parser;

import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;

import com.daleelo.Business.Model.ReviewModel;
import com.daleelo.Utilities.BaseHttpLoader;

public class BusinessReviewsParser extends BaseHttpLoader {

	private final int FOUND_RESULT = 0;
	private final int NO_RESULT = 1;
	private final int ERROR_WHILE_GETTING_RESULT = 2;

	private Handler parentHandler;
	boolean requiredTagFound = false;

	public BusinessReviewsParser(String feedUrl, Handler handler) {
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
			
			
			ArrayList<ReviewModel> mAllReviewModel = null;	
			ReviewModel mReviewModel = null;
			

			while (eventType != XmlPullParser.END_DOCUMENT && !done) {
				String name = null;
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					

					break;
				case XmlPullParser.START_TAG:
					
					name = parser.getName();
					if (name.equalsIgnoreCase("ArrayOfReviewsinfo")){
						
						mAllReviewModel= new ArrayList<ReviewModel>();
						
					}else if (name.equals("Reviewsinfo")) {

							mReviewModel = new ReviewModel();

						} else if (name.equalsIgnoreCase("ReviewContent")) {
							
							requiredTagFound = true;
							mReviewModel.setReviewContent(parser.nextText());

						} else if (name.equalsIgnoreCase("ReviewRank")) {

							mReviewModel.setReviewRank(parser.nextText());

						} else if (name.equalsIgnoreCase("ReviewPostedBy")) {

							mReviewModel.setReviewPostedBy(parser.nextText());

						} else if (name.equalsIgnoreCase("ReviewPostedOn")) {

							mReviewModel.setReviewPostedOn(parser.nextText());

						} else if (name.equalsIgnoreCase("ReviewTittle")) {

							mReviewModel.setReviewTittle(parser.nextText());

						} 								


					break;
					
				case XmlPullParser.END_TAG:
					
					name = parser.getName();

					if (name.equals("Reviewsinfo") && mReviewModel != null && mAllReviewModel != null) {

						mAllReviewModel.add(mReviewModel);
						mReviewModel = null;
						Log.e("LogCat", "END_TAG Reviewsinfo");

					}else if (name.equalsIgnoreCase("ArrayOfReviewsinfo") && requiredTagFound) {
						
						Log.e("LogCat", "END_TAG ArrayOfBusinessDetail");
						Message messageToParent = new Message();
						Bundle messageData = new Bundle();
						messageToParent.what = FOUND_RESULT;
						messageData.putSerializable("datafeeds", mAllReviewModel);
						messageToParent.setData(messageData);
						// send message to mainThread
						parentHandler.sendMessage(messageToParent);
						done = true;

					} else if (name.equalsIgnoreCase("ArrayOfReviewsinfo") && !requiredTagFound) {
						
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
