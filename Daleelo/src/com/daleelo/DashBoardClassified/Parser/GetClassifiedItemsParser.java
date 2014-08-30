package com.daleelo.DashBoardClassified.Parser;

import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;

import com.daleelo.DashBoardClassified.Model.GetClassfiedImageModel;
import com.daleelo.DashBoardClassified.Model.GetClassifiedItemsModel;
import com.daleelo.Utilities.BaseHttpLoader;

public class GetClassifiedItemsParser extends BaseHttpLoader {

	private final int FOUND_RESULT = 0;
	private final int NO_RESULT = 1;
	private final int ERROR_WHILE_GETTING_RESULT = 2;

	private Handler parentHandler;
	boolean requiredTagFound = false;

	public GetClassifiedItemsParser(String feedUrl, Handler handler) {
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
			
			ArrayList<GetClassifiedItemsModel> mClassifiedFeeds = null;
			GetClassifiedItemsModel mGetClassifiedByIdModel = null;
			
			ArrayList<GetClassfiedImageModel> mClassifiedImagesFeeds = null;
			GetClassfiedImageModel mClassifiedImage = null;
			

			while (eventType != XmlPullParser.END_DOCUMENT && !done) {
				String name = null;
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					

					break;
				case XmlPullParser.START_TAG:
					
					name = parser.getName();
					
					if (name.equalsIgnoreCase("ArrayOfClassified")) {

						mClassifiedFeeds = new ArrayList<GetClassifiedItemsModel>();

					}else if (name.equalsIgnoreCase("Classified")) {
						
						requiredTagFound = true;
						mGetClassifiedByIdModel = new GetClassifiedItemsModel();
						Log.e("LogCat", "mGetClassifiedByIdModel");

					} 

					if (mGetClassifiedByIdModel != null) { //  ********************GetSpotLightDetails 

						if (name.equals("ClassifiedId")) {

							mGetClassifiedByIdModel.setClassifiedId(parser.nextText());

						} else if (name.equalsIgnoreCase("ClassifiedTitle")) {

							mGetClassifiedByIdModel.setClassifiedTitle(parser.nextText());

						} else if (name.equalsIgnoreCase("ClassifiedInfo")) {

							mGetClassifiedByIdModel.setClassifiedInfo(parser.nextText());

						} else if (name.equalsIgnoreCase("ClassifiedValidFrom")) {

							mGetClassifiedByIdModel.setClassifiedValidFrom(parser.nextText());

						} else if (name.equalsIgnoreCase("ClassifiedValidTo")) {

							mGetClassifiedByIdModel.setClassifiedValidTo(parser.nextText());

						} else if (name.equalsIgnoreCase("ClassifiedIsactive")) {

							mGetClassifiedByIdModel.setClassifiedIsactive(parser.nextText());

						} else if (name.equalsIgnoreCase("ClassifiedOwner")) {

							mGetClassifiedByIdModel.setClassifiedOwner(parser.nextText());

						} else if (name.equalsIgnoreCase("ClassifiedCategory")) {

							mGetClassifiedByIdModel.setClassifiedCategory(parser.nextText());

						} else if (name.equalsIgnoreCase("Price")) {

							mGetClassifiedByIdModel.setPrice(parser.nextText());

						} else if (name.equalsIgnoreCase("Location")) {

							mGetClassifiedByIdModel.setLocation(parser.nextText());

						} else if (name.equalsIgnoreCase("PhoneNum")) {

							mGetClassifiedByIdModel.setPhoneNum(parser.nextText());

						} else if (name.equalsIgnoreCase("Email")) {

							mGetClassifiedByIdModel.setEmail(parser.nextText());

						} else if (name.equalsIgnoreCase("Images")) {

							mGetClassifiedByIdModel.setImages(parser.nextText());

						} else if (name.equalsIgnoreCase("Latitude")) {

							mGetClassifiedByIdModel.setLatitude(parser.nextText());

						} else if (name.equalsIgnoreCase("Longitude")) {

							mGetClassifiedByIdModel.setLongitude(parser.nextText());

						} else if (name.equalsIgnoreCase("CategoryName")) {

							mGetClassifiedByIdModel.setCategoryName(parser.nextText());

						} else if (name.equalsIgnoreCase("ClassifiedCreatedOn")) {

							mGetClassifiedByIdModel.setClassifiedCreatedOn(parser.nextText());

						} else if (name.equalsIgnoreCase("OBO")) {

							mGetClassifiedByIdModel.setOBO(parser.nextText());

						} else if (name.equalsIgnoreCase("Cityname")) {

							mGetClassifiedByIdModel.setCityname(parser.nextText());

						} else if (name.equalsIgnoreCase("ClassifiedSection")) {

							mGetClassifiedByIdModel.setClassifiedSection(parser.nextText());

						} else if (name.equalsIgnoreCase("Distance")) {

							mGetClassifiedByIdModel.setDistance(parser.nextText());

						} else if (name.equalsIgnoreCase("CSType")) {

							mGetClassifiedByIdModel.setCSType(parser.nextText());

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

					if (name.equalsIgnoreCase("Classified") && mGetClassifiedByIdModel != null && mClassifiedFeeds != null) {

						mClassifiedFeeds.add(mGetClassifiedByIdModel);
						mGetClassifiedByIdModel = null;

					} else if (name.equalsIgnoreCase("Classified") && mGetClassifiedByIdModel != null && mClassifiedFeeds == null) {

						Log.e("Daleelo", "for MyStuff");
						Message messageToParent = new Message();
						Bundle messageData = new Bundle();
						messageToParent.what = FOUND_RESULT;
						messageData.putSerializable("datafeeds", mGetClassifiedByIdModel);
						messageToParent.setData(messageData);
						// send message to mainThread
						parentHandler.sendMessage(messageToParent);
						done = true;

					} else if (name.equalsIgnoreCase("Classfiedimages") && mClassifiedImage != null) {

						mClassifiedImagesFeeds.add(mClassifiedImage);
						mClassifiedImage = null;

					} else if (name.equalsIgnoreCase("Classfiedimagesinfo") && mGetClassifiedByIdModel != null) {

						mGetClassifiedByIdModel.setClassifiedImages(mClassifiedImagesFeeds);
						mClassifiedImagesFeeds = null;						

					} else	if (name.equalsIgnoreCase("ArrayOfClassified") && requiredTagFound) {

						Message messageToParent = new Message();
						Bundle messageData = new Bundle();
						messageToParent.what = FOUND_RESULT;
						messageData.putSerializable("datafeeds", mClassifiedFeeds);
						messageToParent.setData(messageData);
						// send message to mainThread
						parentHandler.sendMessage(messageToParent);
						done = true;

					} else if (name.equalsIgnoreCase("ArrayOfClassified") && !requiredTagFound) {
						
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
