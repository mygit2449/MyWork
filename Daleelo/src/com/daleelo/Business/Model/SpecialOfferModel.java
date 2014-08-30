package com.daleelo.Business.Model;

import java.io.Serializable;

public class SpecialOfferModel implements Serializable{
	
	private String SpecialOfferId;
	private String SpecialOfferTitle;
	private String SpecialOfferDescription;
	private String SpecialOfferValidFrom;
	private String SpecialOfferValidTo;
	private String SpecialOfferIsAvailable;
	private String SpecialOfferRating;
	private String SpecialOfferIsFeatured;
	
	public String getSpecialOfferId() {
		return SpecialOfferId;
	}
	public void setSpecialOfferId(String specialOfferId) {
		SpecialOfferId = specialOfferId;
	}
	public String getSpecialOfferTitle() {
		return SpecialOfferTitle;
	}
	public void setSpecialOfferTitle(String specialOfferTitle) {
		SpecialOfferTitle = specialOfferTitle;
	}
	public String getSpecialOfferDescription() {
		return SpecialOfferDescription;
	}
	public void setSpecialOfferDescription(String specialOfferDescription) {
		SpecialOfferDescription = specialOfferDescription;
	}
	public String getSpecialOfferValidFrom() {
		return SpecialOfferValidFrom;
	}
	public void setSpecialOfferValidFrom(String specialOfferValidFrom) {
		SpecialOfferValidFrom = specialOfferValidFrom;
	}
	public String getSpecialOfferValidTo() {
		return SpecialOfferValidTo;
	}
	public void setSpecialOfferValidTo(String specialOfferValidTo) {
		SpecialOfferValidTo = specialOfferValidTo;
	}
	public String getSpecialOfferIsAvailable() {
		return SpecialOfferIsAvailable;
	}
	public void setSpecialOfferIsAvailable(String specialOfferIsAvailable) {
		SpecialOfferIsAvailable = specialOfferIsAvailable;
	}
	public String getSpecialOfferRating() {
		return SpecialOfferRating;
	}
	public void setSpecialOfferRating(String specialOfferRating) {
		SpecialOfferRating = specialOfferRating;
	}
	public String getSpecialOfferIsFeatured() {
		return SpecialOfferIsFeatured;
	}
	public void setSpecialOfferIsFeatured(String specialOfferIsFeatured) {
		SpecialOfferIsFeatured = specialOfferIsFeatured;
	}

	
}
