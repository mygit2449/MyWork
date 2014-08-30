package com.daleelo.Masjid.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class MasjidModel implements Serializable {
	
	static String businessDetail="";
	private String businessId=" ";
	private String businessTitle="";
	private String businessAddress="";
	private String addressLine1="";
	private String AddressLine2=" ";
	private String addressLat="";
	private String addressLong="";
	private String businessDescription=" ";
	private String businessHours=" ";
	private String businessIsactive= " ";
	private String businessIsfeatured=" ";
	private String businessRating=" ";
	private String addressId=" ";
	private String cityName=" ";
	private String stateCode=" ";
	private String addressZipcode=" ";
	private String addressPhone=" ";
	private String addressFax=" ";
	private String AddressTollFree=" ";
	private String addressEmail=" ";
	private String addressWeburl=" ";
	private String addressFBurl=" ";
	private String addressTWurl=" ";
	private String cityID=" ";
	private String businessValidFrom=" ";
	private String businessvalidTo=" ";
	private String categoryId=" ";
	private String distance=" ";
	private String mosqueinfo=" ";
	private String eventsdetails=" ";
	private String businessImage=" ";
	private ArrayList<MosqueinfoModel> MosqueDataFeeds=null;
	private ArrayList <String> PayOptions;
	
	public void setPayOptions(ArrayList<String> payOptions) {
		PayOptions = payOptions;
	}
	public String getBusinessDetail() {
		return businessDetail;
	}
	public void setBusinessDetail(String businessDetail) {
		MasjidModel.businessDetail = businessDetail;
	}
	public String getBusinessTitle() {
		return businessTitle;
	}
	public void setBusinessTitle(String businessTitle) {
		this.businessTitle = businessTitle;
	}
	public String getBusinessAddress() {
		return businessAddress;
	}
	public void setBusinessAddress(String businessAddress) {
		this.businessAddress = businessAddress;
	}
	public String getAddressLine1() {
		return addressLine1;
	}
	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}
	public String getAddressLat() {
		return addressLat;
	}
	public void setAddressLat(String addressLat) {
		this.addressLat = addressLat;
	}
	public String getAddressLong() {
		return addressLong;
	}
	public void setAddressLong(String addressLong) {
		this.addressLong = addressLong;
	}
	public String getBusinessId() {
		return businessId;
	}
	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
	public String getBusinessDescription() {
		return businessDescription;
	}
	public void setBusinessDescription(String businessDescription) {
		this.businessDescription = businessDescription;
	}
	public String getBusinessHours() {
		return businessHours;
	}
	public void setBusinessHours(String businessHours) {
		this.businessHours = businessHours;
	}
	
	public String getStateCode() {
		return stateCode;
	}
	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}
	public String getAddressZipcode() {
		return addressZipcode;
	}
	public void setAddressZipcode(String addressZipcode) {
		this.addressZipcode = addressZipcode;
	}
	public String getAddressPhone() {
		return addressPhone;
	}
	public void setAddressPhone(String addressPhone) {
		this.addressPhone = addressPhone;
	}
	public String getAddressEmail() {
		return addressEmail;
	}
	public void setAddressEmail(String addressEmail) {
		this.addressEmail = addressEmail;
	}
	public String getAddressWeburl() {
		return addressWeburl;
	}
	public void setAddressWeburl(String addressWeburl) {
		this.addressWeburl = addressWeburl;
	}
	public String getAddressFBurl() {
		return addressFBurl;
	}
	public void setAddressFBurl(String addressFBurl) {
		this.addressFBurl = addressFBurl;
	}
	
	public String getAddressTWurl() {
		return addressTWurl;
	}
	public void setAddressTWurl(String addressTWurl) {
		this.addressTWurl = addressTWurl;
	}
	public String getBusinessValidFrom() {
		return businessValidFrom;
	}
	public void setBusinessValidFrom(String businessValidFrom) {
		this.businessValidFrom = businessValidFrom;
	}
	public String getCityID() {
		return cityID;
	}
	public void setCityID(String cityID) {
		this.cityID = cityID;
	}
	public String getBusinessvalidTo() {
		return businessvalidTo;
	}
	public void setBusinessvalidTo(String businessvalidTo) {
		this.businessvalidTo = businessvalidTo;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getAddressId() {
		return addressId;
	}
	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getBusinessRating() {
		return businessRating;
	}
	public void setBusinessRating(String businessRating) {
		this.businessRating = businessRating;
	}
	public String getBusinessIsactive() {
		return businessIsactive;
	}
	public void setBusinessIsactive(String businessIsactive) {
		this.businessIsactive = businessIsactive;
	}
	public String getBusinessIsfeatured() {
		return businessIsfeatured;
	}
	public void setBusinessIsfeatured(String businessIsfeatured) {
		this.businessIsfeatured = businessIsfeatured;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public String getMosqueinfo() {
		return mosqueinfo;
	}
	public void setMosqueinfo(String mosqueinfo) {
		this.mosqueinfo = mosqueinfo;
	}
	public String getAddressLine2() {
		return AddressLine2;
	}
	public void setAddressLine2(String addressLine2) {
		AddressLine2 = addressLine2;
	}
	public String getAddressFax() {
		return addressFax;
	}
	public void setAddressFax(String addressFax) {
		this.addressFax = addressFax;
	}
	public String getAddressTollFree() {
		return AddressTollFree;
	}
	public void setAddressTollFree(String addressTollFree) {
		AddressTollFree = addressTollFree;
	}
	public String getEventsdetails() {
		return eventsdetails;
	}
	public void setEventsdetails(String eventsdetails) {
		this.eventsdetails = eventsdetails;
	}
	public ArrayList<MosqueinfoModel> getMosqueDataFeeds() {
		return MosqueDataFeeds;
	}
	public void setMosqueDataFeeds(
			ArrayList<MosqueinfoModel> MosqueDataFeeds) {
		this.MosqueDataFeeds = MosqueDataFeeds;
	}
	public String getBusinessImage() {
		return businessImage;
	}
	public void setBusinessImage(String businessImage) {
		this.businessImage = businessImage;
	}
	}
