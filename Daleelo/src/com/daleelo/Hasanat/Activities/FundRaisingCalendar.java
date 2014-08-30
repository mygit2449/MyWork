package com.daleelo.Hasanat.Activities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.daleelo.R;
import com.daleelo.DashBoardEvents.Activities.CalendarEventDetailDesc;
import com.daleelo.DashBoardEvents.Activities.EventsCalendarActivity;
import com.daleelo.DashBoardEvents.Activities.EventsCalendarEventListActivity;
import com.daleelo.DashBoardEvents.Activities.EventsCalendarSettingsActivity;
import com.daleelo.DashBoardEvents.Activities.EventsDetailsByDayActivity;
import com.daleelo.DashBoardEvents.Activities.EventsDetailsByWeekActivity;
import com.daleelo.DashBoardEvents.Activities.EventsIslamicCalendarActivity;
import com.daleelo.DashBoardEvents.Activities.EventsCalendarActivity.EventsAdapter;
import com.daleelo.DashBoardEvents.Activities.EventsCalendarActivity.GridCellAdapter;
import com.daleelo.DashBoardEvents.Model.EventsCalenderModel;
import com.daleelo.DashBoardEvents.Parser.EventsCalendarParser;
import com.daleelo.Dialog.AlertDialogMsg;
import com.daleelo.Main.Activities.MainHomeScreen;
import com.daleelo.Utilities.CurrentLocationData;
import com.daleelo.Utilities.Urls;
import com.daleelo.helper.DateFormater;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;

public class FundRaisingCalendar extends Activity implements OnClickListener
{
	private final String tag = EventsCalenderModel.class.getSimpleName();
	private final static int GREEN = 0;
	private final static int GREY = 1;
	private final static int GREEN_GREY = 2;
	
	
	private ImageView prevMonth = null;
	private ImageView nextMonth = null;
	private GridView calendarView = null;
	private GridCellAdapter adapter= null;
	private Calendar _calendar = null,_calendarNow=null;
	private int monthis=0, yearis=0;
	private int current_month, current_year,dayis=0;		
	private ProgressDialog progressdialog;	
	public  ArrayList<EventsCalenderModel> marrEvent,marrEventDump;
	ArrayList<EventsCalenderModel> events_in_selected_month;
	private EventsAdapter  mDataAdapter;
	private ListView mEventList;
	private ArrayList<String> parserDates;
	private TextView greg_txt = null,heading_txt = null;
	private TextView islam_txt = null;
	private Button today = null;	
	private Button week  = null;
	private Button day   = null;
	private Button list  = null;
	private ImageButton imgb_settings;
	private int present_month;
	private boolean from_button = false;
	private LinearLayout buttons_layout;
	
	private Boolean[] selection ;
	private ArrayList<String> weekArray = new ArrayList<String>();
	private int screenWidth;
	private int gridWidth;
	
	
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState){	
			super.onCreate(savedInstanceState);
			this.requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.dashboard_events_calendar);
			initializeObjects();
			heading_txt.setText(""+"Fundraising Calendar");
			getParsedData();
			//inserting false values to array
						
			
			
		//	_calendarNow  = Calendar.getInstance();
			_calendar	  = Calendar.getInstance(Locale.getDefault());
			
			monthis 	  = _calendar.get(Calendar.MONTH);
			current_month = _calendar.get(Calendar.MONTH);
			present_month = _calendar.get(Calendar.MONTH) + 1;
			yearis 		  = _calendar.get(Calendar.YEAR);
			current_year  = _calendar.get(Calendar.YEAR);


			calendarView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View view,int arg2, long arg3) {					
					
					marrEventDump.clear();
					from_button=false;
					
					for(int i = 0; i < selection.length; i++){
						selection[i] = false;
					}
					
					selection[arg2] = true;					
					adapter.notifyDataSetChanged();
					
					String date_month_year = (String) view.getTag();		
					String[] tags = date_month_year.split("-");					
					String Sepatator = tags[3];
					
					monthis=Integer.parseInt(date_month_year.split("-")[1]);					
					dayis=Integer.parseInt(date_month_year.split("-")[0]);
					yearis=Integer.parseInt(date_month_year.split("-")[2]);
					
					Calendar event_start_date = Calendar.getInstance(),event_end_date = Calendar.getInstance();					
					Calendar selected_date = Calendar.getInstance();
					selected_date.set(Calendar.DAY_OF_MONTH, dayis);
					selected_date.set(Calendar.MONTH, monthis);
					selected_date.set(Calendar.YEAR, yearis);
					selected_date.set(Calendar.HOUR_OF_DAY, 0);
					selected_date.set(Calendar.MINUTE, 0);
					selected_date.set(Calendar.SECOND, 0);
					selected_date.set(Calendar.MILLISECOND, 0);
					
					if(Sepatator.equalsIgnoreCase("GREY_TRAILING") || Sepatator.equalsIgnoreCase("GREY_EXTRA")){
					
						setGridCellAdapterToDate(monthis,yearis);
												
					}else{
					
						for(EventsCalenderModel event: events_in_selected_month){
							event_start_date.setTimeInMillis(DateFormater.parseDate(event.getEventStartsOn(), "MM/dd/yyyy h:mm:ss a"));
							event_end_date.setTimeInMillis(DateFormater.parseDate(event.getEventEndsOn(), "MM/dd/yyyy h:mm:ss a"));
							
							event_start_date.set(Calendar.HOUR_OF_DAY, 0);
							event_start_date.set(Calendar.MINUTE, 0);
							event_start_date.set(Calendar.SECOND, 0);
							event_start_date.set(Calendar.MILLISECOND, 0);
							
							event_end_date.set(Calendar.HOUR_OF_DAY, 0);
							event_end_date.set(Calendar.MINUTE, 0);
							event_end_date.set(Calendar.SECOND, 0);
							event_end_date.set(Calendar.MILLISECOND, 0);
							
							if(selected_date.getTime().compareTo(event_start_date.getTime()) >= 0 && 
									selected_date.getTime().compareTo(event_end_date.getTime()) <= 0){
								marrEventDump.add(event);
							}
							
						}
						
						if(marrEventDump.size()>0){							
							
							mDataAdapter = null;					
							
							mDataAdapter =  new EventsAdapter(marrEventDump);
							
							mEventList.setAdapter(mDataAdapter);
							
							PopupWindow pWindow = new PopupWindow(FundRaisingCalendar.this);
							pWindow.setContentView(mEventList);						
							pWindow.setFocusable(true);
							pWindow.setWidth(WindowManager.LayoutParams.FILL_PARENT);
							pWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);						
							pWindow.showAtLocation(calendarView, Gravity.BOTTOM, 0, 70	);
						
						}

						
						
				 }
					
					
				}
			 
		});
			
			mEventList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View view,	int position, long arg3) {					
					startActivity(new Intent(FundRaisingCalendar.this,CalendarEventDetailDesc.class).putExtra("data", marrEventDump.get(position)));
				}
				
			});
			
	}

	
	
	private void initializeObjects() {
		marrEvent 		= new ArrayList<EventsCalenderModel>();
		marrEventDump   = new ArrayList<EventsCalenderModel>();
		//mEventList 	= (ListView)findViewById(R.id.events_listview);
		mEventList 		= new ListView(FundRaisingCalendar.this);
		parserDates		= new ArrayList<String>();
		greg_txt		= (TextView)findViewById(R.id.gregorian_txtView);
		islam_txt		= (TextView)findViewById(R.id.islamic_txtView);
		heading_txt	    = (TextView)findViewById(R.id.events_TXT_main_title);
		
		today 			= (Button)findViewById(R.id.today_btn);
		//month_btn 	   	= (Button)findViewById(R.id.month_btn);
		week  			= (Button)findViewById(R.id.week_btn);
		day   			= (Button)findViewById(R.id.day_btn);
		list  			= (Button)findViewById(R.id.list_btn);
		
		imgb_settings	= (ImageButton)findViewById(R.id.events_IMGBTN_settings);
		
		today.setOnClickListener(this);
		week.setOnClickListener(this);
		day.setOnClickListener(this);
		list.setOnClickListener(this);
		imgb_settings.setOnClickListener(this);
		
		prevMonth = (ImageView) this.findViewById(R.id.prevMonth);
		prevMonth.setOnClickListener(this);		

		nextMonth = (ImageView) this.findViewById(R.id.nextMonth);
		nextMonth.setOnClickListener(this);

		calendarView = (GridView) this.findViewById(R.id.calendar);
		Display display = getWindowManager().getDefaultDisplay();
        screenWidth = display.getWidth();
        gridWidth = (screenWidth/7)-2;
		
		buttons_layout = (LinearLayout)findViewById(R.id.calendar_buttons_layout);
		
		
		
		
		
	}


	private void getParsedData(){
//		progressdialog = ProgressDialog.show(EventsCalendarActivity.this, "", "please wait..."); 
		//String url = String.format(Urls.DASHBOARD_EVENTS_URL,"hyderabad","17.85","78.45","100"); 
		String url = String.format(Urls.DASHBOARD_EVENTS_URL,CurrentLocationData.CURRENT_CITY,
				CurrentLocationData.LATITUDE,CurrentLocationData.LONGITUDE,"50"); 
		EventsCalendarParser mEventsCalenderParser = new EventsCalendarParser(new EventsParserHandler(), url , marrEvent,true);
		mEventsCalenderParser.start(); 
		
	}

	private void setGridCellAdapterToDate(int month, int year){
		
		adapter = new GridCellAdapter(getApplicationContext(), R.layout.dashboard_events_calendar_row, month, year);				
		adapter.notifyDataSetChanged();
		calendarView.setAdapter(adapter);
	}


	@Override
	public void onClick(View v){
		
		
		
		if(v.getId()==R.id.today_btn){
			
			adapter = new GridCellAdapter(getApplicationContext(), R.layout.dashboard_events_calendar_row, current_month, current_year);
			adapter.notifyDataSetChanged();
			calendarView.setAdapter(adapter);
			
		}else if(v.getId()==R.id.week_btn){
			
			 SimpleDateFormat sdf;
	         Calendar cal;
	         Date date;
	         int week = 0;
	         int year = 0;
	         
	         int month = monthis+1;
	         String sample = month+"/"+dayis+"/"+yearis;	   
	         
	         sdf = new SimpleDateFormat("MM/dd/yyyy");
	         try{
					date = sdf.parse(sample);
					cal = Calendar.getInstance();
			        cal.setTime(date);
			        week = cal.get(Calendar.WEEK_OF_YEAR);	        	         
			        year = cal.get(Calendar.YEAR);
			        WeekDays(week,year);
			   }catch (Exception e){			
				e.printStackTrace();
			   }
	         
	       startActivity(new Intent(FundRaisingCalendar.this,EventsDetailsByWeekActivity.class).putExtra("selected_date", sample).putExtra("week", week).putExtra("year", year).putExtra("data", marrEvent).setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));  
	       


			
		} else if (v.getId() == R.id.day_btn) {
			 SimpleDateFormat sdf;
	         Calendar cal;
	         Date date;
	         int week = 0;
	         int year = 0;
	         
	         int month = monthis+1;
	         String selected_date = month+"/"+dayis+"/"+yearis;	         
	         
	         sdf = new SimpleDateFormat("MM/dd/yyyy");
	         try{
					date = sdf.parse(selected_date);
					cal = Calendar.getInstance();
			        cal.setTime(date);
			        week = cal.get(Calendar.WEEK_OF_YEAR);	        	         
			        year = cal.get(Calendar.YEAR);
			        WeekDays(week,year);
			   }catch (Exception e){			
				e.printStackTrace();
			   }

			startActivity(new Intent(FundRaisingCalendar.this,
					EventsDetailsByDayActivity.class).putExtra("data",
					marrEvent).putExtra("selected_date", selected_date).
					putExtra("week", week).putExtra("year", year).setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));

		}
		else if(v.getId() == R.id.events_IMGBTN_settings){
			
			startActivity(new Intent(FundRaisingCalendar.this, EventsCalendarSettingsActivity.class));
			
		}else if(v.getId()==R.id.list_btn){
			
			startActivity(new Intent(FundRaisingCalendar.this,EventsCalendarEventListActivity.class).putExtra("eventslist", marrEvent));
			
		}else if(v == prevMonth){
					
			from_button	=true;
			
					if (monthis <= 0){                                                    
						
							monthis = 11;
							yearis--;
					}else{
							monthis--;
							//present_month--;
					}
				
			setGridCellAdapterToDate(monthis, yearis);
		}else if(v == nextMonth){
					
			from_button	=true;
					
					if (monthis >= 11){
							monthis = 0;
							yearis++;
					}else{
							monthis++;
							//present_month++;
					}
				
			setGridCellAdapterToDate(monthis, yearis);
		}

	}

	
	private void WeekDays(int week,int year) {
		Calendar calendar = Calendar.getInstance(); 
		calendar.clear();
		calendar.set(Calendar.WEEK_OF_YEAR, week); 
		calendar.set(Calendar.YEAR, year);
		SimpleDateFormat formatter = new  SimpleDateFormat("M/d/yyyy");
		
		Date startDate = calendar.getTime();
		String startDateInStr = formatter.format(startDate); 
		Log.v("start date",startDateInStr);
		weekArray.clear();
		weekArray.add(startDateInStr);	
		for(int iCtr=1;iCtr<7;iCtr++){	
			formatter = new  SimpleDateFormat("M/d/yyyy");
			calendar.add(Calendar.DATE,1);  			 
			Date enddate = calendar.getTime();  
			Log.v("end date",""+enddate);
			Log.v("week days",""+formatter.format(enddate));  
			weekArray.add(formatter.format(enddate));  
			
		}
		
	   
		
	}


	// Inner Class
	public class GridCellAdapter extends BaseAdapter 
		{
			
			private final Context _context;
			private final List<String> list;
			private static final int DAY_OFFSET = 1;
			private final String[] weekdays = new String[]{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
			private final String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
			private final int[] daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
			private final int month, year;
			private int daysInMonth, prevMonthDays;
			private int currentDayOfMonth;
			private int currentWeekDay;			
			private TextView num_events_per_day;
			private String islam_month_previous ;
			private final HashMap eventsPerMonthMap;

			private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy");

			// Days in Current Month
			public GridCellAdapter(Context context, int textViewResourceId, int month, int year){
				super();
				this._context = context;
				this.list = new ArrayList<String>();
				this.month = month;
				this.year = year;
				
				
				Calendar calendar = Calendar.getInstance();
				setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
				setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
				
				printMonth(month, year);
				selection = new Boolean[list.size()];
				for(int i = 0; i<selection.length; i++){
					selection[i] = false; 
				}
				eventsPerMonthMap = findNumberOfEventsPerMonth(year, month, marrEvent);
					
			}
			private String getMonthAsString(int i){
					return months[i];
			}
			private int getNumberOfDaysOfMonth(int i){
					return daysOfMonth[i];
			}

			public String getItem(int position){
					return list.get(position);
			}
			@Override
			public int getCount(){
					return list.size();
			}
			
			private void printMonth(int mm, int yy){
				Log.d(tag, "==> printMonth: mm: " + mm + " " + "yy: " + yy);
				// The number of days to leave blank at
				// the start of this month.
				int trailingSpaces = 0;
				int leadSpaces = 0;
				int daysInPrevMonth = 0;
				int prevMonth = 0;
				int prevYear = 0;
				int nextMonth = 0;
				int nextYear = 0;

				int currentMonth = mm;
				String currentMonthName = getMonthAsString(currentMonth);
				
				greg_txt.setText(currentMonthName+"-"+yy);
				
				String islamDate = new EventsIslamicCalendarActivity().getIslamicDate(1, mm+1,yy);
				islam_month_previous = islamDate.split("/")[1];
				//islam_txt.setText(""+islamDate.split("/")[1]);                                 
				
				
				daysInMonth = getNumberOfDaysOfMonth(currentMonth);
				
				// Gregorian Calendar : MINUS 1, set to FIRST OF MONTH
				GregorianCalendar cal = new GregorianCalendar(yy, currentMonth,1);
				Log.d(tag, "Gregorian Calendar:= " + cal.getTime().toString());

				if (currentMonth == 11)
					{
						prevMonth = currentMonth - 1;
						daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
						nextMonth = 0;
						prevYear = yy;
						nextYear = yy + 1;
						Log.d(tag, "*->PrevYear: " + prevYear + " PrevMonth:" + prevMonth + " NextMonth: " + nextMonth + " NextYear: " + nextYear);
					}
				else if (currentMonth == 0)
					{
						prevMonth = 11;
						prevYear = yy - 1;
						nextYear = yy;
						daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
						nextMonth = 1;
						Log.d(tag, "**--> PrevYear: " + prevYear + " PrevMonth:" + prevMonth + " NextMonth: " + nextMonth + " NextYear: " + nextYear);
					}
				else
					{
						prevMonth = currentMonth - 1;
						nextMonth = currentMonth + 1;
						nextYear = yy;
						prevYear = yy;
						daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
						Log.d(tag, "***---> PrevYear: " + prevYear + " PrevMonth:" + prevMonth + " NextMonth: " + nextMonth + " NextYear: " + nextYear);
					}

				// Compute how much to leave before before the first day of the
				// month.
				// getDay() returns 0 for Sunday.
				int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
				trailingSpaces = currentWeekDay;

				Log.d(tag, "No. Trailing space to Add: " + trailingSpaces);
				Log.d(tag, "No. of Days in Previous Month: " + daysInPrevMonth);

				if (cal.isLeapYear(cal.get(Calendar.YEAR)) && mm == 1)
					{
						++daysInMonth;
					}

				// Trailing Month days
				for (int i = 0; i < trailingSpaces; i++)
					{
						Log.d(tag, "PREV MONTH:= " + prevMonth + " => " + getMonthAsString(prevMonth) + " " + String.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET) + i));
						list.add(String.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET) + i) + "-GREY_TRAILING" + "-" + prevMonth + "-" + prevYear);
					}

				// Current Month Days
				for (int i = 1; i <= daysInMonth; i++)
					{
						Log.d(currentMonthName, String.valueOf(i) + " " + currentMonth + " " + yy);
						if (i == getCurrentDayOfMonth())
							{
								list.add(String.valueOf(i) + "-BLUE" + "-" + currentMonth + "-" + yy);
							}
						else
							{
								list.add(String.valueOf(i) + "-WHITE" + "-" + currentMonth + "-" + yy);
							}
					}

				// Leading Month days
				for (int i = 0; i < list.size() % 7; i++)
					{
						Log.d(tag, "NEXT MONTH:= " + getMonthAsString(nextMonth));
						list.add(String.valueOf(i + 1) + "-GREY_EXTRA" + "-" + nextMonth + "-" + nextYear);
					}
				
			}
			
			private HashMap findNumberOfEventsPerMonth(int year, int month, ArrayList<EventsCalenderModel> events)
			{
				HashMap map = new HashMap<String, Integer>();
				HashMap eventsMap = new HashMap<String,String>();
				
				Calendar event_start_date = Calendar.getInstance();
				Calendar event_end_date = Calendar.getInstance();
				Calendar grid_Date = Calendar.getInstance();
				
				events_in_selected_month = new ArrayList<EventsCalenderModel>();
				for(int i= 1; i<=getNumberOfDaysOfMonth(month); i++){
					
					grid_Date.set(Calendar.DAY_OF_MONTH, i);
					grid_Date.set(Calendar.MONTH, month);
					grid_Date.set(Calendar.YEAR, year);
					grid_Date.set(Calendar.HOUR_OF_DAY, 0);
					grid_Date.set(Calendar.MINUTE, 0);
					grid_Date.set(Calendar.SECOND, 0);
					grid_Date.set(Calendar.MILLISECOND, 0);
					
					
					for(EventsCalenderModel event: events){
						event_start_date.setTimeInMillis(DateFormater.parseDate(event.getEventStartsOn(), "MM/dd/yyyy h:mm:ss a"));
						event_end_date.setTimeInMillis(DateFormater.parseDate(event.getEventEndsOn(), "MM/dd/yyyy h:mm:ss a"));
						event_start_date.set(Calendar.HOUR_OF_DAY, 0);
						event_start_date.set(Calendar.MINUTE, 0);
						event_start_date.set(Calendar.SECOND, 0);
						event_start_date.set(Calendar.MILLISECOND, 0);
						
						event_end_date.set(Calendar.HOUR_OF_DAY, 0);
						event_end_date.set(Calendar.MINUTE, 0);
						event_end_date.set(Calendar.SECOND, 0);
						event_end_date.set(Calendar.MILLISECOND, 0);			
						
						String grid_date =event_start_date.getTime().toString()+" "+grid_Date.getTime().toString()+" "+event_end_date.getTime().toString();
							
						if(grid_Date.getTime().compareTo(event_start_date.getTime()) >= 0 && 
								grid_Date.getTime().compareTo(event_end_date.getTime()) <= 0)

						{
								if(!eventsMap.containsKey(event.getEventId())){
									events_in_selected_month.add(event);
									eventsMap.put(event.getEventId(),"true");
								}
								
								String key = i+"-"+month+"-"+year;
								if (map.containsKey(key))
								{
									
									Integer val = (Integer) map.get(key);
									if(Boolean.parseBoolean(event.getFundrising())){
										
										if(val == GREEN){
											map.put(key, GREEN);									
										}								
									}
								}
								else
								{
									if(Boolean.parseBoolean(event.getFundrising())){
										map.put(key, GREEN);								
									}
								}								
							}
						}
					
				}
				
						
				return map;
			}
			
			
			@Override
			public long getItemId(int position){
				
					return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent){				
				
				
					View row;
					LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					row = inflater.inflate(R.layout.dashboard_events_calendar_row, parent, false);
					LayoutParams params = (LayoutParams) row.getLayoutParams();
			        params.width = gridWidth;
					// Get a reference to the Day gridcell
			 TextView	gridcell = (TextView) row.findViewById(R.id.calendar_day_gridcell);
				
			 TextView	gridcell1 = (TextView) row.findViewById(R.id.calendar_day_gridcell1);
					
			 ImageView dot_image = (ImageView)row.findViewById(R.id.event_dot_img);
		

					String[] day_color = list.get(position).split("-");
					String theday   = day_color[0];
					String themonth = day_color[2];
					String theyear  = day_color[3];
					
					// Set the Day GridCell
					gridcell.setText(theday);
					
					row.setTag(theday+"-"+themonth+"-"+theyear+"-"+day_color[1]);
					Log.e("Setting GridCell " ,""+ theday + "-" + themonth + "-" + theyear);
					
//					 
					Double month1= Double.parseDouble(themonth);
					
					String islamDate = new EventsIslamicCalendarActivity().getIslamicDate(Double.parseDouble(theday), month1+1,Double.parseDouble(theyear));
					
					
					
					gridcell1.setText(""+new Double(islamDate.split("/")[0]).intValue());
					
					if(new Double(islamDate.split("/")[0]).intValue()==1){
						islam_txt.setText(islam_month_previous+" - "+islamDate.split("/")[1]+islamDate.split("/")[2]);
					}
					
					
					
					if(dayis ==new Double(theday).intValue() && monthis == new Double(themonth).intValue() && yearis == new Double(theyear).intValue()){
						row.setBackgroundResource(R.drawable.selected_date);
						if(!from_button)
						selection[position] = true;
					}
								
					if(selection[position]){
						row.setBackgroundResource(R.drawable.selected_date);
					}else{
						row.setBackgroundResource(R.drawable.date);
					}
					
					if (day_color[1].equals("GREY_TRAILING") || day_color[1].equals("GREY_EXTRA"))
					{
						gridcell.setTextColor(Color.LTGRAY);
						gridcell1.setTextColor(Color.LTGRAY);
						row.setBackgroundResource(R.drawable.extra_date);
					}
					if (day_color[1].equals("WHITE"))
					{
						gridcell.setTextColor(Color.WHITE);
						
					}
					if (day_color[1].equals("BLUE"))
					{
						if(month == current_month && year == current_year){
							gridcell.setTextColor(getResources().getColor(R.color.white));
							gridcell1.setTextColor(Color.WHITE);
							row.setBackgroundResource(R.drawable.today_date);
						}							

					}
					String key = theday+"-"+themonth+"-"+theyear;
					if(eventsPerMonthMap.containsKey(theday+"-"+themonth+"-"+theyear)){
						int val = (Integer) eventsPerMonthMap.get(theday+"-"+themonth+"-"+theyear);
						
						if(val == GREY){
							dot_image.setBackgroundResource(R.drawable.grey_dot);
						}else if(val == GREEN){
							dot_image.setBackgroundResource(R.drawable.green_dot);
						}else{
							dot_image.setBackgroundResource(R.drawable.double_dot);
						}							
					}else{
						dot_image.setBackgroundResource(R.drawable.transparent_dot);
					}
	
					return row;
			}


			public int getCurrentDayOfMonth(){
					return currentDayOfMonth;
			}

			private void setCurrentDayOfMonth(int currentDayOfMonth){
					this.currentDayOfMonth = currentDayOfMonth;
			}
			public void setCurrentWeekDay(int currentWeekDay){
					this.currentWeekDay = currentWeekDay;
			}
			public int getCurrentWeekDay(){
					return currentWeekDay; 
			}
	}
	
	
	
	class EventsParserHandler extends Handler
	{
		public void handleMessage(android.os.Message msg)
		{
//			progressdialog.dismiss();			
			
			
			String errorMsg=msg.getData().getString("HttpError");
			if(errorMsg.length()>0)
			{					
				alertDialogWithMsg(errorMsg);				
				
			}else{
				  
					if(marrEvent != null && marrEvent.size()>0)
					{						
						Log.v("size",""+marrEvent.size());
						for(int iCtr=0;iCtr<marrEvent.size();iCtr++){							
							parserDates.add(marrEvent.get(iCtr).getEventStartsOn());
						}						
	
					}
			
				
			}
			Log.v("monthe is vvvvvvvvvvvvvv",""+monthis);
			adapter = new GridCellAdapter(getApplicationContext(), R.layout.dashboard_events_calendar_row, monthis, yearis);
			adapter.notifyDataSetChanged();
			calendarView.setAdapter(adapter);
			

				
		}
}	

	public void alertDialogWithMsg(String msg)
	{			
		new AlertDialogMsg(FundRaisingCalendar.this,msg).setPositiveButton("ok", new android.content.DialogInterface.OnClickListener(){
	
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
							
			}
			
		   }).create().show();		
	}
	
	
	public class EventsAdapter extends BaseAdapter
	{
    	
    	
    	ArrayList<EventsCalenderModel> mDataFeeds;
    	
    	public EventsAdapter(ArrayList<EventsCalenderModel> mDataFeeds){
        	this.mDataFeeds = mDataFeeds;          
        }		 
		@Override
		public int getCount(){				
				return mDataFeeds.size();
		}

		@Override
		public Object getItem(int position){		
			return position;
		}
		
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		
		@Override
		public View getView(final int position, View v, ViewGroup parent){


			 if(v==null){
				LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				
					v = inflater.inflate(R.layout.dashboard_events_list_row, null);			

			 }				 
					 
				 TextView title		 = (TextView)v.findViewById(R.id.cal_popup_row_txt_title);
				 TextView time    = (TextView)v.findViewById(R.id.cal_popup_row_txt_time);
				 ImageView img = (ImageView)v.findViewById(R.id.cal_popup_row_img);				 
				
				 
				 title.setText(mDataFeeds.get(position).getEventTitle());
				 time.setText(mDataFeeds.get(position).getEventStartsOn());
				 
				 if(Boolean.parseBoolean(mDataFeeds.get(position).getFundrising())){
					 img.setBackgroundResource(R.drawable.green_dot);
				 }else{
					 img.setBackgroundResource(R.drawable.grey_dot);
				 }
				
					 
			return v;
		}
	}
	
//	@Override
//	public void onBackPressed() {
//		// TODO Auto-generated method stub
//		super.onBackPressed();
//		startActivity(new Intent(this,MainHomeScreen.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//	}
  
}



