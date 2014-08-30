package com.daleelo.DataBase.Model;

import java.io.Serializable;

public class ClassfiedDBModel implements Serializable{
	
	private String ClassifiedId = "";
	private String ClassifiedTitle = "";
	private String Description = "";
	private String Image = "";
	private String Price = "";
	private String Location = "";
	private String Distance = "";
	private String Latitude = "";
	private String Longitude = "";
	private String OBO = "";
	
	
	public String getClassifiedId() {
		return ClassifiedId;
	}
	public void setClassifiedId(String classifiedId) {
		ClassifiedId = classifiedId;
	}
	public String getClassifiedTitle() {
		return ClassifiedTitle;
	}
	public void setClassifiedTitle(String classifiedTitle) {
		ClassifiedTitle = classifiedTitle;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public String getImage() {
		return Image;
	}
	public void setImage(String image) {
		Image = image;
	}
	public String getPrice() {
		return Price;
	}
	public void setPrice(String price) {
		Price = price;
	}
	public String getLocation() {
		return Location;
	}
	public void setLocation(String location) {
		Location = location;
	}
	public String getDistance() {
		return Distance;
	}
	public void setDistance(String distance) {
		Distance = distance;
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
	public String getOBO() {
		return OBO;
	}
	public void setOBO(String oBO) {
		OBO = oBO;
	}
	
	
}
