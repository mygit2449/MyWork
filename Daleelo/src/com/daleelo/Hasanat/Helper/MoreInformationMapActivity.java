package com.daleelo.Hasanat.Helper;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

import com.balloon.readystatesoftware.maps.OnSingleTapListener;
import com.balloon.readystatesoftware.maps.TapControlledMapView;
import com.daleelo.R;
import com.daleelo.Hasanat.Model.BusinessDetailModel;
import com.daleelo.Masjid.Helper.MapOverlayHelper;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;

public class MoreInformationMapActivity extends MapActivity{
	
	
	private Intent mReceiverIntent;
	private TapControlledMapView mapview;
	private String longitude, latitude;
	private HasanatCustomItemizedOverlay<HasanatCustomOverlayItem> mLocOverlay;
	private BusinessDetailModel mBusinessDetailModel = null;

	@Override
	protected void onCreate(Bundle icicle) {
		// TODO Auto-generated method stub
		super.onCreate(icicle);
		setContentView(R.layout.description_map);
		
		mapview = (TapControlledMapView)findViewById(R.id.desc_mapview);
		
		mReceiverIntent = getIntent();
		
		mBusinessDetailModel = (BusinessDetailModel) mReceiverIntent.getExtras().get("data");
		
		latitude = mBusinessDetailModel.getAddressLat();
		longitude = mBusinessDetailModel.getAddressLong();
		
        mLocOverlay = new HasanatCustomItemizedOverlay<HasanatCustomOverlayItem>(MoreInformationMapActivity.this.getResources().getDrawable(R.drawable.pin_red), mapview);
		
        GeoPoint point = new GeoPoint((int)(Double.valueOf(latitude)*1E6), (int)(Double.valueOf(longitude)*1E6));
        
        HasanatCustomOverlayItem overlayitem = new HasanatCustomOverlayItem(point, mBusinessDetailModel.getBusinessTitle(),
        			((mBusinessDetailModel.getAddressLine1()!=null)?mBusinessDetailModel.getAddressLine1():""),MoreInformationMapActivity.this);
        
        mapview.setBuiltInZoomControls(true);
		mapview.getZoomButtonsController().setAutoDismissed(true);
        mapview.postInvalidate();
        
        mLocOverlay.addOverlay(overlayitem);
        mapview.getOverlays().add(mLocOverlay);
        
        MapOverlayHelper myLocationOverlay = new MapOverlayHelper(this, mapview);
		myLocationOverlay.enableMyLocation();
		mapview.getOverlays().add(myLocationOverlay);
		
		mapview.setOnSingleTapListener(new OnSingleTapListener() {		
	    	  
			public boolean onSingleTap(MotionEvent e) {
				mLocOverlay.hideAllBalloons();
				return true;
			}
      });
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	

}
