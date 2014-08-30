package com.daleelo.TripPlanner.Activities;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.balloon.readystatesoftware.mapviewballoons.BalloonItemizedOverlay;
import com.balloon.readystatesoftware.mapviewballoons.BalloonOverlayView;
import com.daleelo.Business.Activities.BusinessDetailDesp;
import com.daleelo.DashBoardClassified.Activities.ClassifiedItemDetailDesp;
import com.daleelo.Dashboard.Activities.DealsDetailDesp;
import com.daleelo.Dashboard.Activities.SpotlightDetailDesp;
import com.daleelo.Masjid.Activities.MasjidDetailDescription;
import com.daleelo.TripPlanner.Helper.TripPlannerCustomBalloonOverlayView;
import com.daleelo.TripPlanner.Helper.TripPlannerCustomOverlayItem;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;


public class TripPlannerCustomizedOverlay<Item extends OverlayItem> extends BalloonItemizedOverlay<TripPlannerCustomOverlayItem>{
	
	private ArrayList<TripPlannerCustomOverlayItem> mapOverlays = new ArrayList<TripPlannerCustomOverlayItem>();
	

	
	ArrayList<GeoPoint> mPoints; 	
	
	MapView mv;
	TripPlannerCustomBalloonOverlayView<TripPlannerCustomOverlayItem> mView;	
	TripPlannerMapActivityNew mContext;
	int index;
	private ArrayList<Integer> int_arr = new ArrayList<Integer>();	
	
	
	public TripPlannerCustomizedOverlay(Drawable defaultMarker, TripPlannerMapActivityNew context,MapView mv) {
		super(boundCenter(defaultMarker), mv);
		  this.mContext = context;		
		  this.mv = mv;		  	  
		  mPoints = new ArrayList<GeoPoint>();  
			  
	} 	
	
	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
			super.draw(canvas, mapView, false);
	}
	
	@Override
	protected TripPlannerCustomOverlayItem createItem(int i){			
		return mapOverlays.get(i);
	}

	@Override
	public int size(){			
		return  mapOverlays.size();
	} 
	
	public void addOverlay(TripPlannerCustomOverlayItem overlay){
		mapOverlays.add(overlay);
	    this.populate();
	}

	@Override
	protected BalloonOverlayView<TripPlannerCustomOverlayItem> createBalloonOverlayView() {
		
		
		mView = new TripPlannerCustomBalloonOverlayView<TripPlannerCustomOverlayItem>(getMapView().getContext(), getBalloonBottomOffset()+20);
			
		mView.remove.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					 index = getcurrentFocusedIndex();
					 if( mContext.loc_points_arr.get(index).isMiddlecity()){
						 mContext.loc_points_arr.remove(index);		
						 mapOverlays.clear();
						 hideBalloon();							
						 mv.invalidate();				 
						 mContext.showRoute();	
					 }else{						 
						 mContext.loc_points_arr.get(index).setCategory("sub");
						 mapOverlays.clear();
						 hideBalloon();							
						 mv.invalidate();				 
						 mContext.showRoute();	
					 }
				}
				
		});
		
		mView.add.setOnClickListener(new OnClickListener() {
			
				@Override
				public void onClick(View v) {
					 index = getcurrentFocusedIndex();				
					 mContext.loc_points_arr.get(index).setCategory("main");
					 mContext.loc_points_arr.get(index).setType("t");
					 mapOverlays.clear();
					 hideBalloon();							
					 mv.invalidate();				 
					 mContext.showRoute();			
				}
	   });
				
		mView.next_img.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						index = getcurrentFocusedIndex();
						String item_type = mContext.loc_points_arr.get(index).getItemType();
						Log.v("item type",""+mContext.loc_points_arr.get(index).getItemType());
						Log.v("gettype",""+mContext.loc_points_arr.get(index).getType());
						if(mContext.loc_points_arr.get(index).getType().equalsIgnoreCase("t")){
						
							if(item_type.equalsIgnoreCase("deal")){
								
								Intent desc_intent = new Intent(mContext,DealsDetailDesp.class);
								desc_intent.putExtra("from", "item");
								desc_intent.putExtra("id",mContext.loc_points_arr.get(index).getBusinessId());
								mContext.startActivity(desc_intent);
								
							}else if(item_type.equalsIgnoreCase("spotlight")){
								
								Intent desc_intent = new Intent(mContext,SpotlightDetailDesp.class);
								desc_intent.putExtra("from", "item");
								desc_intent.putExtra("id",mContext.loc_points_arr.get(index).getBusinessId());
								mContext.startActivity(desc_intent);
								
							}else if(item_type.equalsIgnoreCase("masjid")){
								
								Intent desc_intent = new Intent(mContext,MasjidDetailDescription.class);
								desc_intent.putExtra("from", "item");
								desc_intent.putExtra("id",mContext.loc_points_arr.get(index).getBusinessId());
								mContext.startActivity(desc_intent);
								
							}else if(item_type.equalsIgnoreCase("classified")){
								
								Intent desc_intent = new Intent(mContext,ClassifiedItemDetailDesp.class);
								desc_intent.putExtra("from", "item");
								desc_intent.putExtra("id",mContext.loc_points_arr.get(index).getBusinessId());
								mContext.startActivity(desc_intent);
								
							}else if(item_type.equalsIgnoreCase("business")){
								
								Intent desc_intent = new Intent(mContext,BusinessDetailDesp.class);
								desc_intent.putExtra("from", "item");
								desc_intent.putExtra("id",mContext.loc_points_arr.get(index).getBusinessId());
								mContext.startActivity(desc_intent);
							}
							
						}else{						
							hideBalloon();
						}
					}
    	});
		
		return mView;
	}
	
	
	
	@Override
	protected int getcurrentFocusedIndex() {
		// TODO Auto-generated method stub
		return super.getcurrentFocusedIndex();
	}

	
	
} 
