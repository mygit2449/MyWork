package com.daleelo.TripPlanner.Parser;

import java.io.StringReader;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;

import com.daleelo.TripPlanner.Model.TripPlannerLocationPointsModel;
import com.daleelo.Utilities.HttpLoader;

public class TripPlannerLocationPointsParser extends HttpLoader {
	
	Handler parentHandler;
	ArrayList<TripPlannerLocationPointsModel> parserListArray=null;
	TripPlannerLocationPointsModel modelobj=null;
	
	
public TripPlannerLocationPointsParser(Handler handler,String feedUrl , ArrayList<TripPlannerLocationPointsModel> _arrMyList) 
{
	super(feedUrl);
	parentHandler=handler;
	parserListArray = _arrMyList;
	
}


public void parse(String inputStream,String errMsg) 
{

	if(errMsg.toString().length() == 0)
	{
	
	XmlPullParser parser=Xml.newPullParser();
	try 
	{
	    parser.setInput(new StringReader(inputStream));
		Log.e("net","connected");
		int eventType=parser.getEventType();
		
		
		 boolean done = false;
		while(eventType!=XmlPullParser.END_DOCUMENT && !done)
		{
			String name=null;
		switch (eventType) {
			case XmlPullParser.START_DOCUMENT:		
				
				break;
			case XmlPullParser.START_TAG:
				
				name=parser.getName();
				if(name.equalsIgnoreCase("Tripplanner"))
				{					
					modelobj=new TripPlannerLocationPointsModel();					
			    }else if(modelobj!=null){
			    	
					 if (name.equalsIgnoreCase("BusinessId")) {
							modelobj.setBusinessId(parser.nextText());						
					}else if(name.equalsIgnoreCase("BusinessTitle")) {
						modelobj.setBusinessTitle(parser.nextText());					
					}else if(name.equalsIgnoreCase("BusinessDescription")) {
							modelobj.setBusinessDescription(parser.nextText());						
					}else if(name.equalsIgnoreCase("AddressLine1")){
						modelobj.setAddressLine1(parser.nextText());					
					}else if(name.equalsIgnoreCase("AddressLine2")){
						modelobj.setAddressLine2(parser.nextText());
					}else if(name.equalsIgnoreCase("CityName")){
						modelobj.setCityName(parser.nextText());					
					}else if (name.equalsIgnoreCase("StateCode")){
						modelobj.setStateCode(parser.nextText());					
					}else if(name.equalsIgnoreCase("AddressZipcode")) {
						modelobj.setAddressZipcode(parser.nextText());					
					}else if(name.equalsIgnoreCase("AddressPhone")) {
						modelobj.setAddressPhone(parser.nextText());					
					}else if (name.equalsIgnoreCase("CategoryName")) {
						modelobj.setCategoryName(parser.nextText());					
					}else if (name.equalsIgnoreCase("AddressLat")) {
						modelobj.setAddressLat(new Double(parser.nextText()));					
					}else if (name.equalsIgnoreCase("AddressLong")) {
						modelobj.setAddressLong(new Double(parser.nextText()));					
					}else if (name.equalsIgnoreCase("Distance")) {
						modelobj.setDistance(parser.nextText());					
					}
			 }
			break;
			
           case XmlPullParser.END_TAG:
				name=parser.getName();
				if(name.equalsIgnoreCase("Tripplanner")  && modelobj!=null)
				{								
					modelobj.setType("t");
					modelobj.setCategory("sub");
					modelobj.setItemType("business");
					parserListArray.add(modelobj);
					Log.e("@@@@@", " "+parserListArray.size());
					modelobj = null;
				
				}
							

			break;
			}
			eventType=parser.next();
		}
	}catch (Exception e)
		{		
	   	 	errMsg="Invalid response from Daleelo Server";   		
		}
	}
	
	Message messageToParent = new Message();
	Bundle messageData = new Bundle();
	messageToParent.what = 4;

	messageData.putString("HttpError",errMsg);
	messageToParent.setData(messageData);
	parentHandler.sendMessage(messageToParent);

}
}
