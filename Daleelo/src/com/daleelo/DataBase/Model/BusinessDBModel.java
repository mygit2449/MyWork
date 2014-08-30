package com.daleelo.DataBase.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class BusinessDBModel implements Serializable{
	
	private String BusinessId = "";
	private String BusinessTitle = "";
	private String BusinessAddress = "";
	private String Rating = "";
	private String ReviewsCount = "";
	private String Latitude = "";
	private String Longitude = "";
	
	public String getBusinessId() {
		return BusinessId;
	}
	public void setBusinessId(String businessId) {
		BusinessId = businessId;
	}
	public String getBusinessTitle() {
		return BusinessTitle;
	}
	public void setBusinessTitle(String businessTitle) {
		BusinessTitle = businessTitle;
	}
	public String getBusinessAddress() {
		return BusinessAddress;
	}
	public void setBusinessAddress(String businessAddress) {
		BusinessAddress = businessAddress;
	}
	public String getRating() {
		return Rating;
	}
	public void setRating(String rating) {
		Rating = rating;
	}
	public String getReviewsCount() {
		return ReviewsCount;
	}
	public void setReviewsCount(String reviewsCount) {
		ReviewsCount = reviewsCount;
	}
	public String getLatitude() {
		return Latitude;
	}
	public void setLatitude(String latitude) {
		Latitude = latitude;
	}
	public String getLongitude() {
		return Longitude;
	}
	public void setLongitude(String longitude) {
		Longitude = longitude;
	}
	
	
	
	
		
	
}
