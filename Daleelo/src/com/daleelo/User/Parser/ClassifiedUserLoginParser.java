package com.daleelo.User.Parser;

import java.io.StringReader;

import org.xmlpull.v1.XmlPullParser;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;

import com.daleelo.User.Model.UserDetailsModel;
import com.daleelo.Utilities.HttpLoaderService;

public class ClassifiedUserLoginParser extends HttpLoaderService {
	private Handler parentHandler;
	private String result = "False";

	public ClassifiedUserLoginParser(String url, Handler parentHandler) {
		// TODO Auto-generated constructor stub
		super(url);
		this.parentHandler = parentHandler;

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
			messageData.putString("result", result);
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
			boolean isParentTagFound = false;
			while (eventType != XmlPullParser.END_DOCUMENT && !done) {

				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:

					String name = parser.getName();

					if (name.equalsIgnoreCase("Loginuser")) {

						isValidXmlBeforeEndDoc = true;
						
					}

					if (isValidXmlBeforeEndDoc) {
						
						if (name.equalsIgnoreCase("UserID")) {
														
							UserDetailsModel.UserID = (parser.nextText());			
							
							if(!UserDetailsModel.UserID.equalsIgnoreCase("")){
								
								isParentTagFound = true;
								
							}

						} else if (name.equalsIgnoreCase("UserName")) {

							UserDetailsModel.UserName = (parser.nextText());

						} else if (name.equalsIgnoreCase("NickName")) {

							UserDetailsModel.NickName = (parser.nextText());
							
						} else if (name.equalsIgnoreCase("Flag")) {

							result = parser.nextText();
							
						} 
						
					}
					break;
				case XmlPullParser.END_TAG:
					
					String tagName = parser.getName();
										
					if (tagName.equalsIgnoreCase("Loginuser")) {						
				
						if (isParentTagFound) {
	
							done = true;						
							returnError = "Login Successfully.";
							
						}else{
							
							done = true;
							returnError = "Invalid Username or Password";
							
							
						}
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