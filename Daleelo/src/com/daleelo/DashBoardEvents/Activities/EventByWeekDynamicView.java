package com.daleelo.DashBoardEvents.Activities;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.daleelo.DashBoardEvents.Model.EventsCalenderModel;

public class EventByWeekDynamicView extends TextView implements android.view.View.OnClickListener{

	private Context context;
	private EventsCalenderModel mEventsCalenderModel = null;
	private ArrayList<EventsCalenderModel> event_data;
	private String type;
	private int position;
	
	public EventByWeekDynamicView(Context context) {
		super(context);
		this.context = context;
		this.setGravity(Gravity.CENTER);
		this.setTextColor(Color.BLACK);
		this.setClickable(true);
		this.setOnClickListener(this);
		// TODO Auto-generated constructor stub
	}
	

	/*
	 * This method is written to dynamically set the view margins.
	 * 
	 * @param margin_right add required right margin.
	 * @param margin_left add required left margin.
	 */

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
			for(int i=0	; i<event_data.size();i++){
				
				Log.i("", "Title "+event_data.get(i).getEventTitle());
				
			}
				
			Log.i("", "position "+position);
				
				this.context.startActivity(new Intent(context,CalendarEventDetailDesc.class)
				.putExtra("data", event_data)
				.putExtra("position", position)
				.putExtra("from","list"));
	
		
//		this.context.startActivity(new Intent(context,CalendarEventDetailDesc.class)
//		.putExtra("data", mEventsCalenderModel));
	}
	


	public void setMargin(
			int margin_left, 
			int margin_right, 
			float margin_top, 
			float margin_bottom, 
			EventsCalenderModel mEventsCalenderModel,
			int width,
			ArrayList<EventsCalenderModel> event_data, 
			int position, 
			String type) {
		
		// TODO Auto-generated method stub
		
		
		LinearLayout.LayoutParams params = (LayoutParams) this.getLayoutParams();
		params.width = width;
		params.height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
		params.leftMargin = margin_left;
		params.rightMargin = margin_right;
		params.topMargin = (int) margin_top;
		params.bottomMargin = (int) margin_bottom;
		params.weight = 1f;
		this.setLayoutParams(params);
		this.mEventsCalenderModel = mEventsCalenderModel;
		this.type = type;
		this.event_data = event_data;
		this.position = position;
		
	}
	
	

}
