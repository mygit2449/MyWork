package com.age;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class CalculateAgeExpActivity extends Activity {
	int day = 1, month = 0, year = 1, ageYears, ageMonths, ageDays;
	String dob = "Jul 26 00:00:00 GMT+05:30 2011";
	TextView mAge;
	int years = 0;
	int months = 0;
	int days = 0;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mAge = (TextView)findViewById(R.id.age);
		
		SimpleDateFormat mDateFormat = new SimpleDateFormat("MMM dd HH:mm:ss zzz yyyy");
		Date date_of_birth = null;
		try {
			date_of_birth = mDateFormat.parse(dob);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Calendar birthDay = Calendar.getInstance();
		birthDay.setTime(date_of_birth);
		
		long currentTime = System.currentTimeMillis();
		Calendar currentDay = Calendar.getInstance();
		currentDay.setTimeInMillis(currentTime);
		
		years = currentDay.get(Calendar.YEAR) - birthDay.get(Calendar.YEAR);
		
		
		int currMonth = currentDay.get(Calendar.MONTH)+1;
		int birthMonth = birthDay.get(Calendar.MONTH)+1;
		 
		//Get difference between months
		months = currMonth - birthMonth;
		 
		//if month difference is in negative then reduce years by one and calculate the number of months.
		if(months < 0)
		{
		 years--;
		 months = 12 - birthMonth + currMonth;
		  
		 if(currentDay.get(Calendar.DATE)<birthDay.get(Calendar.DATE))
		  months--;
		  
		}else if(months == 0 && currentDay.get(Calendar.DATE) < birthDay.get(Calendar.DATE)){
		 years--;
		 months = 11;
		}
		 
		//Calculate the days
		if(currentDay.get(Calendar.DATE)>birthDay.get(Calendar.DATE))
		 days = currentDay.get(Calendar.DATE) -  birthDay.get(Calendar.DATE);
		else if(currentDay.get(Calendar.DATE)<birthDay.get(Calendar.DATE)){
		 int today = currentDay.get(Calendar.DAY_OF_MONTH);
		 currentDay.add(Calendar.MONTH, -1);
		 days = currentDay.getActualMaximum(Calendar.DAY_OF_MONTH)-birthDay.get(Calendar.DAY_OF_MONTH)+today;
		}else{
		 days=0;
		  
		 if(months == 12){
		  years++;
		  months = 0;
		 }
		}
		
		
		System.out.println("The age is : "+years+" years, "+months+" months and "+days+" days" );
		mAge.setText("The age is : "+years+" years, "+months+" months and "+days+" days");
	}
}
