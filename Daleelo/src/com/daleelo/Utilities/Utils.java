package com.daleelo.Utilities;

import java.util.ArrayList;

import android.app.AlarmManager;
import android.content.Context;


public class Utils {

	public static String OS_VERSION;
	public static int OS_LEVEL;
	public static int HEIGHT ;
	public static int WIDTH ;
	public static String RANGE = "25";
	public static String SEARCH_SUB_KEY = "";
	public static boolean fundraising_events = true;
	public static boolean normal_events = true;
	
	public static Context ALARM_CONTEXT = null;
	
	public static AlarmManager ALARM_MANAGER = null;
	
	public static ArrayList<String> dailyPraytime;
	public static int mZakatDueAmount = 0;
}
