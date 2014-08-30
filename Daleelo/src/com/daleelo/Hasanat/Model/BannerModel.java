package com.daleelo.Hasanat.Model;

import java.io.Serializable;
import java.util.ArrayList;


public class BannerModel implements Serializable {
	
	 private String BannerID;
     private String BannerTittle;
     private String BannerImageUrl;
     private String CalanderImage;
     private String ValidForm;
     private String ValidTo;
     private String MinAmount;
     private String DonationUrl;
     private String AddLocation;
     private String CustomerID;
     private String AuthorizenetPayment;
     
	public String getBannerID() {
		return BannerID;
	}
	public void setBannerID(String bannerID) {
		BannerID = bannerID;
	}
	public String getBannerTittle() {
		return BannerTittle;
	}
	public void setBannerTittle(String bannerTittle) {
		BannerTittle = bannerTittle;
	}
	public String getBannerImageUrl() {
		return BannerImageUrl;
	}
	public void setBannerImageUrl(String bannerImageUrl) {
		BannerImageUrl = bannerImageUrl;
	}
	public String getCalanderImage() {
		return CalanderImage;
	}
	public void setCalanderImage(String calanderImage) {
		CalanderImage = calanderImage;
	}
	public String getValidForm() {
		return ValidForm;
	}
	public void setValidForm(String validForm) {
		ValidForm = validForm;
	}
	public String getValidTo() {
		return ValidTo;
	}
	public void setValidTo(String validTo) {
		ValidTo = validTo;
	}
	public String getMinAmount() {
		return MinAmount;
	}
	public void setMinAmount(String minAmount) {
		MinAmount = minAmount;
	}
	public String getDonationUrl() {
		return DonationUrl;
	}
	public void setDonationUrl(String donationUrl) {
		DonationUrl = donationUrl;
	}
	public String getAddLocation() {
		return AddLocation;
	}
	public void setAddLocation(String addLocation) {
		AddLocation = addLocation;
	}
	public String getCustomerID() {
		return CustomerID;
	}
	public void setCustomerID(String customerID) {
		CustomerID = customerID;
	}
	public String getAuthorizenetPayment() {
		return AuthorizenetPayment;
	}
	public void setAuthorizenetPayment(String authorizenetPayment) {
		AuthorizenetPayment = authorizenetPayment;
	}
     
     
}
