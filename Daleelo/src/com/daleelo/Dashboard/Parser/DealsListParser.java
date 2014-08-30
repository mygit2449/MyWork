package com.daleelo.Dashboard.Parser;

import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;

import com.daleelo.Dashboard.Model.GetDealsInfoModel;
import com.daleelo.Dashboard.Model.GetDealsInfoModel;
import com.daleelo.Utilities.BaseHttpLoader;

public class DealsListParser extends BaseHttpLoader {

	private final int FOUND_RESULT = 0;
	private final int NO_RESULT = 1;
	private final int ERROR_WHILE_GETTING_RESULT = 2;

	private Handler parentHandler;
	boolean requiredTagFound = false;

	public DealsListParser(String feedUrl, Handler handler) {
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
			ArrayList<GetDealsInfoModel> mDealsFeeds = null;

			GetDealsInfoModel mGetDealsInfoModel = null;
			

			while (eventType != XmlPullParser.END_DOCUMENT && !done) {
				String name = null;
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					

					break;
				case XmlPullParser.START_TAG:
					
					name = parser.getName();
					
					if (name.equalsIgnoreCase("ArrayOfBusinessDeal")) {

						mDealsFeeds = new ArrayList<GetDealsInfoModel>();

					}else if (name.equalsIgnoreCase("BusinessDeal")) {
						
						requiredTagFound = true;
						mGetDealsInfoModel = new GetDealsInfoModel();
						Log.e("LogCat", "mGetDealsInfoModel");

					} 

					if (mGetDealsInfoModel != null) { //  ********************GetSpotLightDetails 

						if (name.equalsIgnoreCase("BusinessId")) {

							mGetDealsInfoModel.setBusinessId(parser.nextText());

						} else if (name.equalsIgnoreCase("DealId")) {

							mGetDealsInfoModel.setDealId(parser.nextText());

						}else if (name.equalsIgnoreCase("DealTittle")) {

							mGetDealsInfoModel.setDealTittle(parser.nextText());

						}else if (name.equalsIgnoreCase("SubTittle")) {

							mGetDealsInfoModel.setSubTittle(parser.nextText());

						}else if (name.equalsIgnoreCase("Conditions")) {

							mGetDealsInfoModel.setConditions(parser.nextText());

						}else if (name.equalsIgnoreCase("DealInfo")) {

							mGetDealsInfoModel.setDealInfo(parser.nextText());

						}else if (name.equalsIgnoreCase("DealValidFrom")) {

							mGetDealsInfoModel.setDealValidFrom(parser.nextText());

						}else if (name.equalsIgnoreCase("DealValidTo")) {

							mGetDealsInfoModel.setDealValidTo(parser.nextText());

						}else if (name.equalsIgnoreCase("DealPrice")) {

							mGetDealsInfoModel.setDealPrice(parser.nextText());

						}else if (name.equalsIgnoreCase("DealOriginalprice")) {

							mGetDealsInfoModel.setDealOriginalprice(parser.nextText());

						}else if (name.equalsIgnoreCase("DealRating")) {

							mGetDealsInfoModel.setDealRating(parser.nextText());

						}else if (name.equalsIgnoreCase("DealIsFeatured")) {

							mGetDealsInfoModel.setDealIsFeatured(parser.nextText());

						}else if (name.equalsIgnoreCase("DealType")) {

							mGetDealsInfoModel.setDealType(parser.nextText());

						}else if (name.equalsIgnoreCase("DealImage")) {

							mGetDealsInfoModel.setDealImage(parser.nextText());

						}else if (name.equalsIgnoreCase("BusinessTitle")) {

							mGetDealsInfoModel.setBusinessTitle(parser.nextText());

						}else if (name.equalsIgnoreCase("BusinessDescription")) {

							mGetDealsInfoModel.setBusinessDescription(parser.nextText());

						}else if (name.equalsIgnoreCase("BusinessAddress")) {

							mGetDealsInfoModel.setBusinessAddress(parser.nextText());

						}else if (name.equalsIgnoreCase("AddressLine1")) {

							mGetDealsInfoModel.setAddressLine1(parser.nextText());

						}else if (name.equalsIgnoreCase("AddressLine2")) {

							mGetDealsInfoModel.setAddressLine2(parser.nextText());

						}else if (name.equalsIgnoreCase("CityName")) {

							mGetDealsInfoModel.setCityName(parser.nextText());

						}else if (name.equalsIgnoreCase("StateCode")) {

							mGetDealsInfoModel.setStateCode(parser.nextText());

						}else if (name.equalsIgnoreCase("AddressZipcode")) {

							mGetDealsInfoModel.setAddressZipcode(parser.nextText());

						}else if (name.equalsIgnoreCase("AddressPhone")) {

							mGetDealsInfoModel.setAddressPhone(parser.nextText());

						}else if (name.equalsIgnoreCase("AddressLat")) {

							mGetDealsInfoModel.setAddressLat(parser.nextText());

						}else if (name.equalsIgnoreCase("AddressLong")) {

							mGetDealsInfoModel.setAddressLong(parser.nextText());

						}else if (name.equalsIgnoreCase("MasterCategory")) {

							mGetDealsInfoModel.setMasterCategory(parser.nextText());

						}else if (name.equalsIgnoreCase("Distance")) {

							mGetDealsInfoModel.setDistance(parser.nextText());

						}
										
					}


					break;
					
				case XmlPullParser.END_TAG:
					
					name = parser.getName();

					if (name.equalsIgnoreCase("BusinessDeal") && mGetDealsInfoModel != null) {

						mDealsFeeds.add(mGetDealsInfoModel);
						mGetDealsInfoModel = null;

					} 

					if (name.equalsIgnoreCase("ArrayOfBusinessDeal") && requiredTagFound) {

						Message messageToParent = new Message();
						Bundle messageData = new Bundle();
						messageToParent.what = FOUND_RESULT;
						messageData.putSerializable("datafeeds", mDealsFeeds);
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
