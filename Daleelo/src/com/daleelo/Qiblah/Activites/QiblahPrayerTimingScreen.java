package com.daleelo.Qiblah.Activites;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daleelo.R;
import com.daleelo.DashBoardEvents.Activities.EventsCalendarActivity;
import com.daleelo.DashBoardEvents.Activities.EventsIslamicCalendarActivity;
import com.daleelo.DataBase.DatabaseHelper;
import com.daleelo.Masjid.Activities.MasjidMapActivity;
import com.daleelo.Utilities.CurrentLocationData;
import com.daleelo.Utilities.Urls;
import com.daleelo.Views.QiblahRotateBig;
import com.daleelo.helper.DateFormater;


public class QiblahPrayerTimingScreen extends Activity implements SensorListener{
	/** Called when the activity is first created. */

	
	
	QiblahRotateBig mQiblahRotate;
	LinearLayout mLLCompass;
	SensorManager sensorManager;
	ImageButton mQuiblaSettings, mQuiblaMasjid, mQuiblaCalender;
	
	static final int sensor = SensorManager.SENSOR_ORIENTATION;
	float[] mValues;
	private TextView mTxtFajr, mTxtShuruk, mTxtDhuhr, mTxtAsr, mTxtMaghrib, mTxtIsha, mtxt_Day,mtxt_date;
	private TextView mtxt_calmethod, mtxt_remaining_time;
	private Calendar cal;
	private Timer timer = new Timer();
	private ArrayList<String> prayerTimes;
	private int mCalMethodType ;
	private SharedPreferences mCalcMethodSharedPrference;
	private DatabaseHelper mDatabaseHelper;
	
	String[] WEEK_DAY = {"Sunday","Monday","Tueday","Wednesday","Thursday","Friday","Saturday"};
	
	String[] MONTH_NAME = {"January","February","March","April","May","June","July","August","September","October","November","December"};
	
	String[]  calcMethod= {
    		"Muslim World League,Europe",
    		"Egyptian General Authority of Survey",
    		"UIS, Karachi",
    		"Uhm-AL-Qura, Makkah",
    		"ISN, North America"};
	
	Calendar today;
	ArrayList<PrayerTimeModel> monthlyPrayTimes;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		today = Calendar.getInstance();
		try {
			mDatabaseHelper = new DatabaseHelper(this);		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setContentView(R.layout.qiblah_prayer_screen);
		initUI();
		
		
	}
	
	
	@Override
	  public void onResume() {
	    super.onResume();
	    sensorManager.registerListener(this, sensor);
	    
	    
	    if(mCalcMethodSharedPrference.getBoolean("prayertimes", false))
	    	setCompleteData();
	    else
	    	myAlertDialog();
	    	
	    
	  }

	@Override
	  public void onPause() {
	    super.onPause();
	    sensorManager.unregisterListener(this);
	  }
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(timer != null){
			timer.cancel();
		}
	}
	
	
	private void initUI() {

		mLLCompass = (LinearLayout) findViewById(R.id.qiblah_REL_compass);
		mQuiblaSettings = (ImageButton)findViewById(R.id.qiblah_IMGBTN_settings);
		mQuiblaMasjid = (ImageButton)findViewById(R.id.qiblah_IMGBTN_masjid);
		mQuiblaCalender = (ImageButton)findViewById(R.id.qiblah_IMGBTN_calender);
		
		mTxtFajr = (TextView)findViewById(R.id.qiblah_TXT_fajr_time);
		mTxtShuruk = (TextView)findViewById(R.id.qiblah_TXT_shuruq_time);
		mTxtDhuhr =(TextView)findViewById(R.id.qiblah_TXT_dhuhr_time);
		mTxtAsr = (TextView)findViewById(R.id.qiblah_TXT_asr_time);
		mTxtMaghrib = (TextView)findViewById(R.id.qiblah_TXT_maghrib_time);
		mTxtIsha = (TextView)findViewById(R.id.qiblah_TXT_isha_time);
		mtxt_date = (TextView)findViewById(R.id.qiblah_TXT_date);
		mtxt_Day = (TextView)findViewById(R.id.qiblah_TXT_weekday);
		mtxt_calmethod = (TextView)findViewById(R.id.qiblah_TXT_calcmethod);
		mtxt_remaining_time = (TextView)findViewById(R.id.qiblah_TXT_remaintime);
	
		mCalcMethodSharedPrference = getSharedPreferences("praytimestatus", MODE_PRIVATE);

		cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		
		mQiblahRotate = new QiblahRotateBig(this);	
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		
		mQuiblaSettings.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				startActivity(new Intent(QiblahPrayerTimingScreen.this,QiblahSettingsActivity.class));
				
			}
		});
		
		mQuiblaMasjid.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				startActivity(new Intent(QiblahPrayerTimingScreen.this,MasjidMapActivity.class)
				.putExtra("from", "home"));
				
			}
		});
		
		mQuiblaCalender.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				startActivity(new Intent(QiblahPrayerTimingScreen.this,EventsCalendarActivity.class));				
				
				
			}
		});
		
		mLLCompass.bringToFront();
		mLLCompass.addView(mQiblahRotate);
		setData();
	}
	
	private void setData() {
		
		String islamDate = new EventsIslamicCalendarActivity().getIslamicDate(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH)+1,cal.get(Calendar.YEAR));		
		Log.e("", "************ Date "+Calendar.DAY_OF_MONTH+" "+cal.get(Calendar.MONTH)+" "+cal.get(Calendar.YEAR));
		mtxt_Day.setText(WEEK_DAY[cal.get(Calendar.DAY_OF_WEEK) - 1 ]);
		mtxt_date.setText(MONTH_NAME[cal.get(Calendar.MONTH)]+" "+ cal.get(Calendar.DAY_OF_MONTH) + ", "+ cal.get(Calendar.YEAR) +" / "+islamDate.split("/")[1]+" "+new Double(islamDate.split("/")[0]).intValue()+", "+islamDate.split("/")[2]);
	}

	
	public void setCompleteData(){
		
		mCalMethodType = mCalcMethodSharedPrference.getInt("prayermethod", 5);
	    mtxt_calmethod.setText(calcMethod[mCalMethodType-1]);
	
				int mm = today.get(Calendar.MONTH) + 1;
				int dd = today.get(Calendar.DAY_OF_MONTH);
				mDatabaseHelper.openDataBase();
				PrayerTimeModel mTimeModel;
				
				if(getIntent().getBooleanExtra("showNextDayTimes",false))
					 mTimeModel = mDatabaseHelper.getPrayerTimingsbyDay(String.valueOf(dd+1),String.valueOf(mm));
				else
					mTimeModel = mDatabaseHelper.getPrayerTimingsbyDay(String.valueOf(dd),String.valueOf(mm));
				
				mDatabaseHelper.closeDataBase();				
					
				if(mTimeModel != null){
						
					if(mTimeModel.prayerTimings.size() > 0){
						
						prayerTimes = mTimeModel.prayerTimings;
						setTimings();
						
					}else{
						
						myAlertDialog();
						
					}
					
				}else{
					
					myAlertDialog();
				}				
	
	}
	
	

	@Override
	public void onAccuracyChanged(int sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
	// Listen to sensor and provide output
	  public void onSensorChanged(int sensor, float[] values) {
	    if (sensor != QiblahPrayerTimingScreen.sensor)
	      return;
	    float orientation = -values[0];
	    mQiblahRotate.setDirection(orientation);
	  }
	  
	
	
	private void setTimings(){
	    
	    for (int i = 0; i < prayerTimes.size(); i++) {
		     
	    	
		     mTxtFajr.setText(prayerTimes.get(0));
		     mTxtShuruk.setText(prayerTimes.get(1));
		     mTxtDhuhr.setText(prayerTimes.get(2));
		     mTxtAsr.setText(prayerTimes.get(3));
		     mTxtMaghrib.setText(prayerTimes.get(4));
		     mTxtIsha.setText(prayerTimes.get(5));
		     
		     calculateRemainingTime(prayerTimes);		     
		   
		     setRemainingTime();
	    }
  	
	}
	
	void calculateRemainingTime(ArrayList<String> prayerTimes){
		boolean flag = true;
		for (int i = 0; i < prayerTimes.size(); i++) {
			long time = new DateFormater().parseDate(prayerTimes.get(i), "hh:mm a");
			Date date = new Date(time);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
			if(System.currentTimeMillis() <= calendar.getTimeInMillis()){
				
				if(flag){
					flag = false;
					long timediff = calendar.getTimeInMillis() - System.currentTimeMillis();
					mtxt_remaining_time.setText((timediff/3600000)+":"+((timediff%3600000)/60000)+":"+(((timediff%3600000)%60000)/1000));
				}
			}
		}
		
	}

	
	
	private void setRemainingTime() {
		
		 timer.scheduleAtFixedRate(new TimerTask() {
		  @Override
		  public void run() {
		   // Imagine here a freaking cool web access 
			  showReaminingTimeHandler.sendEmptyMessage(5);
		  }
		 }, 0, 100);
		 Log.i(getClass().getSimpleName(), "Timer started.");

		}
	
	

	Handler showReaminingTimeHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			
			if(msg.what == 5){
				
				calculateRemainingTime(prayerTimes);
				
			}
		}	
	};	
	
	
//	*******************************
//	Prayer Timmings
//	*******************************
	
	
	private boolean remainTime = true;
	private TimeZoneModel mTimeZoneModel = null;
	private PrayerTimeModel mPrayerTimeModel = null;
	private ArrayList<PrayerTimeModel> prayerTimesFeeds = new ArrayList<PrayerTimeModel>();
	private SharedPreferences prayTimeStatus; 
	ProgressDialog mProgressDialog;	

	
	private void getPrayerTimes() {		
		
		mProgressDialog = ProgressDialog.show(QiblahPrayerTimingScreen.this, null, "Please wait..");		
		
		today = Calendar.getInstance();
	
		prayTimeStatus = getSharedPreferences("praytimestatus", MODE_PRIVATE);
		mCalMethodType = prayTimeStatus.getInt("prayermethod", 5);	

		Log.e("prayer TIme", "*************************downloading prayer times");
		prayTimeStatus.edit().putString("month",String.valueOf(today.get(Calendar.MONTH))).commit();
		prayTimeStatus.edit().putString("year",String.valueOf(today.get(Calendar.YEAR))).commit();
		prayTimeStatus.edit().putString("city",CurrentLocationData.CURRENT_CITY).commit();		
		
		new AsynTimeZoneGetter().execute();
		
	}				
				

	class AsynTimeZoneGetter extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			String url = String.format(Urls.TIME_ZONE_URL_ONE,
					CurrentLocationData.LATITUDE,
					CurrentLocationData.LONGITUDE);
			Log.e("", "************url "+url);
			new TimeZoneParser(url,showTimeHandler).parser();
			
			return null;
			
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			
			super.onPostExecute(result);
			new AsynPrayerTimeGetter().execute();
			
		}

	}

	class AsynPrayerTimeGetter extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {		
				
				
				prayTimeStatus.edit().putString("countryshortname",CurrentLocationData.CURRENT_COUNTRY_SHORT_NAME).commit();
				prayTimeStatus.edit().putString("city",CurrentLocationData.CURRENT_CITY).commit();
				prayTimeStatus.edit().putString("statename",CurrentLocationData.CURRENT_STATE_SHORT_NAME).commit();
				prayTimeStatus.edit().putString("lat",CurrentLocationData.LATITUDE).commit();
				prayTimeStatus.edit().putString("long",CurrentLocationData.LONGITUDE).commit();
				prayTimeStatus.edit().putString("timezone",mTimeZoneModel.getOffset()).commit();
				prayTimeStatus.edit().putBoolean("prayertimes",true).commit();


				 new PrayerTimeParser(String.format(Urls.PRAYER_TIMING_URL,
                         URLEncoder.encode(CurrentLocationData.CURRENT_COUNTRY_SHORT_NAME,"UTF-8"), 
                         URLEncoder.encode(CurrentLocationData.CURRENT_CITY, "UTF-8"),
                         URLEncoder.encode(CurrentLocationData.CURRENT_STATE_SHORT_NAME,"UTF-8"), 
                         CurrentLocationData.LATITUDE,
                         CurrentLocationData.LONGITUDE, 
                         mTimeZoneModel.getOffset(), 
                         String.valueOf(mCalMethodType)),
                         showTimeHandler).parser();   

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				
				

			}
			return null;
		}

	}

	Handler showTimeHandler = new Handler() {
		PrayerTimeModel mTimeModel;
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);

			if (msg.what == TimeZoneParser.FOUND_RESULT) {

				mTimeZoneModel = (TimeZoneModel) msg.getData().get("data");
				prayTimeStatus.edit().putString("timezoneoffset", mTimeZoneModel.getOffset()).commit();
			
			} else if (msg.what == PrayerTimeParser.FOUND_RESULT) {
				
				mDatabaseHelper.openDataBase();				
				mDatabaseHelper.deletPrayTimes();
				prayerTimesFeeds = (ArrayList<PrayerTimeModel>) msg.getData().get("data");
				for(int i = 0; i<prayerTimesFeeds.size(); i++){
					mTimeModel = prayerTimesFeeds.get(i);
					long status = mDatabaseHelper.insertPrayerTimings(mTimeModel.getDate(), mTimeModel.getMonth(), mTimeModel.getYear(), mTimeModel.prayerTimings.get(0), mTimeModel.prayerTimings.get(1), mTimeModel.prayerTimings.get(2), mTimeModel.prayerTimings.get(3), mTimeModel.prayerTimings.get(4), mTimeModel.prayerTimings.get(5));
					Log.e("status", status+" null");
				}
				mDatabaseHelper.closeDataBase();				
	            mProgressDialog.dismiss();
	            
	            setCompleteData();

			} 

		}

	};
	
	
//	****************************************
//	 Alert Dailogs
//	****************************************

			
			private void myAlertDialog(){
				
				
				
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Daleelo");
				builder.setMessage("Do you want to get prayer timings for the current location.")
				       .setCancelable(false)
				       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				        	
				        	   getPrayerTimes();		        		
				               
				           }
				       })
				       .setNegativeButton("No", new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				        	   
				                dialog.cancel();				      		
				        		finish();
				        		
				           }
				       });
				AlertDialog alert = builder.create();
				alert.show();
			}
	
	
	
}
