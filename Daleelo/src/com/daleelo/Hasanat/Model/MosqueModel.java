package com.daleelo.Hasanat.Model;

import java.io.Serializable;

public class MosqueModel implements Serializable{
	
	private String mosqueId=" ";
	private String mosqueImam=" ";
	private String mosqueDemographic=" ";
	private String mosqueDenomination=" ";
	private String mosqueLanguages=" ";
	private String prayerType=" ";
	private String merchentId=" ";
	
	public String getMosqueId() {
		return mosqueId;
	}
	public void setMosqueId(String mosqueId) {
		this.mosqueId = mosqueId;
	}
	public String getMosqueImam() {
		return mosqueImam;
	}
	public void setMosqueImam(String mosqueImam) {
		this.mosqueImam = mosqueImam;
	}
	public String getMosqueDemographic() {
		return mosqueDemographic;
	}
	public void setMosqueDemographic(String mosqueDemographic) {
		this.mosqueDemographic = mosqueDemographic;
	}
	public String getMosqueDenomination() {
		return mosqueDenomination;
	}
	public void setMosqueDenomination(String mosqueDenomination) {
		this.mosqueDenomination = mosqueDenomination;
	}
	public String getMosqueLanguages() {
		return mosqueLanguages;
	}
	public void setMosqueLanguages(String mosqueLanguages) {
		this.mosqueLanguages = mosqueLanguages;
	}
	public String getPrayerType() {
		return prayerType;
	}
	public void setPrayerType(String prayerType) {
		this.prayerType = prayerType;
	}
	public String getMerchentId() {
		return merchentId;
	}
	public void setMerchentId(String merchentId) {
		this.merchentId = merchentId;
	}
}
