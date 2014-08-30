package com.daleelo.Utilities;

public class CurrentLocationData {
	
	public static String LATITUDE = "0",LONGITUDE = "0", LATITUDE_DUMP = "0",LONGITUDE_DUMP = "0";	
	public static String CURRENT_CITY = "", CURRENT_CITY_DUMP = "none";
	public static String ADDRESS_LINE = "", ADDRESS_LINE_DUMP = "";
	public static String CURRENT_COUNTRY = "", CURRENT_COUNTRY_DUMP = "";
	public static String CURRENT_COUNTRY_SHORT_NAME = "",CURRENT_COUNTRY_SHORT_NAME_DUMP = "";
	public static String CURRENT_STATE = "", CURRENT_STATE_DUMP = "";
	public static String CURRENT_STATE_SHORT_NAME = "",CURRENT_STATE_SHORT_NAME_DUMP = "";
	public static boolean GET_LOCATION = false;
	public static String LOCATION_OLD = "";
	public static String LOCATION_NEW = "";
	public static boolean IS_CURRENT_LOCATION = false, NON_CURRENT_LOCATION_SELECTED = false;
	
//	public static int CURRENT_LOCATION_NOT_FOUND_COUNT = 0; 
//	public static boolean CURRENT_LOCATION_NOT_FOUND = false;
	
	
	public static String TIME_ZONE;
	
	
	
	public static void clearData(){
		
		LATITUDE = "0";
		LONGITUDE = "0";	
		CURRENT_CITY = "";
//		ADDRESS_LINE = "";
		CURRENT_COUNTRY = "";
		CURRENT_STATE = "";
		CURRENT_COUNTRY_SHORT_NAME = "";
		CURRENT_STATE_SHORT_NAME = "";
		GET_LOCATION = false;
		IS_CURRENT_LOCATION = false;
		NON_CURRENT_LOCATION_SELECTED = false;
		CURRENT_CITY_DUMP = "none";
		
	}

}
