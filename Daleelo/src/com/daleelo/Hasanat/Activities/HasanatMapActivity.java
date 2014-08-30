package com.daleelo.Hasanat.Activities;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

import com.balloon.readystatesoftware.maps.OnSingleTapListener;
import com.balloon.readystatesoftware.maps.TapControlledMapView;
import com.daleelo.R;
import com.daleelo.Business.Activities.BusinessMapActivity;
import com.daleelo.Hasanat.Helper.HasanatCustomItemizedOverlay;
import com.daleelo.Hasanat.Helper.HasanatCustomOverlayItem;
import com.daleelo.Hasanat.Model.BusinessDetailModel;
import com.daleelo.Map.helper.CircleOverlay;
import com.daleelo.Map.helper.CurrentLocationCustomItemizedOverlay;
import com.daleelo.Map.helper.CurrentLocationCustomOverlayItem;
import com.daleelo.Masjid.Activities.MasjidMapActivity;
import com.daleelo.Utilities.CurrentLocationData;
import com.daleelo.helper.MyLocOverlay;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MyLocationOverlay;


public class HasanatMapActivity extends MapActivity implements OnClickListener {


	boolean FIRST_LOC = true;
	ArrayList<BusinessDetailModel> mDataModelList = null;
	private HasanatCustomItemizedOverlay<HasanatCustomOverlayItem> mDriverLocOverlay;
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
			progressDialog = ProgressDialog.show(HasanatMapActivity.this, "","Loading please wait...", true,false);
		
		mMapController = mMapView.getController();     
        mDriverLocOverlay = new HasanatCustomItemizedOverlay<HasanatCustomOverlayItem>(HasanatMapActivity.this.getResources().getDrawable(R.drawable.pin_red), mMapView);
             
        
        try {
        	
        	mMyLocOverlay = new MyLocOverlay(HasanatMapActivity.this, mMapView);
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
						mDataModelList = new ArrayList<BusinessDetailModel>();
						mDataModelList.add((BusinessDetailModel)getIntent().getExtras().getSerializable("data"));
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
						mDataModelList = (ArrayList<BusinessDetailModel>) getIntent().getExtras().getSerializable("data");				
						locationMapPin();
						
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
		});
		
		mThread.start();
		
		
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
        mLocationLocOverlay = new CurrentLocationCustomItemizedOverlay<CurrentLocationCustomOverlayItem>(HasanatMapActivity.this.getResources().getDrawable(R.drawable.current_location_pin), mMapView);

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
				HasanatMapActivity.this);

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
	
	private void addPin(BusinessDetailModel mBusinessDetailModel){
		
		
		mLat = Double.parseDouble(mBusinessDetailModel.getAddressLat());
		mLong = Double.parseDouble(mBusinessDetailModel.getAddressLong());
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
				 (mBusinessDetailModel.getAddressLine1().length()>0 ? mBusinessDetailModel.getAddressLine1()+", ":"")+
				 (mBusinessDetailModel.getAddressLine2().length()>0 ? mBusinessDetailModel.getAddressLine2()+", ":"")+
				 (mBusinessDetailModel.getCityName().length()>0 ? mBusinessDetailModel.getCityName()+", ":"")+""+
				 (mBusinessDetailModel.getStateCode().length()>0 ? mBusinessDetailModel.getStateCode()+", ":"")+""+
				 (mBusinessDetailModel.getAddressZipcode().length()>0 ? mBusinessDetailModel.getAddressZipcode():"");
		
		
		HasanatCustomOverlayItem overlayitem = new HasanatCustomOverlayItem( 
				point, 
				mBusinessDetailModel.getBusinessTitle(),				
				address,	
				HasanatMapActivity.this,
				mDataModelList, mCallFrom);

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
		
	



//package com.daleelo.Hasanat.Activities;
//
//import java.util.ArrayList;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.ImageButton;
//
//import com.balloon.readystatesoftware.maps.OnSingleTapListener;
//import com.balloon.readystatesoftware.maps.TapControlledMapView;
//import com.daleelo.R;
//import com.daleelo.Hasanat.Model.mBusinessDetailModel;
//import com.daleelo.Masjid.Helper.MapOverlayHelper;
//import com.daleelo.Masjid.Helper.MasjidCustomItemizedOverlay;
//import com.daleelo.Masjid.Helper.MasjidCustomOverlayItem;
//import com.google.android.maps.GeoPoint;
//import com.google.android.maps.MapActivity;
//
//public class HasanatMapActivity extends MapActivity{
//	
//	private Intent mReceiverIntent;
//	private TapControlledMapView mapview;
//	private String longitude, latitude;
//	private MasjidCustomItemizedOverlay<MasjidCustomOverlayItem> mLocOverlay;
//	private ArrayList<mBusinessDetailModel> mFilteredDataList = null;
//	private ImageButton mbtn_list;
//	
//	
//	@Override
//	protected void onCreate(Bundle icicle) {
//		// TODO Auto-generated method stub
//		super.onCreate(icicle);
//		setContentView(R.layout.hasanat_map);
//		
//		mapview = (TapControlledMapView)findViewById(R.id.hasanat_mapview);
//		mbtn_list = (ImageButton)findViewById(R.id.hasanat_IMGB_list);
//		
//		mReceiverIntent = getIntent();
//		
//		mFilteredDataList = (ArrayList<mBusinessDetailModel>) mReceiverIntent.getExtras().get("data");
//		
//		mapview.setBuiltInZoomControls(true);
//		mapview.getZoomButtonsController().setAutoDismissed(true);
//		
//		mapview.invalidate();
//		
//        MapOverlayHelper myLocationOverlay = new MapOverlayHelper(this, mapview);
//		myLocationOverlay.enableMyLocation();
//		mapview.getOverlays().add(myLocationOverlay);
//		
//		dropPins();
//		
//		mbtn_list.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				HasanatMapActivity.this.finish();
//			}
//		});
//	}
//	
//	
//	private void dropPins(){
//		
//		for(mBusinessDetailModel mmBusinessDetailModel :  mFilteredDataList){
//			latitude = mmBusinessDetailModel.getAddressLat();
//			longitude = mmBusinessDetailModel.getAddressLong();
//			
//			mapview.setOnSingleTapListener(new OnSingleTapListener() {		
//		    	  
//				public boolean onSingleTap(MotionEvent e) {
//					mLocOverlay.hideAllBalloons();
//					return true;
//				}
//	      });
//			
//	        mLocOverlay = new MasjidCustomItemizedOverlay<MasjidCustomOverlayItem>(HasanatMapActivity.this.getResources().getDrawable(R.drawable.pin_red), mapview);
//			
//	        GeoPoint point = new GeoPoint((int)(Double.valueOf(latitude)*1E6), (int)(Double.valueOf(longitude)*1E6));
//	        
//	        MasjidCustomOverlayItem overlayitem = new MasjidCustomOverlayItem(point, mmBusinessDetailModel.getBusinessTitle(),
//	        			((mmBusinessDetailModel.getAddressLine1()!=null)?mmBusinessDetailModel.getAddressLine1():""),HasanatMapActivity.this);
//	        
//	        mLocOverlay.addOverlay(overlayitem);
//	        mapview.getOverlays().add(mLocOverlay);
//		}
//	}
//	
//
//	@Override
//	protected boolean isRouteDisplayed() {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//}
