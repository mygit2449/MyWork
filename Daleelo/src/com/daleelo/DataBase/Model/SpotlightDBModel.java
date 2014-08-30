package com.daleelo.DataBase.Model;

import java.io.Serializable;

public class SpotlightDBModel implements Serializable{
	
	private String SpotlightId = "";
	private String SpotlightTitle = "";
	private String SpotlightDesp = "";
	private String SpotlightAddress = "";
	private String SpotlightImage = "";
	private String BusinessId = "";
	private String BusinessTitle = "";
	private String Latitude = "";
	private String Longitude = "";
	
	public String getSpotlightId() {
		return SpotlightId;
	}
	public void setSpotlightId(String spotlightId) {
		SpotlightId = spotlightId;
	}
	public String getSpotlightTitle() {
		return SpotlightTitle;
	}
	public void setSpotlightTitle(String spotlightTitle) {
		SpotlightTitle = spotlightTitle;
	}
	public String getSpotlightDesp() {
		return SpotlightDesp;
	}
	public void setSpotlightDesp(String spotlightDesp) {
		SpotlightDesp = spotlightDesp;
	}
	public String getSpotlightAddress() {
		return SpotlightAddress;
	}
	public void setSpotlightAddress(String spotlightAddress) {
		SpotlightAddress = spotlightAddress;
	}
	public String getSpotlightImage() {
		return SpotlightImage;
	}
	public void setSpotlightImage(String spotlightImage) {
		SpotlightImage = spotlightImage;
	}
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
