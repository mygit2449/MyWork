package com.daleelo.Dashboard.Model;

import java.io.Serializable;

public class GetHomePageCategoriesModel implements Serializable{
	

	
	private String CategoryId = "";
	private String CategoryName = "";
	private String Imageurl = "";
	private String Imageurl1 = "";
	private String Imageurl2 = "";
	private String MatsterCategoryId = "";
	
	public String getCategoryId() {
		return CategoryId;
	}
	public void setCategoryId(String categoryId) {
		CategoryId = categoryId;
	}
	public String getCategoryName() {
		return CategoryName;
	}
	public void setCategoryName(String categoryName) {
		CategoryName = categoryName;
	}
	public String getImageurl() {
		return Imageurl;
	}
	public void setImageurl(String imageurl) {
		Imageurl = imageurl;
	}
	public String getImageurl1() {
		return Imageurl1;
	}
	public void setImageurl1(String imageurl1) {
		Imageurl1 = imageurl1;
	}
	public String getImageurl2() {
		return Imageurl2;
	}
	public void setImageurl2(String imageurl2) {
		Imageurl2 = imageurl2;
	}
	public String getMatsterCategoryId() {
		return MatsterCategoryId;
	}
	public void setMatsterCategoryId(String matsterCategoryId) {
		MatsterCategoryId = matsterCategoryId;
	}
	
	
	
	
}
