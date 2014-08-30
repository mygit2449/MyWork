package com.daleelo.helper;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.net.ParseException;


public class DateFormater {
	static Date date;
    static int day;
	static int month;
	static int year;
   public static long  parseDate(String date, String format) throws ParseException 
   {
	   try {
		       if (date == null || date.length() == 0) 
		       {
		           return 0;
		       }
		       SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		       DateFormater.date=dateFormat.parse(date);
       	
       		} catch (java.text.ParseException e) {
		// TODO Auto-generated catch block
       			e.printStackTrace();
       		}
       
       return DateFormater.date.getTime();
   	}
}