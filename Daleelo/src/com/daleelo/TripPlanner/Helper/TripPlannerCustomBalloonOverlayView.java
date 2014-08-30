package com.daleelo.TripPlanner.Helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.balloon.readystatesoftware.mapviewballoons.BalloonOverlayView;
import com.daleelo.R;
import com.google.android.maps.OverlayItem;

public class TripPlannerCustomBalloonOverlayView<Item extends OverlayItem> extends BalloonOverlayView<TripPlannerCustomOverlayItem> {

	private TextView title;
	private TextView snippet;
	public ImageView next_img;
	public ImageView add;
	public ImageView remove;
	
	
	
	public TripPlannerCustomBalloonOverlayView(Context context, int balloonBottomOffset) {
		super(context, balloonBottomOffset);
	}
	
	@Override
	protected void setupView(Context context, final ViewGroup parent) {
		
		// inflate our custom layout into parent
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.tripplanner_ballon_overlay, parent);
		
		// setup our fields
		title   	= (TextView) v.findViewById(R.id.balloon_item_title);
		snippet 	= (TextView) v.findViewById(R.id.balloon_item_snippet);
        next_img   	= (ImageView) v.findViewById(R.id.next_img);
        add    	    = (ImageView) v.findViewById(R.id.tripplanner_add_balloon); 
        remove  	= (ImageView) v.findViewById(R.id.tripplanner_remove_balloon);
      
	}

	@Override
	protected void setBalloonData(TripPlannerCustomOverlayItem item, ViewGroup parent) {
		
		// map our custom item data to fields
		title.setText(item.getTitle());
		snippet.setText(item.getSnippet());

		if(item.type_of_item == TripPlannerCustomOverlayItem.DESTINATION){
			next_img.setVisibility(View.GONE);
			add.setVisibility(View.GONE);
			remove.setVisibility(View.GONE);	
		}else if(item.type_of_item == TripPlannerCustomOverlayItem.SOURCE){
			next_img.setVisibility(View.GONE);
			add.setVisibility(View.GONE);
			remove.setVisibility(View.GONE);			
		}else if(item.type_of_item == TripPlannerCustomOverlayItem.MIDDLE_ADD){
			add.setVisibility(View.VISIBLE);
			remove.setVisibility(View.GONE);
			next_img.setVisibility(View.VISIBLE);
		}else if(item.type_of_item == TripPlannerCustomOverlayItem.MIDDLE_REMOVE){
			remove.setVisibility(View.VISIBLE);
			add.setVisibility(View.GONE);
			next_img.setVisibility(View.VISIBLE);
		}
		
		
	}

	
	
}
