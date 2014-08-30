package com.daleelo.Hasanat.Model;

import java.io.Serializable;

public class CountryModel implements Serializable{
	
	private String countryId;
	private String countryCode;
	private String countryName;
	private String countryFlag;
	private String isActive;
	private String currencyFlag;
	private String currencyCode;
	private String inActiveSince;
	
	public String getCountryId() {
		return countryId;
	}
	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public String getCountryFlag() {
		return countryFlag;
	}
	public void setCountryFlag(String countryFlag) {
		this.countryFlag = countryFlag;
	}
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	public String getCurrencyFlag() {
		return currencyFlag;
	}
	public void setCurrencyFlag(String currencyFlag) {
		this.currencyFlag = currencyFlag;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getInActiveSince() {
		return inActiveSince;
	}
	public void setInActiveSince(String inActiveSince) {
		this.inActiveSince = inActiveSince;
	}
	
	

}
