package com.daleelo.DataBase.Model;

import java.io.Serializable;

public class DealsDBModel implements Serializable{
	
	
	private String DealId = "";
	private String DealTitle = "";	
	private String DealAddress = "";
	private String DealImage = "";
	private String BusinessId = "";
	private String BusinessTitle = "";
	private String Latitude = "";
	private String Longitude = "";
	
	
	public String getDealId() {
		return DealId;
	}
	public void setDealId(String dealId) {
		DealId = dealId;
	}
	public String getDealTitle() {
		return DealTitle;
	}
	public void setDealTitle(String dealTitle) {
		DealTitle = dealTitle;
	}
	public String getDealAddress() {
		return DealAddress;
	}
	public void setDealAddress(String dealAddress) {
		DealAddress = dealAddress;
	}
	public String getDealImage() {
		return DealImage;
	}
	public void setDealImage(String dealImage) {
		DealImage = dealImage;
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
