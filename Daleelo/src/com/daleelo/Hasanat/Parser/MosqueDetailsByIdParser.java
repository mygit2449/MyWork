package com.daleelo.Hasanat.Parser;

import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Xml;

import com.daleelo.Hasanat.Model.BusinessDetailModel;
import com.daleelo.Hasanat.Model.MosqueModel;
import com.daleelo.Utilities.BaseHttpLoader;

public class MosqueDetailsByIdParser extends BaseHttpLoader {

	private final int FOUND_RESULT = 0;
	private final int NO_RESULT = 1;
	private final int ERROR_WHILE_GETTING_RESULT = 2;

	private Handler parentHandler;
	boolean requiredTagFound = false;

	public MosqueDetailsByIdParser(String feedUrl, Handler handler) {
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

			ArrayList<BusinessDetailModel> mBusinessList = null;
			ArrayList<String> payment_option = null;
			ArrayList<MosqueModel> moqueInfoList = null;
			MosqueModel mosqueModel = null;
			BusinessDetailModel mBusinessDetailModel = null;
			
			while (eventType != XmlPullParser.END_DOCUMENT && !done) {
				String name = null;
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					
					mBusinessList = new ArrayList<BusinessDetailModel>();

					break;
				case XmlPullParser.START_TAG:
					
					name = parser.getName();
					
					if (name.equalsIgnoreCase("BusinessDetail")) {
						
						
						mBusinessDetailModel  = new BusinessDetailModel();
						payment_option  = new ArrayList<String>();

					} else if(mBusinessDetailModel != null ){
						if (name.equalsIgnoreCase("BusinessId")) {
							requiredTagFound = true;
							mBusinessDetailModel.setBusinessId(parser.nextText());
						} else if (name.equalsIgnoreCase("BusinessTitle")) {
							mBusinessDetailModel.setBusinessTitle(parser.nextText());
						} else if (name.equalsIgnoreCase("BusinessDescription")) {
							mBusinessDetailModel.setBusinessDescription(parser.nextText());
						}else if (name.equalsIgnoreCase("BusinessAddress")) {
							mBusinessDetailModel.setBusinessAddress(parser.nextText());
						}else if (name.equalsIgnoreCase("BusinessHours")) {
							mBusinessDetailModel.setBusinessHours(parser.nextText());
						}else if (name.equalsIgnoreCase("PaymentOpt")) {
							payment_option.add(parser.nextText());
							mBusinessDetailModel.setPayOptions(payment_option);
						}else if (name.equalsIgnoreCase("BusinessImage")) {
							mBusinessDetailModel.setBusinessImage(parser.nextText());
						}else if (name.equalsIgnoreCase("BusinessIsactive")) {
							mBusinessDetailModel.setBusinessIsactive(parser.nextText());
						}else if (name.equalsIgnoreCase("BusinessIsfeatured")) {
							mBusinessDetailModel.setBusinessIsfeatured(parser.nextText());
						}else if (name.equalsIgnoreCase("BusinessRating")) {
							mBusinessDetailModel.setBusinessRating(parser.nextText());
						}else if (name.equalsIgnoreCase("AddressId")) {
							mBusinessDetailModel.setAddressId(parser.nextText());
						}else if (name.equalsIgnoreCase("AddressLine1")) {
							mBusinessDetailModel.setAddressLine1(parser.nextText());
						}else if (name.equalsIgnoreCase("AddressLine2")) {
							mBusinessDetailModel.setAddressLine2(parser.nextText());
						}else if (name.equalsIgnoreCase("CityName")) {
							mBusinessDetailModel.setCityName(parser.nextText());
						}else if (name.equalsIgnoreCase("StateCode")) {
							mBusinessDetailModel.setStateCode(parser.nextText());
						}else if (name.equalsIgnoreCase("AddressZipcode")) {
							mBusinessDetailModel.setAddressZipcode(parser.nextText());
						}else if (name.equalsIgnoreCase("AddressPhone")) {
							mBusinessDetailModel.setAddressPhone(parser.nextText());
						}else if (name.equalsIgnoreCase("AddressFax")) {
							mBusinessDetailModel.setAddressFax(parser.nextText());
						}else if (name.equalsIgnoreCase("AddressTollFree")) {
							mBusinessDetailModel.setAddressTollFree(parser.nextText());
						}else if (name.equalsIgnoreCase("AddressEmail")) {
							mBusinessDetailModel.setAddressEmail(parser.nextText());
						}else if (name.equalsIgnoreCase("AddressWeburl")) {
							mBusinessDetailModel.setAddressWeburl(parser.nextText());
						}else if (name.equalsIgnoreCase("AddressFBurl")) {
							mBusinessDetailModel.setAddressFBurl(parser.nextText());
						}else if (name.equalsIgnoreCase("AddressTWurl")) {
							mBusinessDetailModel.setAddressTWurl(parser.nextText());
						}else if (name.equalsIgnoreCase("AddressLat")) {
							mBusinessDetailModel.setAddressLat(parser.nextText());
						}else if (name.equalsIgnoreCase("AddressLong")) {
							mBusinessDetailModel.setAddressLong(parser.nextText());
						}else if (name.equalsIgnoreCase("CityID")) {
							mBusinessDetailModel.setCityID(parser.nextText());
						}else if (name.equalsIgnoreCase("BusinessValidFrom")) {
							mBusinessDetailModel.setBusinessValidFrom(parser.nextText());
						}else if (name.equalsIgnoreCase("BusinessvalidTo")) {
							mBusinessDetailModel.setBusinessvalidTo(parser.nextText());
						}else if (name.equalsIgnoreCase("CategoryId")) {
							mBusinessDetailModel.setCategoryId(parser.nextText());
						}else if (name.equalsIgnoreCase("Distance")) {
							mBusinessDetailModel.setDistance(parser.nextText());
						}else if (name.equalsIgnoreCase("Eventsdetails")) {
							mBusinessDetailModel.setEventsdetails(parser.nextText());
						}else if (name.equalsIgnoreCase("Mosqueinfo")) {
							moqueInfoList = new ArrayList<MosqueModel>();
						}else if (name.equalsIgnoreCase("Mosque")) {
							mosqueModel = new MosqueModel();
						}else if (name.equalsIgnoreCase("MosqueId")) {
							mosqueModel.setMosqueId(parser.nextText());
						}else if (name.equalsIgnoreCase("MosqueImam")) {
							mosqueModel.setMosqueImam(parser.nextText());
						}else if (name.equalsIgnoreCase("MosqueDemographic")) {
							mosqueModel.setMosqueDemographic(parser.nextText());
						}else if (name.equalsIgnoreCase("MosqueDenomination")) {
							mosqueModel.setMosqueDenomination(parser.nextText());
						}else if (name.equalsIgnoreCase("MosqueLanguages")) {
							mosqueModel.setMosqueLanguages(parser.nextText());
						}else if (name.equalsIgnoreCase("PrayerType")) {
							mosqueModel.setPrayerType(parser.nextText());
						}else if (name.equalsIgnoreCase("MerchentId")) {
							mosqueModel.setMerchentId(parser.nextText());
						}

					}
					break;
					
				case XmlPullParser.END_TAG:
					
					name = parser.getName();

					 if(name.equalsIgnoreCase("Mosqueinfo")){
						mBusinessDetailModel.setMosqueList(moqueInfoList);
						
					} else if(name.equalsIgnoreCase("Mosque") && mosqueModel != null){
						moqueInfoList.add(mosqueModel);
						mosqueModel = null;
						
					} else if (name.equalsIgnoreCase("BusinessDetail") && requiredTagFound) {						
						
						Message messageToParent = new Message();
						Bundle messageData = new Bundle();
						messageToParent.what = FOUND_RESULT;
						messageData.putSerializable("datafeeds", mBusinessDetailModel);
						messageToParent.setData(messageData);
						// send message to mainThread
						parentHandler.sendMessage(messageToParent);
						done = true;

					} else if (name.equalsIgnoreCase("BusinessDetail") && !requiredTagFound) {
						
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
