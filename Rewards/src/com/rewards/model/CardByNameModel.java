package com.rewards.model;

import java.io.Serializable;

public class CardByNameModel implements Serializable{

	
	String cardId = "";
	String cardName = "";
	String cardDetails = "";
	String amount = "";
	String points = "";
	String card_image = "";
	String businessId = "";
	String pointsendDate = "";
	String businessTitle = "";
	
	public String getCardId() {
		return cardId;
	}
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
	public String getCardName() {
		return cardName;
	}
	public void setCardName(String cardName) {
		this.cardName = cardName;
	}
	public String getCardDetails() {
		return cardDetails;
	}
	public void setCardDetails(String cardDetails) {
		this.cardDetails = cardDetails;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getPoints() {
		return points;
	}
	public void setPoints(String points) {
		this.points = points;
	}
	public String getCard_image() {
		return card_image;
	}
	public void setCard_image(String card_image) {
		this.card_image = card_image;
	}
	public String getBusinessId() {
		return businessId;
	}
	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
	public String getPointsendDate() {
		return pointsendDate;
	}
	public void setPointsendDate(String pointsendDate) {
		this.pointsendDate = pointsendDate;
	}
	public String getBusinessTitle() {
		return businessTitle;
	}
	public void setBusinessTitle(String businessTitle) {
		this.businessTitle = businessTitle;
	}

	
}
