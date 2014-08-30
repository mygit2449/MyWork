package com.daleelo.Business.Parser;

import java.io.StringReader;

import org.xmlpull.v1.XmlPullParser;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;

import com.daleelo.Utilities.HttpLoaderService;

public class WriteReviewParser extends HttpLoaderService {
	private Handler parentHandler;

	public WriteReviewParser(String url, Handler parentHandler) {
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
			boolean isParentTagFound = false;
			while (eventType != XmlPullParser.END_DOCUMENT && !done) {

				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:

					String name = parser.getName();

					if (name.equalsIgnoreCase("string")) {

						
						returnError = parser.nextText().toString();
						Log.e("", "returnError*********"+returnError);
						
						if(returnError.length()>0){
							
							isParentTagFound = true;
							
						}
						
					}

					
					break;
					
				case XmlPullParser.END_TAG:
					
					String tagName = parser.getName();
					
					if (isParentTagFound && tagName.equalsIgnoreCase("string")) {
						
						done = true;
										
					}else{
						
						returnError = "Invalid response from the Server";
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