package com.daleelo.Business.Activities;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.balloon.readystatesoftware.maps.OnSingleTapListener;
import com.balloon.readystatesoftware.maps.TapControlledMapView;
import com.daleelo.R;
import com.daleelo.Business.Helper.BusinessCustomItemizedOverlay;
import com.daleelo.Business.Helper.BusinessCustomOverlayItem;
import com.daleelo.Business.Model.BusinessListModel;
import com.daleelo.GlobalSearch.GlobalSearchActivity;
import com.daleelo.Main.Activities.MainHomeScreen;
import com.daleelo.Map.helper.CircleOverlay;
import com.daleelo.Map.helper.CurrentLocationCustomItemizedOverlay;
import com.daleelo.Map.helper.CurrentLocationCustomOverlayItem;
import com.daleelo.Masjid.Activities.MasjidMapActivity;
import com.daleelo.MyStuff.Activities.MyStuffActivity;
import com.daleelo.Utilities.CurrentLocationData;
import com.daleelo.helper.MyLocOverlay;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MyLocationOverlay;


public class BusinessMapActivity extends MapActivity implements OnClickListener {


	boolean FIRST_LOC = true;
	ArrayList<BusinessListModel> mDataModelList = null;
	private BusinessCustomItemizedOverlay<BusinessCustomOverlayItem> mDriverLocOverlay;
	TapControlledMapView mMapView;
	Drawable drawable;    	
	Button mBtnBusinessList;
	ImageButton mBTNHome;		
	MapController mMapController;
	MyLocationOverlay myLocationOverlay;
	ProgressDialog progressDialog;
	String mCallFrom;
	MyLocOverlay mMyLocOverlay;
	
	
	@Override
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.business_map_view_screen);
        initializeUI();
        
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
	
	
	private void initializeUI() {
		// TODO Auto-generated method stub
		
		mCallFrom = getIntent().getExtras().getString("from");
		
        mMapView = (TapControlledMapView) this.findViewById(R.id.mapview);
		mBtnBusinessList = (Button) findViewById(R.id.business_map_BTN_list);
		mBTNHome = (ImageButton) findViewById(R.id.business_IMGB_home);
		mBtnBusinessList.setOnClickListener(this);
		mBTNHome.setOnClickListener(this);		
		
		if(mCallFrom.equalsIgnoreCase("desp"))
			mBtnBusinessList.setVisibility(View.INVISIBLE);		
		else
			progressDialog = ProgressDialog.show(BusinessMapActivity.this, "","Loading please wait...", true,false);
		
		mMapController = mMapView.getController();     
        mDriverLocOverlay = new BusinessCustomItemizedOverlay<BusinessCustomOverlayItem>(BusinessMapActivity.this.getResources().getDrawable(R.drawable.pin_red), mMapView);
             
        
        try {
        	
        	mMyLocOverlay = new MyLocOverlay(BusinessMapActivity.this, mMapView);
        	mMapView.getOverlays().add(mMyLocOverlay);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
        
        
		mThread = new Thread(new Runnable() {
			public void run() {
				
				
				try {
					
					addLocationPin();
					
					if(mCallFrom.equalsIgnoreCase("desp")){
						
						mDataModelList = null;
						mDataModelList = new ArrayList<BusinessListModel>();
						mDataModelList.add((BusinessListModel)getIntent().getExtras().getSerializable("data"));
						addPin(mDataModelList.get(0));	
						
						try {
							
							Thread.sleep(2000);
							myhandler.sendEmptyMessage(0);
							
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
					}else{
						
						Thread.sleep(3000);					
						
						mDataModelList = (ArrayList<BusinessListModel>) getIntent().getExtras().getSerializable("data");						
						if(mDataModelList.size()>10)
							mDataModelList.remove(mDataModelList.size()-1);
						
						locationMapPin();
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
		});
		
		mThread.start();
		
		

		setBottomBar();
		
	}
	
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
				
				startActivity(new Intent(BusinessMapActivity.this, GlobalSearchActivity.class));
				
			}
		});		
		mHome.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				startActivity(new Intent(BusinessMapActivity.this,MainHomeScreen.class)
				.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)); 
				finish();
				
			}
		});
		
		mMyStuff.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				startActivity(new Intent(BusinessMapActivity.this,MyStuffActivity.class)
				.putExtra("from", "bottom")); 
			
			}
		});
	}
	
	
	
	Thread mThread;


	
	
	@SuppressWarnings("unused")
	private void locationMapPin() {
		
//		addLocationPin();
		for (int i = 0; i < mDataModelList.size(); i++) {
				
			addPin(mDataModelList.get(i));
		}
		
		try {
			Thread.sleep(100);
			myhandler.sendEmptyMessage(0);
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
   
	private CurrentLocationCustomItemizedOverlay<CurrentLocationCustomOverlayItem> mLocationLocOverlay;
	private Double mLat, mCuLat, mZoomLat = 0.0, mLong, mCuLong, mZoomLong = 0.0 ;
	private CircleOverlay mCircleOverlay;
	
	private void addLocationPin(){
        mLocationLocOverlay = new CurrentLocationCustomItemizedOverlay<CurrentLocationCustomOverlayItem>(BusinessMapActivity.this.getResources().getDrawable(R.drawable.current_location_pin), mMapView);

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
				BusinessMapActivity.this);

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
	
	private void addPin(BusinessListModel businessListModel){
		
		
		mLat = Double.parseDouble(businessListModel.getAddressLat());
		mLong = Double.parseDouble(businessListModel.getAddressLong());
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
				 (businessListModel.getAddressLine1().length()>0 ? businessListModel.getAddressLine1()+", ":"")+
				 (businessListModel.getAddressLine2().length()>0 ? businessListModel.getAddressLine2()+", ":"")+
				 (businessListModel.getCityName().length()>0 ? businessListModel.getCityName()+", ":"")+""+
				 (businessListModel.getStateCode().length()>0 ? businessListModel.getStateCode()+", ":"")+""+
				 (businessListModel.getAddressZipcode().length()>0 ? businessListModel.getAddressZipcode():"");
				
		BusinessCustomOverlayItem overlayitem = new BusinessCustomOverlayItem( 
				point, 
				businessListModel.getBusinessTitle(),				
				address,	
				BusinessMapActivity.this,
				mDataModelList, mCallFrom, "business");

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
			
			if(mCallFrom.equalsIgnoreCase("list")){
				
				mMapView.invalidate();
				progressDialog.dismiss();
				
			}
			mThread.interrupt();			
			
		}
	}


	public void onClick(View v) {
		
		// TODO Auto-generated method stub
		
		switch(v.getId()){
		
		case R.id.business_map_BTN_list:
			
			finish();
			
			break;
		
		
		}
      
      
	}


	
	@Override
	protected boolean isRouteDisplayed() {
		
		// TODO Auto-generated method stub
		return false;
		
	}
	
	
	

}
		
	