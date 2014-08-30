package com.daleelo.DashBoardClassified.Parser;

import java.io.StringReader;

import org.xmlpull.v1.XmlPullParser;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;

import com.daleelo.Utilities.HttpLoaderService;

public class PostClassifiedParser extends HttpLoaderService {
	private Handler parentHandler;
	private String result = "False", cId = "0";

	public PostClassifiedParser(String url, Handler parentHandler) {
		// TODO Auto-generated constructor stub
		super(url);
		this.parentHandler = parentHandler;

	}
	
	
//	http://www.daleelo.com/service/daleeloservice.asmx/AddClassifiedMobile?Classified_Title=Sampke&Classified_Info=Sff&Classified_Owner=sai&Classified_CreatedBy=54&Classified_Category=2&Price=109&Location=Guntur&PhoneNum=&Email=msb.mca%40gmail.com&Latitude=80.4365402&Longitude=16.3066525&PostFrom=Android&obo=true&cityname=Andhra+Pradesh&Classified_Section=136&PaymentResponse=sample&CSType=1

//	<ErrrMessage xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="http://tempuri.org/">
//	   <StatusFlag>true</StatusFlag>
//	   <StatusMessage>242</StatusMessage>
//	</ErrrMessage>


	
	public void handleHttpResponse() {

		Log.v("Network", "The response data " + httpResponseMessage.toString());
		
		
		if (errMsg.length() == 0) {

			errMsg = parseXml(httpResponseMessage);
		
			
		}

			Message messageToParent = new Message();
			Bundle messageData = new Bundle();
			messageToParent.what = 0;
			messageData.putString("httpError", errMsg);
			messageData.putString("result", result);
			messageData.putString("id", cId);
			messageToParent.setData(messageData);
			parentHandler.sendMessage(messageToParent);
		
		

	}

	public String parseXml(StringBuffer httpResponse) {
		XmlPullParser parser = Xml.newPullParser();

		String returnError = "";

		try {

			String xml = httpResponse.toString();
			parser.setInput(new StringReader(xml));

			int eventType = parser.getEventType();

			boolean done = false;
			boolean isValidXmlBeforeEndDoc = false;
			while (eventType != XmlPullParser.END_DOCUMENT && !done) {

				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:

					String name = parser.getName();

					if (name.equalsIgnoreCase("ErrrMessage")) {

						isValidXmlBeforeEndDoc = true;
						
					}

					if (isValidXmlBeforeEndDoc) {
						
						if (name.equalsIgnoreCase("StatusFlag")) {
														
							result = (parser.nextText());										
							
						} else if (name.equalsIgnoreCase("StatusMessage")) {
														
							cId = (parser.nextText());										
							
						}
					}
					
					break;
				case XmlPullParser.END_TAG:
					
					String tagName = parser.getName();
										
					if (tagName.equalsIgnoreCase("ErrrMessage")) {				
				
							done = true;
					}

					break;
			
				}

				if (eventType != XmlPullParser.END_DOCUMENT)
					eventType = parser.next();
			}
		} catch (Exception e) {

			returnError = "Invalid response from the Server";

		} finally {

			parser = null;
		}
		return returnError;
	}
}