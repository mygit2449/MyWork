package com.map;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

/*My overlay Class starts*/
 class MyMapOverlays extends com.google.android.maps.Overlay
 {
 GeoPoint location = null;
 Context context;
public MyMapOverlays(GeoPoint location, Context context)
 {
 super();
 this.location = location;
 this.context = context;
 }

@Override
 public void draw(Canvas canvas, MapView mapView, boolean shadow)
 {

 super.draw(canvas, mapView, shadow);
 //translate the screen pixels
 Point screenPoint = new Point();
 mapView.getProjection().toPixels(this.location, screenPoint);

 //add the image
 canvas.drawBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.red),screenPoint.x, screenPoint.y , null); //Setting the image  location on the screen (x,y).
 }
 }
 /*My overlay Class ends*/
