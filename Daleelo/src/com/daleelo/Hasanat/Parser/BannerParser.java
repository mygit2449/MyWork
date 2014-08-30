package com.daleelo.Hasanat.Parser;

import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Xml;

import com.daleelo.Hasanat.Model.BannerModel;
import com.daleelo.Utilities.BaseHttpLoader;

public class BannerParser extends BaseHttpLoader {

	private final int FOUND_RESULT = 0;
	private final int NO_RESULT = 1;
	private final int ERROR_WHILE_GETTING_RESULT = 2;

	private Handler parentHandler;
	boolean requiredTagFound = false;

	public BannerParser(String feedUrl, Handler handler) {
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

			ArrayList<BannerModel> mBannerFeeds = null;
			BannerModel mBannerModel = null;
			
			while (eventType != XmlPullParser.END_DOCUMENT && !done) {
				String name = null;
				switch (eventType) {
				
				case XmlPullParser.START_DOCUMENT:
					
					mBannerFeeds = new ArrayList<BannerModel>();

					break;
					
				case XmlPullParser.START_TAG:
					
					name = parser.getName();
					
					if (name.equalsIgnoreCase("AddsInfo")) {
						
						requiredTagFound = true;
						mBannerModel  = new BannerModel();

					} else if(mBannerModel != null ){
						if (name.equalsIgnoreCase("BannerID")) {
							mBannerModel.setBannerID(parser.nextText());
						} else if (name.equalsIgnoreCase("BannerTittle")) {
							mBannerModel.setBannerTittle(parser.nextText());
						} else if (name.equalsIgnoreCase("BannerImageUrl")) {
							mBannerModel.setBannerImageUrl(parser.nextText());
						}else if (name.equalsIgnoreCase("CalanderImage")) {
							mBannerModel.setCalanderImage(parser.nextText());
						}else if (name.equalsIgnoreCase("ValidForm")) {
							mBannerModel.setValidForm(parser.nextText());
						}else if (name.equalsIgnoreCase("MinAmount")) {
							mBannerModel.setMinAmount(parser.nextText());
						}else if (name.equalsIgnoreCase("DonationUrl")) {
							mBannerModel.setDonationUrl(parser.nextText());
						}else if (name.equalsIgnoreCase("AddLocation")) {
							mBannerModel.setAddLocation(parser.nextText());
						}else if (name.equalsIgnoreCase("CustomerID")) {
							mBannerModel.setCustomerID(parser.nextText());
						}else if (name.equalsIgnoreCase("AuthorizenetPayment")) {
							mBannerModel.setAuthorizenetPayment(parser.nextText());
						}

					}
					break;
					
				case XmlPullParser.END_TAG:
					
					name = parser.getName();

					if (name.equalsIgnoreCase("AddsInfo") && mBannerModel != null) {

						mBannerFeeds.add(mBannerModel);
						mBannerModel = null;

					} else if (name.equalsIgnoreCase("ArrayOfAddsInfo") && requiredTagFound) {

						Message messageToParent = new Message();
						Bundle messageData = new Bundle();
						messageToParent.what = FOUND_RESULT;
						messageData.putSerializable("datafeeds", mBannerFeeds);
						messageToParent.setData(messageData);
						// send message to mainThread
						parentHandler.sendMessage(messageToParent);
						done = true;

					} else if (name.equalsIgnoreCase("ArrayOfAddsInfo") && !requiredTagFound) {
						
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
