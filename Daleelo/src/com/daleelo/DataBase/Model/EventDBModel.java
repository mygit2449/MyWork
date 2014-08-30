package com.daleelo.DataBase.Model;

import java.io.Serializable;

public class EventDBModel implements Serializable{
	
	private String EventId;
	private String EventTitle;
	private String EventLocation;
	private String EventOrganizer;
	private String EventIsFeatured;
	private String EventStartDate;
	private String EventEndDate;
	private String BusinessID;
	private String CityName;
	
	public String getEventId() {
		return EventId;
	}
	public void setEventId(String eventId) {
		EventId = eventId;
	}
	public String getEventTitle() {
		return EventTitle;
	}
	public void setEventTitle(String eventTitle) {
		EventTitle = eventTitle;
	}
	public String getEventLocation() {
		return EventLocation;
	}
	public void setEventLocation(String eventLocation) {
		EventLocation = eventLocation;
	}
	public String getEventOrganizer() {
		return EventOrganizer;
	}
	public void setEventOrganizer(String eventOrganizer) {
		EventOrganizer = eventOrganizer;
	}
	public String getEventIsFeatured() {
		return EventIsFeatured;
	}
	public void setEventIsFeatured(String eventIsFeatured) {
		EventIsFeatured = eventIsFeatured;
	}
	public String getEventStartDate() {
		return EventStartDate;
	}
	public void setEventStartDate(String eventStartDate) {
		EventStartDate = eventStartDate;
	}
	public String getEventEndDate() {
		return EventEndDate;
	}
	public void setEventEndDate(String eventEndDate) {
		EventEndDate = eventEndDate;
	}
	public String getBusinessID() {
		return BusinessID;
	}
	public void setBusinessID(String businessID) {
		BusinessID = businessID;
	}
	public String getCityName() {
		return CityName;
	}
	public void setCityName(String cityName) {
		CityName = cityName;
	}	
	
	
}
