package com.daleelo.helper;

import java.util.ArrayList;
import java.util.Calendar;

import com.daleelo.R;
import com.daleelo.Utilities.CurrentLocationData;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class PrayerScheduleActivity extends Activity{
	
	private Calendar cal;
	private TextView mTxtFajr, mTxtShuruk, mTxtDhuhr, mTxtAsr, mTxtMaghrib, mTxtIsha;
	private int calcmethod;
	private TextView mtxt_Title;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hasanat_desc_prayer_layout);
		initializeUI();
		
		SharedPreferences mCalcMethodSharedPrference = this.getSharedPreferences("calcMethod", MODE_PRIVATE);
		calcmethod = mCalcMethodSharedPrference.getInt("methodtype", 4);
		
		setTimings(calcmethod);
	}
	
	private void initializeUI(){
		mTxtFajr = (TextView)findViewById(R.id.hasanat_desc_prayer_TXT_fajr_time);
		mTxtShuruk = (TextView)findViewById(R.id.hasanat_desc_prayer_TXT_shuruq_time);
		mTxtDhuhr =(TextView)findViewById(R.id.hasanat_desc_prayer_TXT_dhuhr_time);
		mTxtAsr = (TextView)findViewById(R.id.hasanat_desc_prayer_TXT_asr_time);
		mTxtMaghrib = (TextView)findViewById(R.id.hasanat_desc_prayer_TXT_maghrib_time);
		mTxtIsha = (TextView)findViewById(R.id.hasanat_desc_prayer_TXT_isha_time);
		
		mtxt_Title = (TextView)findViewById(R.id.hasanat_desc_prayer_TXT_main_title);
		
		mtxt_Title.setText("Prayer Schedule for "+ getIntent().getExtras().getString("title"));
		
		cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
	}
	
	
	private void setTimings(int calcmethod){
		
		PrayTime prayers = new PrayTime();
	     	
     	prayers.setTimeFormat(prayers.Time12);
        prayers.setCalcMethod(calcmethod);
//        prayers.setAsrJuristic(prayers.Shafii);
//        prayers.setAdjustHighLats(prayers.AngleBased);
        int[] offsets = {0, 0, 0, 0, 0,0}; // {Fajr,shuruk,Dhuhr,Asr,Sunset,Maghrib,Isha}
//    prayers.tune(offsets);
        

        double timezone = 5.5;
	
	    ArrayList<String> prayerTimes = prayers.getPrayerTimes(cal, Double.valueOf(CurrentLocationData.LATITUDE), Double.valueOf(CurrentLocationData.LONGITUDE), timezone);
	    
	    ArrayList<String> prayerNames = prayers.getTimeNames();
	    
	    for (int i = 0; i < prayerTimes.size(); i++) {
		     Log.e(prayerNames.get(i) , "  " + prayerTimes.get(i));
		     
		     mTxtFajr.setText(prayerTimes.get(0));
		     mTxtShuruk.setText(prayerTimes.get(1));
		     mTxtDhuhr.setText(prayerTimes.get(2));
		     mTxtAsr.setText(prayerTimes.get(3));
		     mTxtMaghrib.setText(prayerTimes.get(5));
		     mTxtIsha.setText(prayerTimes.get(6));
		     
	    }
  	
	}
	
}
