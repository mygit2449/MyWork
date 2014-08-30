
package com.daleelo.Main.Activities;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daleelo.R;
import com.daleelo.Ads.Activities.AdvertiseActivity;
import com.daleelo.Business.Activities.BusinessListByID;
import com.daleelo.Business.Activities.BusinessMoreCategories;
import com.daleelo.DashBoardClassified.Activities.ClassifiedItemDetailDesp;
import com.daleelo.DashBoardClassified.Activities.ClassifiedListActivity;
import com.daleelo.DashBoardClassified.Model.GetClassifiedItemsModel;
import com.daleelo.DashBoardEvents.Activities.CalendarEventDetailDesc;
import com.daleelo.DashBoardEvents.Activities.EventsCalendarActivity;
import com.daleelo.DashBoardEvents.Model.EventsCalenderModel;
import com.daleelo.Dashboard.Activities.DealsDetailDesp;
import com.daleelo.Dashboard.Activities.DealsListActivity;
import com.daleelo.Dashboard.Activities.SpotlightDetailDesp;
import com.daleelo.Dashboard.Activities.SpotlightListActivity;
import com.daleelo.Dashboard.Model.GetDealsInfoModel;
import com.daleelo.Dashboard.Model.GetHomePageCategoriesModel;
import com.daleelo.Dashboard.Model.GetSpotLightModel;
import com.daleelo.DataBase.DatabaseHelper;
import com.daleelo.Dialog.AlertDialogMsg;
import com.daleelo.Dialog.FeedsAreUnavialableDialog;
import com.daleelo.Dialog.LocationNotFoundDialog;
import com.daleelo.Dialog.NetworkNotFoundDialog;
import com.daleelo.Dialog.ServerErrorDialog;
import com.daleelo.GlobalSearch.GlobalSearchActivity;
import com.daleelo.Hasanat.Activities.HasanatActivity;
import com.daleelo.Main.Model.MainGetTodayDetailsModel;
import com.daleelo.Main.Parser.GetDashBoardItemsParser;
import com.daleelo.Main.Parser.GetHomePageCategoriesParser;
import com.daleelo.Masjid.Activities.MasjidMapActivity;
import com.daleelo.More.Activities.MoreOptionList;
import com.daleelo.MyStuff.Activities.MyStuffActivity;
import com.daleelo.Qiblah.Activites.DailyPrayerTimeParser;
import com.daleelo.Qiblah.Activites.PrayerTimeModel;
import com.daleelo.Qiblah.Activites.PrayerTimeParser;
import com.daleelo.Qiblah.Activites.QiblahPrayerTimingScreen;
import com.daleelo.Qiblah.Activites.TimeZoneModel;
import com.daleelo.Qiblah.Activites.TimeZoneParser;
import com.daleelo.TripPlanner.Activities.TripPlannerActivity;
import com.daleelo.User.Activities.LoginActivity;
import com.daleelo.Utilities.CurrentLocationData;
import com.daleelo.Utilities.Urls;
import com.daleelo.Utilities.Utils;
import com.daleelo.Views.QiblahRotate;
import com.daleelo.helper.DateFormater;
import com.daleelo.helper.ImageDownloader;
import com.daleelo.location.helper.CurrentLocation;
import com.daleelo.location.helper.LocationHelper;
import com.daleelo.location.helper.NetworkConnectionHelper;


public class MainHomeScreen extends Activity implements SensorListener, OnClickListener,CurrentLocation{
	/** Called when the activity is first created. */

	Button mFetchLocation, mMyStuff;
	
	ImageView mImgKabah, mImgPrayerPosotion;
	QiblahRotate mQiblahRotate;
	LinearLayout mLLCompass;
	SensorManager sensorManager;
	static final int sensor = SensorManager.SENSOR_ORIENTATION;
	float[] mValues;
	private boolean foundCurrentLocation = false, mBtnSelectCity = true, mMoreCatDone = false;
	private ProgressDialog progressdialog;
	private int mCalMethodType;

	private TextView mPrayerName, mPrayerTime;
	private EditText mETXT_Search;
	
	TextView 
		mSpotlightName, mSpotlightAddress, mSpotlightDistance, 
		mDealName, mDealAddress, mDealDistance, 
		mEventName, mEventAddress, mClassifiedName, 
		mClassifiedAddress;
	
	RelativeLayout 
		mSpotlightLayout, mSpotlightLayoutIcon, mDealsLayout, 
		mDealsLayoutIcon, mEventsLayout, mEventsLayoutIcon, 
		mClasifiedLayout, mClasifiedLayoutIcon, mPrayerQiblah;
	
	LinearLayout mHomeCategories, mPrayerTimings, mPrayerProgress;
	
	ImageView mImgMasjid, mImgTripPlan, mImgHasanat, mImgAdvertise, mImgMore;
	
	
	ArrayList<GetSpotLightModel> 		mMainGetSpotLightModel = null;
	ArrayList<GetDealsInfoModel >		mMainGetDealsInfoModel = null;
	ArrayList<EventsCalenderModel >	mMainGetEventsDetailsModel = null;
	ArrayList<GetClassifiedItemsModel> 	mMainGetClassfiedItemsModel = null;
	
	public int mSpotItemCount = 0, mDealItemCount = 0, mEventItemCount = 0, mClassItemCount = 0;
	private final int SELECT_CITY = 1;

	public SharedPreferences sharedpreference;
	
	Thread myThread;
	LocationHelper mLocationHelper;

	private Calendar cal;
	Calendar today ;
	private int calcmethod;

	DatabaseHelper mDatabaseHelper;
	
	private SharedPreferences prayTimeStatus;

	private LocationManager locationmanager;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_home_screen);
		
		today = Calendar.getInstance();
		prayTimeStatus = getSharedPreferences("praytimestatus", MODE_PRIVATE);
		mCalMethodType = prayTimeStatus.getInt("prayermethod", 5);			

		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mQiblahRotate = new QiblahRotate(this);
		
		if(Utils.ALARM_MANAGER ==null)
			Utils.ALARM_MANAGER = (AlarmManager) getSystemService(ALARM_SERVICE);
		
		locationmanager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
		if (!locationmanager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			
			alertForGps();
			
		}else{
		
			initializeUI();		
		}
		
	}

	
	@Override
	public void onResume() {
	    super.onResume();
	    
	    sensorManager.registerListener(this, sensor);	    
	     	  
	    
	    if(!mBtnSelectCity)
	    	setTimings();
	    
	    
	    if(!mBtnSelectCity && CurrentLocationData.GET_LOCATION){
	    
	    	
	    	if(!mFetchLocation.getText().toString().equalsIgnoreCase(CurrentLocationData.ADDRESS_LINE)){
		    	
		    	mFetchLocation.setText(CurrentLocationData.ADDRESS_LINE);  
			    getDashBoardItemsData();
			    	
			 }else{		
				 
				 if (mHomeCategoryItems != null) {	    	
				    	
				    	Log.e("LogCat", "count resume "+mSpotItemCount+" "+mDealItemCount+" "+mEventItemCount+"  "+mClassItemCount);
				    	setParseData();	   
						
				 }	
			 } 
		    
	    }	
	    
		 mBtnSelectCity = false;

	}
	
	

	@Override
	public void onPause() {
	    super.onPause();
	    sensorManager.unregisterListener(this);
	    
	    
	    if (mHomeCategoryItems != null) {
	    	
	    	mSpotItemCount += 1; mDealItemCount += 1;  mEventItemCount += 1; mClassItemCount += 1;	 
	    	Log.e("LogCat", "count pause "+mSpotItemCount+" "+mDealItemCount+" "+mEventItemCount+"  "+mClassItemCount);
			
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		CurrentLocationData.clearData();
	}
		
//	****************************	
	
	
	
//	****************************		
	
// Initialize and set data to UI



	private void initializeUI() {

		Display  display= ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		Utils.WIDTH = display.getWidth(); 
		Utils.HEIGHT = display.getHeight(); 
		
		sharedpreference = getSharedPreferences("userlogin",MODE_PRIVATE);

		try {
			mDatabaseHelper = new DatabaseHelper(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String daleeloFolderPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Daleelo";
		File bMarketDirectory=new File(daleeloFolderPath);
	    bMarketDirectory.mkdirs();
		
		mFetchLocation = (Button) findViewById(R.id.mainhome_BTN_current_location);
		mMyStuff = (Button)findViewById(R.id.mainhome_BTN_my_stuff);
		
		mImgKabah = (ImageView) findViewById(R.id.mainhome_IMG_kabah);
		mLLCompass = (LinearLayout) findViewById(R.id.mainhome_IMG_compass_small);
		
		cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		
		mPrayerName =(TextView)findViewById(R.id.mainhome_TXT_next_prayer_name);
		mPrayerTime =(TextView)findViewById(R.id.mainhome_TXT_next_prayer_time);
		mSpotlightName = (TextView) findViewById(R.id.home_TXT_SpotlightName);
		mSpotlightAddress = (TextView) findViewById(R.id.home_TXT_SpotlightAddress);
		mSpotlightDistance = (TextView) findViewById(R.id.home_TXT_SpotlightDistance);
		mDealName = (TextView) findViewById(R.id.home_TXT_DealName); 
		mDealAddress = (TextView) findViewById(R.id.home_TXT_DealAddress); 
		mDealDistance = (TextView) findViewById(R.id.home_TXT_DealDistance);
		mEventName = (TextView) findViewById(R.id.home_TXT_EventName); 
		mEventAddress = (TextView) findViewById(R.id.home_TXT_EventAddress); 
		mClassifiedName = (TextView) findViewById(R.id.home_TXT_ClassifiedName); 
		mClassifiedAddress = (TextView) findViewById(R.id.home_TXT_ClassifiedAddress);
		
		mETXT_Search = (EditText)findViewById(R.id.editText1);
		
		mSpotlightLayout = (RelativeLayout) findViewById(R.id.mainhome_REL_today_spotLight);
		mSpotlightLayoutIcon = (RelativeLayout) findViewById(R.id.mainhome_REL_today_spotLight_icon);
		mDealsLayout = (RelativeLayout) findViewById(R.id.mainhome_REL_today_deals);
		mDealsLayoutIcon = (RelativeLayout) findViewById(R.id.mainhome_REL_today_deals_icon);
		mEventsLayout = (RelativeLayout) findViewById(R.id.mainhome_REL_today_events);
		mEventsLayoutIcon = (RelativeLayout) findViewById(R.id.mainhome_REL_today_events_icon);
		mClasifiedLayout = (RelativeLayout) findViewById(R.id.mainhome_REL_today_class);
		mClasifiedLayoutIcon = (RelativeLayout) findViewById(R.id.mainhome_REL_today_class_icon);
		mPrayerQiblah = (RelativeLayout) findViewById(R.id.mainhome_REL_title_qiblah);	
		
		mPrayerTimings = (LinearLayout) findViewById(R.id.mainhome_LIN_title_prayer);
		mPrayerProgress = (LinearLayout) findViewById(R.id.mainhome_LIN_title_progress);
		mHomeCategories = (LinearLayout) findViewById(R.id.mainhome_LIN_category_items);
		
		mImgMasjid = (ImageView)findViewById(R.id.mainhome_IMG_masjid);
		mImgTripPlan = (ImageView)findViewById(R.id.mainhome_IMG_tripplan);
		mImgHasanat = (ImageView)findViewById(R.id.mainhome_IMG_hasanat);
		mImgAdvertise = (ImageView)findViewById(R.id.mainhome_IMG_advertise);
		mImgMore = (ImageView)findViewById(R.id.mainhome_IMG_more);
		
		mPrayerQiblah.setOnClickListener(this);
		mPrayerTimings.setOnClickListener(this);
		mPrayerProgress.setOnClickListener(this);
		
		mFetchLocation.setOnClickListener(this);
		mMyStuff.setOnClickListener(this);
		mImgKabah.setOnClickListener(this);
		mSpotlightLayout.setOnClickListener(this);
		mSpotlightLayoutIcon.bringToFront();
		mSpotlightLayoutIcon.setOnClickListener(this);
		mDealsLayout.setOnClickListener(this);
		mDealsLayoutIcon.setOnClickListener(this);
		mEventsLayout.setOnClickListener(this);
		mEventsLayoutIcon.setOnClickListener(this);
		mClasifiedLayout.setOnClickListener(this);
		mClasifiedLayoutIcon.setOnClickListener(this);
		mImgMasjid.setOnClickListener(this);
		mImgTripPlan.setOnClickListener(this);
		mImgHasanat.setOnClickListener(this);
		mImgAdvertise.setOnClickListener(this);
		mImgMore.setOnClickListener(this);
		mETXT_Search.setOnClickListener(this);
		
		SharedPreferences mCalcMethodSharedPrference = this.getSharedPreferences("calcMethod", MODE_PRIVATE);
		calcmethod = mCalcMethodSharedPrference.getInt("methodtype", 4);
	
		CurrentLocationData.clearData();
		setData();
		getHomeCategoriesData();
		getCurrentLocation();	
    	setTimings();

	}
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		Log.i("", "resultCode *************************" + resultCode);
		// TODO Auto-generated method stub
		
		if (requestCode == SELECT_CITY) {
			
			if (resultCode == 100) {
				
				if(!CurrentLocationData.LOCATION_OLD.equalsIgnoreCase(CurrentLocationData.LOCATION_NEW)){
					
					Log.e("",CurrentLocationData.LOCATION_OLD+"   "+CurrentLocationData.LOCATION_NEW);
//					CurrentLocationData.LOCATION_OLD = CurrentLocationData.LOCATION_NEW;
					if(CurrentLocationData.LOCATION_NEW.equalsIgnoreCase("Current Location")){
						Log.e("","current   "+CurrentLocationData.LOCATION_NEW);
						
						if(!CurrentLocationData.IS_CURRENT_LOCATION){
							
							foundCurrentLocation = false;
							getCurrentLocation();
							
						}else{
							
							setTimings();
							mFetchLocation.setText(CurrentLocationData.ADDRESS_LINE);
							getDashBoardItemsData();
							
						}
						
					}else{
						
						Log.e("","other   "+CurrentLocationData.LOCATION_NEW);
						
						setTimings();
						mFetchLocation.setText(CurrentLocationData.ADDRESS_LINE);						
						getDashBoardItemsData();
							
					}
					
				}
				


			}
		}

	}

	private void setData() {

		mLLCompass.bringToFront();
		mImgKabah.setBackgroundResource(R.anim.kabah_animation);
		mLLCompass.addView(mQiblahRotate);
		
		
	}
	
	
	
	private void clearData(){
		
		
		mSpotlightName.setText("No records found");
		mSpotlightAddress.setText("");
		mSpotlightDistance.setText("");
		mDealName.setText("No records found");
		mDealAddress.setText("");
		mDealDistance.setText("");
		mEventName.setText("No records found");
		mEventAddress.setText("");
		mClassifiedName.setText("No records found");
		mClassifiedAddress.setText("");
		
		
	}
	
	
	
	
	private void setParseData() {
		
		
		if(mMainGetTodayDetailsModel != null){
		
			if(mMainGetTodayDetailsModel.getSpotLightModel() != null ){
				
				if( mMainGetTodayDetailsModel.getSpotLightModel() .size() > 0){
					
					if(!(mMainGetTodayDetailsModel.getSpotLightModel().size() > mSpotItemCount))
						mSpotItemCount = 0;
					
					
					mMainGetSpotLightModel = mMainGetTodayDetailsModel.getSpotLightModel();
					mSpotlightName.setText(mMainGetSpotLightModel.get(mSpotItemCount).getSpotLightName());
					mSpotlightAddress.setText(mMainGetSpotLightModel.get(mSpotItemCount).getAddressLine1()+" "
							+mMainGetSpotLightModel.get(mSpotItemCount).getAddressLine2());
					//mSpotlightDistance.setText("0.3 mi")	
					mSpotlightDistance.setText(mMainGetSpotLightModel.get(mSpotItemCount).getDistance()+" mi");
					
				}
			
			}
			if(mMainGetTodayDetailsModel.getDealInfoModel() != null ){
			
				if( mMainGetTodayDetailsModel.getDealInfoModel().size() > 0){
					
					if(!(mMainGetTodayDetailsModel.getDealInfoModel().size() > mDealItemCount))
						mDealItemCount = 0;
					
					mMainGetDealsInfoModel = mMainGetTodayDetailsModel.getDealInfoModel();
					mDealName.setText(mMainGetDealsInfoModel.get(mDealItemCount).getDealTittle());
					mDealAddress.setText(mMainGetDealsInfoModel.get(mDealItemCount).getDealInfo());
					mDealDistance.setText(mMainGetDealsInfoModel.get(mDealItemCount).getDistance()+" mi");	
				}
			
			}
			if(mMainGetTodayDetailsModel.getEventsDetailsModel() != null ){
				
				if( mMainGetTodayDetailsModel.getEventsDetailsModel().size() > 0){
								
					if(!(mMainGetTodayDetailsModel.getEventsDetailsModel().size() > mEventItemCount))
						mEventItemCount = 0;
					
					mMainGetEventsDetailsModel = mMainGetTodayDetailsModel.getEventsDetailsModel();
					mEventName.setText(mMainGetEventsDetailsModel.get(mEventItemCount).getEventTitle());
					mEventAddress.setText(mMainGetEventsDetailsModel.get(mEventItemCount).getVenueName());
					
				}
				
			}
			if(mMainGetTodayDetailsModel.getClassfiedItemsModel() != null) {
				
				if( mMainGetTodayDetailsModel.getClassfiedItemsModel().size() > 0){
							
					if(!(mMainGetTodayDetailsModel.getClassfiedItemsModel().size() > mClassItemCount))
						mClassItemCount = 0;
					
					mMainGetClassfiedItemsModel = mMainGetTodayDetailsModel.getClassfiedItemsModel();
					mClassifiedName.setText(mMainGetClassfiedItemsModel.get(mClassItemCount).getClassifiedTitle());
					mClassifiedAddress.setText(mMainGetClassfiedItemsModel.get(mClassItemCount).getClassifiedInfo());
				
				}
				
			}
		}

		

	}
	
	
	
	private void setHomeCategoriesData() {
		
		mHomeCategories.removeAllViews();
		int mWidth = Utils.WIDTH/5;
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(mWidth, RelativeLayout.LayoutParams.WRAP_CONTENT);
		
		
		for(int i=0; i<mHomeCategoryItems.size(); i++){
						
			LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View v = vi.inflate(R.layout.home_business_icon_list_row, null);		
			RelativeLayout mData = (RelativeLayout)v.findViewById(R.id.business_icon_row_REl_data);
			ImageView mImg = (ImageView)v.findViewById(R.id.business_icon_row_IMG_data);
			TextView mName = (TextView)v.findViewById(R.id.business_icon_row_TXT_name);	
			mData.setLayoutParams(layoutParams);
			String name = mHomeCategoryItems.get(i).getCategoryName().toString().split(" ")[0];
			
			if(name.equalsIgnoreCase("real"))
				name = "Real Estate";			
			mName.setText(name);	
			
			v.setId(1);
			v.setTag(i);	
			v.setClickable(true);
			v.setOnClickListener(this);
			new ImageDownloader().download(String.format(Urls.CAT_IMG_URL,mHomeCategoryItems.get(i).getImageurl1()), mImg);
			mHomeCategories.addView(v);
		}
		
		
		LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = vi.inflate(R.layout.home_business_icon_list_row, null);		
		ImageView mImg = (ImageView)v.findViewById(R.id.business_icon_row_IMG_data);
		TextView mName = (TextView)v.findViewById(R.id.business_icon_row_TXT_name);			
		mName.setText("More");	
		v.setId(2);	
		v.setClickable(true);
		v.setOnClickListener(this);	
		mHomeCategories.addView(v);		

		
	}
	
//	****************************	
	
	
//	****************************	
	
//	GetCurrentLocation	

	@Override
	public void getCurrentLocation(String locationName, String addressline,
			String currentState, String currentCountry, double longitude,
			double latitude,String country_short_name,String state_short_name) {
		// TODO Auto-generated method stub

		interruptThread();
		
		if(!foundCurrentLocation ){
			
			progressdialog.dismiss();
			
			if(!locationName.equalsIgnoreCase("")){
				
				
				foundCurrentLocation=true;
				
				Log.e("LogCat", "Location "+locationName);
				CurrentLocationData.CURRENT_CITY_DUMP = CurrentLocationData.CURRENT_CITY  = locationName;
				CurrentLocationData.ADDRESS_LINE = addressline+", "+locationName+", "+currentState;
				CurrentLocationData.CURRENT_STATE = currentState;
				CurrentLocationData.CURRENT_COUNTRY = currentCountry;
				CurrentLocationData.LATITUDE_DUMP = CurrentLocationData.LATITUDE = Double.toString(latitude);
				CurrentLocationData.LONGITUDE_DUMP = CurrentLocationData.LONGITUDE = Double.toString(longitude);
				CurrentLocationData.IS_CURRENT_LOCATION = true;	
				CurrentLocationData.GET_LOCATION = true;
				CurrentLocationData.LOCATION_OLD = CurrentLocationData.LOCATION_NEW;
				CurrentLocationData.LOCATION_NEW = locationName+", "+currentState;
				CurrentLocationData.CURRENT_COUNTRY_SHORT_NAME = country_short_name;
				CurrentLocationData.CURRENT_STATE_SHORT_NAME = state_short_name;
				
				mFetchLocation.setText(CurrentLocationData.ADDRESS_LINE);
				getDashBoardItemsData();				
				
			}else{
				
				mFetchLocation.setText("Location not found");
				progressdialog.dismiss();
				locationNotFound();
			}
				
		}else{

			mFetchLocation.setText("Location not found");
			progressdialog.dismiss();
			locationNotFound();
		}
	}
	
	
//	****************************	

	
	
//	****************************	
	
//	OnclickListener actions
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		
		
		case R.id.mainhome_IMG_kabah:
			
			if(CurrentLocationData.GET_LOCATION){
							
				startActivity(new Intent(MainHomeScreen.this,QiblahPrayerTimingScreen.class).putExtra("showNextDayTimes", showNextDayTimes));				
				
			}else{
				
				Toast.makeText(MainHomeScreen.this, "Currently location not found, select city manually", Toast.LENGTH_SHORT).show();
			}
			
			break;
			
		case R.id.mainhome_LIN_title_prayer:
			
			if(CurrentLocationData.GET_LOCATION){
							
				startActivity(new Intent(MainHomeScreen.this,QiblahPrayerTimingScreen.class).putExtra("showNextDayTimes", showNextDayTimes));
								
			}else{
				
				Toast.makeText(MainHomeScreen.this, "Currently location not found, select city manually", Toast.LENGTH_SHORT).show();
			}
			
			break;
			
		case R.id.mainhome_REL_title_qiblah:
			
			if(CurrentLocationData.GET_LOCATION){
					
				startActivity(new Intent(MainHomeScreen.this,QiblahPrayerTimingScreen.class).putExtra("showNextDayTimes", showNextDayTimes));
				
			}else{
				
				Toast.makeText(MainHomeScreen.this, "Currently location not found, select city manually", Toast.LENGTH_SHORT).show();
			}
			
			break;
			
		case R.id.mainhome_LIN_title_progress:
			
			if(CurrentLocationData.GET_LOCATION){
							
				startActivity(new Intent(MainHomeScreen.this,QiblahPrayerTimingScreen.class).putExtra("showNextDayTimes", showNextDayTimes));
								
			}else{
				
				Toast.makeText(MainHomeScreen.this, "Currently location not found, select city manually", Toast.LENGTH_SHORT).show();
			}
			
			break;
			
		case R.id.mainhome_IMG_hasanat:
			
			if(CurrentLocationData.GET_LOCATION){
				
				Utils.SEARCH_SUB_KEY = "Hasanat";
				startActivity(new Intent(MainHomeScreen.this,HasanatActivity.class));
				
			}else{
				
				Toast.makeText(MainHomeScreen.this, "Currently location not found, select city manually", Toast.LENGTH_SHORT).show();
			}
			break;

		
		case R.id.mainhome_REL_today_spotLight:
			
			if(mMainGetSpotLightModel != null){			
			
				Utils.SEARCH_SUB_KEY = "SpotlLight";
				startActivity(new Intent(MainHomeScreen.this,SpotlightDetailDesp.class)
				.putExtra("data", mMainGetSpotLightModel.get(mSpotItemCount))
				.putExtra("from", "home"));
			
			}else{
				
				Toast.makeText(MainHomeScreen.this, "Currently no data to display", Toast.LENGTH_SHORT).show();
			}
			
			break;
			
			
		case R.id.mainhome_REL_today_spotLight_icon:		
			
			if(CurrentLocationData.GET_LOCATION){
				
				Utils.SEARCH_SUB_KEY = "SpotlLight";
				startActivity(new Intent(MainHomeScreen.this,SpotlightListActivity.class));	
			
			}else{
				
				Toast.makeText(MainHomeScreen.this, "Currently location not found, select city manually", Toast.LENGTH_SHORT).show();
			}
			
			break;			
			
		case R.id.mainhome_REL_today_deals:
			
			if(mMainGetDealsInfoModel != null){
			
				Utils.SEARCH_SUB_KEY = "Deals";
				startActivity(new Intent(MainHomeScreen.this,DealsDetailDesp.class)
				.putExtra("data", mMainGetDealsInfoModel.get(mDealItemCount))
				.putExtra("from", "home"));
			
			}else{
				
				Toast.makeText(MainHomeScreen.this, "Currently no data to display", Toast.LENGTH_SHORT).show();
			}
			
			break;
			
			
		case R.id.mainhome_REL_today_deals_icon:
			
			if(CurrentLocationData.GET_LOCATION){
			
				if(mMoreCatDone){
					
					Utils.SEARCH_SUB_KEY = "Deals";
					startActivity(new Intent(MainHomeScreen.this,DealsListActivity.class)
					.putExtra("cat", mHomeCategoryItems)
					.putExtra("morecat", mHomeMoreCategoryItems));
					
				}else{
					
					Toast.makeText(MainHomeScreen.this, "Loding Categories please wait..", Toast.LENGTH_SHORT).show();
					
				}
				
			}else{
				
				Toast.makeText(MainHomeScreen.this, "Currently location not found, select city manually", Toast.LENGTH_SHORT).show();
			}
			
			break;
			
		case R.id.mainhome_REL_today_events_icon:
			
			if(CurrentLocationData.GET_LOCATION){
			
				Utils.SEARCH_SUB_KEY = "Event";
				startActivity(new Intent(MainHomeScreen.this,EventsCalendarActivity.class));
			
			}else{
				
				Toast.makeText(MainHomeScreen.this, "Currently location not found, select city manually", Toast.LENGTH_SHORT).show();
			}
			
			break;
			
		case R.id.mainhome_REL_today_events:
			
			if(mMainGetEventsDetailsModel != null){
			
				Utils.SEARCH_SUB_KEY = "Event";
				startActivity(new Intent(MainHomeScreen.this,CalendarEventDetailDesc.class)
				.putExtra("data", mMainGetEventsDetailsModel.get(mEventItemCount))
				.putExtra("from", "home"));
			
			}else{
				
				Toast.makeText(MainHomeScreen.this, "Currently no data to display", Toast.LENGTH_SHORT).show();
			}
			
			break;
			
		case R.id.mainhome_REL_today_class_icon:
			
			if(CurrentLocationData.GET_LOCATION){
				
				Utils.SEARCH_SUB_KEY = "Classified";
				startActivity(new Intent(MainHomeScreen.this,ClassifiedListActivity.class));
			
			}else{
				
				Toast.makeText(MainHomeScreen.this, "Currently location not found, select city manually", Toast.LENGTH_SHORT).show();
			}
			
			break;
			
		case R.id.mainhome_REL_today_class:
			
			if(mMainGetClassfiedItemsModel != null){
			
				Utils.SEARCH_SUB_KEY = "Classified";
				startActivity(new Intent(MainHomeScreen.this,ClassifiedItemDetailDesp.class)
				.putExtra("data", mMainGetClassfiedItemsModel.get(mClassItemCount))
				.putExtra("from", "home"));
			
			}else{
				
				Toast.makeText(MainHomeScreen.this, "Currently no data to display", Toast.LENGTH_SHORT).show();
			}
			
			break;
			
		case R.id.mainhome_BTN_current_location:		
				
				mBtnSelectCity = true;
				startActivityForResult(new Intent(MainHomeScreen.this,SelectCityScreen.class), SELECT_CITY);			
			
			
			break;
			
		case R.id.mainhome_BTN_my_stuff:		
			
			startActivity(new Intent(MainHomeScreen.this,MyStuffActivity.class)
			.putExtra("from", "home")); 			
		
		
		break;
			
			
		case 1:
			
			if(CurrentLocationData.GET_LOCATION){
				
				String categoryName = mHomeCategoryItems.get((Integer) v.getTag()).getCategoryName();
				Utils.SEARCH_SUB_KEY = categoryName;
				
				startActivity(new Intent(MainHomeScreen.this,BusinessListByID.class)
				.putExtra("categoryId", mHomeCategoryItems.get((Integer) v.getTag()).getCategoryId())
				.putExtra("categoryName", categoryName)
				.putExtra("from", "home"));
			
			}else{
				
				Toast.makeText(MainHomeScreen.this, "Currently location not found, select city manually", Toast.LENGTH_SHORT).show();
			}
			
			break;
			
		case 2:
			
			if(CurrentLocationData.GET_LOCATION){			
			
				Utils.SEARCH_SUB_KEY = "";
				startActivity(new Intent(MainHomeScreen.this,BusinessMoreCategories.class)
				.putExtra("data", mHomeMoreCategoryItems )
				.putExtra("from", "home"));										
			
			}else{
				
				Toast.makeText(MainHomeScreen.this, "Currently location not found, select city manually", Toast.LENGTH_SHORT).show();
			}
			
			break;
			
			
		case R.id.mainhome_IMG_masjid:
			
			if(CurrentLocationData.GET_LOCATION){
			
				Utils.SEARCH_SUB_KEY = "Masjid";
				startActivity(new Intent(MainHomeScreen.this,MasjidMapActivity.class)
				.putExtra("from", "home"));
			
			}else{
				
				Toast.makeText(MainHomeScreen.this, "Currently location not found, select city manually", Toast.LENGTH_SHORT).show();
			}
			
			break;
			
			
		case R.id.mainhome_IMG_tripplan:
			
			if(CurrentLocationData.GET_LOCATION){
			
				startActivity(new Intent(MainHomeScreen.this,TripPlannerActivity.class));
		
			}else{
				
				Toast.makeText(MainHomeScreen.this, "Currently location not found, select city manually", Toast.LENGTH_SHORT).show();
			}
			
			break;
		
		
		case R.id.mainhome_IMG_advertise:
			
			if(!sharedpreference.getString("userid", "0").equalsIgnoreCase("0")){
				
				startActivity(new Intent(MainHomeScreen.this,AdvertiseActivity.class));
							
			}else{
						
				myAlertDialog();
			}		
//			
			break;
			
			
		case R.id.mainhome_IMG_more:
			
//			if(CurrentLocationData.GET_LOCATION){
			
			startActivity(new Intent(MainHomeScreen.this,MoreOptionList.class));
			
//			}else{
//				
//				Toast.makeText(MainHomeScreen.this, "Currently location not found, select city manually", Toast.LENGTH_SHORT).show();
//			}
			
			break;
			
		case R.id.editText1:
			
			Utils.SEARCH_SUB_KEY = "";
			 startActivity(new Intent(MainHomeScreen.this, GlobalSearchActivity.class));
			 
			 break;
		
		}
		
	}	
	
	
	
//	****************************	
	
	
	
	
//	****************************	
	
//	WebService call
	
	
	private void getCurrentLocation(){
		
		if(NetworkConnectionHelper.checkNetworkAvailability()){
			
	   		    if(!CurrentLocationData.IS_CURRENT_LOCATION){
	   		    	
	   		    	progressdialog = ProgressDialog.show(MainHomeScreen.this, "","Fetching current location...", true);
		   		    mFetchLocation.setText("Fetching Location");
			    	mLocationHelper = new  LocationHelper(this);
			    	
			    	
			    	myThread = new Thread(new Runnable() {
	
						@Override
						public void run() {
	
							try {
								Log.e("****before Thread", "getting location");
//								Thread.sleep(100);
								Thread.sleep(30000);
								mLocationHelper.stopFetchingCurrentLocation();
								mainLocationHandler.sendEmptyMessage(0);
	
								Log.e("****after Thread", "getting loaction");
	
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								Log.e("****Thread", "InterruptedException");
							}
	
						}
					});
					myThread.start();
				
			    }	
		    
			}else{
			
				mFetchLocation.setText("Location not found");
				networkNotFound();
			
		}
		
	}
	
	MainGetTodayDetailsModel mMainGetTodayDetailsModel = null;
	
	public void getDashBoardItemsData(){
    	
		progressdialog = ProgressDialog.show(MainHomeScreen.this, "","Loading data..", true);
    	
		try {
			
			
			
			
			
			String mCity = URLEncoder.encode(CurrentLocationData.CURRENT_CITY, "UTF-8");
			
			new mAsyncClassifiedsFeedFetcher(
					String.format(Urls.HOME_PAGE_DASHBOARD_ITEMS_URL,
							mCity,
							CurrentLocationData.LATITUDE,
							CurrentLocationData.LONGITUDE,
							Utils.RANGE),new ParserHandler()).start();
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
	    
	    
	    class mAsyncClassifiedsFeedFetcher extends Thread
		{
			String  feedUrl;
			Handler handler;
			
			public mAsyncClassifiedsFeedFetcher(String mUrl, Handler handler) throws MalformedURLException {
			
				
				Log.i("", "mUrl********* "+mUrl);
				
//				String url=String.format(mUrl, brandId);
				
				this.feedUrl = mUrl;			
				this.handler = handler;
			
			} 
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try
				{
					
					new GetDashBoardItemsParser(feedUrl,handler).parser();
				
				}catch(Exception e){
					
					e.printStackTrace();
				}
				super.run();
			}
			
		}
	    
	    
	   
		class ParserHandler extends Handler
		{
			public void handleMessage(android.os.Message msg)
			{
				
				progressdialog.dismiss();
				clearData();
				
				if(msg.what==0)
				{	
					
					mMainGetTodayDetailsModel=null;					
					mMainGetTodayDetailsModel = (MainGetTodayDetailsModel) msg.getData().getSerializable("datafeeds");
					setParseData();					
					

				}else if(msg.what==1){
					
					new FeedsAreUnavialableDialog(MainHomeScreen.this).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
							
						}
						
					}).create().show();
					
					
				}else if(msg.what==2){
					
					new ServerErrorDialog(MainHomeScreen.this).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface dialog, int which) {
							
//							MainHomeScreen.this.finish();
							
						}
						
					}).create().show();
				}
				
				
			}
		}
		
	
		ArrayList<GetHomePageCategoriesModel> mHomeCategoryItems = null, mHomeMoreCategoryItems = null;
		
		public void getHomeCategoriesData(){
	    	
//			progressdialog = ProgressDialog.show(MainHomeScreen.this, "","Please wait...", true);
	    	
			try {
				
				new mAsyncCategoriesFeedFetcher(Urls.HOME_PAGE_CATEGORIES_URL,new CategoriesParserHandler()).start();
				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	    }
		    
		    
		    class mAsyncCategoriesFeedFetcher extends Thread
			{
				String  feedUrl;
				Handler handler;
				
				public mAsyncCategoriesFeedFetcher(String mUrl, Handler handler) throws MalformedURLException {
				
					
					Log.i("", "mUrl********* "+mUrl);

					
					this.feedUrl = mUrl;			
					this.handler = handler;
				
				} 
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try
					{
						
						new GetHomePageCategoriesParser(feedUrl,handler).parser();
					
					}catch(Exception e){
						
						e.printStackTrace();
					}
					super.run();
				}
				
			}
		    
		    
		   
			class CategoriesParserHandler extends Handler
			{
				public void handleMessage(android.os.Message msg)
				{
					
//					progressdialog.dismiss();
					
					if(msg.what==0)
					{	
						
						Log.e("", "mHomeCategoryItems ");
						
						mHomeCategoryItems = (ArrayList<GetHomePageCategoriesModel>) msg.getData().getSerializable("datafeeds");
						setHomeCategoriesData();
						getHomeMoreCategoriesData();

					}
					
					
				}
			}
			
			
			public void getHomeMoreCategoriesData(){
		    	
//				progressdialog = ProgressDialog.show(MainHomeScreen.this, "","Please wait...", true);
		    	
				try {
					new mAsyncMoreCategoriesFeedFetcher(Urls.HOME_PAGE_MORE_CATEGORIES_URL,new MoreCategoriesParserHandler()).start();
					
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	
		    }
			    
			    
			    class mAsyncMoreCategoriesFeedFetcher extends Thread
				{
					String  feedUrl;
					Handler handler;
					
					public mAsyncMoreCategoriesFeedFetcher(String mUrl, Handler handler) throws MalformedURLException {
					
						
						Log.i("", "mUrl********* "+mUrl);

						
						this.feedUrl = mUrl;			
						this.handler = handler;
					
					} 
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try
						{
							
							new GetHomePageCategoriesParser(feedUrl,handler).parser();
						
						}catch(Exception e){
							
							e.printStackTrace();
						}
						super.run();
					}
					
				}
			    
			    
			   
				class MoreCategoriesParserHandler extends Handler
				{
					public void handleMessage(android.os.Message msg)
					{
						
//						progressdialog.dismiss();
						
						if(msg.what==0)
						{	
							
							Log.e("", "mHomeCategoryItems ");
							
							mHomeMoreCategoryItems = (ArrayList<GetHomePageCategoriesModel>) msg.getData().getSerializable("datafeeds");							
							mMoreCatDone = true;
						}						
					}
				}
				
//	****************************	
	
	
//	****************************	
	
//	Setting Animation and rotating the compass	
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		
		
		super.onWindowFocusChanged(hasFocus);
		AnimationDrawable frameAnimation = (AnimationDrawable) mImgKabah
				.getBackground();
		if (hasFocus) {
			frameAnimation.start();
		} else {
			frameAnimation.stop();
		}
	}

//	Handling the prayer position 
	
	@Override
	public void onAccuracyChanged(int sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	// Listen to sensor and provide output
	  public void onSensorChanged(int sensor, float[] values) {
	    if (sensor != MainHomeScreen.sensor)
	      return;
	    int orientation = (int) -values[0];
	    mQiblahRotate.setDirection(orientation);
	  }
	  
	  
//		****************************	  
//		Alert Dialogs 
//		****************************

	  
	  private void locationNotFound(){

		  new LocationNotFoundDialog(MainHomeScreen.this).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener() {
			
			
			  @Override
			
			  public void onClick(DialogInterface dialog, int which) {

			
			  }
			
		
		  }).create().show();
	  
	  }	  
	  
	  private void networkNotFound(){

		  new NetworkNotFoundDialog(MainHomeScreen.this).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener() {
			
			
			  @Override
			
			  public void onClick(DialogInterface dialog, int which) {
				
				
			
			  }
			
		
		  }).create().show();
	  
	  }
	  
	  public Handler mainLocationHandler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				progressdialog.dismiss();
				if (msg.what == 0) {
					
					interruptThread();
					mFetchLocation.setText("Location not found");					
					locationNotFound();
					
					
				}}};
				
				
				
				private void interruptThread() {

					try {
						
						if (myThread != null)
							myThread.interrupt();
						
					} catch (Exception e) {
						// TODO: handle exception
					}
					
					
				}
//	****************************
//	Get Prayer Time

	private boolean showNextDayTimes = false;
	
	private void setTimings() {
		
//		 if(prayTimeStatus.getBoolean("prayertimes", false)){
//			 
//			 mPrayerTimings.setVisibility(View.VISIBLE);
//			 mPrayerProgress.setVisibility(View.GONE);
//		 }
//		 
		 Log.e("", "****************** prayertimes ");
		
		ArrayList<String> prayerNames = new ArrayList<String>();
	    prayerNames.add("Fajr   ");
	    prayerNames.add("Shuruk ");
	    prayerNames.add("Dhuhr  ");
	    prayerNames.add("Asr    ");
	    prayerNames.add("Maghrib");
	    prayerNames.add("Isha   ");
		
		try {
			
			PrayerTimeModel mTimeModel, mNextTimeModel;
			
			mDatabaseHelper.openDataBase();
		
			int dd = today.get(Calendar.DAY_OF_MONTH);
			int mm = today.get(today.MONTH) + 1;
			
			mTimeModel = mDatabaseHelper.getPrayerTimingsbyDay(String.valueOf(dd), String.valueOf(mm));

			mNextTimeModel = mDatabaseHelper.getPrayerTimingsbyDay(String.valueOf(dd+1), String.valueOf(mm));
			
			mDatabaseHelper.closeDataBase();		
			
			Log.e("", " ******** mTimeModel.prayerTimings "+mTimeModel.prayerTimings.size());
			
			boolean flag = true;
			for (int i = 0; i < mTimeModel.prayerTimings.size(); i++) {
				
				long time = new DateFormater().parseDate(mTimeModel.prayerTimings.get(i),"hh:mm a");
				Date date = new Date(time);
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);
				calendar.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
						cal.get(Calendar.DAY_OF_MONTH));
				
//				Log.e("", "System.currentTimeMillis() "+System.currentTimeMillis());
//				Log.e("", "calendar.getTimeInMillis() "+calendar.getTimeInMillis());
//				Log.e("", "prayerNames "+prayerNames);
//				Log.e("", "prayerNames i "+prayerNames.get(i));
//				Log.e("", "prayerTimings i "+mTimeModel.prayerTimings.get(i));
				
				
				if (System.currentTimeMillis() <= calendar.getTimeInMillis()) {
					if (flag) {
						flag = false;
						Log.e("", "**********************");
						Log.e("", "prayerNames  "+prayerNames.get(i));
						Log.e("", "prayerTimings  "+mTimeModel.prayerTimings.get(i));
						Log.e("", "**********************");
						mPrayerTimings.setVisibility(View.VISIBLE);
						mPrayerProgress.setVisibility(View.GONE);
						mPrayerName.setText(prayerNames.get(i));
						mPrayerTime.setText(mTimeModel.prayerTimings.get(i));
						showNextDayTimes = false;
					}
				}
			}
			
			if(flag){
				
				if(mTimeModel != null){
					
					if(mTimeModel.prayerTimings.size() > 0){				
				
						
						mPrayerName.setText(prayerNames.get(0));
						mPrayerTime.setText(mNextTimeModel.prayerTimings.get(0));
						Log.e("", "mNextTimeModel.prayerTimings.get(0) "+mNextTimeModel.prayerTimings.get(0));
						mPrayerTimings.setVisibility(View.VISIBLE);
						mPrayerProgress.setVisibility(View.GONE);
						showNextDayTimes = true;
					}
					
				}
				
			}
			
		} catch (Exception e7) {
			e7.printStackTrace();
			showNextDayTimes = true;
			
		}
	}

	
	
	
//	 Alert Dailogs
	
	
	private void myAlertDialog(){
		
		
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Daleelo");
		builder.setMessage("Please login to post your Advertise")
		       .setCancelable(false)
		       .setPositiveButton("Login", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   
						startActivity(new Intent(MainHomeScreen.this,LoginActivity.class)
						.putExtra("from", "forads"));
		               
		           }
		       })
		       .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                dialog.cancel();
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	
	private void alertForGps(){
		
		
		String msg = "GPS Provider not available, switch on the GPS for better performance";
		new AlertDialogMsg(MainHomeScreen.this, msg).setPositiveButton("Ok", new android.content.DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				initializeUI();		
				
			}
		}).create().show();
	}	  

}