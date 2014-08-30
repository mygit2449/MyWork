package com.daleelo.Masjid.Activities;

import java.net.MalformedURLException;
import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.balloon.readystatesoftware.maps.OnSingleTapListener;
import com.balloon.readystatesoftware.maps.TapControlledMapView;
import com.daleelo.R;
import com.daleelo.Dialog.AlertDialogMsg;
import com.daleelo.GlobalSearch.GlobalSearchActivity;
import com.daleelo.Main.Activities.MainHomeScreen;
import com.daleelo.Map.helper.CircleOverlay;
import com.daleelo.Map.helper.CurrentLocationCustomItemizedOverlay;
import com.daleelo.Map.helper.CurrentLocationCustomOverlayItem;
import com.daleelo.Masjid.Helper.MasjidCustomItemizedOverlay;
import com.daleelo.Masjid.Helper.MasjidCustomOverlayItem;
import com.daleelo.Masjid.Model.MasjidModel;
import com.daleelo.Masjid.Parser.MasjidLocationParser;
import com.daleelo.MyStuff.Activities.MyStuffActivity;
import com.daleelo.Utilities.CurrentLocationData;
import com.daleelo.Utilities.Urls;
import com.daleelo.Utilities.Utils;
import com.daleelo.helper.MyLocOverlay;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MyLocationOverlay;


public class MasjidMapActivity extends MapActivity implements OnClickListener {


	boolean FIRST_LOC = true, DO_RESUME = false;
	ArrayList<MasjidModel> MasjidDataFeeds =null;
	
	private MasjidCustomItemizedOverlay<MasjidCustomOverlayItem> mDriverLocOverlay; 
	private CurrentLocationCustomItemizedOverlay<CurrentLocationCustomOverlayItem> mLocationLocOverlay;
	TapControlledMapView mMapView;
	Drawable drawable;    	
	Button mBtnMasjidList, mFilter;
	ImageButton mBTNHome;		
	MapController mMapController;
	MyLocationOverlay myLocationOverlay;
	ProgressDialog progressDialog; 
	Thread mThread;
	String mCurrentLocation, mSearchLocation;
	String mCallFrom;
	MyLocOverlay mMyLocOverlay;

    public SharedPreferences sharedpreference;
	
	@Override
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.masjid_mapview);
        initializeUI();
        
        
      
	}
	
	private void initializeUI() {
		// TODO Auto-generated method stub
		
		mCallFrom = getIntent().getExtras().getString("from");
		
		MasjidDataFeeds = new ArrayList<MasjidModel>();
		sharedpreference= getSharedPreferences("masjid",MODE_PRIVATE);
		
		mCurrentLocation = CurrentLocationData.ADDRESS_LINE;
		mSearchLocation = CurrentLocationData.CURRENT_CITY;		
		
        mMapView = (TapControlledMapView) this.findViewById(R.id.mapview);
		mBtnMasjidList = (Button) findViewById(R.id.masjid_map_BTN_list);
		mFilter = (Button) findViewById(R.id.masjid_map_BTN_filter);
		
		mBtnMasjidList.setOnClickListener(this);
		mFilter.setOnClickListener(this);
		
		
		
		mMapController = mMapView.getController();     
        mDriverLocOverlay = new MasjidCustomItemizedOverlay<MasjidCustomOverlayItem>(MasjidMapActivity.this.getResources().getDrawable(R.drawable.pin_red), mMapView);
        mLocationLocOverlay = new CurrentLocationCustomItemizedOverlay<CurrentLocationCustomOverlayItem>(MasjidMapActivity.this.getResources().getDrawable(R.drawable.current_location_pin), mMapView);
            
        try {
			
        	mMyLocOverlay = new MyLocOverlay(MasjidMapActivity.this, mMapView);
        	mMapView.getOverlays().add(mMyLocOverlay);
        	
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		if(mCallFrom.equalsIgnoreCase("home")){
        
			progressDialog = ProgressDialog.show(MasjidMapActivity.this, "","Loading please wait...", true,false);
		
		}

			
			
			
	        mThread = new Thread(new Runnable() {
				public void run() {
					
					
				
						
					if(mCallFrom.equalsIgnoreCase("desp")){
						
						addLocationPin();
							mFilter.setVisibility(View.INVISIBLE);
							mBtnMasjidList.setVisibility(View.INVISIBLE);
							
							MasjidDataFeeds = null;
							MasjidDataFeeds = new ArrayList<MasjidModel>();
							MasjidDataFeeds.add((MasjidModel)getIntent().getExtras().getSerializable("data"));
							addPin(MasjidDataFeeds.get(0));	
							
							try {
								
								Thread.sleep(2000);
								myhandler.sendEmptyMessage(0);
								
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
					
					}else{
					
					
					 Editor e = sharedpreference.edit();
				      e.putString("location", CurrentLocationData.CURRENT_CITY);
				      e.putString("lat", "00.00");
				      e.putString("long", "00.00");
				      e.putString("range", Utils.RANGE);
				      e.putString("sort", "2");
				      e.commit();
			        
					downloadParsedData();
					
				}
				
				
				}});
			
				mThread.start();        
//				addLocationPin();
				setBottomBar();
	}
	
		
//	@Override
//	protected void onResume() {
//		// TODO Auto-generated method stub
//		super.onResume();
//		
//		mMyLocOverlay.enableMyLocation();
//	}
//
//
//
//	@Override
//	protected void onPause() {
//		// TODO Auto-generated method stub
//		super.onPause();
//		mMyLocOverlay.disableMyLocation();
//	}
	



	private ImageButton mHome, mMyStuff;
	private EditText mSearch;
	
	private void setBottomBar(){
		
		
		mHome = (ImageButton)findViewById(R.id.business_IMGB_home);
		mMyStuff = (ImageButton)findViewById(R.id.business_IMGB_mystuff);
		mSearch = (EditText)findViewById(R.id.business_TXT_serach);
		
		mSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				startActivity(new Intent(MasjidMapActivity.this, GlobalSearchActivity.class));
				
			}
		});	
		mHome.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				startActivity(new Intent(MasjidMapActivity.this,MainHomeScreen.class)
				.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)); 
				finish();
				
			}
		});
		
		mMyStuff.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				startActivity(new Intent(MasjidMapActivity.this,MyStuffActivity.class)
				.putExtra("from", "bottom"));  
			
			}
		});
		
		
	}
	
	
	private void downloadParsedData(){
		
		
		mDriverLocOverlay.hideAllBalloons();
		mDriverLocOverlay.removeOverlay();
		MasjidDataFeeds = new ArrayList<MasjidModel>();		
		getParsedData();	
		
	}
	                                  
	                                  
	

	

//	Get parsed data
//	***************
	
	public void getFilterrData() throws MalformedURLException{    	
	    	
	    	try {	
	    		
	    		mDriverLocOverlay.hideAllBalloons();
				mDriverLocOverlay.removeOverlay();
				MasjidDataFeeds.clear();
	    		
	    		 progressDialog = ProgressDialog.show(MasjidMapActivity.this, "","Loading please wait...", true,false); 
					
				MasjidLocationParser mUserAuth = new MasjidLocationParser(String.format(
							Urls.MOSQUE_BY_FILTER_URL								
							,"146","0","0"
							,sharedpreference.getString("location", "")
							,sharedpreference.getString("lat", "")
							,sharedpreference.getString("long", "")
							,sharedpreference.getString("range", "")
							,sharedpreference.getString("sort", "")), mainHandler, MasjidDataFeeds);		
				
				mUserAuth.start();
			
										
							} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	    }
	   private void getParsedData(){
			
			try {	
				
				
				MasjidLocationParser mUserAuth = new MasjidLocationParser(String.format(
							Urls.MOSQUE_BY_CATEGORY_URL
							,"146",CurrentLocationData.CURRENT_CITY
							,CurrentLocationData.LATITUDE
							,CurrentLocationData.LONGITUDE
							,Utils.RANGE,"1","1000"), mainHandler, MasjidDataFeeds);		
				
				mUserAuth.start();
													
							} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		
		public Handler mainHandler = new Handler() 
	    {
			public void handleMessage(android.os.Message msg) 
			{
				
				
				
				String handleErrMsg = "";
				
				handleErrMsg = msg.getData().getString("httpError");
				
				Log.e("", "handleErrMsg "+handleErrMsg);
				
				if(handleErrMsg.equalsIgnoreCase("")){
					
					Log.e("", "handleErrMsg_pin "+handleErrMsg);					
					

													
							try {						
								locationMapPin();
								
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							

			
				
				}else{
					
					addLocationPin();
					
					progressDialog.dismiss();

					new AlertDialogMsg(MasjidMapActivity.this, handleErrMsg).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
							
							
						}
						
					}).create().show();
					
				}				
				
			}
		};

		

//		***************
//		OnclickListener
//		***************	


		public void onClick(View v) {
			
			// TODO Auto-generated method stub
			
			switch(v.getId()){
			
			case R.id.masjid_map_BTN_list:
				
				startActivityForResult(new Intent(MasjidMapActivity.this,MasjidListActivity.class).putExtra("data", MasjidDataFeeds),LIST);
				
				break;
			
			case R.id.masjid_map_BTN_filter:
				
				startActivityForResult(	new Intent(MasjidMapActivity.this,MasjidFilterActivity.class),FILTER);
							
				
				break;
				
			
			}
	      
	      
		}

		private final int FILTER = 2;
		private final int LIST = 3;
		
		
		@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			super.onActivityResult(requestCode, resultCode, data);

			Log.i("", "resultCode *************************" + resultCode);
			// TODO Auto-generated method stub
			
			if (requestCode == FILTER) {
				
				if (resultCode == 101) {
					
					mSearchLocation = sharedpreference.getString("location", "none");
					
					 try {
							getFilterrData();
						} catch (MalformedURLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
					


				}else if (requestCode == LIST) {
					
					if (resultCode == 103) {
						
						mDriverLocOverlay.hideAllBalloons();
						mDriverLocOverlay.removeOverlay();
						MasjidDataFeeds = null;
						MasjidDataFeeds = (ArrayList<MasjidModel>) data.getSerializableExtra("data");
					
						mThread = new Thread(new Runnable() {
							public void run() {
														
								try {		
									
									locationMapPin();
									
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
								
							}
						});
						
						mThread.start();
					}
				}
		}
		
		
//		***************
		
		
//		Adding pins to the map
//		**********************
		
		@SuppressWarnings("unused")
		private void locationMapPin() {
			
			Log.e("", "MasjidDataFeeds "+MasjidDataFeeds.size());
			
			
			FIRST_LOC = true;
			addLocationPin();
			for (int i = 0; i < MasjidDataFeeds.size(); i++) {
		
				Log.e("", "MasjidDataFeeds "+MasjidDataFeeds.get(i).getBusinessId());
				addPin(MasjidDataFeeds.get(i));
				
			}
			
			try {
				
				Thread.sleep(1000);
				myhandler.sendEmptyMessage(0);
				Thread.sleep(2000);
				
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		
		CircleOverlay mCircleOverlay;
		private void addLocationPin(){
			
			try {
				
			mCuLat = Double.parseDouble(CurrentLocationData.LATITUDE_DUMP);
			mCuLong = Double.parseDouble(CurrentLocationData.LONGITUDE_DUMP);
			GeoPoint point = new GeoPoint((int) (mCuLat * 1E6), (int) (mCuLong * 1E6));
			mCircleOverlay = new CircleOverlay(point);
			mMapView.getOverlays().add(mCircleOverlay);
			mMapView.getOverlays().add(mLocationLocOverlay);
			mMapController.animateTo(point);
			mMapController.setZoom(5);			
			
			CurrentLocationCustomOverlayItem overlayitem = new CurrentLocationCustomOverlayItem( 
					point, 
					CurrentLocationData.CURRENT_CITY_DUMP,				
					"",	
					MasjidMapActivity.this);

			mLocationLocOverlay.addOverlay(overlayitem);
			mMapView.setOnSingleTapListener(new OnSingleTapListener() {

				public boolean onSingleTap(MotionEvent e) {
					mLocationLocOverlay.hideAllBalloons();
					return true;
				}
			});	
			
			
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		
		private GeoPoint getCenterPoint(){
			
			mZoomLat = mLat + mCuLat;
			mZoomLong =mLong + mCuLong;
			
			GeoPoint point = new GeoPoint((int) (mZoomLat * 1E6), (int) (mZoomLong * 1E6));	
			return point;
		}
		
		Double mLat, mCuLat, mZoomLat = 0.0, mLong, mCuLong, mZoomLong = 0.0 ;
		private void addPin(MasjidModel masjidModel){
			
			
			 mLat = Double.parseDouble(masjidModel.getAddressLat());
			 mLong = Double.parseDouble(masjidModel.getAddressLong());
			GeoPoint point = new GeoPoint((int) (mLat * 1E6), (int) (mLong * 1E6));			
			mMapView.getOverlays().add(mDriverLocOverlay);
			 
			
			if (FIRST_LOC) {
				FIRST_LOC = false;
				
				if(!CurrentLocationData.IS_CURRENT_LOCATION){					
			
					mMapController.animateTo(getCenterPoint());
					mMapController.setZoom(2);
				
				}else{
					
					mMapController.animateTo(point);
					mMapController.setZoom(9);
				}
				
			}
			
			
			String address = 
					 (masjidModel.getAddressLine1().length()>0 ? masjidModel.getAddressLine1()+", ":"")+
					 (masjidModel.getAddressLine2().length()>0 ? masjidModel.getAddressLine2()+", ":"")+
					 (masjidModel.getCityName().length()>0 ? masjidModel.getCityName()+", ":"")+""+
					 (masjidModel.getStateCode().length()>0 ? masjidModel.getStateCode()+", ":"")+""+
					 (masjidModel.getAddressZipcode().length()>0 ? masjidModel.getAddressZipcode():"");
			
			
			MasjidCustomOverlayItem overlayitem = new MasjidCustomOverlayItem( 
					point, 
					masjidModel.getBusinessTitle(),				
					address,	
					MasjidMapActivity.this,
					MasjidDataFeeds, mCallFrom);

			mDriverLocOverlay.addOverlay(overlayitem);
			mMapView.setOnSingleTapListener(new OnSingleTapListener() {

				public boolean onSingleTap(MotionEvent e) {
					mDriverLocOverlay.hideAllBalloons();
					return true;
				}
			});	
		}
	   
		
		MyHandelr myhandler= new MyHandelr();
		class MyHandelr extends Handler{		
			public void handleMessage(android.os.Message message){	
				
				if(mCallFrom.equalsIgnoreCase("home")){
					
//					mMapView.invalidate();
					progressDialog.dismiss();
					
				}				
							
//				mThread.interrupt();	
//				mThread = null;
//				
			}
		}
		
//		**********************
		
	
	@Override
	protected boolean isRouteDisplayed() {
		
		// TODO Auto-generated method stub
		return false;
		
	}
	
	
	

}
		
	