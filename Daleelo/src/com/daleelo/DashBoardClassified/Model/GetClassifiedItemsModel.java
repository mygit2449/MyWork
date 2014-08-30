package com.daleelo.DashBoardClassified.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class GetClassifiedItemsModel implements Serializable {

	private String ClassifiedId = "";
	private String ClassifiedTitle = "";
	private String ClassifiedInfo = "";
	private String ClassifiedValidFrom = "";
	private String ClassifiedValidTo = "";
	private String ClassifiedIsactive = "";
	private String ClassifiedOwner = "";
	private String ClassifiedCategory = "";
	private String Price = "";
	private String Location = "";
	private String PhoneNum = "";
	private String Email = "";
	private String Images = "";
	private String Latitude = "";
	private String Longitude = "";
	private String CategoryName = "";
	private String ClassifiedCreatedOn = "";
	private String OBO = "";
	private String Cityname = "";
	private String ClassifiedSection = "";
	private String CSType = "";
	private String Distance = "";
	private ArrayList<GetClassfiedImageModel> ClassifiedImages;
	
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
	public String getClassifiedInfo() {
		return ClassifiedInfo;
	}
	public void setClassifiedInfo(String classifiedInfo) {
		ClassifiedInfo = classifiedInfo;
	}
	public String getClassifiedValidFrom() {
		return ClassifiedValidFrom;
	}
	public void setClassifiedValidFrom(String classifiedValidFrom) {
		ClassifiedValidFrom = classifiedValidFrom;
	}
	public String getClassifiedValidTo() {
		return ClassifiedValidTo;
	}
	public void setClassifiedValidTo(String classifiedValidTo) {
		ClassifiedValidTo = classifiedValidTo;
	}
	public String getClassifiedIsactive() {
		return ClassifiedIsactive;
	}
	public void setClassifiedIsactive(String classifiedIsactive) {
		ClassifiedIsactive = classifiedIsactive;
	}
	public String getClassifiedOwner() {
		return ClassifiedOwner;
	}
	public void setClassifiedOwner(String classifiedOwner) {
		ClassifiedOwner = classifiedOwner;
	}
	public String getClassifiedCategory() {
		return ClassifiedCategory;
	}
	public void setClassifiedCategory(String classifiedCategory) {
		ClassifiedCategory = classifiedCategory;
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
	public String getPhoneNum() {
		return PhoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		PhoneNum = phoneNum;
	}
	public String getEmail() {
		return Email;
	}
	public void setEmail(String email) {
		Email = email;
	}
	public String getImages() {
		return Images;
	}
	public void setImages(String images) {
		Images = images;
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
	public String getCategoryName() {
		return CategoryName;
	}
	public void setCategoryName(String categoryName) {
		CategoryName = categoryName;
	}
	public String getClassifiedCreatedOn() {
		return ClassifiedCreatedOn;
	}
	public void setClassifiedCreatedOn(String classifiedCreatedOn) {
		ClassifiedCreatedOn = classifiedCreatedOn;
	}
	public String getOBO() {
		return OBO;
	}
	public void setOBO(String oBO) {
		OBO = oBO;
	}
	public String getCityname() {
		return Cityname;
	}
	public void setCityname(String cityname) {
		Cityname = cityname;
	}
	public String getClassifiedSection() {
		return ClassifiedSection;
	}
	public void setClassifiedSection(String classifiedSection) {
		ClassifiedSection = classifiedSection;
	}
	public String getCSType() {
		return CSType;
	}
	public void setCSType(String cSType) {
		CSType = cSType;
	}
	public String getDistance() {
		return Distance;
	}
	public void setDistance(String distance) {
		Distance = distance;
	}
	public ArrayList<GetClassfiedImageModel> getClassifiedImages() {
		return ClassifiedImages;
	}
	public void setClassifiedImages(ArrayList<GetClassfiedImageModel> classifiedImages) {
		ClassifiedImages = classifiedImages;
	}
	
	
	

}
