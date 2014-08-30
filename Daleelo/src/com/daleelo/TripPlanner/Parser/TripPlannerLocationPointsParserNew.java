package com.daleelo.TripPlanner.Parser;

import java.io.StringReader;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;

import com.daleelo.TripPlanner.Activities.TripPlannerRoadModel;
import com.daleelo.Utilities.HttpLoader;

public class TripPlannerLocationPointsParserNew extends HttpLoader {
	
	Handler parentHandler;
	boolean step_found = false;
	boolean distance_tag = false;
	
	TripPlannerRoadModel modelobj=null;
	boolean start_location = false;
	boolean end_location = false;
	
public TripPlannerLocationPointsParserNew(Handler handler,String feedUrl) 
{
	super(feedUrl);
	parentHandler=handler;
	
	
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
				if(name.equalsIgnoreCase("status"))
				{		
					if(parser.nextText().equalsIgnoreCase("OK")){
						modelobj=new TripPlannerRoadModel();						
					}
			    }else if(modelobj!=null){
			    	
					 if (name.equalsIgnoreCase("step")){
							step_found = true;					
					 }else if (name.equalsIgnoreCase("start_location")){
							start_location = true;				
					 }else if (name.equalsIgnoreCase("end_location")){
							end_location   = true;				
					 }else if(name.equalsIgnoreCase("lat") && step_found && start_location){
						Double lat=Double.parseDouble(parser.nextText()); 						
							modelobj.lat_arr.add(lat);
						
					}else if(name.equalsIgnoreCase("lng") && step_found && start_location){
						Double lng=Double.parseDouble(parser.nextText()); 						
							modelobj.long_arr.add(lng);
														
					}else if(name.equalsIgnoreCase("lat") && step_found && end_location){
						Double lat=Double.parseDouble(parser.nextText()); 						
						modelobj.lat_arr.add(lat);
					
					}else if(name.equalsIgnoreCase("lng") && step_found && end_location){
					Double lng=Double.parseDouble(parser.nextText()); 						
						modelobj.long_arr.add(lng);
													
					}else if(name.equalsIgnoreCase("distance") && !step_found){
						distance_tag = true;										
					}else if(name.equalsIgnoreCase("text")&& distance_tag){
						modelobj.mDescription = parser.nextText();									
					}
			 }
			break;
			
           case XmlPullParser.END_TAG:
				name=parser.getName();
				if(name.equalsIgnoreCase("step"))
				{								
					step_found = false;				
				}else if(name.equalsIgnoreCase("start_location")){								
					start_location = false;				
				}else if(name.equalsIgnoreCase("end_location")){								
					end_location = false;				
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
	messageToParent.what = 10;	
	
	if(modelobj!=null){
		messageData.putSerializable("points", modelobj);
	}else{
		errMsg="Invalid response from Daleelo Server"; 
	}
	messageData.putString("HttpError",errMsg);
	
	messageToParent.setData(messageData);
	parentHandler.sendMessage(messageToParent);

}
}
