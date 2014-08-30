package com.daleelo.DashBoardClassified.Model;

import java.io.Serializable;

public class GetClassfiedImageModel implements Serializable{
	
	
	private String CSID;
	private String ClassifiedID;
	private String ImageUrl;
	
	
	public String getCSID() {
		return CSID;
	}
	public void setCSID(String cSID) {
		CSID = cSID;
	}
	public String getClassifiedID() {
		return ClassifiedID;
	}
	public void setClassifiedID(String classifiedID) {
		ClassifiedID = classifiedID;
	}
	public String getImageUrl() {
		return ImageUrl;
	}
	public void setImageUrl(String imageUrl) {
		ImageUrl = imageUrl;
	}
	
	
	
}
