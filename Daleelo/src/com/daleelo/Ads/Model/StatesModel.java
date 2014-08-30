package com.daleelo.Ads.Model;

import java.io.Serializable;

public class StatesModel implements Serializable{

	public String state_id = "";
	public String state_name = "";
	public String state_code = "";
	public String country_code = "";
	public String select_state = "";
	
	
	public String getSelect_state() {
		return select_state;
	}
	public void setSelect_state(String select_state) {
		this.select_state = select_state;
	}
	public String getState_id() {
		return state_id;
	}
	public void setState_id(String state_id) {
		this.state_id = state_id;
	}
	public String getState_name() {
		return state_name;
	}
	public void setState_name(String state_name) {
		this.state_name = state_name;
	}
	public String getState_code() {
		return state_code;
	}
	public void setState_code(String state_code) {
		this.state_code = state_code;
	}
	public String getCountry_code() {
		return country_code;
	}
	public void setCountry_code(String country_code) {
		this.country_code = country_code;
	}
}
