package com.daleelo.Business.Parser;

import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;

import com.daleelo.Business.Model.BusinessListModel;
import com.daleelo.Business.Model.ReviewModel;
import com.daleelo.Business.Model.SpecialOfferModel;
import com.daleelo.Utilities.BaseHttpLoader;

public class BusinessListParser extends BaseHttpLoader {

	private final int FOUND_RESULT = 0;
	private final int NO_RESULT = 1;
	private final int ERROR_WHILE_GETTING_RESULT = 2;

	private Handler parentHandler;
	boolean requiredTagFound = false;

	public BusinessListParser(String feedUrl, Handler handler) {
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
			
			
			ArrayList<BusinessListModel> mBusinessFeeds = null;		
			BusinessListModel mBusinessListModel = null;
			ArrayList <String> mPayOptions = null;
			

			while (eventType != XmlPullParser.END_DOCUMENT && !done) {
				String name = null;
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					

					break;
				case XmlPullParser.START_TAG:
					
					name = parser.getName();
					
					if (name.equalsIgnoreCase("ArrayOfBusinessDetail")) {

						mBusinessFeeds = new ArrayList<BusinessListModel>();				
						

					}else if (name.equalsIgnoreCase("BusinessDetail")) {

						requiredTagFound = true;
						mBusinessListModel = new BusinessListModel();					

					}else if (name.equalsIgnoreCase("BusinessId")) {

							mBusinessListModel.setBusinessId(parser.nextText());

							Log.e("LogCat", "BusinessId");

						} else if (name.equalsIgnoreCase("BusinessTitle")) {

							mBusinessListModel.setBusinessTitle(parser.nextText());

						} else if (name.equalsIgnoreCase("BusinessDescription")) {

							mBusinessListModel.setBusinessDescription(parser.nextText());

						} else if (name.equalsIgnoreCase("BusinessAddress")) {

							mBusinessListModel.setBusinessAddress(parser.nextText());

						} else if (name.equalsIgnoreCase("BusinessHours")) {

							mBusinessListModel.setBusinessHours(parser.nextText());

						}else if (name.equalsIgnoreCase("payOptions")) {
							
											mPayOptions = new ArrayList<String>();
											Log.e("LogCat", "payOptions");
				
										}else if (name.equalsIgnoreCase("PaymentOpt")) {
				
											mPayOptions.add(parser.nextText().toString());

						}else if (name.equalsIgnoreCase("BusinessImage")) {

							mBusinessListModel.setBusinessImage(parser.nextText());

						}else if (name.equalsIgnoreCase("BusinessIsactive")) {

							mBusinessListModel.setBusinessIsactive(parser.nextText());

						}else if (name.equalsIgnoreCase("BusinessIsfeatured")) {

							mBusinessListModel.setBusinessIsfeatured(parser.nextText());

						}else if (name.equalsIgnoreCase("BusinessRating")) {

							mBusinessListModel.setBusinessRating(parser.nextText());

						}else if (name.equalsIgnoreCase("AddressId")) {

							mBusinessListModel.setAddressId(parser.nextText());

						}else if (name.equalsIgnoreCase("AddressLine1")) {

							mBusinessListModel.setAddressLine1(parser.nextText());

						}else if (name.equalsIgnoreCase("AddressLine2")) {

							mBusinessListModel.setAddressLine2(parser.nextText());

						}else if (name.equalsIgnoreCase("CityName")) {

							mBusinessListModel.setCityName(parser.nextText());

						}else if (name.equalsIgnoreCase("StateCode")) {

							mBusinessListModel.setStateCode(parser.nextText());

						}else if (name.equalsIgnoreCase("AddressZipcode")) {

							mBusinessListModel.setAddressZipcode(parser.nextText());

						}else if (name.equalsIgnoreCase("AddressPhone")) {

							mBusinessListModel.setAddressPhone(parser.nextText());

						}else if (name.equalsIgnoreCase("AddressFax")) {

							mBusinessListModel.setAddressFax(parser.nextText());

						}else if (name.equalsIgnoreCase("AddressTollFree")) {

							mBusinessListModel.setAddressTollFree(parser.nextText());

						}else if (name.equalsIgnoreCase("AddressEmail")) {

							mBusinessListModel.setAddressEmail(parser.nextText());

						}else if (name.equalsIgnoreCase("AddressWeburl")) {

							mBusinessListModel.setAddressWeburl(parser.nextText());

						}else if (name.equalsIgnoreCase("AddressFBurl")) {

							mBusinessListModel.setAddressFBurl(parser.nextText());

						}else if (name.equalsIgnoreCase("AddressTWurl")) {

							mBusinessListModel.setAddressTWurl(parser.nextText());

						}else if (name.equalsIgnoreCase("AddressLat")) {

							mBusinessListModel.setAddressLat(parser.nextText());

						}else if (name.equalsIgnoreCase("AddressLong")) {

							mBusinessListModel.setAddressLong(parser.nextText());

						}else if (name.equalsIgnoreCase("CityID")) {

							mBusinessListModel.setCityID(parser.nextText());

						}else if (name.equalsIgnoreCase("BusinessValidFrom")) {

							mBusinessListModel.setBusinessValidFrom(parser.nextText());

						}else if (name.equalsIgnoreCase("BusinessvalidTo")) {

							mBusinessListModel.setBusinessvalidTo(parser.nextText());

						}else if (name.equalsIgnoreCase("CategoryId")) {

							mBusinessListModel.setCategoryId(parser.nextText());

						}else if (name.equalsIgnoreCase("MasterCategoryId")) {

							mBusinessListModel.setMasterCategoryId(parser.nextText());

						}else if (name.equalsIgnoreCase("RevierwsCount")) {

							mBusinessListModel.setRevierwsCount(parser.nextText());

						}else if (name.equalsIgnoreCase("Distance")) {

							mBusinessListModel.setDistance(parser.nextText());

						}else if (name.equalsIgnoreCase("ItemsCount")) {
							
							String mItemCnt = parser.nextText();
							
							if(Integer.parseInt(mItemCnt)<=0){
								
								Message messageToParent = new Message();
								Bundle messageData = new Bundle();
								messageToParent.what = NO_RESULT;
								messageData.putString("notfound", "notfound");
								messageToParent.setData(messageData);
								// send message to mainThread
								parentHandler.sendMessage(messageToParent);						
								done = true;
								
							}else{	
								
								mBusinessListModel.setItemsCount(mItemCnt);
								
							}

						}
					
					// 	Here sepcilaoffersinfo not used because we are getting deals in the 
					//  place of sepcilaoffersinfo dynamically in the detail description page.
					
					break;
					
				case XmlPullParser.END_TAG:
					
					name = parser.getName();

					if (name.equalsIgnoreCase("payOptions") && mPayOptions != null && mBusinessListModel != null) {

						mBusinessListModel.setmPayOptions(mPayOptions);
						mPayOptions = null;
						Log.e("LogCat", "END_TAG payOptions");

					}else if (name.equalsIgnoreCase("BusinessDetail") && mBusinessListModel != null && mBusinessFeeds != null) {

						mBusinessFeeds.add(mBusinessListModel);
						mBusinessListModel = null;
						Log.e("LogCat", "END_TAG BusinessDetail");

					}else if (name.equalsIgnoreCase("ArrayOfBusinessDetail") && requiredTagFound) {
						
						Log.e("LogCat", "END_TAG ArrayOfBusinessDetail");
						Message messageToParent = new Message();
						Bundle messageData = new Bundle();
						messageToParent.what = FOUND_RESULT;
						messageData.putSerializable("datafeeds", mBusinessFeeds);
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
