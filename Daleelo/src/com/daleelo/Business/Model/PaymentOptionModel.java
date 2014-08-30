package com.daleelo.Business.Model;

import java.io.Serializable;

public class PaymentOptionModel implements Serializable{
	
	private String PaymentOpt;

	public String getPaymentOpt() {
		return PaymentOpt;
	}

	public void setPaymentOpt(String paymentOpt) {
		PaymentOpt = paymentOpt;
	}
	
}
