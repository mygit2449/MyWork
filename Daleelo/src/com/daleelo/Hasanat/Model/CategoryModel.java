package com.daleelo.Hasanat.Model;

import java.io.Serializable;

public class CategoryModel implements Serializable{
	
	private String categoryId = "";
	private String categoryMasterId = "";
	private String categoryName = "";
	private String imgUrl = "";
	
	
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryMasterId() {
		return categoryMasterId;
	}
	public void setCategoryMasterId(String categoryMasterId) {
		this.categoryMasterId = categoryMasterId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

}
