package com.daleelo.DashBoardEvents.Activities;

import java.util.ArrayList;

import com.daleelo.R;
import com.daleelo.DashBoardEvents.Model.EventsCalenderModel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class EventsCalendarEventListActivity extends Activity implements OnItemClickListener{
	
	private ArrayList<EventsCalenderModel> mEventsList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calendar_events_list);
		mEventsList = (ArrayList<EventsCalenderModel>) getIntent().getExtras().get("eventslist");		
		ListView events_list = (ListView) findViewById(R.id.events_LIST_list);
		events_list.setOnItemClickListener(this);
		EventsListAdapter mEventsListAdapter = new EventsListAdapter(this, mEventsList);
		events_list.setAdapter(mEventsListAdapter);
		
	}
	
	
	class EventsListAdapter extends ArrayAdapter<String>{

		Context context;
		ArrayList<EventsCalenderModel> feeds;
		public EventsListAdapter(Context context,  ArrayList<EventsCalenderModel> mEventsList) {
			super(context, R.layout.calendar_events_listrow);
			this.context = context;
			feeds = mEventsList;
		}
		
		@Override
		public int getCount() {
			return feeds.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.calendar_events_listrow, null);
			TextView txt_date = (TextView)convertView.findViewById(R.id.calendar_events_listrow_TXT_date);
			TextView txt_time = (TextView)convertView.findViewById(R.id.calendar_events_listrow_TXT_time);
			TextView txt_desc = (TextView)convertView.findViewById(R.id.calendar_events_listrow_TXT_desc);
			ImageView dot_img = (ImageView)convertView.findViewById(R.id.calendar_events_listrow_IMG_dot);
			
			txt_date.setText(feeds.get(position).getEventStartsOn().split(" ")[0]);
			txt_time.setText(feeds.get(position).getEventStartsOn().split(" ")[1]+" "+feeds.get(position).getEventStartsOn().split(" ")[2]);
			txt_desc.setText(feeds.get(position).getEventTitle());
			
			if(feeds.get(position).getFundrising().equalsIgnoreCase("true")){
				dot_img.setBackgroundResource(R.drawable.green_dot);
			}else{
				dot_img.setBackgroundResource(R.drawable.grey_dot);
			}
			
			
			return convertView;
		}
		
		
		
	}


	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
		startActivity(new Intent(this,CalendarEventDetailDesc.class)
		.putExtra("data", mEventsList)
		.putExtra("position", position)
		.putExtra("from","list"));
	}

}
