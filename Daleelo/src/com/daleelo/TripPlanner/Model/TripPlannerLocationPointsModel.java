package com.daleelo.TripPlanner.Model;

import java.io.Serializable;

public class TripPlannerLocationPointsModel implements Serializable {
	
	private String BusinessId="";
	private String BusinessTitle=" ";
	private String BusinessDescription="";
	private String AddressLine1="";
	private String AddressLine2="";
	private String CityName=" ";
	private String StateCode="";
	private String AddressZipcode="";
	private String AddressPhone="";
	private String CategoryName=" ";
	private double AddressLat;	
	private double AddressLong;
	private String Distance="";
	
	private boolean middlecity = false;
	private String Category = "";
	private String Type = "";
	private String ItemType = "";
	
	public boolean isMiddlecity() {
		return middlecity;
	}
	public void setMiddlecity(boolean middlecity) {
		this.middlecity = middlecity;
	}
	public double getAddressLat() {
		return AddressLat;
	}
	public void setAddressLat(double addressLat) {
		AddressLat = addressLat;
	}
	public double getAddressLong() {
		return AddressLong;
	}
	public void setAddressLong(double addressLong) {
		AddressLong = addressLong;
	}
	public String getCategory() {
		return Category;
	}
	public void setCategory(String category) {
		Category = category;
	}	
	public String getType() {
		return Type;
	}
	public void setType(String type) {
		Type = type;
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
	public String getBusinessDescription() {
		return BusinessDescription;
	}
	public void setBusinessDescription(String businessDescription) {
		BusinessDescription = businessDescription;
	}
	public String getAddressLine1() {
		return AddressLine1;
	}
	public void setAddressLine1(String addressLine1) {
		AddressLine1 = addressLine1;
	}
	public String getAddressLine2() {
		return AddressLine2;
	}
	public void setAddressLine2(String addressLine2) {
		AddressLine2 = addressLine2;
	}
	public String getCityName() {
		return CityName;
	}
	public void setCityName(String cityName) {
		CityName = cityName;
	}
	public String getStateCode() {
		return StateCode;
	}
	public void setStateCode(String stateCode) {
		StateCode = stateCode;
	}
	public String getAddressZipcode() {
		return AddressZipcode;
	}
	public void setAddressZipcode(String addressZipcode) {
		AddressZipcode = addressZipcode;
	}
	public String getAddressPhone() {
		return AddressPhone;
	}
	public void setAddressPhone(String addressPhone) {
		AddressPhone = addressPhone;
	}
	public String getCategoryName() {
		return CategoryName;
	}
	public void setCategoryName(String categoryName) {
		CategoryName = categoryName;
	}
	
	public String getDistance() {
		return Distance;
	}
	public void setDistance(String distance) {
		Distance = distance;
	}
	public String getItemType() {
		return ItemType;
	}
	public void setItemType(String itemType) {
		ItemType = itemType;
	}
	
	
	
	
	
}