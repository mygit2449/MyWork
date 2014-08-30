package com.daleelo.Qiblah.Activites;

import java.io.Serializable;
import java.util.ArrayList;

public class PrayerTimeModel implements Serializable{
	
	private int date;
	private int month;
	private int year;
	public ArrayList<String> prayerTimings;
	
	public int getDate() {
		return date;
	}
	public void setDate(int date) {
		this.date = date;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	
}
