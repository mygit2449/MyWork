package com.daleelo.DataBase.Model;

import java.io.Serializable;

public class HasanatDBModel implements Serializable{
	
	private String BusinessId = "";
	private String BusinessTitle = "";
	private String BusinessAddress = "";
	private String PhoneNo = "";
	private String Latitude = "";
	private String Longitude = "";
	private String CategoryId = "";
	private String CityName = "";
	
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
	public String getPhoneNo() {
		return PhoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		PhoneNo = phoneNo;
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
	public String getCategoryId() {
		return CategoryId;
	}
	public void setCategoryId(String categoryId) {
		CategoryId = categoryId;
	}
	public String getCityName() {
		return CityName;
	}
	public void setCityName(String cityName) {
		CityName = cityName;
	}	
	
	
	
}
