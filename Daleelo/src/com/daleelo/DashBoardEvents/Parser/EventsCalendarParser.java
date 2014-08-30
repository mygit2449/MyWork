package com.daleelo.DashBoardEvents.Parser;

import java.io.StringReader;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Xml;

import com.daleelo.DashBoardEvents.Model.EventsCalenderModel;
import com.daleelo.Utilities.HttpLoader;

public class EventsCalendarParser extends HttpLoader {
	
	//private ArrayList <CarsListModel> carsListFeeds = null;
	private Handler parentHandler;
	ArrayList<EventsCalenderModel> marrEventsList;
	ArrayList<EventsCalenderModel> marrFundRaisingEventsList;
	boolean eventFound = false;
	boolean fromFundRaising  = false;
	
	public EventsCalendarParser(Handler handler,String feedUrl , ArrayList<EventsCalenderModel> _arrMyCarsList) 
	{
		super(feedUrl);
		parentHandler=handler;	
		marrEventsList = _arrMyCarsList;
	}
	public EventsCalendarParser(Handler handler,String feedUrl , ArrayList<EventsCalenderModel> _arrMyCarsList,boolean fromFundRaising) 
	{
		super(feedUrl);
		parentHandler=handler;	
		marrFundRaisingEventsList = _arrMyCarsList;
		this.fromFundRaising = fromFundRaising;
		
	}
	
	
	@Override	
	public void parse(String inputStream,String errMsg) 
	{
		
		if(errMsg.toString().length() == 0)
		{
	
			XmlPullParser parser = Xml.newPullParser();
			
	        try {
	        	boolean isValidXmlBeforeEndDoc = false;
	            parser.setInput(new StringReader(inputStream));
	            int eventType = parser.getEventType();	           
	            EventsCalenderModel mEventsCalenderModel = null;
	            boolean done = false;
	            
	          
	            while ( !done)
	            {
	            	
	                String name = null;
	                switch (eventType)
	                {
	                    case XmlPullParser.START_DOCUMENT:
	                    	
	                        break;
	                    case XmlPullParser.START_TAG:
	                        name = parser.getName();	                      
	                        if (name.equalsIgnoreCase("ArrayOfEvent"))
	                        {	             	
	                        	isValidXmlBeforeEndDoc = true;
	                        }else if (name.equalsIgnoreCase("Event")){                        		
	                        		mEventsCalenderModel = new EventsCalenderModel();	
		                        	eventFound = true;
		                        	
	                        }else if (eventFound){	           		                        	
	                        	
	                        	
	                        	if (name.equalsIgnoreCase("EventId")){	                        		
	                        		mEventsCalenderModel.setEventId(parser.nextText());	                        		
	                        	}else if (name.equalsIgnoreCase("EventTitle")){
	                        		mEventsCalenderModel.setEventTitle((parser.nextText()));
	                            }else if (name.equalsIgnoreCase("EventDetails")){
	                            	mEventsCalenderModel.setEventDetails((parser.nextText()));
	                            }else if (name.equalsIgnoreCase("EventStartsOn")){
	                            	mEventsCalenderModel.setEventStartsOn((parser.nextText()));	                            	
	                            }else if (name.equalsIgnoreCase("EventEndsOn")){
	                            	mEventsCalenderModel.setEventEndsOn((parser.nextText()));	                            
	                            }else if (name.equalsIgnoreCase("EventOrganizer")){
	                            	mEventsCalenderModel.setEventOrganizer((parser.nextText()));	                            
	                            }else if (name.equalsIgnoreCase("EventRating")){
	                            	mEventsCalenderModel.setEventRating((parser.nextText()));	                            
	                            }else if (name.equalsIgnoreCase("EventIsFeatured")){
	                            	mEventsCalenderModel.setEventIsFeatured((parser.nextText()));	                            
	                            }else if (name.equalsIgnoreCase("VenueLocation")){
	                            	mEventsCalenderModel.setVenueLocation((parser.nextText()));	                            
	                            }else if (name.equalsIgnoreCase("VenueCity")){
	                            	mEventsCalenderModel.setVenueCity((parser.nextText()));	                            	
	                            }else if (name.equalsIgnoreCase("Fundrising")){
	                            	mEventsCalenderModel.setFundrising((parser.nextText()));
	                            }else if (name.equalsIgnoreCase("EventType")){
	                            	mEventsCalenderModel.setEventType((parser.nextText()));
	                            }else if (name.equalsIgnoreCase("VenueName")){
	                            	mEventsCalenderModel.setVenueName((parser.nextText()));
	                            }else if (name.equalsIgnoreCase("EventImage")){
	                            	mEventsCalenderModel.setEventImage((parser.nextText()));
	                            }else if (name.equalsIgnoreCase("Latitude")){
	                            	mEventsCalenderModel.setLatitude((parser.nextText()));
	                            }else if (name.equalsIgnoreCase("Longitude")){
	                            	mEventsCalenderModel.setLongitude((parser.nextText()));
	                            }else if (name.equalsIgnoreCase("StateCode")){
	                            	mEventsCalenderModel.setStateCode((parser.nextText()));
	                            }else if (name.equalsIgnoreCase("ZipCode")){
	                            	mEventsCalenderModel.setZipcode((parser.nextText()));
	                            }else if (name.equalsIgnoreCase("Distance")){
	                            	mEventsCalenderModel.setDistance((parser.nextText()));
	                            }
	                        	
	                        	 
	                        	
	                        }
	                        break;
	                        
	                    case XmlPullParser.END_TAG:
	                        name = parser.getName();
	                        if (name.equalsIgnoreCase("Event")){
	                        	if(Boolean.parseBoolean(mEventsCalenderModel.getFundrising()) && fromFundRaising){
	                        		marrFundRaisingEventsList.add(mEventsCalenderModel);
	                        		mEventsCalenderModel=null;
	                        	}	
	                        	if(marrEventsList!=null){
		                        	marrEventsList.add(mEventsCalenderModel);
		                        	mEventsCalenderModel=null;
	                        	}	
	                        }	
	                        	
	                    	
	                        	                        
	                        break;
	                    case XmlPullParser.END_DOCUMENT: 
							
							if (isValidXmlBeforeEndDoc == false) 
							{
								errMsg="Invalid response from Daleelo Server";
							}
							done = true;
		
							break;
						
	                }
	                if(eventType != XmlPullParser.END_DOCUMENT)
						eventType = parser.next();
	            }
	        
	        }
	        catch (Exception e) {
	        	
	        	 errMsg="Invalid response from Easy Buy Bid Server";
	        
	        }

		}
		Message messageToParent = new Message();
		Bundle messageData = new Bundle();		
		messageData.putString("HttpError",errMsg);		
		messageToParent.setData(messageData);
		parentHandler.sendMessage(messageToParent);

	}

}
