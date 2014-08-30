package com.daleelo.Business.Model;

import java.io.Serializable;

public class ReviewModel implements Serializable{
	
	private String ReviewContent;
	private String ReviewRank;
	private String ReviewPostedBy;
	private String ReviewPostedOn;
	private String ReviewTittle;
	
	
	public String getReviewContent() {
		return ReviewContent;
	}
	public void setReviewContent(String reviewContent) {
		ReviewContent = reviewContent;
	}
	public String getReviewRank() {
		return ReviewRank;
	}
	public void setReviewRank(String reviewRank) {
		ReviewRank = reviewRank;
	}
	public String getReviewPostedBy() {
		return ReviewPostedBy;
	}
	public void setReviewPostedBy(String reviewPostedBy) {
		ReviewPostedBy = reviewPostedBy;
	}
	public String getReviewPostedOn() {
		return ReviewPostedOn;
	}
	public void setReviewPostedOn(String reviewPostedOn) {
		ReviewPostedOn = reviewPostedOn;
	}
	public String getReviewTittle() {
		return ReviewTittle;
	}
	public void setReviewTittle(String reviewTittle) {
		ReviewTittle = reviewTittle;
	}
	
	
}
