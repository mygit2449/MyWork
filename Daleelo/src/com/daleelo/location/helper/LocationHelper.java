package com.daleelo.location.helper;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.daleelo.Application.DaleeloApplication;
import com.daleelo.Dialog.NetworkNotFoundDialog;
import com.daleelo.Utilities.Utils;
import com.daleelo.location.model.LocationModel;
import com.daleelo.location.parser.LocationParser;



public class LocationHelper 
{
	
	private static String 			TAG=LocationManager.class.getSimpleName();
	private LocationManager 		mLocationManager			=null;
	private CurrentLocationListener mCurrentLocationListener	=null;
	private CurrentLocation 		mCurrentLocation;
	private Geocoder 				mGeocoder					=null;
	private String 					currentCityName				="";		
	private String 					currentState				="";
	private String 					currentCountry				="";
	private String					addressLine					="";
	private String 					country_short_name = "";
	private String 					state_short_name = "";
	private LocationModel			mLocationModel;
	private locationReciverHandler  handler=new locationReciverHandler();
	private static Context 				context;
	private double latitude,longitude;
	private locationReciverHandler mReciverHandler = new locationReciverHandler();
	private final int FOUND_RESULT				= 0;

	// 9966283672
	
	public LocationHelper(Context context)
	{
		this.context=context;
		mLocationManager=(LocationManager)context.getSystemService(context.LOCATION_SERVICE);
		mCurrentLocationListener=new CurrentLocationListener();
		mCurrentLocation=(CurrentLocation) context;
		mGeocoder=new Geocoder(context);
		
		Log.e(TAG, LocationManager.NETWORK_PROVIDER+"");
		Log.e(TAG, mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)+"");

		
		if(NetworkConnectionHelper.checkNetworkAvailability())
		{
			if(mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
			{
				mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,mCurrentLocationListener);
			}
			if(mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
			{
				mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,mCurrentLocationListener);
			}
		}else{
			Log.e(TAG, "internet connection is not available");
			new NetworkNotFoundDialog(context).setPositiveButton("ok", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			}).create().show();
		}
		
	}
	
	public void stopFetchingCurrentLocation(){
		

		mLocationManager.removeUpdates(mCurrentLocationListener);
	}
	
	class CurrentLocationListener implements LocationListener{
		
		@Override
		public void onLocationChanged(Location location) 
		{
			mLocationManager.removeUpdates(this);
			
			latitude	=	location.getLatitude();
			longitude	=	location.getLongitude();	

//			latitude	=	32.7828;
//			longitude	=	-96.8039;
			
			new LocationNameFetcherThread(latitude, longitude).start();
			Log.d(TAG, latitude+"::"+longitude);
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private double mLatitude,mLongitude;
	
	class LocationNameFetcherThread extends Thread{
		
		String url;
		public LocationNameFetcherThread(double latitude,double longitude)
		{
			mLatitude	=	latitude;
			mLongitude	=	longitude;
			url = String.format("http://maps.googleapis.com/maps/api/geocode/xml?latlng=%s,%s&sensor=true",mLatitude,mLongitude);
			Log.e(TAG, url);
		}
		@Override
		public void run() {
			super.run();
			new LocationParser(url, mReciverHandler ).parse();
		}
		
	}
	
	
	int  getCityTryCnt=0;
	
	
	public void getCity(){
		List<Address> locationSuggetion;
		try {
			Log.e(TAG, "inside the run methode");
			
			locationSuggetion=new Geocoder(DaleeloApplication.context,Locale.getDefault()).getFromLocation(mLatitude, mLongitude, 1);
			currentCityName= locationSuggetion.get(0).getLocality();
			addressLine = locationSuggetion.get(0).getAddressLine(0);
			currentCountry = locationSuggetion.get(0).getCountryName();
			currentState = locationSuggetion.get(0).getAdminArea();
		
			Log.i("TAG", "*********** state "+currentCityName);
		
			handler.sendEmptyMessage(1);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			getCityTryCnt++;
			if(getCityTryCnt<=10)
				getCity();
			else
				handler.sendEmptyMessage(0);
			e.printStackTrace();
			
		}
		
	}
	
	class locationReciverHandler extends Handler
	{
		
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what==FOUND_RESULT)
			{
				mLocationModel = (LocationModel) msg.getData().getSerializable("locationresult");
				
				Log.e(TAG, mLocationModel.result.get(0).address_component.size()+"");
				addressLine =  mLocationModel.result.get(0).getFormatted_address();
				for(int i=0;i<mLocationModel.result.get(0).address_component.size();i++){
					if(mLocationModel.result.get(0).address_component.get(i).type.get(0).equalsIgnoreCase("locality")){
						currentCityName= mLocationModel.result.get(0).address_component.get(i).getLongName();
					}else if(mLocationModel.result.get(0).address_component.get(i).type.get(0).equalsIgnoreCase("administrative_area_level_1")){
						currentState =  mLocationModel.result.get(0).address_component.get(i).getLongName();
						state_short_name = mLocationModel.result.get(0).address_component.get(i).getShortName();
					}else if(mLocationModel.result.get(0).address_component.get(i).type.get(0).equalsIgnoreCase("country")){
						currentCountry = mLocationModel.result.get(0).address_component.get(i).getLongName();
						country_short_name = mLocationModel.result.get(0).address_component.get(i).getShortName();	
						
						if(country_short_name.equalsIgnoreCase("us")){							
							country_short_name = "USA";
						}
					}

				}
				
				Log.e(TAG, "city "+currentCityName+" state "+currentState+" country "+currentCountry+" country_short_name "+country_short_name);
				mCurrentLocation.getCurrentLocation(currentCityName, addressLine, currentState, currentCountry, longitude, latitude,country_short_name,state_short_name);
			}else{
				mCurrentLocation.getCurrentLocation(currentCityName, addressLine, currentState, currentCountry, longitude, latitude,country_short_name,state_short_name);
			}
			
		}
		
		
	}
	

	
}
