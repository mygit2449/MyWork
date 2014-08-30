package com.daleelo.TripPlanner.Model;

import java.io.Serializable;

public class RouteCitiesModel implements Serializable {
	
	
	private String CityName = "";	
	private double AddressLat;
	private double AddressLong;
	
	public String getCityName() {
		return CityName;
	}
	public void setCityName(String cityName) {
		CityName = cityName;
	}
	public double getAddressLat() {
		return AddressLat;
	}
	public void setAddressLat(double addressLat) {
		AddressLat = addressLat;
	}
	public double getAddressLong() {
		return AddressLong;
	}
	public void setAddressLong(double addressLong) {
		AddressLong = addressLong;
	}
	
	

}