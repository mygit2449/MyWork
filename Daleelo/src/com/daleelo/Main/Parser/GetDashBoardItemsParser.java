package com.daleelo.Main.Parser;

import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;

import com.daleelo.DashBoardClassified.Model.GetClassfiedImageModel;
import com.daleelo.DashBoardClassified.Model.GetClassifiedItemsModel;
import com.daleelo.DashBoardEvents.Model.EventsCalenderModel;
import com.daleelo.Dashboard.Model.GetDealsInfoModel;
import com.daleelo.Dashboard.Model.GetSpotLightModel;
import com.daleelo.Main.Model.MainGetTodayDetailsModel;
import com.daleelo.Utilities.BaseHttpLoader;

public class GetDashBoardItemsParser extends BaseHttpLoader {

	private final int FOUND_RESULT = 0;
	private final int NO_RESULT = 1;
	private final int ERROR_WHILE_GETTING_RESULT = 2;
	private final int FINISH = 4;

	private Handler parentHandler;
	boolean requiredTagFound = false, tempSpotLight = false;
	

	public GetDashBoardItemsParser(String feedUrl, Handler handler) {
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

			MainGetTodayDetailsModel mMainGetTodayDetailsModel = null;

			GetSpotLightModel mMainGetSpotLightModel = null;
			GetDealsInfoModel mMainGetDealsInfoModel = null;
			EventsCalenderModel mMainEventsCalenderModel = null;
			GetClassifiedItemsModel mMainGetClassfiedItemsModel = null;
			GetClassfiedImageModel mClassifiedImage = null;
			
			ArrayList<GetSpotLightModel> mSpotLightData = null;
			ArrayList<GetDealsInfoModel> mDealData = null;
			ArrayList<EventsCalenderModel> mEventData = null;
			ArrayList<GetClassifiedItemsModel> mClassData = null;
			ArrayList<GetClassfiedImageModel> mClassifiedImagesFeeds = null;
			
			

			while (eventType != XmlPullParser.END_DOCUMENT && !done) {
				String name = null;
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					

					break;
				case XmlPullParser.START_TAG:
					
					name = parser.getName();
					
					if (name.equalsIgnoreCase("DashBoarditem")) {

						mMainGetTodayDetailsModel = new MainGetTodayDetailsModel();

					}else if (name.equalsIgnoreCase("Spotlightitem") && !tempSpotLight) {
						
						requiredTagFound = true;
						tempSpotLight = true;
						mSpotLightData = new ArrayList<GetSpotLightModel>();
						Log.e("LogCat", "Spotlightitem");

					}else if (name.equalsIgnoreCase("Spotlightitem")) {
						
						requiredTagFound = true;
						mMainGetSpotLightModel = new GetSpotLightModel();

					}else if (mMainGetSpotLightModel != null) { //  ********************GetSpotLightDetails 

						if (name.equalsIgnoreCase("SpotLightID")) {

							mMainGetSpotLightModel.setSpotLightID(parser.nextText());

							Log.e("LogCat", "SpotLightID");

						} else if (name.equalsIgnoreCase("SpotLightName")) {

							mMainGetSpotLightModel.setSpotLightName(parser.nextText());

						} else if (name.equalsIgnoreCase("Spotlightsubname")) {

							mMainGetSpotLightModel.setSpotlightsubname(parser.nextText());

						} else if (name.equalsIgnoreCase("Description")) {

							mMainGetSpotLightModel.setDescription(parser.nextText());

						} else if (name.equalsIgnoreCase("BusinessID")) {

							mMainGetSpotLightModel.setBusinessID(parser.nextText());

						} else if (name.equalsIgnoreCase("Type")) {

							mMainGetSpotLightModel.setType(parser.nextText());

						} else if (name.equalsIgnoreCase("CreatedDate")) {

							mMainGetSpotLightModel.setCreatedDate(parser.nextText());

						} else if (name.equalsIgnoreCase("BusinessTitle")) {

							mMainGetSpotLightModel.setBusinessTitle(parser.nextText());

						} else if (name.equalsIgnoreCase("BusinessDescription")) {

							mMainGetSpotLightModel.setBusinessDescription(parser.nextText());

						} else if (name.equalsIgnoreCase("BusinessAddress")) {

							mMainGetSpotLightModel.setBusinessAddress(parser.nextText());

						} else if (name.equalsIgnoreCase("AddressLine1")) {

							mMainGetSpotLightModel.setAddressLine1(parser.nextText());

						} else if (name.equalsIgnoreCase("AddressLine2")) {

							mMainGetSpotLightModel.setAddressLine2(parser.nextText());

						} else if (name.equalsIgnoreCase("CityName")) {

							mMainGetSpotLightModel.setCityName(parser.nextText());

						} else if (name.equalsIgnoreCase("StateCode")) {

							mMainGetSpotLightModel.setStateCode(parser.nextText());

						} else if (name.equalsIgnoreCase("AddressZipcode")) {

							mMainGetSpotLightModel.setAddressZipcode(parser.nextText());

						} else if (name.equalsIgnoreCase("AddressPhone")) {

							mMainGetSpotLightModel.setAddressPhone(parser.nextText());

						} else if (name.equalsIgnoreCase("AddressLat")) {

							mMainGetSpotLightModel.setAddressLat(parser.nextText());

						} else if (name.equalsIgnoreCase("AddressLong")) {

							mMainGetSpotLightModel.setAddressLong(parser.nextText());

						} else if (name.equalsIgnoreCase("ImageUrl")) {

							mMainGetSpotLightModel.setImageUrl(parser.nextText());

						} else if (name.equalsIgnoreCase("Distance")) {

							mMainGetSpotLightModel.setDistance(parser.nextText());

						} 
						
					}else if (name.equalsIgnoreCase("Dealsinfo")) {//  ********************GetDealsDetails 
						
						requiredTagFound = true;
						mDealData = new ArrayList<GetDealsInfoModel>();
						Log.e("LogCat", "Dealsinfo");

					} else if (name.equalsIgnoreCase("BusinessDeal")) {
						
						mMainGetDealsInfoModel = new GetDealsInfoModel();

					}else if (mMainGetDealsInfoModel != null) { 

						if (name.equalsIgnoreCase("BusinessId")) {

							mMainGetDealsInfoModel.setBusinessId(parser.nextText());

						} else if (name.equalsIgnoreCase("DealId")) {

							mMainGetDealsInfoModel.setDealId(parser.nextText());

						}else if (name.equalsIgnoreCase("DealTittle")) {

							mMainGetDealsInfoModel.setDealTittle(parser.nextText());

						}else if (name.equalsIgnoreCase("SubTittle")) {

							mMainGetDealsInfoModel.setSubTittle(parser.nextText());

						}else if (name.equalsIgnoreCase("Conditions")) {

							mMainGetDealsInfoModel.setConditions(parser.nextText());

						}else if (name.equalsIgnoreCase("DealInfo")) {

							mMainGetDealsInfoModel.setDealInfo(parser.nextText());

						}else if (name.equalsIgnoreCase("DealValidFrom")) {

							mMainGetDealsInfoModel.setDealValidFrom(parser.nextText());

						}else if (name.equalsIgnoreCase("DealValidTo")) {

							mMainGetDealsInfoModel.setDealValidTo(parser.nextText());

						}else if (name.equalsIgnoreCase("DealPrice")) {

							mMainGetDealsInfoModel.setDealPrice(parser.nextText());

						}else if (name.equalsIgnoreCase("DealOriginalprice")) {

							mMainGetDealsInfoModel.setDealOriginalprice(parser.nextText());

						}else if (name.equalsIgnoreCase("DealRating")) {

							mMainGetDealsInfoModel.setDealRating(parser.nextText());

						}else if (name.equalsIgnoreCase("DealIsFeatured")) {

							mMainGetDealsInfoModel.setDealIsFeatured(parser.nextText());

						}else if (name.equalsIgnoreCase("DealType")) {

							mMainGetDealsInfoModel.setDealType(parser.nextText());

						}else if (name.equalsIgnoreCase("DealImage")) {

							mMainGetDealsInfoModel.setDealImage(parser.nextText());

						}else if (name.equalsIgnoreCase("BusinessTitle")) {

							mMainGetDealsInfoModel.setBusinessTitle(parser.nextText());

						}else if (name.equalsIgnoreCase("BusinessDescription")) {

							mMainGetDealsInfoModel.setBusinessDescription(parser.nextText());

						}else if (name.equalsIgnoreCase("BusinessAddress")) {

							mMainGetDealsInfoModel.setBusinessAddress(parser.nextText());

						}else if (name.equalsIgnoreCase("AddressLine1")) {

							mMainGetDealsInfoModel.setAddressLine1(parser.nextText());

						}else if (name.equalsIgnoreCase("AddressLine2")) {

							mMainGetDealsInfoModel.setAddressLine2(parser.nextText());

						}else if (name.equalsIgnoreCase("CityName")) {

							mMainGetDealsInfoModel.setCityName(parser.nextText());

						}else if (name.equalsIgnoreCase("StateCode")) {

							mMainGetDealsInfoModel.setStateCode(parser.nextText());

						}else if (name.equalsIgnoreCase("AddressZipcode")) {

							mMainGetDealsInfoModel.setAddressZipcode(parser.nextText());

						}else if (name.equalsIgnoreCase("AddressPhone")) {

							mMainGetDealsInfoModel.setAddressPhone(parser.nextText());

						}else if (name.equalsIgnoreCase("AddressLat")) {

							mMainGetDealsInfoModel.setAddressLat(parser.nextText());

						}else if (name.equalsIgnoreCase("AddressLong")) {

							mMainGetDealsInfoModel.setAddressLong(parser.nextText());

						}else if (name.equalsIgnoreCase("MasterCategory")) {

							mMainGetDealsInfoModel.setMasterCategory(parser.nextText());

						}else if (name.equalsIgnoreCase("Distance")) {

							mMainGetDealsInfoModel.setDistance(parser.nextText());

						}
					}else if (name.equalsIgnoreCase("Eventsdetails")) {//  ********************GetEventsDetails 
						
						requiredTagFound = true;
						mEventData = new ArrayList<EventsCalenderModel>();
						Log.e("LogCat", "Eventsdetails");

					} else if (name.equalsIgnoreCase("Event")) {
						
						mMainEventsCalenderModel = new EventsCalenderModel();

					}else if (mMainEventsCalenderModel != null) { 

						if (name.equalsIgnoreCase("EventId")) {

							mMainEventsCalenderModel.setEventId(parser.nextText());

						} else if (name.equalsIgnoreCase("EventTitle")) {

							mMainEventsCalenderModel.setEventTitle(parser.nextText());

						}else if (name.equalsIgnoreCase("EventDetails")) {

							mMainEventsCalenderModel.setEventDetails(parser.nextText());

						}else if (name.equalsIgnoreCase("EventStartsOn")) {

							mMainEventsCalenderModel.setEventStartsOn(parser.nextText());

						}else if (name.equalsIgnoreCase("EventEndsOn")) {

							mMainEventsCalenderModel.setEventEndsOn(parser.nextText());

						}else if (name.equalsIgnoreCase("EventOrganizer")) {

							mMainEventsCalenderModel.setEventOrganizer(parser.nextText());

						}else if (name.equalsIgnoreCase("EventRating")) {

							mMainEventsCalenderModel.setEventRating(parser.nextText());

						}else if (name.equalsIgnoreCase("EventIsFeatured")) {

							mMainEventsCalenderModel.setEventIsFeatured(parser.nextText());

						}else if (name.equalsIgnoreCase("VenueLocation")) {

							mMainEventsCalenderModel.setVenueLocation(parser.nextText());

						}else if (name.equalsIgnoreCase("VenueCity")) {

							mMainEventsCalenderModel.setVenueCity(parser.nextText());

						}else if (name.equalsIgnoreCase("Fundrising")) {

							mMainEventsCalenderModel.setFundrising(parser.nextText());

						}else if (name.equalsIgnoreCase("EventType")) {

							mMainEventsCalenderModel.setEventType(parser.nextText());

						}else if (name.equalsIgnoreCase("VenueName")) {

							mMainEventsCalenderModel.setVenueName(parser.nextText());

						}else if (name.equalsIgnoreCase("Business_ID")) {

							mMainEventsCalenderModel.setBusinessID(parser.nextText());

						}else if (name.equalsIgnoreCase("BusinessTitle")) {

							mMainEventsCalenderModel.setBusinessTitle(parser.nextText());

						}else if (name.equalsIgnoreCase("BusinessDescription")) {

							mMainEventsCalenderModel.setBusinessDescription(parser.nextText());

						}else if (name.equalsIgnoreCase("BusinessAddress")) {

							mMainEventsCalenderModel.setBusinessAddress(parser.nextText());

						}else if (name.equalsIgnoreCase("AddressLine1")) {

							mMainEventsCalenderModel.setAddressLine1(parser.nextText());

						}else if (name.equalsIgnoreCase("AddressLine2")) {

							mMainEventsCalenderModel.setAddressLine2(parser.nextText());

						}else if (name.equalsIgnoreCase("CityName")) {

							mMainEventsCalenderModel.setCityName(parser.nextText());

						}else if (name.equalsIgnoreCase("Latitude")) {

							mMainEventsCalenderModel.setLatitude(parser.nextText());

						}else if (name.equalsIgnoreCase("Longitude")) {

							mMainEventsCalenderModel.setLongitude(parser.nextText());

						}else if (name.equalsIgnoreCase("StateCode")) {

							mMainEventsCalenderModel.setStateCode(parser.nextText());

						}else if (name.equalsIgnoreCase("Zipcode")) {

							mMainEventsCalenderModel.setZipcode(parser.nextText());

						}else if (name.equalsIgnoreCase("AddressPhone")) {

							mMainEventsCalenderModel.setAddressPhone(parser.nextText());

						}else if (name.equalsIgnoreCase("BusinessID")) {

							mMainEventsCalenderModel.setBusinessID(parser.nextText());

						}else if (name.equalsIgnoreCase("Distance")) {

							mMainEventsCalenderModel.setDistance(parser.nextText());

						}
					}else if (name.equalsIgnoreCase("Classfieditems")) { //  ********************GetClassifiedDetails 
						
						requiredTagFound = true;						
						mClassData = new ArrayList<GetClassifiedItemsModel>();
						Log.e("LogCat", "Classfieditems");

					} else if (name.equalsIgnoreCase("Classified")) {
						
						mMainGetClassfiedItemsModel = new GetClassifiedItemsModel();

					}else if (mMainGetClassfiedItemsModel != null) { 

						if (name.equalsIgnoreCase("ClassifiedId")) {

							mMainGetClassfiedItemsModel.setClassifiedId(parser.nextText());

						}else if (name.equalsIgnoreCase("ClassifiedTitle")) {

							mMainGetClassfiedItemsModel.setClassifiedTitle(parser.nextText());

						}else if (name.equalsIgnoreCase("ClassifiedInfo")) {

							mMainGetClassfiedItemsModel.setClassifiedInfo(parser.nextText());

						}else if (name.equalsIgnoreCase("ClassifiedValidFrom")) {

							mMainGetClassfiedItemsModel.setClassifiedValidFrom(parser.nextText());

						}else if (name.equalsIgnoreCase("ClassifiedValidTo")) {

							mMainGetClassfiedItemsModel.setClassifiedValidTo(parser.nextText());

						}else if (name.equalsIgnoreCase("ClassifiedIsactive")) {

							mMainGetClassfiedItemsModel.setClassifiedIsactive(parser.nextText());

						}else if (name.equalsIgnoreCase("ClassifiedOwner")) {

							mMainGetClassfiedItemsModel.setClassifiedOwner(parser.nextText());

						}else if (name.equalsIgnoreCase("ClassifiedCategory")) {

							mMainGetClassfiedItemsModel.setClassifiedCategory(parser.nextText());

						}else if (name.equalsIgnoreCase("Price")) {

							mMainGetClassfiedItemsModel.setPrice(parser.nextText());

						}else if (name.equalsIgnoreCase("Location")) {

							mMainGetClassfiedItemsModel.setLocation(parser.nextText());

						}else if (name.equalsIgnoreCase("PhoneNum")) {

							mMainGetClassfiedItemsModel.setPhoneNum(parser.nextText());

						}else if (name.equalsIgnoreCase("Email")) {

							mMainGetClassfiedItemsModel.setEmail(parser.nextText());

						}else if (name.equalsIgnoreCase("Images")) {

							mMainGetClassfiedItemsModel.setImages(parser.nextText());

						}else if (name.equalsIgnoreCase("Latitude")) {

							mMainGetClassfiedItemsModel.setLatitude(parser.nextText());

						}else if (name.equalsIgnoreCase("Longitude")) {

							mMainGetClassfiedItemsModel.setLongitude(parser.nextText());

						}else if (name.equalsIgnoreCase("CategoryName")) {

							mMainGetClassfiedItemsModel.setCategoryName(parser.nextText());

						}else if (name.equalsIgnoreCase("ClassifiedCreatedOn")) {

							mMainGetClassfiedItemsModel.setClassifiedCreatedOn(parser.nextText());

						}else if (name.equalsIgnoreCase("OBO")) {

							mMainGetClassfiedItemsModel.setOBO(parser.nextText());

						}else if (name.equalsIgnoreCase("Cityname")) {

							mMainGetClassfiedItemsModel.setCityname(parser.nextText());

						}else if (name.equalsIgnoreCase("ClassifiedSection")) {

							mMainGetClassfiedItemsModel.setClassifiedSection(parser.nextText());

						} else if (name.equalsIgnoreCase("Distance")) {

							mMainGetClassfiedItemsModel.setDistance(parser.nextText());

						} else if (name.equalsIgnoreCase("CSType")) {

							mMainGetClassfiedItemsModel.setCSType(parser.nextText());

						} else if (name.equals("Classfiedimagesinfo")) {

							mClassifiedImagesFeeds = new ArrayList<GetClassfiedImageModel>();
							
								} else if (name.equalsIgnoreCase("Classfiedimages")) {
		
									mClassifiedImage = new GetClassfiedImageModel();
		
										} else if (name.equalsIgnoreCase("CSID")) {
				
											mClassifiedImage.setCSID(parser.nextText());
				
										} else if (name.equals("ClassifiedID")) {
				
											mClassifiedImage.setClassifiedID(parser.nextText());
				
										} else if (name.equalsIgnoreCase("ImageUrl")) {
				
											mClassifiedImage.setImageUrl(parser.nextText());
				
										} 
						
					}

					
					break;
					
				case XmlPullParser.END_TAG:
					
					name = parser.getName();
					
					
					if (name.equalsIgnoreCase("Spotlightitem") && mSpotLightData != null && mMainGetSpotLightModel != null) {

						mSpotLightData.add(mMainGetSpotLightModel);
						mMainGetSpotLightModel = null;
						Log.e("LogCat", "END_TAG Spotlightitem");
					
					}else if (name.equalsIgnoreCase("BusinessDeal") && mDealData != null && mMainGetDealsInfoModel != null) {

						mDealData.add(mMainGetDealsInfoModel);
						mMainGetDealsInfoModel = null;
						Log.e("LogCat", "END_TAG BusinessDeal");

					}else if (name.equalsIgnoreCase("Dealsinfo") && mMainGetTodayDetailsModel != null && mDealData != null) {

						mMainGetTodayDetailsModel.setDealInfoModel(mDealData);
						mDealData = null;
						Log.e("LogCat", "END_TAG Dealsinfo");

						
						
					}else if (name.equalsIgnoreCase("Event") && mEventData != null && mMainEventsCalenderModel != null) {

						mEventData.add(mMainEventsCalenderModel);
						mMainEventsCalenderModel = null;
						Log.e("LogCat", "END_TAG Event");

					}else if (name.equalsIgnoreCase("Eventsdetails") && mMainGetTodayDetailsModel != null && mEventData != null) {

						mMainGetTodayDetailsModel.setEventsDetailsModel(mEventData);
						mEventData = null;
						Log.e("LogCat", "END_TAG Eventsdetails");
						
						
					}else if (name.equalsIgnoreCase("Classfiedimages")) {
						
						mClassifiedImagesFeeds.add(mClassifiedImage);
						mClassifiedImage = null;							
						
					}else if (name.equalsIgnoreCase("Classfiedimagesinfo")) {
						
						mMainGetClassfiedItemsModel.setClassifiedImages(mClassifiedImagesFeeds); 
						mClassifiedImagesFeeds = null;
						
					}else if (name.equalsIgnoreCase("Classified") && mClassData != null && mMainGetClassfiedItemsModel != null) {

						mClassData.add(mMainGetClassfiedItemsModel);
						mMainGetClassfiedItemsModel = null;
						Log.e("LogCat", "END_TAG Classified");

					}else if (name.equalsIgnoreCase("Classfieditems") && mMainGetTodayDetailsModel != null && mClassData != null) {

						mMainGetTodayDetailsModel.setClassfiedItemsModel(mClassData);
						mClassData = null;
						Log.e("LogCat", "END_TAG Classfieditems");

					}


					if (name.equalsIgnoreCase("DashBoarditem") && requiredTagFound) {
						
						mMainGetTodayDetailsModel.setSpotLightModel(mSpotLightData);
						mSpotLightData = null;
						Log.e("LogCat", "END_TAG Spotlightitem");

						Message messageToParent = new Message();
						Bundle messageData = new Bundle();
						messageToParent.what = FOUND_RESULT;
						messageData.putSerializable("datafeeds",	mMainGetTodayDetailsModel);
						messageToParent.setData(messageData);
						// send message to mainThread
						parentHandler.sendMessage(messageToParent);
						done = true;

					} else if (name.equalsIgnoreCase("ArrayOfDashBoarditem") && !requiredTagFound) {
						
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
		}
		// return citiesFeeds;

	}

}
