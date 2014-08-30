package com.daleelo.GlobalSearch;

import java.io.Serializable;
import java.util.ArrayList;

import com.daleelo.Masjid.Model.MosqueinfoModel;

public class GlobalSearchModel implements Serializable{
	
	private String BusinessID = "";
	private String BusinessTitle = "";
	private String Descinfo = "";
	private String OtherDescinfo = "";
	private String AddressInfo = "";
	private String CityName = "";
	private String StateCode = "";
	private String Typeinfo = "";
	private ArrayList<MosqueinfoModel> MosqueDataFeeds = null;
	
	public String getBusinessID() {
		return BusinessID;
	}
	public void setBusinessID(String businessID) {
		BusinessID = businessID;
	}
	public String getBusinessTitle() {
		return BusinessTitle;
	}
	public void setBusinessTitle(String businessTitle) {
		BusinessTitle = businessTitle;
	}
	public String getDescinfo() {
		return Descinfo;
	}
	public void setDescinfo(String descinfo) {
		Descinfo = descinfo;
	}
	public String getOtherDescinfo() {
		return OtherDescinfo;
	}
	public void setOtherDescinfo(String otherDescinfo) {
		OtherDescinfo = otherDescinfo;
	}
	public String getAddressInfo() {
		return AddressInfo;
	}
	public void setAddressInfo(String addressInfo) {
		AddressInfo = addressInfo;
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
	public String getTypeinfo() {
		return Typeinfo;
	}
	public void setTypeinfo(String typeinfo) {
		Typeinfo = typeinfo;
	}
	public ArrayList<MosqueinfoModel> getMosqueDataFeeds() {
		return MosqueDataFeeds;
	}
	public void setMosqueDataFeeds(ArrayList<MosqueinfoModel> mosqueDataFeeds) {
		MosqueDataFeeds = mosqueDataFeeds;
	}
	
	
	
}
