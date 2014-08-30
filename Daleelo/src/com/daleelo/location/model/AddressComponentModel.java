package com.daleelo.location.model;

import java.util.ArrayList;

public class AddressComponentModel {
		/*	
		<address_component>
			<long_name>Koti</long_name>
			<short_name>Koti</short_name>
			<type>sublocality</type>
			<type>political</type>
		</address_component>
		*/
	private String longName, shortName;
	public ArrayList<String> type;

	public String getLongName() {
		return longName;
	}
	public void setLongName(String longName) {
		this.longName = longName;
	}
	
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	
	
	
}
