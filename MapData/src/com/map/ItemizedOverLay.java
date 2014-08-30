package com.map;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class ItemizedOverLay extends ItemizedOverlay<OverlayItem> {

	List<OverlayItem> mapOverLay = new ArrayList<OverlayItem>();
	Context context;
	public ItemizedOverLay(Drawable defaultMarker, Context context) {
		super(boundCenterBottom(defaultMarker));
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	@Override
	protected OverlayItem createItem(int i) {
		// TODO Auto-generated method stub
		return mapOverLay.get(i);
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return mapOverLay.size();
	}

	public void addOverLayItem(OverlayItem overlayItem){
		mapOverLay.add(overlayItem);
		populate();
	}
	
	public void addOverlayItem(int lat, int lon, String title) {
		 GeoPoint point = new GeoPoint(lat, lon);
		 OverlayItem overlayItem = new OverlayItem(point, title, null);
		 addOverLayItem(overlayItem);
		 }
	@Override
	public boolean onTouchEvent(MotionEvent event, MapView map){
		 //---when user lifts his finger---
		 if (event.getAction() == 1) {
		 GeoPoint p = map.getProjection().fromPixels((int) event.getX(), (int) event.getY());
		 Toast.makeText(context.getApplicationContext(), p.getLatitudeE6() / 1E6 + "," + p.getLongitudeE6() /1E6 ,Toast.LENGTH_SHORT).show();
		 
		 Geocoder coder = new Geocoder(context.getApplicationContext(), Locale.getDefault());
		 try 
		 {
			 List<Address> addresses = coder.getFromLocation(p.getLatitudeE6()  / 1E6,  p.getLongitudeE6() / 1E6, 1);			
			 
			 String strCompleteAddress= "";
			 if (addresses.size() > 0)
			 {
			 for (int i=0; i<addresses.get(0).getMaxAddressLineIndex();i++)
			  strCompleteAddress+= addresses.get(0).getAddressLine(i) + "\n";
			 }
			 Log.i("MyLocTAG => ", strCompleteAddress);
			 Toast.makeText(context.getApplicationContext(), strCompleteAddress, Toast.LENGTH_LONG).show();
			 }
			 catch (IOException e) {
			 Log.i("MyLocTAG => ", "this is the exception part");
			 e.printStackTrace();
			 }
			 return true;
			 }
			 else
			 return false;
	}
		 
}
