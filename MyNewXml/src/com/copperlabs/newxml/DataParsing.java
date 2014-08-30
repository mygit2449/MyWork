package com.copperlabs.newxml;

import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;

public class DataParsing extends NewXmlBaseFeed{
	private Handler parentHandler;
	private boolean	requiredTagFound=false;
	private int 			TEMP_COUNTER=1;
	private int	  			ITEM_PER_PAGE = 15;
  public DataParsing(String feedUrl, Handler handler){
	  super(feedUrl);
	  parentHandler = handler;
  }
  
  public void parse(){
//	  ArrayList<NewXmlModel> mNewXmlItems = null;
	   XmlPullParser parser = Xml.newPullParser();
	 try{
		 parser.setInput(this.getInputStream(), null);
		 Log.v("Satya", "Satya");
		 int eventType = parser.getEventType();
		 NewXmlModel mXmlmodel = null;
		 while (eventType != XmlPullParser.END_DOCUMENT) {
			String name = null;
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				//mNewXmlItems = new ArrayList<NewXmlModel>(); 
				Log.d("START_DOCUMENT", "This Is Desc");
				break;
			case XmlPullParser.START_TAG:
				name = parser.getName();
			if (name.equalsIgnoreCase(ITEM)) {
				mXmlmodel = new NewXmlModel();
				Log.d(ITEM, "This Is ITEM");
			}
			else if (mXmlmodel != null) {
				if (name.equalsIgnoreCase(TITLE)) {
					mXmlmodel.title = parser.nextText();
				}else if (name.equalsIgnoreCase(DESCRIPTION)) {
					mXmlmodel.description = parser.nextText();
				}else if (name.equalsIgnoreCase(PUBDATE)) {
					mXmlmodel.pubdate = parser.nextText();
				}else if (name.equalsIgnoreCase(THUMBNAIL)) {
					mXmlmodel.thumbnail = parser.getAttributeValue(2);
					Log.v("Here The Image",""+THUMBNAIL);
				}
			}
			break;
			case XmlPullParser.END_TAG:
				name = parser.getName();
				if (name.equalsIgnoreCase(ITEM) && mXmlmodel != null) {
					//mNewXmlItems.add(mXmlmodel);
					//mXmlmodel = null;
					requiredTagFound=true;
               		Message messageToParent = new Message();
    				Bundle messageData = new Bundle();
    				messageToParent.what = 0;
    				messageData.putSerializable("Datafeed",mXmlmodel);
    				messageToParent.setData(messageData);
    				// send message to mainThread
    				parentHandler.sendMessage(messageToParent);	
    				if(TEMP_COUNTER%ITEM_PER_PAGE==0)
                	{
                		synchronized (this) 
                		{
                			wait();
						}
                	}
                		
    				TEMP_COUNTER=TEMP_COUNTER+1;
				}
				break;
			}
			eventType = parser.next();
		}
	 }catch (Exception e) {
		// TODO: handle exception
		 Log.e("error......", "socket exception error");
	}
//	return mNewXmlItems;
  }
}
