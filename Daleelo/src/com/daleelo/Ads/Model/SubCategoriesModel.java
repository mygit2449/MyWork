package com.daleelo.Ads.Model;

import java.io.Serializable;

public class SubCategoriesModel implements Serializable{

	private String Sub_CategoryId = "";
	private String Sub_CategoryName = "";
	private String Sub_Imageurl = "";
	private String Sub_Imageurl1 = "";
	private String Sub_Imageurl2 = "";
	private String Sub_MatsterCategoryId = "";
	public String getSub_CategoryId() {
		return Sub_CategoryId;
	}
	public void setSub_CategoryId(String sub_CategoryId) {
		Sub_CategoryId = sub_CategoryId;
	}
	public String getSub_CategoryName() {
		return Sub_CategoryName;
	}
	public void setSub_CategoryName(String sub_CategoryName) {
		Sub_CategoryName = sub_CategoryName;
	}
	public String getSub_Imageurl() {
		return Sub_Imageurl;
	}
	public void setSub_Imageurl(String sub_Imageurl) {
		Sub_Imageurl = sub_Imageurl;
	}
	public String getSub_Imageurl1() {
		return Sub_Imageurl1;
	}
	public void setSub_Imageurl1(String sub_Imageurl1) {
		Sub_Imageurl1 = sub_Imageurl1;
	}
	public String getSub_Imageurl2() {
		return Sub_Imageurl2;
	}
	public void setSub_Imageurl2(String sub_Imageurl2) {
		Sub_Imageurl2 = sub_Imageurl2;
	}
	public String getSub_MatsterCategoryId() {
		return Sub_MatsterCategoryId;
	}
	public void setSub_MatsterCategoryId(String sub_MatsterCategoryId) {
		Sub_MatsterCategoryId = sub_MatsterCategoryId;
	}
}
