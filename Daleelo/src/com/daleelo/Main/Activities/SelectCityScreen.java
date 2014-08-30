package com.daleelo.Main.Activities;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daleelo.R;
import com.daleelo.DataBase.DatabaseHelper;
import com.daleelo.Dialog.LocationNotFoundDialog;
import com.daleelo.Dialog.NetworkNotFoundDialog;
import com.daleelo.Main.Model.GetCitiesModel;
import com.daleelo.Qiblah.Activites.CancelAlarms;
import com.daleelo.Qiblah.Activites.DailyPrayerTimeParser;
import com.daleelo.Qiblah.Activites.PrayerTimeModel;
import com.daleelo.Qiblah.Activites.PrayerTimeParser;
import com.daleelo.Qiblah.Activites.QiblahSettingsActivity;
import com.daleelo.Qiblah.Activites.TimeZoneModel;
import com.daleelo.Qiblah.Activites.TimeZoneParser;
import com.daleelo.Utilities.CurrentLocationData;
import com.daleelo.Utilities.Urls;
import com.daleelo.Utilities.Utils;
import com.daleelo.location.helper.CurrentLocation;
import com.daleelo.location.helper.LocationHelper;
import com.daleelo.location.helper.NetworkConnectionHelper;

public class SelectCityScreen extends ListActivity implements CurrentLocation{

	ArrayList<GetCitiesModel> 	mGetRecentCitiesModelFeeds = null,
								mGetCitiesModelFeeds = null,
								mGetCitiesModelFeedsDump = null;
	
	GetCitiesModel mCurrentLocationModel;
	
	AppCitiesAdapter mAppCitiesAdapter, mAppRecentCitiesAdapter;
	DatabaseHelper mDbHelper;		
	EditText filter_edittext;
	
	boolean main_list = true;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.main_home_select_city);		
		initializeUI();	
		
	}
	
	
	private void initializeUI(){
		
		filter_edittext = (EditText)findViewById(R.id.app_city_TXT_serach);
//		mCitiesList = (ListView)findViewById(R.id.app_city_LIST_view);	
		mCityParserHandler = new CityParserHandler();
		Log.e("", "initializeUI");
		
		filter_edittext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
		    @Override
		    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
		        	new AsynGetLatLong().execute();
		            return true;
		        }
		        return false;
		    }
		});
		
		new mAsyncFetchCityFormDB().start();
		
		mCurrentLocationModel = new GetCitiesModel();
		mCurrentLocationModel.setCityID("0");
		mCurrentLocationModel.setCityName("Current Location");
		
		
		
	}	
	
	private void setDataToList(){
		
		mAppRecentCitiesAdapter = new AppCitiesAdapter(this, mGetRecentCitiesModelFeeds);
		mAppCitiesAdapter = new AppCitiesAdapter(this, mGetCitiesModelFeeds);
		setListAdapter(mAppRecentCitiesAdapter);		
		
		filter_edittext.addTextChangedListener(new TextWatcher() { 
	       	 
			@Override 
			public void onTextChanged(CharSequence s, int start, int before, 
			int count) { 
				  
				mGetCitiesModelFeeds.clear();
				mAppCitiesAdapter.clear();
				
		          int textlength = filter_edittext.getText().length();
		          
		         
				if(textlength==0){
					
		        	  main_list=true;
		        	  setListAdapter(mAppRecentCitiesAdapter);
		        	  
		          }else{
		        	  
		        	  main_list=false;
		        	  
		        	  mGetCitiesModelFeeds.add( mCurrentLocationModel);
		        	
			           for (int i = 0; i < mGetCitiesModelFeedsDump.size(); i++)
			           {		 
			        	
			        	   try{
			        	 
					         if(filter_edittext.getText().toString().trim().toLowerCase().equalsIgnoreCase(
					          (String)mGetCitiesModelFeedsDump.get(i).getCityName().trim().toLowerCase().subSequence(0,textlength)))
					          {
					        	 
					        	 mGetCitiesModelFeeds.add(mGetCitiesModelFeedsDump.get(i));		        	 
					              
					          }			
			        	   }catch (Exception e) {
							// TODO: handle exception
						}
			          }
//			          mAppCitiesAdapter.notifyDataSetChanged(); 
			          setListAdapter(mAppCitiesAdapter);	           
		          }		          
			} 
			
			@Override 
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {} 
			
			@Override 
			public void afterTextChanged(Editable s) {} 
		});
		
	}
	
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		CurrentLocationData.LOCATION_OLD = CurrentLocationData.LOCATION_NEW;
		
		if(main_list){			
			
			CurrentLocationData.LOCATION_NEW = mGetRecentCitiesModelFeeds.get(position).getCityName();	
			Log.e("", "CurrentLocationData.LOCATION_NEW "+CurrentLocationData.LOCATION_NEW);
			
			if(!mGetRecentCitiesModelFeeds.get(position).getCityName().equalsIgnoreCase("Current Location")){
							
				setLocationDump();
				
				
				
				CurrentLocationData.CURRENT_CITY  = mGetRecentCitiesModelFeeds.get(position).getCityName().split(",")[0];
				CurrentLocationData.ADDRESS_LINE = mGetRecentCitiesModelFeeds.get(position).getCityName();
				CurrentLocationData.CURRENT_STATE = mGetRecentCitiesModelFeeds.get(position).getCityName().split(",")[1];
				CurrentLocationData.CURRENT_COUNTRY = mGetRecentCitiesModelFeeds.get(position).getCountry_code();
				CurrentLocationData.CURRENT_STATE_SHORT_NAME = mGetRecentCitiesModelFeeds.get(position).getStateCode();
				CurrentLocationData.CURRENT_COUNTRY_SHORT_NAME = mGetRecentCitiesModelFeeds.get(position).getCountry_code();
				CurrentLocationData.LATITUDE = mGetRecentCitiesModelFeeds.get(position).getLatitude();
				CurrentLocationData.LONGITUDE = mGetRecentCitiesModelFeeds.get(position).getLongitude();
				CurrentLocationData.IS_CURRENT_LOCATION = false;	
				CurrentLocationData.GET_LOCATION = true;
				CurrentLocationData.LOCATION_NEW = mGetRecentCitiesModelFeeds.get(position).getCityName();
				
				myAlertDialog();				
			
			}else{
				
				getCurrentLocation();
				
				
			}
			
			
					
			 
			
		}else{			
			
			CurrentLocationData.LOCATION_NEW = mGetCitiesModelFeeds.get(position).getCityName();
			Log.e("", "CurrentLocationData.LOCATION_NEW "+CurrentLocationData.LOCATION_NEW);

			
			if(!mGetCitiesModelFeeds.get(position).getCityName().equalsIgnoreCase("Current Location")){
				
				mDbHelper.insertRecentCity(
						mGetCitiesModelFeeds.get(position).getCityID(),
						mGetCitiesModelFeeds.get(position).getCityName(), 
						mGetCitiesModelFeeds.get(position).getStateCode(),
						mGetCitiesModelFeeds.get(position).getLatitude(), 
						mGetCitiesModelFeeds.get(position).getLongitude(),
						mGetCitiesModelFeeds.get(position).getCountry_code());	
				
				setLocationDump();
				
				CurrentLocationData.CURRENT_CITY  = mGetCitiesModelFeeds.get(position).getCityName().split(",")[0];
				CurrentLocationData.ADDRESS_LINE = mGetCitiesModelFeeds.get(position).getCityName();				
				CurrentLocationData.CURRENT_STATE = mGetCitiesModelFeeds.get(position).getStateCode();
				CurrentLocationData.CURRENT_STATE_SHORT_NAME = mGetCitiesModelFeeds.get(position).getStateCode();
				CurrentLocationData.CURRENT_COUNTRY_SHORT_NAME = mGetCitiesModelFeeds.get(position).getCountry_code();
				CurrentLocationData.LATITUDE = mGetCitiesModelFeeds.get(position).getLatitude();
				CurrentLocationData.LONGITUDE = mGetCitiesModelFeeds.get(position).getLongitude();
				CurrentLocationData.IS_CURRENT_LOCATION = false;	
				CurrentLocationData.GET_LOCATION = true;
				CurrentLocationData.LOCATION_NEW = mGetCitiesModelFeeds.get(position).getCityName();
				
				myAlertDialog();
				
			}else{
				
				getCurrentLocation();
								
			}
		}
		
		mDbHelper.closeDataBase();
		
	}
	
	
	
	
	private void changeLocation(){
		
		
		mDbHelper.insertRecentCity(
				"0",
				mCity+", "+mStateCode, 
				mStateCode,
				mLat, 
				mLong,
				mCounteyCode);	
		
		setLocationDump();
		
		CurrentLocationData.CURRENT_CITY  = mCity;
		CurrentLocationData.ADDRESS_LINE = mCity+", "+mStateCode;
		CurrentLocationData.CURRENT_STATE = mStateCode;
		CurrentLocationData.CURRENT_STATE_SHORT_NAME = mStateCode;
		CurrentLocationData.CURRENT_COUNTRY_SHORT_NAME = mCounteyCode;
		CurrentLocationData.LATITUDE = mLat;
		CurrentLocationData.LONGITUDE = mLong;
		CurrentLocationData.IS_CURRENT_LOCATION = false;	
		CurrentLocationData.GET_LOCATION = true;
		CurrentLocationData.LOCATION_NEW = mCity;
		
		 mDbHelper.closeDataBase();
		 myAlertDialog();
	
	}
	
	private void setLocationDump(){
		
		CurrentLocationData.CURRENT_CITY_DUMP = CurrentLocationData.CURRENT_CITY;
		CurrentLocationData.ADDRESS_LINE_DUMP = CurrentLocationData.ADDRESS_LINE;
		CurrentLocationData.CURRENT_STATE_DUMP = CurrentLocationData.CURRENT_STATE;
		CurrentLocationData.CURRENT_COUNTRY_DUMP  = CurrentLocationData.CURRENT_COUNTRY; 
		CurrentLocationData.CURRENT_STATE_SHORT_NAME_DUMP = CurrentLocationData.CURRENT_STATE_SHORT_NAME;
		CurrentLocationData.CURRENT_COUNTRY_SHORT_NAME_DUMP = CurrentLocationData.CURRENT_COUNTRY_SHORT_NAME;
		CurrentLocationData.LONGITUDE_DUMP = CurrentLocationData.LONGITUDE;
		CurrentLocationData.LATITUDE_DUMP = CurrentLocationData.LATITUDE;		
		
	}
	
	private void getLocationDump(){
		
		CurrentLocationData.CURRENT_CITY = CurrentLocationData.CURRENT_CITY_DUMP;
		CurrentLocationData.ADDRESS_LINE = CurrentLocationData.ADDRESS_LINE_DUMP;
		CurrentLocationData.CURRENT_STATE = CurrentLocationData.CURRENT_STATE_DUMP;
		CurrentLocationData.CURRENT_COUNTRY = CurrentLocationData.CURRENT_COUNTRY_DUMP; 
		CurrentLocationData.CURRENT_STATE_SHORT_NAME = CurrentLocationData.CURRENT_STATE_SHORT_NAME_DUMP;
		CurrentLocationData.CURRENT_STATE_SHORT_NAME = CurrentLocationData.CURRENT_STATE_SHORT_NAME_DUMP;
		CurrentLocationData.LONGITUDE = CurrentLocationData.LONGITUDE_DUMP;
		CurrentLocationData.LATITUDE = CurrentLocationData.LATITUDE_DUMP;
		CurrentLocationData.IS_CURRENT_LOCATION = true;
		
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		mDbHelper.closeDataBase();
	}

		
	
	
	
	class AppCitiesAdapter extends ArrayAdapter<GetCitiesModel>
	{
	
		ArrayList<GetCitiesModel> feed;
		Context context;
		public AppCitiesAdapter(Context context, ArrayList<GetCitiesModel> feed) 
		{
			super(context, R.layout.city_list_row,feed);
			this.context=context;
			this.feed=feed;
		}

		

		@Override
		public GetCitiesModel getItem(int position) {
			// TODO Auto-generated method stub
			return feed.get(position);
		}		

	

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) 
		{
			
			LayoutInflater inflater= LayoutInflater.from(context);
			convertView=inflater.inflate(R.layout.city_list_row, null);		
			TextView txt_type_name=(TextView)convertView.findViewById(R.id.city_TXT_list_row_name);	
			if(position == 0){
				txt_type_name.setTextColor(Color.BLUE);
				txt_type_name.setTextSize(18);
				
			}
			txt_type_name.setText(feed.get(position).getCityName());
					
			return convertView;
		}
		
	}
	
	
	
	



	class mAsyncFetchCityFormDB extends Thread
	{
		

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try
			{
							
				mDbHelper= new DatabaseHelper(getApplicationContext());			
//				mDbHelper.createDataBase();
				mDbHelper.openDataBase();
				mGetRecentCitiesModelFeeds = mDbHelper.getRecentCitiesFromDB();
				mGetCitiesModelFeedsDump = mDbHelper.getCitiesFromDB();
				mGetCitiesModelFeeds = new ArrayList<GetCitiesModel>();
				mCityParserHandler.sendEmptyMessage(0);
				Log.e("", "mAsyncFetchCityFormDB");
				
			}catch(Exception e){
				e.printStackTrace();
			}
			super.run();
		}
		
	}
	
	
	
	
	CityParserHandler mCityParserHandler;
	
	class CityParserHandler extends Handler
	{
		public void handleMessage(android.os.Message msg)
		{
			Log.e("", "CityParserHandler");
			
			if(msg.what==0)
			{
				Log.e("", "CityParserHandler  wnat");
				setDataToList();
			}			
			
		}
	}
	
	
	
	
	
//	*******************************
//	get lat long by address
	
	
	class AsynGetLatLong extends AsyncTask<Void, Void, String>
    {
		//String result;
    	 ProgressDialog mProgressDialog;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			this.mProgressDialog=ProgressDialog.show(SelectCityScreen.this, "", "Validating location..");
		}

		@Override
		protected String doInBackground(Void... params) {
			
			getLatLong(getLocationInfo(filter_edittext.getText().toString()));
			
			return mCity;
  
			
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			mProgressDialog.dismiss();
			
			if(mCity.equalsIgnoreCase("none")){				
				
				Toast.makeText(SelectCityScreen.this, "Please enter vaild address..", Toast.LENGTH_SHORT).show();
				
			}else{
				
				changeLocation();
				Toast.makeText(SelectCityScreen.this, mCity, Toast.LENGTH_SHORT).show();				
			}		
		}    	
    }
	
	
	
	public double longitute, latitude;
	private String mCity = "none", mStateCode = "", mCounteyCode = "";
	private String mLat, mLong;
	
	public static JSONObject getLocationInfo(String address) {
        StringBuilder stringBuilder = new StringBuilder();
        try {

        address = address.replaceAll(" ","%20");  
        Log.e("", "url "+"http://maps.google.com/maps/api/geocode/json?address=" + address + "&sensor=false");
        HttpPost httppost = new HttpPost("http://maps.google.com/maps/api/geocode/json?address=" + address + "&sensor=false");
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        stringBuilder = new StringBuilder();


            response = client.execute(httppost);
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();
            int b;
            while ((b = stream.read()) != -1) {
                stringBuilder.append((char) b);
            }
        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(stringBuilder.toString());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return jsonObject;
    }
	
	
	public boolean getLatLong(JSONObject jsonObject) {

        try {
        	
            latitude = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                .getJSONObject("geometry").getJSONObject("location")
                .getDouble("lat");
            
            mLat = latitude+"";
            
            longitute = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
            .getJSONObject("geometry").getJSONObject("location")
            .getDouble("lng");
    	
            mLong  = longitute+"";
            
            JSONObject tempJSONObject;
            
            try {				
			
	            for(int i=0 ; i<4 ; i++){
	            	
		            tempJSONObject =  
		            	((JSONArray)jsonObject.get("results"))
		            	.getJSONObject(0)
		            	.getJSONArray("address_components")
		            	.getJSONObject(i);
		            
		            String strLog = tempJSONObject.getJSONArray("types").get(0).toString();
		            Log.e("", "********String area"+strLog);
		            
		            if( strLog.toString().equalsIgnoreCase("locality") ){
		            
		            	mCity = tempJSONObject.getString("long_name");
		            
		            }else if( strLog.equalsIgnoreCase("administrative_area_level_1") ){
		                
		            	mStateCode = tempJSONObject.getString("short_name");
		            	
		            }else if( strLog.equalsIgnoreCase("country") ){
		                
		            	mCounteyCode = tempJSONObject.getString("short_name");
		            }	            	
	           
	            }
            
            } catch (Exception e) {
				// TODO: handle exception
            	e.printStackTrace();
			}
            
            Log.e("", "longitute "+longitute+" latitude "+latitude+" mCityAddress "+mCity+" mStaeCode "+mStateCode+" mCountryCode "+mCounteyCode);

         
           
            

        } catch (JSONException e) {
        	
        	e.printStackTrace();
            return false;

        }

        return true;
    }
	
	
	

//	*******************************
//	Prayer Timmings
//	*******************************
	
	
	private Timer timer = new Timer();
	private boolean remainTime = true;
	private TimeZoneModel mTimeZoneModel = null;
	private PrayerTimeModel mPrayerTimeModel = null;
	private ArrayList<PrayerTimeModel> prayerTimes = new ArrayList<PrayerTimeModel>();
	
	private SharedPreferences prayTimeStatus; 
	private int mCalMethodType;
	
	Calendar today ;
	
	ProgressDialog mProgressDialog;
	

	
	private void setTimings() {		
		
		mProgressDialog = ProgressDialog.show(SelectCityScreen.this, null, "Please wait..");
		
		
		
		today = Calendar.getInstance();
	
		prayTimeStatus = getSharedPreferences("praytimestatus", MODE_PRIVATE);
		mCalMethodType = prayTimeStatus.getInt("prayermethod", 5);	

		Log.e("prayer TIme", "*************************downloading prayer times");
		prayTimeStatus.edit().putString("month",String.valueOf(today.get(Calendar.MONTH))).commit();
		prayTimeStatus.edit().putString("year",String.valueOf(today.get(Calendar.YEAR))).commit();
		prayTimeStatus.edit().putString("city",CurrentLocationData.CURRENT_CITY).commit();		
		
		CancelAlarms cancelAlarms = new CancelAlarms();
		cancelAlarms.deletePrevious(getApplicationContext());
		
		new AsynTimeZoneGetter().execute();
		
	}				
//			5.00 6.00 12.20
	

	class AsynTimeZoneGetter extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			String url = String.format(Urls.TIME_ZONE_URL_ONE,
					CurrentLocationData.LATITUDE,
					CurrentLocationData.LONGITUDE);
			Log.e("", "************url "+url);
			new TimeZoneParser(url,prayerTimeHandler).parser();
			
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
                         prayerTimeHandler).parser();   

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				
				

			}
			return null;
		}

	}

	Handler prayerTimeHandler = new Handler() {
		PrayerTimeModel mTimeModel;
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);

			if (msg.what == TimeZoneParser.FOUND_RESULT) {

				mTimeZoneModel = (TimeZoneModel) msg.getData().get("data");
				prayTimeStatus.edit().putString("timezoneoffset", mTimeZoneModel.getOffset()).commit();
			
			} else if (msg.what == PrayerTimeParser.FOUND_RESULT) {
				
				mDbHelper.openDataBase();				
				mDbHelper.deletPrayTimes();
				
				prayerTimes = (ArrayList<PrayerTimeModel>) msg.getData().get("data");
				for(int i = 0; i<prayerTimes.size(); i++){
					mTimeModel = prayerTimes.get(i);
					long status = mDbHelper.insertPrayerTimings(mTimeModel.getDate(), mTimeModel.getMonth(), mTimeModel.getYear(), mTimeModel.prayerTimings.get(0), mTimeModel.prayerTimings.get(1), mTimeModel.prayerTimings.get(2), mTimeModel.prayerTimings.get(3), mTimeModel.prayerTimings.get(4), mTimeModel.prayerTimings.get(5));
					Log.e("status", status+" null");
				}
				mDbHelper.closeDataBase();
				
	             mProgressDialog.dismiss();
				
				Intent in = new Intent();
                setResult(100,in);			
        		finish();
        		
			} 

		}

	};


	
	
	
//	***************************
//	Getcurrent location
//	***************************
	
	private ProgressDialog progressdialog;

	private LocationHelper mLocationHelper;

	private Thread myThread;
	
	
	private void getCurrentLocation(){
		
		if(NetworkConnectionHelper.checkNetworkAvailability()){			
	   		    	
	   		    	progressdialog = ProgressDialog.show(SelectCityScreen.this, "","Updating current location...", true);
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
			  
		    
			}else{
			
				networkNotFound();
			
		}
		
	}
	
	
		public Handler mainLocationHandler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				progressdialog.dismiss();
				
				if (msg.what == 0) {
					
					interruptThread();
					locationNotFound();
					}
				}
			};			
				
				
			private void interruptThread() {

					try {
						
						if (myThread != null)
							myThread.interrupt();
						
					} catch (Exception e) {
						// TODO: handle exception
					}			
					
				}
			
			
			@Override
			public void getCurrentLocation(String locationName, String addressline,
					String currentState, String currentCountry, double longitude,
					double latitude,String country_short_name,String state_short_name) {
				// TODO Auto-generated method stub

				interruptThread();
				
					progressdialog.dismiss();
					if(!locationName.equalsIgnoreCase("")){
						
						
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
						
						myAlertDialog();
						
					}else{
						
						progressdialog.dismiss();
						locationNotFound();
					}
				
			}
			
	
	
//		****************************************
//		 Alert Dailogs
//		****************************************

				
				private void myAlertDialog(){
					
					
					
					AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setTitle("Daleelo");
					builder.setMessage("Do you want to change prayer timings for selected location.")
					       .setCancelable(false)					       
					       .setNegativeButton("No", new DialogInterface.OnClickListener() {
					           public void onClick(DialogInterface dialog, int id) {
					        	   
					                dialog.cancel();
					               
					        		Intent in = new Intent();
					                setResult(100,in);			
					        		finish();
					        		
					           }
					       })
					       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					           public void onClick(DialogInterface dialog, int id) {
					        	
					        	   setTimings();		        		
					               
					           }
					       });
					AlertDialog alert = builder.create();
					alert.show();
				}
				
				
				  
				  private void locationNotFound(){

					  new LocationNotFoundDialog(SelectCityScreen.this).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener() {
						
						
						  @Override
						
						  public void onClick(DialogInterface dialog, int which) {

						
						  }
						
					
					  }).create().show();
				  
				  }
				  
				  
				  private void networkNotFound(){

					  new NetworkNotFoundDialog(SelectCityScreen.this).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener() {
						
						
						  @Override
						
						  public void onClick(DialogInterface dialog, int which) {
							
							
						
						  }
						
					
					  }).create().show();
				  
				  }
	

}
