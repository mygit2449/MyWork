package com.daleelo.Business.Parser;

import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;

import com.daleelo.Business.Model.DealModel;
import com.daleelo.Utilities.BaseHttpLoader;

public class BusinessDealsParser extends BaseHttpLoader {

	private final int FOUND_RESULT = 0;
	private final int NO_RESULT = 1;
	private final int ERROR_WHILE_GETTING_RESULT = 2;

	private Handler parentHandler;
	boolean requiredTagFound = false;

	public BusinessDealsParser(String feedUrl, Handler handler) {
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
			
			
			ArrayList<DealModel> mAllDealModel = null;	
			DealModel mDealModel = null;
			

			while (eventType != XmlPullParser.END_DOCUMENT && !done) {
				String name = null;
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					

					break;
				case XmlPullParser.START_TAG:
					
					name = parser.getName();
					if (name.equalsIgnoreCase("ArrayOfBusinessDeal")){
						
						mAllDealModel= new ArrayList<DealModel>();
						
					}else if (name.equals("BusinessDeal")) {

							mDealModel = new DealModel();

						} else if (name.equalsIgnoreCase("BusinessId")) {
							
							requiredTagFound = true;
							mDealModel.setBusinessId(parser.nextText());

						} else if (name.equalsIgnoreCase("DealId")) {

							mDealModel.setDealId(parser.nextText());

						} else if (name.equalsIgnoreCase("DealTittle")) {

							mDealModel.setDealTittle(parser.nextText());

						} else if (name.equalsIgnoreCase("SubTittle")) {

							mDealModel.setSubTittle(parser.nextText());

						} else if (name.equalsIgnoreCase("Conditions")) {

							mDealModel.setConditions(parser.nextText());

						} else if (name.equalsIgnoreCase("DealInfo")) {

							mDealModel.setDealInfo(parser.nextText());

						} 	else if (name.equalsIgnoreCase("DealValidFrom")) {

							mDealModel.setDealValidFrom(parser.nextText());

						} 	else if (name.equalsIgnoreCase("DealValidTo")) {

							mDealModel.setDealValidTo(parser.nextText());

						} 	else if (name.equalsIgnoreCase("DealPrice")) {

							mDealModel.setDealPrice(parser.nextText());

						} 	else if (name.equalsIgnoreCase("DealOriginalprice")) {

							mDealModel.setDealOriginalprice(parser.nextText());

						} 	else if (name.equalsIgnoreCase("DealRating")) {

							mDealModel.setDealRating(parser.nextText());

						} 	else if (name.equalsIgnoreCase("DealIsFeatured")) {

							mDealModel.setDealIsFeatured(parser.nextText());

						} 	else if (name.equalsIgnoreCase("DealType")) {

							mDealModel.setDealType(parser.nextText());

						} 	else if (name.equalsIgnoreCase("DealImage")) {

							mDealModel.setDealImage(parser.nextText());

						} 	else if (name.equalsIgnoreCase("BusinessTitle")) {

							mDealModel.setBusinessTitle(parser.nextText());

						} 	else if (name.equalsIgnoreCase("BusinessDescription")) {

							mDealModel.setBusinessDescription(parser.nextText());

						} 	else if (name.equalsIgnoreCase("BusinessAddress")) {

							mDealModel.setBusinessAddress(parser.nextText());

						} 	else if (name.equalsIgnoreCase("AddressLine1")) {

							mDealModel.setAddressLine1(parser.nextText());

						} 	else if (name.equalsIgnoreCase("AddressLine2")) {

							mDealModel.setAddressLine2(parser.nextText());

						} 	else if (name.equalsIgnoreCase("CityName")) {

							mDealModel.setCityName(parser.nextText());

						} 	else if (name.equalsIgnoreCase("StateCode")) {

							mDealModel.setStateCode(parser.nextText());

						} 	else if (name.equalsIgnoreCase("AddressZipcode")) {

							mDealModel.setAddressZipcode(parser.nextText());

						} 	else if (name.equalsIgnoreCase("AddressPhone")) {

							mDealModel.setAddressPhone(parser.nextText());

						} 	else if (name.equalsIgnoreCase("AddressLat")) {

							mDealModel.setAddressLat(parser.nextText());

						} 	else if (name.equalsIgnoreCase("AddressLong")) {

							mDealModel.setAddressLong(parser.nextText());

						} 	else if (name.equalsIgnoreCase("MasterCategory")) {

							mDealModel.setMasterCategory(parser.nextText());

						} 	else if (name.equalsIgnoreCase("Distance")) {

							mDealModel.setDistance(parser.nextText());

						} 									


					break;
					
				case XmlPullParser.END_TAG:
					
					name = parser.getName();

					if (name.equals("BusinessDeal") && mDealModel != null && mAllDealModel != null) {

						mAllDealModel.add(mDealModel);
						mDealModel = null;

					}else if (name.equalsIgnoreCase("ArrayOfBusinessDeal") && requiredTagFound) {
						
						Log.e("LogCat", "END_TAG ArrayOfBusinessDetail");
						Message messageToParent = new Message();
						Bundle messageData = new Bundle();
						messageToParent.what = FOUND_RESULT;
						messageData.putSerializable("datafeeds", mAllDealModel);
						messageToParent.setData(messageData);
						// send message to mainThread
						parentHandler.sendMessage(messageToParent);
						done = true;

					} else if (name.equalsIgnoreCase("ArrayOfBusinessDeal") && !requiredTagFound) {
						
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
