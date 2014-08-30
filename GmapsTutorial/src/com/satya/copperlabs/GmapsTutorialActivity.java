package com.satya.copperlabs;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class GmapsTutorialActivity extends MapActivity {

	MapView _mTestMapView;
	MapController _mMapController;
	GeoPoint p;
	private LocationManager locationManager;
	private LocationListener locationListener;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		_mTestMapView = (MapView) findViewById(R.id.mapView);
		_mTestMapView.setBuiltInZoomControls(true);

		_mMapController = _mTestMapView.getController();
		String coordinates[] = { "17.4452519", "78.3861293" };
		double lat = Double.parseDouble(coordinates[0]);
		double lng = Double.parseDouble(coordinates[1]);

		p = new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		locationListener = new GPSLocationListener();

		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, locationListener);

		
	}

	
	public class GPSLocationListener implements LocationListener 
    {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                GeoPoint point = new GeoPoint(
                        (int) (location.getLatitude() * 1E6), 
                        (int) (location.getLongitude() * 1E6));
                
                /* Toast.makeText(getBaseContext(), 
                        "Latitude: " + location.getLatitude() + 
                        " Longitude: " + location.getLongitude(), 
                        Toast.LENGTH_SHORT).show();*/
                                
                MapOverlay mapOverlay = new MapOverlay();
        		List<Overlay> listOfOverlays = _mTestMapView.getOverlays();
        		listOfOverlays.clear();
        		listOfOverlays.add(mapOverlay);

        		_mMapController.animateTo(p);
        		_mMapController.setZoom(17);
        		_mTestMapView.invalidate();
        		
        		
//                _mMapController.animateTo(point);
//                _mMapController.setZoom(16);
                
                // add marker
              
            }
        }
        
	class MapOverlay extends com.google.android.maps.Overlay {
		
    	private GeoPoint pointToDraw;

		public void setPointToDraw(GeoPoint point) {
    		pointToDraw = point;
    	}

    	public GeoPoint getPointToDraw() {
    		return pointToDraw;
    	}
		@Override
		public boolean draw(Canvas canvas, MapView mapView, boolean shadow,
				long when) {
			super.draw(canvas, mapView, shadow);

			// ---translate the GeoPoint to screen pixels---
			Point screenPts = new Point();
			mapView.getProjection().toPixels(pointToDraw, screenPts);

			// ---add the marker---
			Bitmap bmp = BitmapFactory.decodeResource(getResources(),
					R.drawable.map_icon);
			canvas.drawBitmap(bmp, screenPts.x, screenPts.y - 50, null);
			return true;
		}

		@Override
		public boolean onTouchEvent(MotionEvent event, MapView mapView) {
			// ---when user lifts his finger---
			if (event.getAction() == 1) {
				GeoPoint p = mapView.getProjection().fromPixels(
						(int) event.getX(), (int) event.getY());

				Geocoder geoCoder = new Geocoder(getBaseContext(),
						Locale.getDefault());
				try {
					List<Address> addresses = geoCoder.getFromLocation(
							p.getLatitudeE6() / 1E6, p.getLongitudeE6() / 1E6,
							1);

					String add = "";
					if (addresses.size() > 0) {
						for (int i = 0; i < addresses.get(0)
								.getMaxAddressLineIndex(); i++)
							add += addresses.get(0).getAddressLine(i) + "\n";
					}

					Toast.makeText(getBaseContext(), add, Toast.LENGTH_SHORT)
							.show();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return true;
			} else
				return false;
		}
	}

	

	@Override
	public void onProviderDisabled(String arg0) {
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


	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}
