package com.daleelo.DashBoardEvents.Activities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daleelo.R;
import com.daleelo.DashBoardEvents.Model.EventsCalenderModel;
import com.daleelo.Main.Activities.MainHomeScreen;
import com.daleelo.Utilities.Urls;
import com.daleelo.Utilities.Utils;
import com.daleelo.helper.DateFormater;
import com.daleelo.helper.ImageDownloader;

public class EventsDetailsByDayActivity extends Activity{
	
	private String selected_date_string;
	private int week,year;
	private ArrayList<EventsCalenderModel> all_events_data, day_event_data;
	private ArrayList<EventDetail> filltered_events_data;
	private Intent reciverIntent;
	private Calendar cal;
	private final int SETTINGS_RQST_CODE = 0;
	//UI Components.	
	private TextView txt_eng_header,txt_ism_header;
	
	private RelativeLayout rl_0_am,rl_1_am,rl_2_am,rl_3_am,rl_4_am,rl_5_am,rl_6_am,rl_7_am,rl_8_am,rl_9_am,rl_10_am,rl_11_am,rl_12_pm,
		rl_1_pm,rl_2_pm,rl_3_pm,rl_4_pm,rl_5_pm,rl_6_pm,rl_7_pm,rl_8_pm,rl_9_pm,rl_10_pm,rl_11_pm;
	
	private RelativeLayout[] rl_views ;
	private HashMap<Integer, LinearLayout> view_status; 
	private Calendar today;	
	private ImageView mCalenderBanner;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		today = Calendar.getInstance();
		today.setTimeInMillis(System.currentTimeMillis());
		
		
		Log.e("today", today.get(Calendar.DAY_OF_MONTH)+"-"+today.get(Calendar.MONTH)+"-"+today.get(Calendar.YEAR));
		
		setContentView(R.layout.dashboard_events_calendar_by_day);
		initializeUI();
		reciverIntent = getIntent();
		selected_date_string = reciverIntent.getExtras().getString("selected_date");
		week = reciverIntent.getExtras().getInt("week");
		year = reciverIntent.getExtras().getInt("year");
		all_events_data = (ArrayList<EventsCalenderModel>) reciverIntent.getExtras().getSerializable("data");
		view_status = new HashMap<Integer, LinearLayout>();
		
		long selected_date_in_milli = DateFormater.parseDate(selected_date_string, "MM/dd/yyyy");
		cal = Calendar.getInstance();
		cal.setTimeInMillis(selected_date_in_milli);
		
		
		filltered_events_data = getEventByDate(cal, all_events_data);
		
		day_event_data = new ArrayList<EventsCalenderModel>();
		for(int i=0;i<filltered_events_data.size();i++){
			Log.i("", "Title "+filltered_events_data.get(i).event_data.getEventTitle());
			day_event_data.add(filltered_events_data.get(i).event_data);
			Log.i("", "Title dd"+day_event_data.get(i).getEventTitle());
			
		}
		
		setCalheader(getDates(cal));
		setEvents();
	}
	
	public void initializeUI(){
		
		rl_0_am = (RelativeLayout) findViewById(R.id.cal_by_day_rlayout_0am);
		rl_1_am = (RelativeLayout) findViewById(R.id.cal_by_day_rlayout_1am);
		rl_2_am = (RelativeLayout) findViewById(R.id.cal_by_day_rlayout_2am);
		rl_3_am = (RelativeLayout) findViewById(R.id.cal_by_day_rlayout_3am);
		rl_4_am = (RelativeLayout) findViewById(R.id.cal_by_day_rlayout_4am);
		rl_5_am = (RelativeLayout) findViewById(R.id.cal_by_day_rlayout_5am);
		rl_6_am = (RelativeLayout) findViewById(R.id.cal_by_day_rlayout_6am);
		rl_7_am = (RelativeLayout) findViewById(R.id.cal_by_day_rlayout_7am);
		rl_8_am = (RelativeLayout) findViewById(R.id.cal_by_day_rlayout_8am);
		rl_9_am = (RelativeLayout) findViewById(R.id.cal_by_day_rlayout_9am);
		rl_10_am = (RelativeLayout) findViewById(R.id.cal_by_day_rlayout_10am);
		rl_11_am = (RelativeLayout) findViewById(R.id.cal_by_day_rlayout_11am);
		rl_12_pm = (RelativeLayout) findViewById(R.id.cal_by_day_rlayout_12pm);
		rl_1_pm = (RelativeLayout) findViewById(R.id.cal_by_day_rlayout_1pm);
		rl_2_pm = (RelativeLayout) findViewById(R.id.cal_by_day_rlayout_2pm);
		rl_3_pm = (RelativeLayout) findViewById(R.id.cal_by_day_rlayout_3pm);
		rl_4_pm = (RelativeLayout) findViewById(R.id.cal_by_day_rlayout_4pm);
		rl_5_pm = (RelativeLayout) findViewById(R.id.cal_by_day_rlayout_5pm);
		rl_6_pm = (RelativeLayout) findViewById(R.id.cal_by_day_rlayout_6pm);
		rl_7_pm = (RelativeLayout) findViewById(R.id.cal_by_day_rlayout_7pm);
		rl_8_pm = (RelativeLayout) findViewById(R.id.cal_by_day_rlayout_8pm);
		rl_9_pm = (RelativeLayout) findViewById(R.id.cal_by_day_rlayout_9pm);
		rl_10_pm = (RelativeLayout) findViewById(R.id.cal_by_day_rlayout_10pm);
		rl_11_pm = (RelativeLayout) findViewById(R.id.cal_by_day_rlayout_11pm);
		
		txt_eng_header = (TextView) findViewById(R.id.cal_by_day_txt_gregorian_header);
		txt_ism_header = (TextView) findViewById(R.id.cal_by_day_txt_islamic_header);
		
		mCalenderBanner  = (ImageView)findViewById(R.id.event_IMG_banner_day);
		
		RelativeLayout[] views = {rl_0_am,rl_1_am,rl_2_am,rl_3_am,rl_4_am,rl_5_am,rl_6_am,rl_7_am,rl_8_am,rl_9_am,rl_10_am,rl_11_am,rl_12_pm,
					rl_1_pm,rl_2_pm,rl_3_pm,rl_4_pm,rl_5_pm,rl_6_pm,rl_7_pm,rl_8_pm,rl_9_pm,rl_10_pm,rl_11_pm};
		rl_views = views;
		
		if(!EventsCalendarActivity.mCalenderBannerImg.equalsIgnoreCase("none")){
			
			mCalenderBanner.setVisibility(View.VISIBLE);
			new ImageDownloader().download(String.format(Urls.CALENDER_BANNER_IMG_URL,EventsCalendarActivity.mCalenderBannerImg), mCalenderBanner);
			
		}
	}
	
	int mPosition ;
	
	public void setEvents(){
		int count = 0;
		for(EventDetail event: filltered_events_data){
			RelativeLayout view = rl_views[event.position];
			EventByWeekDynamicView event_txt = new EventByWeekDynamicView(this);
			event_txt.setText(event.event_data.getEventTitle());
			Drawable dot = null ;
			
			if(view_status.containsKey(event.position)){
				DynamicHourlyEventView events_container= (DynamicHourlyEventView) view_status.get(event.position);
				events_container.addView(event_txt);
				event_txt.setMargin(0, 0, 0, 0,event.event_data,0, day_event_data, count, "day");			
				
				if(Boolean.parseBoolean(event.event_data.getFundrising())){
					dot = this.getResources().getDrawable(R.drawable.green_dot);					
				}else{
					dot = this.getResources().getDrawable(R.drawable.grey_dot);
				}
				Rect rect = new Rect(0, 0, 10, 10);
				dot.setBounds(rect);
				event_txt.setCompoundDrawables(dot, null, null, null);							
				events_container.addParamsForRel();
				
			}else{
				DynamicHourlyEventView events_container = new DynamicHourlyEventView(this);
				view.addView(events_container);
				events_container.addView(event_txt);
				event_txt.setMargin(0, 0, 0, 0, event.event_data, 0, day_event_data, count, "day");
				if(Boolean.parseBoolean(event.event_data.getFundrising())){
					dot = this.getResources().getDrawable(R.drawable.green_dot);					
				}else{
					dot = this.getResources().getDrawable(R.drawable.grey_dot);
				}
				Rect rect = new Rect(0, 0, 10, 10);
				dot.setBounds(rect);
				event_txt.setCompoundDrawables(dot, null, null, null);	
				
				events_container.addParamsForRel();
				
				view_status.put(event.position, events_container);
			}
			count++;
		}
	}
	
	public Date_Pair getDates(Calendar date){
		Date_Pair mDate_Pair = new Date_Pair();
		int eng_dd = date.get(Calendar.DAY_OF_MONTH);
		int eng_mm = date.get(Calendar.MONTH);
		int eng_yyyy = date.get(Calendar.YEAR);		
		
		String[] ism_values = getIsmDate(eng_mm+1, eng_dd, eng_yyyy).split("/");
		
		int ism_dd = (int) Double.parseDouble(ism_values[0]);
		String ism_mm = ism_values[1];
		String ism_yyyy = ism_values[2];
		
		mDate_Pair.eng_date = String.valueOf(eng_dd);
		mDate_Pair.eng_month = String.valueOf(eng_mm);
		mDate_Pair.eng_year = String.valueOf(eng_yyyy);
		mDate_Pair.ism_date = String.valueOf(ism_dd);
		mDate_Pair.ism_month = String.valueOf(ism_mm);
		mDate_Pair.ism_year = String.valueOf(ism_yyyy);		
		
		return mDate_Pair;
		
	}
	
	public ArrayList<EventDetail> getEventByDate(Calendar cal,ArrayList<EventsCalenderModel> events ){
		Calendar event_start_date = Calendar.getInstance();
		Calendar event_end_date = Calendar.getInstance();
	
		EventDetail mEventDetail;
		ArrayList<EventDetail> filltered_events_data = new ArrayList<EventDetail>();
		
		for(EventsCalenderModel event: events){
			mEventDetail= new EventDetail();
			event_start_date.setTimeInMillis(DateFormater.parseDate(event.getEventStartsOn(), "MM/dd/yyyy h:mm:ss a"));			
			event_end_date.setTimeInMillis(DateFormater.parseDate(event.getEventEndsOn(), "MM/dd/yyyy h:mm:ss a"));
			mEventDetail.position = event_start_date.get(Calendar.HOUR_OF_DAY);

			event_start_date.set(Calendar.HOUR_OF_DAY, 0);
			event_start_date.set(Calendar.MINUTE, 0);
			event_start_date.set(Calendar.SECOND, 0);
			event_start_date.set(Calendar.MILLISECOND, 0);
			
			event_end_date.set(Calendar.HOUR_OF_DAY, 0);
			event_end_date.set(Calendar.MINUTE, 0);
			event_end_date.set(Calendar.SECOND, 0);
			event_end_date.set(Calendar.MILLISECOND, 0);
			
			if(cal.getTime().compareTo(event_start_date.getTime()) >= 0 && cal.getTime().compareTo(event_end_date.getTime()) <= 0){
				mEventDetail.event_data = event;
				if(Boolean.parseBoolean(event.getFundrising())){
					if(Utils.fundraising_events){
						
						filltered_events_data.add(mEventDetail);
						
					}
				}else{
					if(Utils.normal_events){
						
						filltered_events_data.add(mEventDetail);
						
					}
				}
			}
			mEventDetail = null;
			
		}		
		
		day_event_data = null;
		day_event_data = new ArrayList<EventsCalenderModel>();
		
		for(int i=0;i<filltered_events_data.size();i++){
			Log.i("", "Title "+filltered_events_data.get(i).event_data.getEventTitle());
			day_event_data.add(filltered_events_data.get(i).event_data);
			Log.i("", "Title dd"+day_event_data.get(i).getEventTitle());
			
		}
		return filltered_events_data;
		
	}
	
	public void onPreviousClinked(View v){
		cal.add(Calendar.DAY_OF_MONTH, -1);
		filltered_events_data = getEventByDate(cal, all_events_data);
		clearView(view_status);
		setCalheader(getDates(cal));
		setEvents();
	}
	
	public void onNextClicked(View v){
		cal.add(Calendar.DAY_OF_MONTH, +1);
		filltered_events_data = getEventByDate(cal, all_events_data);
		clearView(view_status);
		setCalheader(getDates(cal));
		setEvents();
	}
	
	public void onMonthClicked(View v){
		startActivity(new Intent(this,EventsCalendarActivity.class).setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
	}	
	public void onWeekClicked(View v){
		 startActivity(new Intent(this,EventsDetailsByWeekActivity.class).putExtra("selected_date", selected_date_string).putExtra("week", week).putExtra("year", year).putExtra("data", all_events_data).setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));  

	}
	
	public void onListClicked(View v){
		startActivity(new Intent(this,EventsCalendarEventListActivity.class).putExtra("eventslist", all_events_data));
	}
	
	public void onTodayClicked(View v){
		cal.setTimeInMillis(System.currentTimeMillis());
	
		filltered_events_data = getEventByDate(cal, all_events_data);
		clearView(view_status);
		setCalheader(getDates(cal));
		setEvents();

//		startActivity(new Intent(this,EventsCalendarActivity.class).setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
	}
	
	public void onSettingCliked(View v){
		startActivityForResult(new Intent(this, EventsCalendarSettingsActivity.class),SETTINGS_RQST_CODE);
	}
	
	public void setCalheader(Date_Pair date){
		String eng_header = EventsDetailsByWeekActivity.MONTHS[Integer.parseInt(date.eng_month)]+" "+
									date.eng_date+","+date.eng_year;
		String ism_header = date.ism_month+" "+date.ism_date+","+ date.ism_year;
		txt_eng_header.setText(eng_header);
		txt_ism_header.setText(ism_header);
	}
	
	public String getIsmDate(int eng_month, int eng_date,int eng_year){
		String ism_string = new EventsIslamicCalendarActivity().getIslamicDate(Double.valueOf(eng_date), Double.valueOf(eng_month),Double.valueOf(eng_year));
		
		return ism_string;
	}
	
	public void clearView(HashMap<Integer,LinearLayout> view_status){
		Set set = view_status.entrySet();
		Iterator i = set.iterator();
		
		while(i.hasNext()){
			Map.Entry me = (Map.Entry)i.next();
			LinearLayout view = (LinearLayout) me.getValue();
			view.removeAllViews();
		}
	}
	
	class Date_Pair{
		String eng_date, eng_month, eng_year;		
		String ism_date, ism_month, ism_year;
	}
	
	class EventDetail{
		private EventsCalenderModel event_data;
		private int position;
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		startActivity(new Intent(this,MainHomeScreen.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case SETTINGS_RQST_CODE:
				filltered_events_data = getEventByDate(cal, all_events_data);
				clearView(view_status);
				setCalheader(getDates(cal));
				setEvents();
			break;

		default:
			break;
		}
	}
}
