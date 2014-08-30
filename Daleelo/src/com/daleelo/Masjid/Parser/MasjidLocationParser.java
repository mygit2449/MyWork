package com.daleelo.Masjid.Parser;

import java.io.StringReader;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;

import com.daleelo.Masjid.Model.MasjidModel;
import com.daleelo.Masjid.Model.MosqueinfoModel;
import com.daleelo.Utilities.HttpLoaderService;

public class MasjidLocationParser extends HttpLoaderService {
	
	private Handler parentHandler;
	ArrayList<MasjidModel> MasjidDataFeeds =null;

	public MasjidLocationParser(String url, Handler parentHandler, ArrayList<MasjidModel> _arrMyList)  {
		// TODO Auto-generated constructor stub
		super(url);
		this.parentHandler = parentHandler;
		MasjidDataFeeds = _arrMyList;
		

	}

	
	public void handleHttpResponse() {

		Log.v("Network", "The response data " + httpResponseMessage.toString());
		
		
		if (errMsg.length() == 0) {

			errMsg = parseXml(httpResponseMessage);
		
			
		}

			Message messageToParent = new Message();
			Bundle messageData = new Bundle();
			messageToParent.what = 0;
			messageData.putString("httpError", errMsg);
			messageToParent.setData(messageData);
			parentHandler.sendMessage(messageToParent);
		
		

	}

	public String parseXml(StringBuffer httpResponse) 
	{

		if(errMsg.toString().length() == 0){
		
			XmlPullParser parser=Xml.newPullParser();
			try 
			{
				String xml = httpResponse.toString();
				parser.setInput(new StringReader(xml));
				Log.e("net","connected");
				int eventType=parser.getEventType();			
				
				MasjidModel MasjidFeeds=null;
				MosqueinfoModel mMosqueinfoModel=null;				
				ArrayList<MosqueinfoModel> mMosqueinfoModelFeeds = null;
				ArrayList <String> mPayOptions = null;
				
				boolean done = false;
				boolean isParentTagFound = false;
				
				while(eventType!=XmlPullParser.END_DOCUMENT && !done){
					
					String name=null;
					switch (eventType) {
					
						case XmlPullParser.START_DOCUMENT:					
							
							
							break;
						case XmlPullParser.START_TAG:
							
							name=parser.getName();
							
							if(name.equalsIgnoreCase("BusinessDetail")){
						    	
						    	isParentTagFound = true;							
								MasjidFeeds=new MasjidModel();
								
						    }else if (name.equalsIgnoreCase("BusinessId")) {
								 
								MasjidFeeds.setBusinessId(parser.nextText());
								
							}else if(name.equalsIgnoreCase("BusinessTitle")) {
								
								MasjidFeeds.setBusinessTitle(parser.nextText());
								
							}else if (name.equalsIgnoreCase("BusinessDescription")) {
								
								MasjidFeeds.setBusinessDescription(parser.nextText());
								
							}else if (name.equalsIgnoreCase("BusinessAddress")) {
								
								MasjidFeeds.setBusinessAddress(parser.nextText());
								
							}else if (name.equalsIgnoreCase("BusinessHours")) {
								
								MasjidFeeds.setBusinessHours(parser.nextText());
								
							}else if (name.equalsIgnoreCase("PaymentOptionsinfo")) {
								
										mPayOptions = new ArrayList<String>();
			
									}else if (name.equalsIgnoreCase("PaymentOpt")) {
			
										mPayOptions.add(parser.nextText().toString());
			
							}else if (name.equalsIgnoreCase("BusinessImage")) {
								
								MasjidFeeds.setBusinessImage(parser.nextText());
								
							}else if (name.equalsIgnoreCase("BusinessIsactive")) {
								
								MasjidFeeds.setBusinessIsactive(parser.nextText());
								
							}else if (name.equalsIgnoreCase("BusinessIsfeatured")) {
								
								MasjidFeeds.setBusinessIsfeatured(parser.nextText());
								
							}else if (name.equalsIgnoreCase("BusinessRating")) {
								
								MasjidFeeds.setBusinessRating(parser.nextText());
								
							}else if (name.equalsIgnoreCase("AddressId")) {
								
								MasjidFeeds.setAddressId(parser.nextText());
								
							}else if (name.equalsIgnoreCase("AddressLine1")) {
								
								MasjidFeeds.setAddressLine1(parser.nextText());
			
							}else if (name.equalsIgnoreCase("AddressLine2")) {
								
								MasjidFeeds.setAddressLine2(parser.nextText());
								
							}else if (name.equalsIgnoreCase("CityName")) {
								
								MasjidFeeds.setCityName(parser.nextText());
								
							}else if (name.equalsIgnoreCase("StateCode")) {
								
								MasjidFeeds.setStateCode(parser.nextText());
								
							}else if (name.equalsIgnoreCase("AddressZipcode")) {
								
								MasjidFeeds.setAddressZipcode(parser.nextText());
								
							}else if (name.equalsIgnoreCase("AddressPhone")) {
								
								MasjidFeeds.setAddressPhone(parser.nextText());
								
							}else if (name.equalsIgnoreCase("AddressFax")) {
								
								MasjidFeeds.setAddressFax(parser.nextText());
			
							}else if (name.equalsIgnoreCase("AddressTollFree")) {
								
								MasjidFeeds.setAddressTollFree(parser.nextText());
			
							}else if (name.equalsIgnoreCase("AddressEmail")) {
								
								MasjidFeeds.setAddressEmail(parser.nextText());
								
							}else if (name.equalsIgnoreCase("AddressWeburl")) {
								
								MasjidFeeds.setAddressWeburl(parser.nextText());
								
							}else if (name.equalsIgnoreCase("AddressFBurl")) {
								
								MasjidFeeds.setAddressFBurl(parser.nextText());
								
							}else if (name.equalsIgnoreCase("AddressTWurl")) {
								
								MasjidFeeds.setAddressTWurl(parser.nextText());
								
							}else if (name.equalsIgnoreCase("AddressLat")){
								
							     MasjidFeeds.setAddressLat(parser.nextText());
			
							}else if (name.equalsIgnoreCase("AddressLong")) {
								
								MasjidFeeds.setAddressLong(parser.nextText());
								
							}else if (name.equalsIgnoreCase("CityID")) {
								
								MasjidFeeds.setCityID(parser.nextText());
								
							}else if (name.equalsIgnoreCase("BusinessValidFrom")) {
								
								MasjidFeeds.setBusinessValidFrom(parser.nextText());
								
							}else if (name.equalsIgnoreCase("BusinessvalidTo")) {
								
								MasjidFeeds.setBusinessvalidTo(parser.nextText());
								
							}else if (name.equalsIgnoreCase("CategoryId")) {
								
								MasjidFeeds.setCategoryId(parser.nextText());
			
							}else if (name.equalsIgnoreCase("Distance")) {
								
								MasjidFeeds.setDistance(parser.nextText());
								
							}else if (name.equalsIgnoreCase("Mosqueinfo")){
								
								mMosqueinfoModelFeeds = new ArrayList<MosqueinfoModel>();
							
								}else if(name.equalsIgnoreCase("Mosque")){
									
									mMosqueinfoModel=new MosqueinfoModel();
									
									}else if(mMosqueinfoModel!=null){
										
										if(name.equalsIgnoreCase("MosqueId")){
											
										mMosqueinfoModel.setMosqueId(parser.nextText());	
										
										}else if(name.equalsIgnoreCase("MosqueImam")){
											
											mMosqueinfoModel.setMosqueImam(parser.nextText());
											
										}else if (name.equalsIgnoreCase("MosqueDemographic")) {
											
											mMosqueinfoModel.setMosqueDemographic(parser.nextText());
				
										}else if (name.equalsIgnoreCase("MosqueDenomination")) {
											
											mMosqueinfoModel.setMosqueDenomination(parser.nextText());
				
										}else if (name.equalsIgnoreCase("MosqueLanguages")) {
											
											mMosqueinfoModel.setMosqueLanguages(parser.nextText());
				
										}else if (name.equalsIgnoreCase("PrayerType")) {
											
											mMosqueinfoModel.setPrayerType(parser.nextText());
				
										}else if (name.equalsIgnoreCase("MerchentId")) {
											
											mMosqueinfoModel.setMerchentId(parser.nextText());
				
										}
									}
							
							else if (name.equalsIgnoreCase("Eventsdetails")){
								
								MasjidFeeds.setEventsdetails(parser.nextText());
							}			 
				
						break;
			           case XmlPullParser.END_TAG:
			        	   
							name=parser.getName();
							Log.e("LogCat", "END_TAG");
						
							if (name.equalsIgnoreCase("payOptions") && mPayOptions != null && MasjidFeeds != null) {

								MasjidFeeds.setPayOptions(mPayOptions);
								mPayOptions = null;

							}else if (name.equalsIgnoreCase("Mosque") && mMosqueinfoModel != null && mMosqueinfoModelFeeds != null) {

								
								mMosqueinfoModelFeeds.add(mMosqueinfoModel);
								mMosqueinfoModel = null;								

							}else if (name.equalsIgnoreCase("Mosqueinfo") && mMosqueinfoModelFeeds != null && MasjidFeeds != null) {

								MasjidFeeds.setMosqueDataFeeds(mMosqueinfoModelFeeds);
								mMosqueinfoModelFeeds = null;

							}else if (name.equalsIgnoreCase("BusinessDetail") && isParentTagFound) {

								MasjidDataFeeds.add(MasjidFeeds);
								MasjidFeeds= null;								

							}else if (name.equalsIgnoreCase("ArrayOfBusinessDetail")) {

								if (isParentTagFound) {
									
									done = true;						
									
								}else{
									
									done = true;
									errMsg = "Masjid data not found";
									
									
								}

							}
							
			
						break;
					}
					eventType = parser.next();
				}
			} 
			catch (Exception e)
			{
				
//				errMsg="Invalid response from the Server";
				errMsg = "Server is not responding or the network timedout. Please try again later.";
		        throw new RuntimeException(e);
				
			}
		}
		
		return errMsg;
	}	
}
