package com.map;

import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Projection;

public class MapDataActivity extends MapActivity {
	MapView myMap;
	private Projection projection;
	MyOverlay mMapOverLay;
	private List mapOverlays;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		LocationListener ll = new GetLocation();

		myMap = (MapView) findViewById(R.id.mapview);
		myMap.setBuiltInZoomControls(true);

		 Drawable makerDefault =
		 this.getResources().getDrawable(R.drawable.red);
		 ItemizedOverLay itemizedOverlay = new ItemizedOverLay(makerDefault,
		 MapDataActivity.this);
		 GeoPoint point = new GeoPoint(33480000, 73000000);
		
		 OverlayItem overlayItem = new OverlayItem(point, "Islamabad", null);
		 itemizedOverlay.addOverlayItem(33695043, 73000000, "Islamabad");
		 itemizedOverlay.addOverlayItem(33480000, 73000000,
		 "Some Other Pakistani City");
		 itemizedOverlay.addOverlayItem(33380000, 73000000,
		 "Some Other Pakistani City");
		
		 myMap.getOverlays().add(itemizedOverlay);
		
		 MapController mc = myMap.getController();
		 mc.setCenter(new GeoPoint(33580000, 73000000)); // Some where near
		 mc.zoomToSpan(itemizedOverlay.getLatSpanE6(),
		 itemizedOverlay.getLonSpanE6());

//		MapController mc = myMap.getController();
//		mMapOverLay = new MyOverlay();
//
//		GeoPoint gP = new GeoPoint(33695043, 73000000);// Creating a GeoPoint
//
//		mc.setCenter(gP);
//		mc.setZoom(9);// Initializing the MapController and setting the map to
//						// center at the
//		// defined GeoPoint
//
//		mapOverlays = myMap.getOverlays();
//		projection = myMap.getProjection();
//
//		mapOverlays.add(mMapOverLay);

		// boolean isGPS = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
		//
		// // If GPS is not enable then it will be on
		// if(!isGPS)
		// {
		// Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
		// intent.putExtra("enabled", true);
		// sendBroadcast(intent);
		// }
		//
		// lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,
		// ll);
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	class GetLocation implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			String coordinates[] = { "" + location.getLatitude(),
					"" + location.getLongitude() };
			double lat = Double.parseDouble(coordinates[0]);
			double lng = Double.parseDouble(coordinates[1]);

			GeoPoint p = new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));

			MyMapOverlays marker = new MyMapOverlays(p, MapDataActivity.this);
			List listOfOverLays = myMap.getOverlays();
			listOfOverLays.clear();
			listOfOverLays.add(marker);

			myMap.invalidate();
			// Toast.makeText(MapDataActivity.this,
			// "lat "+location.getLatitude()+"long "+location.getLongitude(),
			// Toast.LENGTH_SHORT).show();
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

	class MyOverlay extends Overlay {

		public MyOverlay() {

		}

		public void draw(Canvas canvas, MapView mapv, boolean shadow) {
			super.draw(canvas, mapv, shadow);
			// Configuring the paint brush
			Paint mPaint = new Paint();
			mPaint.setDither(true);
			mPaint.setColor(Color.RED);
			mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
			mPaint.setStrokeJoin(Paint.Join.ROUND);
			mPaint.setStrokeCap(Paint.Cap.ROUND);
			mPaint.setStrokeWidth(4);

			GeoPoint gP1 = new GeoPoint(34159000, 73220000);// starting point
															// Abbottabad
			GeoPoint gP2 = new GeoPoint(33695043, 73050000);// End point
															// Islamabad

			GeoPoint gP4 = new GeoPoint(33695043, 73050000);// Start point
															// Islamabad
			GeoPoint gP3 = new GeoPoint(33615043, 73050000);// End Point
															// Rawalpindi

			Point p1 = new Point();
			Point p2 = new Point();
			Path path1 = new Path();

			Point p3 = new Point();
			Point p4 = new Point();
			Path path2 = new Path();
			projection.toPixels(gP2, p3);
			projection.toPixels(gP1, p4);

			path1.moveTo(p4.x, p4.y);// Moving to Abbottabad location
			path1.lineTo(p3.x, p3.y);// Path till Islamabad

			projection.toPixels(gP3, p1);
			projection.toPixels(gP4, p2);

			path2.moveTo(p2.x, p2.y);// Moving to Islamabad location
			path2.lineTo(p1.x, p1.y);// Path to Rawalpindi

			canvas.drawPath(path1, mPaint);// Actually drawing the path from
											// Abbottabad to Islamabad
			canvas.drawPath(path2, mPaint);// Actually drawing the path from
											// Islamabad to Rawalpindi

		}

	}
}