package com.daleelo.Dashboard.Parser;

import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;

import com.daleelo.Business.Model.ReviewModel;
import com.daleelo.Business.Model.SpecialOfferModel;
import com.daleelo.Dashboard.Model.BusinessDetailsByIdModel;
import com.daleelo.Utilities.BaseHttpLoader;

public class BusinessDetailsByIdParser extends BaseHttpLoader {
	 
	private final int FOUND_RESULT = 0;
	private final int NO_RESULT = 1;
	private final int ERROR_WHILE_GETTING_RESULT = 2;

	private Handler parentHandler;
	boolean requiredTagFound = false;

	public BusinessDetailsByIdParser(String feedUrl, Handler handler) {
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
			
			
			ArrayList<BusinessDetailsByIdModel> mBusinessFeeds = null;
			ArrayList<SpecialOfferModel> mAllSpecialOfferModel = null;	
			ArrayList<ReviewModel> mAllReviewModel = null;	
			
			
			BusinessDetailsByIdModel mBusinessDetailsByIdModel = null;
			ArrayList <String> mPayOptions = null;
			SpecialOfferModel mSpecialOfferModel = null;
			ReviewModel mReviewModel = null;
			

			while (eventType != XmlPullParser.END_DOCUMENT && !done) {
				String name = null;
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					

					break;
				case XmlPullParser.START_TAG:
					
					name = parser.getName();
					
					if (name.equalsIgnoreCase("ArrayOfBusinessDetail")) {

						mBusinessFeeds = new ArrayList<BusinessDetailsByIdModel>();				
						

					}else if (name.equalsIgnoreCase("BusinessDetail")) {

						requiredTagFound = true;
						mBusinessDetailsByIdModel = new BusinessDetailsByIdModel();					

					}else if (name.equalsIgnoreCase("BusinessId")) {

							mBusinessDetailsByIdModel.setBusinessId(parser.nextText());

							Log.e("LogCat", "BusinessId");

						} else if (name.equalsIgnoreCase("BusinessTitle")) {

							mBusinessDetailsByIdModel.setBusinessTitle(parser.nextText());

						} else if (name.equalsIgnoreCase("BusinessDescription")) {

							mBusinessDetailsByIdModel.setBusinessDescription(parser.nextText());

						} else if (name.equalsIgnoreCase("BusinessAddress")) {

							mBusinessDetailsByIdModel.setBusinessAddress(parser.nextText());

						} else if (name.equalsIgnoreCase("BusinessHours")) {

							mBusinessDetailsByIdModel.setBusinessHours(parser.nextText());

						}else if (name.equalsIgnoreCase("payOptions")) {
							
											mPayOptions = new ArrayList<String>();
											Log.e("LogCat", "payOptions");
				
										}else if (name.equalsIgnoreCase("PaymentOpt")) {
				
											mPayOptions.add(parser.nextText().toString());

						}else if (name.equalsIgnoreCase("BusinessImage")) {

							mBusinessDetailsByIdModel.setBusinessImage(parser.nextText());

						}else if (name.equalsIgnoreCase("BusinessIsactive")) {

							mBusinessDetailsByIdModel.setBusinessIsactive(parser.nextText());

						}else if (name.equalsIgnoreCase("BusinessIsfeatured")) {

							mBusinessDetailsByIdModel.setBusinessIsfeatured(parser.nextText());

						}else if (name.equalsIgnoreCase("BusinessRating")) {

							mBusinessDetailsByIdModel.setBusinessRating(parser.nextText());

						}else if (name.equalsIgnoreCase("AddressId")) {

							mBusinessDetailsByIdModel.setAddressId(parser.nextText());

						}else if (name.equalsIgnoreCase("AddressLine1")) {

							mBusinessDetailsByIdModel.setAddressLine1(parser.nextText());

						}else if (name.equalsIgnoreCase("AddressLine2")) {

							mBusinessDetailsByIdModel.setAddressLine2(parser.nextText());

						}else if (name.equalsIgnoreCase("CityName")) {

							mBusinessDetailsByIdModel.setCityName(parser.nextText());

						}else if (name.equalsIgnoreCase("StateCode")) {

							mBusinessDetailsByIdModel.setStateCode(parser.nextText());

						}else if (name.equalsIgnoreCase("AddressZipcode")) {

							mBusinessDetailsByIdModel.setAddressZipcode(parser.nextText());

						}else if (name.equalsIgnoreCase("AddressPhone")) {

							mBusinessDetailsByIdModel.setAddressPhone(parser.nextText());

						}else if (name.equalsIgnoreCase("AddressFax")) {

							mBusinessDetailsByIdModel.setAddressFax(parser.nextText());

						}else if (name.equalsIgnoreCase("AddressTollFree")) {

							mBusinessDetailsByIdModel.setAddressTollFree(parser.nextText());

						}else if (name.equalsIgnoreCase("AddressEmail")) {

							mBusinessDetailsByIdModel.setAddressEmail(parser.nextText());

						}else if (name.equalsIgnoreCase("AddressWeburl")) {

							mBusinessDetailsByIdModel.setAddressWeburl(parser.nextText());

						}else if (name.equalsIgnoreCase("AddressFBurl")) {

							mBusinessDetailsByIdModel.setAddressFBurl(parser.nextText());

						}else if (name.equalsIgnoreCase("AddressTWurl")) {

							mBusinessDetailsByIdModel.setAddressTWurl(parser.nextText());

						}else if (name.equalsIgnoreCase("AddressLat")) {

							mBusinessDetailsByIdModel.setAddressLat(parser.nextText());

						}else if (name.equalsIgnoreCase("AddressLong")) {

							mBusinessDetailsByIdModel.setAddressLong(parser.nextText());

						}else if (name.equalsIgnoreCase("CityID")) {

							mBusinessDetailsByIdModel.setCityID(parser.nextText());

						}else if (name.equalsIgnoreCase("BusinessValidFrom")) {

							mBusinessDetailsByIdModel.setBusinessValidFrom(parser.nextText());

						}else if (name.equalsIgnoreCase("BusinessvalidTo")) {

							mBusinessDetailsByIdModel.setBusinessvalidTo(parser.nextText());

						}else if (name.equalsIgnoreCase("CategoryId")) {

							mBusinessDetailsByIdModel.setCategoryId(parser.nextText());

						}else if (name.equalsIgnoreCase("Distance")) {

							mBusinessDetailsByIdModel.setDistance(parser.nextText());

						}


					break;
					
				case XmlPullParser.END_TAG:
					
					name = parser.getName();

					if (name.equalsIgnoreCase("payOptions") && mPayOptions != null && mBusinessDetailsByIdModel != null) {

						mBusinessDetailsByIdModel.setmPayOptions(mPayOptions);
						mPayOptions = null;
						Log.e("LogCat", "END_TAG payOptions");

					}else if (name.equalsIgnoreCase("ArrayOfBusinessDetail") && requiredTagFound) {
						
						Log.e("LogCat", "END_TAG ArrayOfBusinessDetail");
						Message messageToParent = new Message();
						Bundle messageData = new Bundle();
						messageToParent.what = FOUND_RESULT;
						messageData.putSerializable("datafeeds", mBusinessDetailsByIdModel);
						messageToParent.setData(messageData);
						// send message to mainThread
						parentHandler.sendMessage(messageToParent);
						done = true;

					} else if (name.equalsIgnoreCase("ArrayOfBusinessDetail") && !requiredTagFound) {
						
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
