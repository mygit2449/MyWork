package com.daleelo.DashBoardEvents.Activities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.AbsoluteLayout;
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

public class EventsDetailsByWeekActivity extends Activity{
	
	public final static String[] MONTHS = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
	private final int EVENT_WHOLE_WEEK = 0, EVENT_HOURLY_BASE = 1;
	private final int SETTINGS_RQST_CODE = 0;

	private int week_of_year,year;
	
	int[] location = new int[2];	
	int width;
	
	private ArrayList<EventsCalenderModel> all_events_data, week_event_data; // Array list which contains all events details.
	private ArrayList<EventsDetails> events_data; // Array list contains only the events exists in selected week.
	private Set<String> data ; // Hash set to check existing events in event data to avoid duplicate.
	private ArrayList<Date_Pair> eng_ism_date; // arraylist for english date with there respective ism date.
	
	private DatePosition[] date_view_offset = { new DatePosition(),new DatePosition(),new DatePosition(),
												new DatePosition(),new DatePosition(),new DatePosition(),
												new DatePosition()};
	
	private TimePosition[] time_view_offset = { new TimePosition(),new TimePosition(),new TimePosition(),
												new TimePosition(),new TimePosition(),new TimePosition(),
												new TimePosition(),new TimePosition(),new TimePosition(),
												new TimePosition(),new TimePosition(),new TimePosition(),
												new TimePosition(),new TimePosition(),new TimePosition(),
												new TimePosition(),new TimePosition(),new TimePosition(),
												new TimePosition(),new TimePosition(),new TimePosition(),
												new TimePosition(),new TimePosition(),new TimePosition()};
	private Calendar eng_date,event_date;
	private HashMap<Date, LinearLayout> linearViews;
	private String selected_date;
	
	//UI components
	private TextView txt_cal_eng_header,txt_cal_ism_header,txt_sun_eng_date,txt_sun_ism_date,txt_mon_eng_date,txt_mon_ism_date
	,txt_tue_eng_date,txt_tue_ism_date,txt_wed_eng_date,txt_wed_ism_date,txt_thu_eng_date,txt_thu_ism_date
	,txt_fri_eng_date,txt_fri_ism_date,txt_sat_eng_date,txt_sat_ism_date;
	private LinearLayout events_container;
	private AbsoluteLayout event_container_by_hour;
	private RelativeLayout rl_0_am,rl_1_am,rl_2_am,rl_3_am,rl_4_am,rl_5_am,rl_6_am,rl_7_am,rl_8_am,rl_9_am,rl_10_am,rl_11_am,rl_12_pm,
			rl_1_pm,rl_2_pm,rl_3_pm,rl_4_pm,rl_5_pm,rl_6_pm,rl_7_pm,rl_8_pm,rl_9_pm,rl_10_pm,rl_11_pm;
	private EventByWeekDynamicView event_view;
	private DynamicHourlyEventView events_in_a_day_container;
	
	private ImageView mCalenderBanner;
	Calendar today;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		today = Calendar.getInstance();
		setContentView(R.layout.dashboard_events_calendar_by_week);
		initializeUi();
		eng_date = Calendar.getInstance();
		event_date = Calendar.getInstance();
		eng_ism_date = new ArrayList<EventsDetailsByWeekActivity.Date_Pair>();

		selected_date = getIntent().getExtras().getString("selected_date");
		week_of_year = getIntent().getExtras().getInt("week");
		year = getIntent().getExtras().getInt("year");
		all_events_data = (ArrayList<EventsCalenderModel>) getIntent().getExtras().getSerializable("data");
		getWeekDates(week_of_year, year);
		
		
	
		setContent(eng_ism_date);
		Display display = getWindowManager().getDefaultDisplay();
		width = display.getWidth(); // deprecated
		linearViews = new HashMap<Date, LinearLayout>();

		myThread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				myhandler.sendEmptyMessage(0);

			}
		});
		myThread.start();

	}
	
	

	Thread myThread;

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	MyHandelr myhandler= new MyHandelr();
	class MyHandelr extends Handler{		
		public void handleMessage(android.os.Message message){		
			getViewPositionsOnScreen();
			addEventViewsByWeek();
			addEventViewByHours();			
		}
	}

	/*
	 * @param eng_week_dates pass the array of selected week dates.
	 * 
	 * @return hash map of pairs of eng date with respective ism date.
	 */
	
	public ArrayList<Date_Pair> getWeekDates(int week_of_year, int year){
		int week_day_date,week_end_date = 0;
		int event_end_day_of_year,event_start_day_of_year;
		int eng_dd, eng_mm, eng_yyyy;
		int ism_dd;
		String ism_mm , ism_yyyy;
		
		Date event_start_date,event_end_date;
		
		Date_Pair mDate_Pair;
		EventsDetails mEvent_details;
		eng_ism_date.clear();
		eng_date.clear();
		
		events_data = new ArrayList<EventsDetails>();
		data =  new HashSet<String>();
		eng_date.set(Calendar.WEEK_OF_YEAR, week_of_year);
		eng_date.set(Calendar.YEAR, year);
		
		for(int ictr = 0; ictr < 7; ictr++){
			eng_dd = eng_date.get(Calendar.DAY_OF_MONTH);
			eng_mm = eng_date.get(Calendar.MONTH)+1;
			eng_yyyy = eng_date.get(Calendar.YEAR);
			week_day_date = eng_date.get(Calendar.DAY_OF_YEAR);
			if(ictr == 0){
				week_end_date = week_day_date+6;
			}
			
			int mPosition = 0;
			
			for(EventsCalenderModel event: all_events_data){
				
				event_date.setTimeInMillis(DateFormater.parseDate(event.getEventStartsOn(), "MM/dd/yyyy h:mm:ss a"));
				event_start_date = event_date.getTime();
				event_start_day_of_year = event_date.get(Calendar.DAY_OF_YEAR);
				
				event_date.setTimeInMillis(DateFormater.parseDate(event.getEventEndsOn(), "MM/dd/yyyy h:mm:ss a"));
				event_end_date = event_date.getTime();
				event_end_day_of_year = event_date.get(Calendar.DAY_OF_YEAR);

				
				if (event_start_day_of_year <= week_day_date && event_end_day_of_year >= week_day_date) {
					long diff = (event_end_date.getTime() - event_start_date.getTime());
					int diff_in_hours = (int) (diff / (60 * 60 * 1000));
					if (!data.contains(event.getEventId())) {
						mEvent_details = new EventsDetails();
						mEvent_details.position = mPosition;
						mEvent_details.event_data = event;
						mEvent_details.start_offset = ictr;
						Calendar cal = Calendar.getInstance();
						cal.setTime(event_start_date);
						cal.set(Calendar.HOUR_OF_DAY, 0);
						cal.set(Calendar.MINUTE, 0);
						cal.set(Calendar.SECOND, 0);
						cal.set(Calendar.MILLISECOND,0);						
						mEvent_details.start_date = cal.getTime();

						if (event_end_day_of_year >= week_end_date) {
							mEvent_details.end_offset = 6;
						} else {
							mEvent_details.end_offset = 6 - (week_end_date - event_end_day_of_year);
						}
						
						
						
						if(event_end_date.getHours() == event_start_date.getHours()){
							mEvent_details.event_type = EVENT_WHOLE_WEEK;
						}else{
							if(mEvent_details.end_offset - mEvent_details.start_offset < 6){
								mEvent_details.event_type = EVENT_HOURLY_BASE;
							}else{
								mEvent_details.event_type = EVENT_WHOLE_WEEK;
							}
						}
						
						mEvent_details.vertical_start_offset = event_start_date.getHours();
						mEvent_details.vertical_end_offset = event_end_date.getHours();
						float min =( event_start_date.getMinutes()/60f)*100f;
						mEvent_details.vertical_start_min_offset =   (( event_start_date.getMinutes()/60f)*100f);
						mEvent_details.vertical_end_min_offset =  (( event_end_date.getMinutes()/60f)*100f);
						if(Boolean.parseBoolean(event.getFundrising())){
							if(Utils.fundraising_events){
								events_data.add(mEvent_details);								
							}
						}else{
							if(Utils.normal_events){
								events_data.add(mEvent_details);
							}
						}
						
						data.add(event.getEventId());
						mEvent_details = null;
						mPosition++;

					}
				}
			}		
			
			String[] ism_values = getIsmDate(eng_mm, eng_dd, eng_yyyy).split("/"); 
			ism_dd = (int) Double.parseDouble(ism_values[0]);
			ism_mm = ism_values[1];
			ism_yyyy = ism_values[2];
			
			mDate_Pair = new Date_Pair();
			mDate_Pair.eng_date = String.valueOf(eng_dd);
			mDate_Pair.eng_month = MONTHS[eng_date.get(Calendar.MONTH)];
			mDate_Pair.eng_year = String.valueOf(eng_yyyy);
			
			mDate_Pair.ism_date = String.valueOf(ism_dd);
			mDate_Pair.ism_month = ism_mm;
			mDate_Pair.ism_year = ism_yyyy;
			
			eng_ism_date.add(mDate_Pair);
			mDate_Pair = null;
			eng_date.add(Calendar.DATE, 1);			
		}
		
		week_event_data = null;		
		week_event_data = new ArrayList<EventsCalenderModel>();		
		
		for(int i=0;i<events_data.size();i++){
			Log.i("", "Title "+events_data.get(i).event_data.getEventTitle());
			week_event_data.add(events_data.get(i).event_data);
			Log.i("", "Title dd"+week_event_data.get(i).getEventTitle());			
		}
		
		
		return eng_ism_date;
		
	}
	
	/*
	 * @param eng_month pass english month.
	 * @param eng_date pass english date.
	 * @param eng_year pass english year.
	 * 
	 * @return this method will return the string islamic date in mm/dd/yyyy.
	 */
	public String getIsmDate(int eng_month, int eng_date,int eng_year){
		String ism_string = new EventsIslamicCalendarActivity().getIslamicDate(Double.valueOf(eng_date), Double.valueOf(eng_month),Double.valueOf(eng_year));
		
		return ism_string;
	}
	
	public void initializeUi(){
		rl_0_am = (RelativeLayout) findViewById(R.id.cal_by_week_rlayout_0am);
		rl_1_am = (RelativeLayout) findViewById(R.id.cal_by_week_rlayout_1am);
		rl_2_am = (RelativeLayout) findViewById(R.id.cal_by_week_rlayout_2am);
		rl_3_am = (RelativeLayout) findViewById(R.id.cal_by_week_rlayout_3am);
		rl_4_am = (RelativeLayout) findViewById(R.id.cal_by_week_rlayout_4am);
		rl_5_am = (RelativeLayout) findViewById(R.id.cal_by_week_rlayout_5am);
		rl_6_am = (RelativeLayout) findViewById(R.id.cal_by_week_rlayout_6am);
		rl_7_am = (RelativeLayout) findViewById(R.id.cal_by_week_rlayout_7am);
		rl_8_am = (RelativeLayout) findViewById(R.id.cal_by_week_rlayout_8am);
		rl_9_am = (RelativeLayout) findViewById(R.id.cal_by_week_rlayout_9am);
		rl_10_am = (RelativeLayout) findViewById(R.id.cal_by_week_rlayout_10am);
		rl_11_am = (RelativeLayout) findViewById(R.id.cal_by_week_rlayout_11am);
		rl_12_pm = (RelativeLayout) findViewById(R.id.cal_by_week_rlayout_12pm);
		rl_1_pm = (RelativeLayout) findViewById(R.id.cal_by_week_rlayout_1pm);
		rl_2_pm = (RelativeLayout) findViewById(R.id.cal_by_week_rlayout_2pm);
		rl_3_pm = (RelativeLayout) findViewById(R.id.cal_by_week_rlayout_3pm);
		rl_4_pm = (RelativeLayout) findViewById(R.id.cal_by_week_rlayout_4pm);
		rl_5_pm = (RelativeLayout) findViewById(R.id.cal_by_week_rlayout_5pm);
		rl_6_pm = (RelativeLayout) findViewById(R.id.cal_by_week_rlayout_6pm);
		rl_7_pm = (RelativeLayout) findViewById(R.id.cal_by_week_rlayout_7pm);
		rl_8_pm = (RelativeLayout) findViewById(R.id.cal_by_week_rlayout_8pm);
		rl_9_pm = (RelativeLayout) findViewById(R.id.cal_by_week_rlayout_9pm);
		rl_10_pm = (RelativeLayout) findViewById(R.id.cal_by_week_rlayout_10pm);
		rl_11_pm = (RelativeLayout) findViewById(R.id.cal_by_week_rlayout_11pm);
		
		
		txt_sun_eng_date = (TextView) findViewById(R.id.cal_by_week_txt_sun_eng_date);
		txt_sun_ism_date = (TextView) findViewById(R.id.cal_by_week_txt_sun_ism_date);
		txt_mon_eng_date = (TextView) findViewById(R.id.cal_by_week_txt_mon_eng_date);
		txt_mon_ism_date = (TextView) findViewById(R.id.cal_by_week_txt_mon_ism_date);
		txt_tue_eng_date = (TextView) findViewById(R.id.cal_by_week_txt_tue_eng_date);
		txt_tue_ism_date = (TextView) findViewById(R.id.cal_by_week_txt_tue_ism_date);
		txt_wed_eng_date = (TextView) findViewById(R.id.cal_by_week_txt_wed_eng_date);
		txt_wed_ism_date = (TextView) findViewById(R.id.cal_by_week_txt_wed_ism_date);
		txt_thu_eng_date = (TextView) findViewById(R.id.cal_by_week_txt_thu_eng_date);
		txt_thu_ism_date = (TextView) findViewById(R.id.cal_by_week_txt_thu_ism_date);
		txt_fri_eng_date = (TextView) findViewById(R.id.cal_by_week_txt_fri_eng_date);
		txt_fri_ism_date = (TextView) findViewById(R.id.cal_by_week_txt_fri_ism_date);
		txt_sat_eng_date = (TextView) findViewById(R.id.cal_by_week_txt_sat_eng_date);
		txt_sat_ism_date = (TextView) findViewById(R.id.cal_by_week_txt_sat_ism_date);
		txt_cal_eng_header = (TextView) findViewById(R.id.cal_by_week_txt_gregorian_header);
		txt_cal_ism_header = (TextView) findViewById(R.id.cal_by_week_txt_islamic_header);
		
		mCalenderBanner  = (ImageView)findViewById(R.id.event_IMG_banner_week);

		
		events_container = (LinearLayout) findViewById(R.id.cal_by_week_events_container);
		event_container_by_hour = (AbsoluteLayout) findViewById(R.id.cal_by_week_llayout_event_by_hour_container);
		
		if(!EventsCalendarActivity.mCalenderBannerImg.equalsIgnoreCase("none")){
			
			mCalenderBanner.setVisibility(View.VISIBLE);
			new ImageDownloader().download(String.format(Urls.CALENDER_BANNER_IMG_URL,EventsCalendarActivity.mCalenderBannerImg), mCalenderBanner);
			
		}
	}

	public void setContent(ArrayList<Date_Pair> eng_ism_date){
		String eng_header = eng_ism_date.get(0).eng_date+" "+eng_ism_date.get(0).eng_month+" "+eng_ism_date.get(0).eng_year+
		" - "+eng_ism_date.get(6).eng_date+" "+eng_ism_date.get(6).eng_month+" "+eng_ism_date.get(6).eng_year ;
		String ism_header = eng_ism_date.get(0).ism_date+" "+eng_ism_date.get(0).ism_month+" "+eng_ism_date.get(0).ism_year+
		" - "+eng_ism_date.get(6).ism_date+" "+eng_ism_date.get(6).ism_month+" "+eng_ism_date.get(6).ism_year ;
		txt_cal_eng_header.setText(eng_header);
		txt_cal_ism_header.setText(ism_header);
		txt_sun_eng_date.setText(eng_ism_date.get(0).eng_date+"/"); // set sun eng date
		txt_sun_ism_date.setText(eng_ism_date.get(0).ism_date); // set sun ism date
		txt_mon_eng_date.setText(eng_ism_date.get(1).eng_date+"/"); 
		txt_mon_ism_date.setText(eng_ism_date.get(1).ism_date);
		txt_tue_eng_date.setText(eng_ism_date.get(2).eng_date+"/"); 
		txt_tue_ism_date.setText(eng_ism_date.get(2).ism_date);
		txt_wed_eng_date.setText(eng_ism_date.get(3).eng_date+"/"); 
		txt_wed_ism_date.setText(eng_ism_date.get(3).ism_date);
		txt_thu_eng_date.setText(eng_ism_date.get(4).eng_date+"/"); 
		txt_thu_ism_date.setText(eng_ism_date.get(4).ism_date);
		txt_fri_eng_date.setText(eng_ism_date.get(5).eng_date+"/"); 
		txt_fri_ism_date.setText(eng_ism_date.get(5).ism_date);
		txt_sat_eng_date.setText(eng_ism_date.get(6).eng_date+"/"); 
		txt_sat_ism_date.setText(eng_ism_date.get(6).ism_date);		

		
	}
	
	public void addEventViewsByWeek(){
		int count = 0;
		for(EventsDetails event: events_data){
			if(event.event_type == EVENT_WHOLE_WEEK){
				event_view = new EventByWeekDynamicView(this);
				
				if(Boolean.parseBoolean(event.event_data.getFundrising())){
					event_view.setBackgroundColor(Color.GREEN);
				}else{
					event_view.setBackgroundColor(Color.GRAY);				
				}
				
				txt_sun_eng_date.getLocationOnScreen(location);			
				events_container.addView(event_view);				
				event_view.setMargin(
						date_view_offset[event.start_offset].left_position,
						width - date_view_offset[event.end_offset].right_postion, 
						0, 
						5,
						event.event_data, 
						android.view.ViewGroup.LayoutParams.FILL_PARENT,
						week_event_data,
						count,
						"week");
				
				event_view.setText(event.event_data.getEventTitle());
				event_view = null;			
			}
			count++;
		}
		getTimeSlotPostion();
	}
	
	public void addEventViewByHours(){
		int count = 0;
		for(EventsDetails event: events_data){
			if(event.event_type == EVENT_HOURLY_BASE){
				event_view = new EventByWeekDynamicView(this);
				
				if(Boolean.parseBoolean(event.event_data.getFundrising())){
					event_view.setBackgroundColor(Color.GREEN);
				}else{
					event_view.setBackgroundResource(R.drawable.bg_event_grey_rect);			
				}
				
				int view_height = rl_0_am.getHeight();
				float start_min_offset = (event.vertical_start_min_offset/100f)*Float.valueOf(view_height);
				float end_min_offset = (event.vertical_end_min_offset/100f)*Float.valueOf(view_height);
				
				

				events_in_a_day_container = new DynamicHourlyEventView(this);				
				if(linearViews.containsKey(event.start_date)){
					linearViews.get(event.start_date).addView(event_view);
					event_view.setMargin(
							0,
							0,
							start_min_offset + time_view_offset[event.vertical_start_offset].top_position - time_view_offset[0].top_position, 
							time_view_offset[23].bottom_postion - time_view_offset[event.vertical_end_offset].bottom_postion + (100-end_min_offset),
							event.event_data,android.view.ViewGroup.LayoutParams.FILL_PARENT,
							week_event_data,
							count,
							"week");
					event_view.setText(event.event_data.getEventTitle()	);
				}else{
					event_container_by_hour.addView(events_in_a_day_container);			
					events_in_a_day_container.setMarginInAbs(
							date_view_offset[event.start_offset].left_position,
							(date_view_offset[event.end_offset].right_postion)-(date_view_offset[event.start_offset].left_position));
					events_in_a_day_container.addView(event_view);
					event_view.setMargin(
							0,0,start_min_offset + time_view_offset[event.vertical_start_offset].top_position - time_view_offset[0].top_position, 
							time_view_offset[23].bottom_postion - time_view_offset[event.vertical_end_offset].bottom_postion + (100-end_min_offset),
							event.event_data,
							android.view.ViewGroup.LayoutParams.FILL_PARENT,
							week_event_data,
							count,
							"week");
					event_view.setText(event.event_data.getEventTitle());
					linearViews.put(event.start_date, events_in_a_day_container);
					events_in_a_day_container = null;					
				}
				event_view = null;	
				
			}
			count++;
		}
	}
	
	public void onPreviousClinked(View v){
		events_container.removeAllViews();
		event_container_by_hour.removeAllViews();
		linearViews.clear();
		week_of_year = week_of_year - 1;
		getWeekDates(week_of_year,year);
		setContent(eng_ism_date);
		addEventViewsByWeek();
		addEventViewByHours();
	}
	
	public void onNextClicked(View v){
		events_container.removeAllViews();
		event_container_by_hour.removeAllViews();
		linearViews.clear();
		week_of_year = week_of_year + 1;
		getWeekDates(week_of_year,year);
		setContent(eng_ism_date);
		addEventViewsByWeek();
		addEventViewByHours();
	}
	
	public void onMonthClicked(View v){
		startActivity(new Intent(this,EventsCalendarActivity.class).setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
	}	
	public void onDayClicked(View v){
		startActivity(new Intent(this,
				EventsDetailsByDayActivity.class).putExtra("data",all_events_data).putExtra("selected_date", selected_date).
				putExtra("week", week_of_year).putExtra("year", year).setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));

	}
	
	public void onListClicked(View v){
		startActivity(new Intent(this,EventsCalendarEventListActivity.class).putExtra("eventslist", all_events_data));
	}
	
	public void onTodayClicked(View v){
		events_container.removeAllViews();
		event_container_by_hour.removeAllViews();
		linearViews.clear();
		week_of_year = today.get(Calendar.WEEK_OF_YEAR);
		getWeekDates(week_of_year,year);
		setContent(eng_ism_date);
		addEventViewsByWeek();
		addEventViewByHours();
//		startActivity(new Intent(this,EventsCalendarActivity.class).setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
	}
	
	public void onSettingClicked(View v){
		startActivityForResult(new Intent(this, EventsCalendarSettingsActivity.class),SETTINGS_RQST_CODE);
	}
	
	public void getViewPositionsOnScreen(){
		txt_sun_eng_date.getLocationOnScreen(location);
		date_view_offset[0].left_position = location[0];
		txt_sun_ism_date.getLocationOnScreen(location);
		date_view_offset[0].right_postion = location[0]+txt_sun_ism_date.getWidth();
		
		txt_mon_eng_date.getLocationOnScreen(location);
		date_view_offset[1].left_position = location[0];
		txt_mon_ism_date.getLocationOnScreen(location);
		date_view_offset[1].right_postion = location[0]+txt_mon_ism_date.getWidth();
		
		txt_tue_eng_date.getLocationOnScreen(location);
		date_view_offset[2].left_position = location[0];
		txt_tue_ism_date.getLocationOnScreen(location);
		date_view_offset[2].right_postion = location[0]+txt_tue_ism_date.getWidth();
		
		txt_wed_eng_date.getLocationOnScreen(location);
		date_view_offset[3].left_position = location[0];
		txt_wed_ism_date.getLocationOnScreen(location);
		date_view_offset[3].right_postion = location[0]+txt_wed_ism_date.getWidth();
		
		txt_thu_eng_date.getLocationOnScreen(location);
		date_view_offset[4].left_position = location[0];
		txt_thu_ism_date.getLocationOnScreen(location);
		date_view_offset[4].right_postion = location[0]+txt_thu_ism_date.getWidth();
		
		txt_fri_eng_date.getLocationOnScreen(location);
		date_view_offset[5].left_position = location[0];
		txt_fri_ism_date.getLocationOnScreen(location);
		date_view_offset[5].right_postion = location[0]+txt_fri_ism_date.getWidth();
		
		txt_sat_eng_date.getLocationOnScreen(location);
		date_view_offset[6].left_position = location[0];
		txt_sat_ism_date.getLocationOnScreen(location);
		date_view_offset[6].right_postion = location[0]+txt_sat_ism_date.getWidth();
	}

	public void getTimeSlotPostion(){
		rl_0_am.getLocationOnScreen(location);
		time_view_offset[0].top_position = location[1];
		time_view_offset[0].bottom_postion = location[1]+rl_0_am.getHeight();
		
		rl_1_am.getLocationOnScreen(location);
		time_view_offset[1].top_position = location[1];
		time_view_offset[1].bottom_postion = location[1]+rl_1_am.getHeight();
		
		rl_2_am.getLocationOnScreen(location);
		time_view_offset[2].top_position = location[1];
		time_view_offset[2].bottom_postion = location[1]+rl_2_am.getHeight();
		
		rl_3_am.getLocationOnScreen(location);
		time_view_offset[3].top_position = location[1];
		time_view_offset[3].bottom_postion = location[1]+rl_0_am.getHeight();
		
		rl_4_am.getLocationOnScreen(location);
		time_view_offset[4].top_position = location[1];
		time_view_offset[4].bottom_postion = location[1]+rl_0_am.getHeight();
		
		rl_5_am.getLocationOnScreen(location);
		time_view_offset[5].top_position = location[1];
		time_view_offset[5].bottom_postion = location[1]+rl_0_am.getHeight();
		
		rl_6_am.getLocationOnScreen(location);
		time_view_offset[6].top_position = location[1];
		time_view_offset[6].bottom_postion = location[1]+rl_0_am.getHeight();
		
		rl_7_am.getLocationOnScreen(location);
		time_view_offset[7].top_position = location[1];
		time_view_offset[7].bottom_postion = location[1]+rl_0_am.getHeight();
		
		rl_8_am.getLocationOnScreen(location);
		time_view_offset[8].top_position = location[1];
		time_view_offset[8].bottom_postion = location[1]+rl_0_am.getHeight();
		
		rl_9_am.getLocationOnScreen(location);
		time_view_offset[9].top_position = location[1];
		time_view_offset[9].bottom_postion = location[1]+rl_0_am.getHeight();
		
		rl_10_am.getLocationOnScreen(location);
		time_view_offset[10].top_position = location[1];
		time_view_offset[10].bottom_postion = location[1]+rl_0_am.getHeight();
		
		rl_11_am.getLocationOnScreen(location);
		time_view_offset[11].top_position = location[1];
		time_view_offset[11].bottom_postion = location[1]+rl_0_am.getHeight();
		
		rl_12_pm.getLocationOnScreen(location);
		time_view_offset[12].top_position = location[1];
		time_view_offset[12].bottom_postion = location[1]+rl_0_am.getHeight();
		
		rl_1_pm.getLocationOnScreen(location);
		time_view_offset[13].top_position = location[1];
		time_view_offset[13].bottom_postion = location[1]+rl_1_am.getHeight();
		
		rl_2_pm.getLocationOnScreen(location);
		time_view_offset[14].top_position = location[1];
		time_view_offset[14].bottom_postion = location[1]+rl_2_am.getHeight();
		
		rl_3_pm.getLocationOnScreen(location);
		time_view_offset[15].top_position = location[1];
		time_view_offset[15].bottom_postion = location[1]+rl_0_am.getHeight();
		
		rl_4_pm.getLocationOnScreen(location);
		time_view_offset[16].top_position = location[1];
		time_view_offset[16].bottom_postion = location[1]+rl_0_am.getHeight();
		
		rl_5_pm.getLocationOnScreen(location);
		time_view_offset[17].top_position = location[1];
		time_view_offset[17].bottom_postion = location[1]+rl_0_am.getHeight();
		
		rl_6_pm.getLocationOnScreen(location);
		time_view_offset[18].top_position = location[1];
		time_view_offset[18].bottom_postion = location[1]+rl_0_am.getHeight();
		
		rl_7_pm.getLocationOnScreen(location);
		time_view_offset[19].top_position = location[1];
		time_view_offset[19].bottom_postion = location[1]+rl_0_am.getHeight();
		
		rl_8_pm.getLocationOnScreen(location);
		time_view_offset[20].top_position = location[1];
		time_view_offset[20].bottom_postion = location[1]+rl_0_am.getHeight();
		
		rl_9_pm.getLocationOnScreen(location);
		time_view_offset[21].top_position = location[1];
		time_view_offset[21].bottom_postion = location[1]+rl_0_am.getHeight();
		
		rl_10_pm.getLocationOnScreen(location);
		time_view_offset[22].top_position = location[1];
		time_view_offset[22].bottom_postion = location[1]+rl_0_am.getHeight();
		
		rl_11_pm.getLocationOnScreen(location);
		time_view_offset[23].top_position = location[1];
		time_view_offset[23].bottom_postion = location[1]+rl_0_am.getHeight();	
		
	}
	class Date_Pair{
		String eng_date, eng_month, eng_year;		
		String ism_date, ism_month, ism_year;
	}
	
	class DatePosition{
		public int left_position = 0;
		public int right_postion = 0;
	}
	class TimePosition{
		public int top_position = 0;
		public int bottom_postion = 0;
	}
	
	class EventsDetails{
		
		EventsCalenderModel event_data;
		private int position;
		int start_offset = 0;
		Date start_date;
		int end_offset = 0;
		int vertical_start_offset = 0;
		float vertical_start_min_offset ;
		int vertical_end_offset = 0;
		float vertical_end_min_offset ;
		int event_type = EVENT_WHOLE_WEEK;
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
			events_container.removeAllViews();
			event_container_by_hour.removeAllViews();
			linearViews.clear();
			getWeekDates(week_of_year,year);
			setContent(eng_ism_date);
			addEventViewsByWeek();
			addEventViewByHours();
			break;

		default:
			break;
		}
	}
}
