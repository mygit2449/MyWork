package com.BabyTracker.BabyGrowth;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.BabyTracker.R;
import com.BabyTracker.Helper.BabyTrackerDataBaseHelper;
import com.BabyTracker.dialog.BabyTrackerAlert;

public class GrowthdetailsListActivity extends Activity {

	public static String LOG_TAG = GrowthdetailsListActivity.class.getSimpleName();
	
	private BabyTrackerDataBaseHelper mBabyTrackerDataBaseHelper = null;

	private ListView mGrowthlistview;

	private TextView mBabygrowth_details_title;
	
	private Cursor mGrowthCursor = null;
	
	private Intent receiverIntent = null;
	
	private int growth_id;
	
	private GrowthListCursorAdapter mGrowthListCursorAdapter = null;
	
	private final String[] MONTH_NAME = {"Jan", "Feb","Mar","Apr","May","June","July","Aug","Sep","Oct","Nov","Dec"};

	private String mTitle = "BabyTracker";
	private String mMessage = "No Data Available";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.babygrowth_listview);

		receiverIntent = getIntent();
		
		if (receiverIntent.getAction().equalsIgnoreCase("forGrowthList")) {
			
			growth_id = receiverIntent.getExtras().getInt("growth_id");
		}
		
		
		mGrowthlistview = (ListView) findViewById(R.id.babygrowth_listview);
		mBabyTrackerDataBaseHelper = new BabyTrackerDataBaseHelper(this);
		mBabyTrackerDataBaseHelper.openDataBase();
		
		
		mBabygrowth_details_title = (TextView)findViewById(R.id.simple_top_bar_Txt_title);
		mBabygrowth_details_title.setText("Baby Growth");

		String query = String.format(BabyTrackerDataBaseHelper.SELECT_QUERY, BabyTrackerDataBaseHelper.GROWTH_TABLE+" where "+BabyTrackerDataBaseHelper.GROWTH_BABY_ID+" = "+ growth_id+" order by "+BabyTrackerDataBaseHelper.GROWTH_DATE_OF_UPDATE+" DESC");
		Log.i(LOG_TAG, "growth query "+query);
		mGrowthCursor = mBabyTrackerDataBaseHelper.select(query);
		
		if (mGrowthCursor.getCount() == 0) 
			alertMessage(mTitle, mMessage);
		
		
		mGrowthListCursorAdapter = new GrowthListCursorAdapter(this, mGrowthCursor);
		mGrowthlistview.setAdapter(mGrowthListCursorAdapter);

	}
	
	public void alertMessage(String title, String message){
		
		new BabyTrackerAlert(this, title, message).setPositiveButton("Ok", new android.content.DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				finish();
			}
		}).setIcon(android.R.drawable.ic_dialog_info)
		.create().show();
	}
	
	public class GrowthListCursorAdapter extends CursorAdapter{

		@SuppressWarnings("unused")
		private Cursor mCursor;
		@SuppressWarnings("unused")
		private Context mContext;
		private final LayoutInflater mInflater;

		SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date babydate = null;
		
		public GrowthListCursorAdapter(Context context, Cursor c) {
			super(context,c);
			// TODO Auto-generated constructor stub
			mInflater=LayoutInflater.from(context);
		    mContext=context;
		    mCursor = c;
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			// TODO Auto-generated method stub
			TextView mBabyDate_Txt = (TextView) view.findViewById(R.id.babygrowth_list_row_TXT_date);
			TextView mBabymonthAndYear_Txt = (TextView) view.findViewById(R.id.babygrowth_list_row_TXT_monthandyear);
			TextView mBabyweight_Txt = (TextView) view.findViewById(R.id.babygrowth_list_row_TXT_babyweight);
			TextView mBabyheight_Txt = (TextView) view.findViewById(R.id.babygrowth_list_row_TXT_babyheight);
			TextView mBabyCircumference = (TextView)view.findViewById(R.id.babygrowth_list_row_TXT_babycircumference);
			
			mBabyweight_Txt.setText(cursor.getFloat(cursor.getColumnIndex(BabyTrackerDataBaseHelper.GROWTH_BABY_WEIGHT))+" Kgs");
			mBabyheight_Txt.setText(cursor.getFloat(cursor.getColumnIndex(BabyTrackerDataBaseHelper.GROWTH_BABY_HEIGHT))+" Cms");
			mBabyCircumference.setText(cursor.getFloat(cursor.getColumnIndex(BabyTrackerDataBaseHelper.GROWTH_BABY_CIRCUMFERENCE))+" Cms");
			
			String mBabyDate_Str = cursor.getString(cursor.getColumnIndex(BabyTrackerDataBaseHelper.GROWTH_DATE_OF_UPDATE));

			try {
				babydate = mDateFormat.parse(mBabyDate_Str);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Calendar cal = Calendar.getInstance();
			cal.setTime(babydate);
			String date = "";
			int Day = cal.get(Calendar.DATE);
			int month = cal.get(Calendar.MONTH);
			int year = cal.get(Calendar.YEAR);

			if (Day > 9)
				date = new Integer(Day).toString();
			else
				date = "0" + new Integer(Day).toString();
			
			mBabyDate_Txt.setText(""+date);
			mBabymonthAndYear_Txt.setText(""+MONTH_NAME[month] + "-" + year);
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			// TODO Auto-generated method stub
			final View view=mInflater.inflate(R.layout.babygrowth_list_row,parent,false); 
	        return view;

		} 
		
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		mBabyTrackerDataBaseHelper.closeDataBase();
		mGrowthCursor.close();
	}
	
}
