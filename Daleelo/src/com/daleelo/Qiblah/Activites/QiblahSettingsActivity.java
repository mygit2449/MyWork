
package com.daleelo.Qiblah.Activites;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.daleelo.R;
import com.daleelo.DataBase.DatabaseHelper;
import com.daleelo.QiblahReceiver.PrayerTimesReceiver;
import com.daleelo.Utilities.Urls;
import com.daleelo.Utilities.Utils;
import com.daleelo.helper.DateFormater;

public class QiblahSettingsActivity extends Activity implements OnClickListener, OnItemSelectedListener, OnCompletionListener{
	
	private PopupWindow mWindow;
	
	private TextView mDialogText;
	
	private ToggleButton mTBTN_fajr, mTBTN_dhuhr, mTBTN_asr,mTBTN_maghrib, mTBTN_isha, mTBTN_shuruq;
	
	protected int mAnimStyle;

	private MediaPlayer player;
	
	private Calendar  calender;
	
	private ArrayList<Integer> cancelIdsforFajr, cancelIdsforShuruq, cancelIdsforDhuhr, cancelIdsforAsr,
							   cancelIdsforMaghrib, cancelIdsforIsha;
	
	private boolean playstatus = false;
	
	private ArrayList<String> prayerTimesforFajr, prayerTimesforShuruq, prayerTimesforDhuhr, prayerTimesforAsr, 
							  prayerTimesforMaghrib, prayerTimesforIsha;
	
	private DatabaseHelper mDatabaseHelper;
	
	private Spinner mSpinnerCalcmethod;
	private Spinner mSpinnerAdhaan;
	private Spinner mSpinnerPrayerreminder;
	
	private SharedPreferences prayTimeStatus;
	
	private SeekBar mSeekbarAlarmvolume;
	
	private Button  mBTNDone, mBTNPlay, mBTNStop;
	private AudioManager audioManager;
	
	private ArrayAdapter<String> adhaanAdapter, prayerReminderAdapter, calcMethodAdapter;
										
	private String[]  calcMethod= {"Muslim World League,Europe","Egyptian General Authority of Survey","UIS,Karachi","Uhm-AL-Qura," +
								   "Makkah","ISNA, North America"};
	
	private String[]  adhaan = {"Default","Abdul Basit","Abdu Majid Asrihi","Adhan Alaqsa","Adhan Makkah"};
	
	private String[]  prayerRemaider = {"0 minutes","5 minutes","10 minutes","15 minute","20 minutes"};
	
	private int 
					local_fajrStatus = 0,
					local_shuruqStatus = 0,
					local_dhuhrStatus = 0,
					local_asrStatus = 0,				
					local_maghribStatus = 0, 
					local_ishaStatus = 0,
					local_ringtoneStatus = 0,
					local_reminderStatus = 0,
					local_calmethodtype,
					
					calmethodtype, 
					mCheck_fajrStatus,
					mCheck_shuruqStatus,
					mCheck_dhuhrStatus,
					mCheck_asrStatus,				
					mCheck_maghribStatus, 
					mCheck_ishaStatus, 
	
					mCheckAudio_SpinnerStatus, 
					mCheckReminder_SpinnerStatus,
					mReminderTimes_size, mAlarmTimes_size,
					selected_adhaan, alarm_size , reminder_size = 0, alarmfor = 0;
	
	private String selected_audiofile = "",  reminderTime_Str = "";
	private SharedPreferences mQiblahpreferences;
	private SharedPreferences.Editor editor;


	public  void onCreate(Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qiblah_prayer_settings);
		initializeUI();

	}

	/**
	 * Initializing User Interface...
	 */
	private void initializeUI() 
	{
	
		try 
		{
			Utils.ALARM_CONTEXT = QiblahSettingsActivity.this;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try 
		{
			mDatabaseHelper = new DatabaseHelper(this);
			mDatabaseHelper.openDataBase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		mSpinnerCalcmethod      =  (Spinner)findViewById(R.id.qiblah_settings_spinner_calcmethod);
		mSpinnerAdhaan          =  (Spinner)findViewById(R.id.qiblah_settings_spinner_adhaan);
		mSpinnerPrayerreminder	=  (Spinner)findViewById(R.id.qiblah_settings_spinner_prayer_reminder);
		
		mTBTN_fajr    =  (ToggleButton)findViewById(R.id.qiblah_settings_BTN_fajr_toggle);
		mTBTN_shuruq  =  (ToggleButton)findViewById(R.id.qiblah_settings_BTN_shuruq_toggle);
		mTBTN_dhuhr   =  (ToggleButton)findViewById(R.id.qiblah_settings_BTN_dhuhr_toggle);
		mTBTN_asr     =  (ToggleButton)findViewById(R.id.qiblah_settings_BTN_asr_toggle);
		mTBTN_maghrib =  (ToggleButton)findViewById(R.id.qiblah_settings_BTN_maghrib_toggle);
		mTBTN_isha    =  (ToggleButton)findViewById(R.id.qiblah_settings_BTN_isha_toggle);
		
		mDialogText = (TextView)findViewById(R.id.qiblah_settings_TXT_adhaan_text_title);
		
		mBTNDone = (Button)findViewById(R.id.qiblah_settings_done_button);
		mBTNPlay = (Button)findViewById(R.id.qiblah_settings_BTN_adhaan_play);
		mBTNStop = (Button)findViewById(R.id.qiblah_settings_BTN_adhaan_stop);

		
		mQiblahpreferences = getSharedPreferences("qiblahstatus", MODE_PRIVATE);
		editor = mQiblahpreferences.edit();
		
		prayTimeStatus = getSharedPreferences("praytimestatus", MODE_PRIVATE);
		local_calmethodtype = calmethodtype = prayTimeStatus.getInt("prayermethod", 5);
		
		local_fajrStatus    =  mCheck_fajrStatus    =  mQiblahpreferences.getInt("fajr", 0);
		local_shuruqStatus  =  mCheck_shuruqStatus  =  mQiblahpreferences.getInt("shuruq", 0);
		local_dhuhrStatus   =  mCheck_dhuhrStatus   =  mQiblahpreferences.getInt("dhuhr", 0);
		local_asrStatus     =  mCheck_asrStatus     =  mQiblahpreferences.getInt("asr", 0);
		local_maghribStatus =  mCheck_maghribStatus =  mQiblahpreferences.getInt("maghrib", 0);
		local_ishaStatus    =  mCheck_ishaStatus    =  mQiblahpreferences.getInt("isha", 0);
		
		
		if (mCheck_fajrStatus == 1) 
			mTBTN_fajr.setChecked(true);
		
		if (mCheck_shuruqStatus == 1) 
			mTBTN_shuruq.setChecked(true);

		if (mCheck_dhuhrStatus == 1) 
			mTBTN_dhuhr.setChecked(true);

		if (mCheck_asrStatus == 1) 
			mTBTN_asr.setChecked(true);
		
		if (mCheck_maghribStatus == 1) 
			mTBTN_maghrib.setChecked(true);
		
		if (mCheck_ishaStatus == 1) 
			mTBTN_isha.setChecked(true);
		
		local_ringtoneStatus = mCheckAudio_SpinnerStatus = mQiblahpreferences.getInt("audiofile", 0);
		local_reminderStatus = mCheckReminder_SpinnerStatus = mQiblahpreferences.getInt("reminder_status", 0);
		
		mAlarmTimes_size = mQiblahpreferences.getInt("mAlarmTimes_size", 0);
		mReminderTimes_size = mQiblahpreferences.getInt("mReminderTimes_size", 0);
		
		calender = Calendar.getInstance();
		
		cancelIdsforFajr    =  new ArrayList<Integer>();
		cancelIdsforShuruq  =  new ArrayList<Integer>();
		cancelIdsforDhuhr   =  new ArrayList<Integer>();
		cancelIdsforAsr     =  new ArrayList<Integer>();
		cancelIdsforMaghrib =  new ArrayList<Integer>();
		cancelIdsforIsha    =  new ArrayList<Integer>();

		calcMethodAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, calcMethod);
		calcMethodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinnerCalcmethod.setAdapter(calcMethodAdapter);
		mSpinnerCalcmethod.setSelection(calmethodtype-1);
		
		adhaanAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, adhaan);
		adhaanAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinnerAdhaan.setAdapter(adhaanAdapter);		
		mSpinnerAdhaan.setSelection(mCheckAudio_SpinnerStatus);
		
		prayerReminderAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, prayerRemaider);
		prayerReminderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinnerPrayerreminder.setAdapter(prayerReminderAdapter);		
		mSpinnerPrayerreminder.setSelection(mCheckReminder_SpinnerStatus);

		valumeControls(); // method for controlling volume 

		player = new MediaPlayer();
		player.setOnCompletionListener(this);
		
		mSpinnerCalcmethod.setOnItemSelectedListener(this);		
		mSpinnerAdhaan.setOnItemSelectedListener(this);
		mSpinnerPrayerreminder.setOnItemSelectedListener(this);
	 
		mBTNPlay.setOnClickListener(this);
		mBTNStop.setOnClickListener(this);
		mBTNDone.setOnClickListener(this);
		
		mTBTN_fajr.setOnClickListener(this);
		mTBTN_shuruq.setOnClickListener(this);
		mTBTN_dhuhr.setOnClickListener(this);
		mTBTN_asr.setOnClickListener(this);
		mTBTN_maghrib.setOnClickListener(this);
		mTBTN_isha.setOnClickListener(this);

	}

	/**
	 * controlling volume by seek bar.
	 */
	 private void valumeControls()
	 {
        try
        {
        	mSeekbarAlarmvolume = (SeekBar)findViewById(R.id.qiblah_settings_seekbar_alarmvolume);
            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            mSeekbarAlarmvolume.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            mSeekbarAlarmvolume.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));   

            mSeekbarAlarmvolume.setOnSeekBarChangeListener(new OnSeekBarChangeListener() 
            {
                @Override
                public void onStopTrackingTouch(SeekBar arg0) 
                {
                }

                @Override
                public void onStartTrackingTouch(SeekBar arg0) 
                {
                }

                @Override
                public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) 
                {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,progress, 0);
                    Log.v(getClass().getSimpleName(), " seek bar value "+progress);
                }
            });
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        
	 }
	 
	 
	 public void onItemSelected(AdapterView<?> arg0, View v, int arg2, long arg3) {
		
		switch (arg0.getId()) 
		{
		
			case R.id.qiblah_settings_spinner_calcmethod:
				
				local_calmethodtype = arg2 + 1;
		             
				break;
				
			case R.id.qiblah_settings_spinner_adhaan:
				
				selected_adhaan = mSpinnerAdhaan.getSelectedItemPosition();
			
				local_ringtoneStatus = mSpinnerAdhaan.getSelectedItemPosition();	
					
				if (selected_adhaan == 0){ 
					selected_audiofile = "android.resource://" + this.getPackageName() + "/" + R.raw.default2;
				}	
				if (selected_adhaan == 1) {
					selected_audiofile = "android.resource://" + this.getPackageName() + "/" + R.raw.abdul_basit2;
				}
				if (selected_adhaan == 2) {
					selected_audiofile = "android.resource://" + this.getPackageName() + "/" + R.raw.abdul_majid_asrihi2;
				}
				if (selected_adhaan == 3) {
					selected_audiofile = "android.resource://" + this.getPackageName() + "/" + R.raw.adhan_alaqsa2;
				}
				if (selected_adhaan == 4) {
					selected_audiofile = "android.resource://" + this.getPackageName() + "/" + R.raw.adhan_makkah2;
				}
				
				break;
				
			case R.id.qiblah_settings_spinner_prayer_reminder:
				
				local_reminderStatus = mSpinnerPrayerreminder.getSelectedItemPosition();
				reminderTime_Str = prayerRemaider[mSpinnerPrayerreminder.getSelectedItemPosition()];
	
				break;
		}
	}
	 
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

	public void show(View anchor, View rootview) 
	{
		
	 int[] location   = new int[2];
	 WindowManager mWindowManager = (WindowManager) QiblahSettingsActivity.this.getSystemService(WINDOW_SERVICE);
	 anchor.getLocationOnScreen(location);
	 
	 Rect  anchorRect  = new Rect(location[0], location[1], location[0]+ anchor.getWidth(), location[1]  + anchor.getHeight());
	 rootview.measure(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
     int rootWidth   = rootview.getWidth();
	 int rootHeight   = rootview.getHeight();

	 int screenWidth  = mWindowManager.getDefaultDisplay().getWidth();
	 
	 int xPos    = (screenWidth - rootWidth)/2;
	 int yPos    = anchorRect.top+ rootHeight;

	 mWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, xPos, yPos); 
	 mWindow.getAnimationStyle();
	 
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId())
		{
		
			case R.id.qiblah_settings_BTN_adhaan_play:
				
				if (!playstatus)
				{
					
					mBTNPlay.setBackgroundResource(R.drawable.stop);
					playstatus = true;
					
					if (selected_adhaan == 0){ 
						playSample(R.raw.default1);
					}	
					if (selected_adhaan == 1) {
						playSample(R.raw.abdul_basit1);
					}
					if (selected_adhaan == 2) {
						playSample(R.raw.abdul_majid_asrihi1);
					}
					if (selected_adhaan == 3) {
						playSample(R.raw.adhan_alaqsa1);
					}
					if (selected_adhaan == 4) {
						playSample(R.raw.adhan_makkah1);
					}
					
				}else if (playstatus) {
	
					mBTNPlay.setBackgroundResource(R.drawable.play);
					playstatus = false;
					player.stop();
				}
				
				break;
	
			case R.id.qiblah_settings_BTN_fajr_toggle:
				
				if (mTBTN_fajr.isChecked())
				{
					
					Log.v(getClass().getSimpleName(), "fajr click ON");
					local_fajrStatus = 1;
	
				}else{
					Log.v(getClass().getSimpleName(), "fajr click OFF");
					local_fajrStatus = 0;
				
				}
				
				
				break;
			case R.id.qiblah_settings_BTN_shuruq_toggle:
				
				if (mTBTN_shuruq.isChecked())
				{
					
					Log.v(getClass().getSimpleName(), "shuruq click ON");
					local_shuruqStatus = 1;
	
				}else{
					
					Log.v(getClass().getSimpleName(), "shuruq click OFF ");
					local_shuruqStatus = 0;
				}
	
				break;
			case R.id.qiblah_settings_BTN_dhuhr_toggle:
				
				if (mTBTN_dhuhr.isChecked()) 
				{
					
					Log.v(getClass().getSimpleName(), "dhuhr click ON");
					local_dhuhrStatus = 1;
				
				}else{
					
					local_dhuhrStatus = 0;
					Log.v(getClass().getSimpleName(), "dhuhr click OFF ");
	 				
				}
	
				break;
			case R.id.qiblah_settings_BTN_asr_toggle:
				
				if (mTBTN_asr.isChecked())
				{
					
					local_asrStatus = 1;
					Log.v(getClass().getSimpleName(), "asr click ON");
					
				}else{
					
					local_asrStatus = 0;
					Log.v(getClass().getSimpleName(), "asr click OFF ");
					
				}
	
				break;
			case R.id.qiblah_settings_BTN_maghrib_toggle:
				
				if (mTBTN_maghrib.isChecked())
				{
					
					local_maghribStatus = 1;
					Log.v(getClass().getSimpleName(), "maghrib click ON");
					
				}else{
					
					local_maghribStatus = 0;
					Log.v(getClass().getSimpleName(), "maghrib click OFF ");
				}
	
				break;
			case R.id.qiblah_settings_BTN_isha_toggle:
				
				if (mTBTN_isha.isChecked())
				{
					
					local_ishaStatus = 1;
					Log.v(getClass().getSimpleName(), "isha click ON");
					
				}else{
					
					local_ishaStatus = 0;
					Log.v(getClass().getSimpleName(), "isha click OFF ");
					
				}
	
				break;
				
				
			case R.id.qiblah_settings_done_button:
					
				if(local_calmethodtype != calmethodtype)
				{
				
					prayTimeStatus.edit().putInt("prayermethod",local_calmethodtype).commit();
					editor.putInt("fajr", local_fajrStatus);
					editor.putInt("shuruq", local_shuruqStatus);
					editor.putInt("dhuhr", local_dhuhrStatus);
					editor.putInt("maghrib", local_maghribStatus);
					editor.putInt("asr", local_asrStatus);
					editor.putInt("isha", local_ishaStatus);						
					editor.putInt("audiofile", local_ringtoneStatus);
					editor.putInt("reminder_status", local_reminderStatus);
					editor.commit();
					
					setTimings();
					
	
				}else{
		
	
					if (
							local_fajrStatus != mCheck_fajrStatus || 
							local_shuruqStatus != mCheck_shuruqStatus || 
							local_dhuhrStatus != mCheck_dhuhrStatus || 
							local_maghribStatus != mCheck_maghribStatus	|| 
							local_asrStatus != mCheck_asrStatus || 
							local_ishaStatus != mCheck_ishaStatus||						
							local_ringtoneStatus != mCheckAudio_SpinnerStatus || 
							local_reminderStatus != mCheckReminder_SpinnerStatus){
							
						  
							editor.putInt("fajr", local_fajrStatus);
							editor.putInt("shuruq", local_shuruqStatus);
							editor.putInt("dhuhr", local_dhuhrStatus);
							editor.putInt("maghrib", local_maghribStatus);
							editor.putInt("asr", local_asrStatus);
							editor.putInt("isha", local_ishaStatus);						
							editor.putInt("audiofile", local_ringtoneStatus);
							editor.putInt("reminder_status", local_reminderStatus);
							editor.commit();
							
							new SetAlarmAsync(QiblahSettingsActivity.this).execute();
							    
							
						}else{
							
							finish();
						}
		
				}
	
				break;
		
		}
	}
	
	/**
	 * Deleting previous alarms.
	 */
	public void deletePrevious()
	{
		
		if (mCheck_fajrStatus == 1) 
		{

			Log.v(getClass().getSimpleName(), "fajr click delete");

			alarm_size = mAlarmTimes_size;
			cancelAlarm(0, alarm_size,0);

			if (reminderTime_Str.equalsIgnoreCase("0 minutes")) 
			{
				
				Log.v(getClass().getSimpleName(), " no reminder to cancel ");
			
			}else{
				
				Log.v(getClass().getSimpleName(), " no reminder to cancel size "+reminder_size);
				reminder_size = mReminderTimes_size + 300;
				cancelAlarm(300, reminder_size,1);
			}
			
		}
		
		if (mCheck_shuruqStatus == 1)
		{
			
			Log.v(getClass().getSimpleName(), "local_shuruqStatus click delete");
			
			alarm_size = mAlarmTimes_size + 50;
			cancelAlarm(50, alarm_size,0);
			
			if (reminderTime_Str.equalsIgnoreCase("0 minutes")) 
			{
				
				Log.v(getClass().getSimpleName(), " no reminder to cancel ");
			
			}else{
				
				Log.v(getClass().getSimpleName(), " no reminder to cancel size "+reminder_size);
				reminder_size = mReminderTimes_size + 350;
				cancelAlarm(350, reminder_size,1);
			}
			
		}
		
		if (mCheck_dhuhrStatus == 1) 
		{
			
			Log.v(getClass().getSimpleName(), "local_dhuhrStatus click delete");

			alarm_size = mAlarmTimes_size + 100;
			cancelAlarm(100, alarm_size,0);

			if (reminderTime_Str.equalsIgnoreCase("0 minutes"))
			{
				
				Log.v(getClass().getSimpleName(), " no reminder to cancel ");
			
			}else{
				
				Log.v(getClass().getSimpleName(), " no reminder to cancel size "+reminder_size);
				reminder_size = mReminderTimes_size + 400;
				cancelAlarm(400, reminder_size,1);
			}
			
		}
		
		
		if (mCheck_asrStatus == 1) 
		{
			
			Log.v(getClass().getSimpleName(), "local_asrStatus click delete");

			alarm_size = mAlarmTimes_size + 150;
			cancelAlarm(150, alarm_size,0);

			if (reminderTime_Str.equalsIgnoreCase("0 minutes")) 
			{
				
				Log.v(getClass().getSimpleName(), " no reminder to cancel ");
			
			}else{
				
				Log.v(getClass().getSimpleName(), " no reminder to cancel size "+reminder_size);
				reminder_size = mReminderTimes_size + 450;
				cancelAlarm(450, reminder_size,1);
			}
			
		}
		
		if (mCheck_maghribStatus == 1)
		{
			
			Log.v(getClass().getSimpleName(), "local_maghribStatus click delete");

			alarm_size = mAlarmTimes_size + 200;
			cancelAlarm(200, alarm_size,0);

			if (reminderTime_Str.equalsIgnoreCase("0 minutes"))
			{
				
				Log.v(getClass().getSimpleName(), " no reminder to cancel ");
			
			}else{
				
				Log.v(getClass().getSimpleName(), " no reminder to cancel size "+reminder_size);
				reminder_size = mReminderTimes_size + 500;
				cancelAlarm(500, reminder_size,1);
			}
			
		}
		
		
		if (mCheck_ishaStatus == 1) 
		{
			
			Log.v(getClass().getSimpleName(), "local_ishaStatus click delete");
			
			alarm_size = mAlarmTimes_size + 250;
			cancelAlarm(250, alarm_size,0);

			if (reminderTime_Str.equalsIgnoreCase("0 minutes"))
			{
				
				Log.v(getClass().getSimpleName(), " no reminder to cancel ");
			
			}else{
				
				Log.v(getClass().getSimpleName(), " no reminder to cancel size "+reminder_size);
				reminder_size = mReminderTimes_size + 550;
				cancelAlarm(550, reminder_size,1);
			}	
			
		}
	}

	
	
	public void resetAlarm()
	{
		
	
		
		if (local_fajrStatus == 1) 
		{
			Log.v(getClass().getSimpleName(), "local_fajrStatus click start");

			local_fajrStatus = 1;
			alarmfor = 1;
			
			PrayerTimeModel mTimeModel = mDatabaseHelper.getAlltimesByType("fajr");
			prayerTimesforFajr = mTimeModel.prayerTimings;
			
			setAlarm(prayerTimesforFajr, cancelIdsforFajr, alarmfor); // setting alarm...
			
			if (reminderTime_Str.equalsIgnoreCase("0 minutes")) 
			{
			
				Log.v(getClass().getSimpleName(), " no reminder ");
				
			}else{
				
				Log.v(getClass().getSimpleName(), " remineder time "+reminderTime_Str);
				setReminderAlarm(prayerTimesforFajr, cancelIdsforFajr, alarmfor);
			}
	
		}
		
		
		if (local_shuruqStatus == 1) 
		{
			Log.v(getClass().getSimpleName(), "local_shuruq click start");

			local_shuruqStatus = 1;
			alarmfor = 2;

			PrayerTimeModel mTimeModel = mDatabaseHelper.getAlltimesByType("shuruq");
			prayerTimesforShuruq = mTimeModel.prayerTimings;
			
			setAlarm(prayerTimesforShuruq, cancelIdsforShuruq, alarmfor); //setting alarm...
			
			if (reminderTime_Str.equalsIgnoreCase("0 minutes"))
			{
				
				Log.v(getClass().getSimpleName(), " no reminder ");
				
			}else{
				
				Log.v(getClass().getSimpleName(), " remineder time "+reminderTime_Str);
				setReminderAlarm(prayerTimesforShuruq, cancelIdsforShuruq, alarmfor);
			}
			
		}
		
		
		if (local_dhuhrStatus == 1)
		{
			Log.v(getClass().getSimpleName(), "local_dhuhr click start");

			alarmfor = 3;
			
			local_dhuhrStatus = 1;
			PrayerTimeModel mTimeModel = mDatabaseHelper.getAlltimesByType("dhuhr");
			prayerTimesforDhuhr = mTimeModel.prayerTimings;
			
			setAlarm(prayerTimesforDhuhr, cancelIdsforDhuhr, alarmfor); //setting alarm..
			
			if (reminderTime_Str.equalsIgnoreCase("0 minutes"))
			{
				
				Log.v(getClass().getSimpleName(), " no reminder ");
				
			}else{
				
				Log.v(getClass().getSimpleName(), " remineder time "+reminderTime_Str);
				setReminderAlarm(prayerTimesforDhuhr, cancelIdsforDhuhr, alarmfor);
			}
		
		}
		
		
		if (local_asrStatus == 1)
		{
			Log.v(getClass().getSimpleName(), "local_asr click start");

			local_asrStatus = 1;
			alarmfor = 4;
			
			PrayerTimeModel mTimeModel = mDatabaseHelper.getAlltimesByType("asr");
			prayerTimesforAsr = mTimeModel.prayerTimings;
			
			setAlarm(prayerTimesforAsr, cancelIdsforAsr, alarmfor); //setting alarm...
			
			if (reminderTime_Str.equalsIgnoreCase("0 minutes"))
			{
				
				Log.v(getClass().getSimpleName(), " no reminder ");
				
			}else{
				
				Log.v(getClass().getSimpleName(), " remineder time "+reminderTime_Str);
				setReminderAlarm(prayerTimesforAsr, cancelIdsforAsr, alarmfor);
			}
		
		}
		
		
		if (local_maghribStatus == 1)
		{
	
			Log.v(getClass().getSimpleName(), "local_maghrib click start");

			alarmfor = 5;
			
			local_maghribStatus = 1;
			
			PrayerTimeModel mTimeModel = mDatabaseHelper.getAlltimesByType("maghrib");
			prayerTimesforMaghrib = mTimeModel.prayerTimings;
			
			setAlarm(prayerTimesforMaghrib, cancelIdsforMaghrib, alarmfor); //setting alarm...
			
			if (reminderTime_Str.equalsIgnoreCase("0 minutes")) 
			{
				
				Log.v(getClass().getSimpleName(), " no reminder ");
				
			}else{
				
				Log.v(getClass().getSimpleName(), " remineder time "+reminderTime_Str);
				setReminderAlarm(prayerTimesforMaghrib, cancelIdsforMaghrib, alarmfor);
			}
		
		}
		
		
		if (local_ishaStatus == 1)
		{
			Log.v(getClass().getSimpleName(), "local_isha click start");

			alarmfor = 6;
			local_ishaStatus = 1;
			
			PrayerTimeModel mTimeModel = mDatabaseHelper.getAlltimesByType("isha");
			prayerTimesforIsha = mTimeModel.prayerTimings;
			
			setAlarm(prayerTimesforIsha, cancelIdsforIsha, alarmfor);//setting alarm..
			
			if (reminderTime_Str.equalsIgnoreCase("0 minutes"))
			{
				
				Log.v(getClass().getSimpleName(), " no reminder");
				
			}else{
				
				Log.v(getClass().getSimpleName(), " remineder time "+reminderTime_Str);
				setReminderAlarm(prayerTimesforIsha, cancelIdsforIsha, alarmfor);
				
			}
		
		}
		
	}
	
	Thread mThread;
	
	Finihandler finihandler= new Finihandler();
	class Finihandler extends Handler{		
		public void handleMessage(android.os.Message message){	
			try {
				
				if (mProgressDialog != null) {
					mProgressDialog.dismiss();
				}							
				
				mThread.interrupt();
				mThread = null;
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			finish();
			
		}
	}
	
	
	/**
	 *  playing the selected audio file.
	 * @param resid
	 */
	private void playSample(int resid)
	{
	    AssetFileDescriptor afd = this.getResources().openRawResourceFd(resid);
	    Log.v(getClass().getSimpleName(), " play method");
	    try
	    {   
			Log.v(getClass().getSimpleName(), " play status3 "+playstatus);
	    	player.reset();
	    	player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getDeclaredLength());
	    	player.prepare();
	    	player.start();
	        afd.close();
	    }
	    catch (IllegalArgumentException e)
	    {
	        Log.e(getClass().getSimpleName(), "Unable to play audio queue do to exception: " + e.getMessage(), e);
	    }
	    catch (IllegalStateException e)
	    {
	        Log.e(getClass().getSimpleName(), "Unable to play audio queue do to exception: " + e.getMessage(), e);
	    }
	    catch (IOException e)
	    {
	        Log.e(getClass().getSimpleName(), "Unable to play audio queue do to exception: " + e.getMessage(), e);
	    }
	}

	/**
	 *  setting Reminder for the given timings.
	 * @param timings
	 */
	public void setReminderAlarm(ArrayList<String> timings, ArrayList<Integer> uniqueIds, int alarmfor){
		
		int dayincrement = 0;
		int uniqueIdvalue = 0;
		long alarmtime;
		long currenttime;
		String messagefor = "";
		int currentDay = calender.get(Calendar.DAY_OF_MONTH);
		
		Calendar currentcalendar = Calendar.getInstance();
		
		Log.v(getClass().getSimpleName(), " reminder for "+alarmfor+" reminder time "+reminderTime_Str);

		
		if (alarmfor == 1){ 
			uniqueIdvalue = 300;
			messagefor = "Fajr";
		}else if(alarmfor == 2){
			uniqueIdvalue = 350;
			messagefor = "Shuruq";
		}else if(alarmfor == 3){
			uniqueIdvalue = 400;
			messagefor = "Dhuhr";
		}else if(alarmfor == 4){
			uniqueIdvalue = 450;
			messagefor = "Asr";
		}else if(alarmfor == 5){
			uniqueIdvalue = 500;
			messagefor = "Maghrib";
		}else if(alarmfor == 6){
			uniqueIdvalue = 550;
			messagefor = "Isha";
		}
		
		for (int iCtr = currentDay - 1; iCtr < timings.size(); iCtr++)
		{
	
			String[] arrDate = reminderTime_Str.split(" ");
			String time_str = arrDate[0];
			int reminderTime_int = Integer.parseInt(time_str);
			
			long time = new DateFormater().parseDate(timings.get(iCtr), "hh:mm a");
			Date date = new Date(time);
			currentcalendar.setTime(date);
			currentcalendar.set(calender.get(Calendar.YEAR), calender.get(Calendar.MONTH), calender.get(Calendar.DAY_OF_MONTH));
			currentcalendar.add(Calendar.DAY_OF_MONTH, dayincrement);

			currentcalendar.add(Calendar.MINUTE, -reminderTime_int);
			
			alarmtime = currentcalendar.getTimeInMillis();
			currenttime = calender.getTimeInMillis();
			
			if (alarmtime >= currenttime) 
			{
	 			
				Intent alarmIntent = new Intent(QiblahSettingsActivity.this, PrayerTimesReceiver.class);
				alarmIntent.setAction("fromQiblahsettingsReminder");
				alarmIntent.putExtra("messagefor", messagefor);
				alarmIntent.putExtra("time", timings.get(iCtr));
				
				PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(QiblahSettingsActivity.this, uniqueIdvalue, alarmIntent, 0);
				
				uniqueIds.add(uniqueIdvalue);
				
				Utils.ALARM_MANAGER.set(AlarmManager.RTC_WAKEUP, currentcalendar.getTimeInMillis(), alarmPendingIntent);
				
				Log.v(getClass().getSimpleName()," reminder time "+currentcalendar.getTime());
				uniqueIdvalue++;
				
			}else{
//				Toast.makeText(QiblahSettingsActivity.this, " today alam time is over ", Toast.LENGTH_SHORT).show();
			}
			dayincrement++;
		}
		
		editor.putInt("mReminderTimes_size", uniqueIds.size());
		editor.commit();
		
	}
	
	
	/**
	 *  setting alarm for the given timings.
	 * @param timings
	 */
	public void setAlarm(ArrayList<String> timings, ArrayList<Integer> uniqueIds, int alarmfor){
		
		int dayincrement = 0;
		int uniqueIdvalue = 0;
		String messagefor = "";
		long alarmtime;
		long currenttime;
		int currentDay = calender.get(Calendar.DAY_OF_MONTH);
		
		Calendar currentcalendar = Calendar.getInstance();
		
		Log.v(getClass().getSimpleName(), " alarm for "+alarmfor);
		
		if (alarmfor == 1){ 
			uniqueIdvalue = 0;
			messagefor = "Fajr";
		}else if(alarmfor == 2){
			uniqueIdvalue = 50;
			messagefor = "Shuruq";
		}else if(alarmfor == 3){
			uniqueIdvalue = 100;
			messagefor = "Dhuhr";
		}else if(alarmfor == 4){
			uniqueIdvalue = 150;
			messagefor = "Asr";
		}else if(alarmfor == 5){
			uniqueIdvalue = 200;
			messagefor = "Maghrib";
		}else if(alarmfor == 6){
			uniqueIdvalue = 250;
			messagefor = "Isha";
		}
		
		Log.v(getClass().getSimpleName()," timings.size() "+timings.size()+" currentDay "+currentDay);

		for (int iCtr = currentDay - 1; iCtr < timings.size(); iCtr++)
		{
	
			long time = new DateFormater().parseDate(timings.get(iCtr), "hh:mm a");
			Date date = new Date(time);
			currentcalendar.setTime(date);
			currentcalendar.set(calender.get(Calendar.YEAR), calender.get(Calendar.MONTH), calender.get(Calendar.DAY_OF_MONTH));
			currentcalendar.add(Calendar.DAY_OF_MONTH, dayincrement);

			alarmtime = currentcalendar.getTimeInMillis();
			currenttime = calender.getTimeInMillis();
			
			if (alarmtime >= currenttime) 
			{
				
				Intent intent = new Intent(QiblahSettingsActivity.this, PrayerTimesReceiver.class);
				intent.setAction("fromQiblahsettings");
				intent.putExtra("selectedaudio", selected_audiofile);
				intent.putExtra("messageFor", messagefor);
		        PendingIntent sender = PendingIntent.getBroadcast(QiblahSettingsActivity.this,uniqueIdvalue, intent, 0);
		        
				Log.v(getClass().getSimpleName()," uniqueIdvalue "+uniqueIdvalue);
				
				uniqueIds.add(uniqueIdvalue);
				
				AlarmManager qiblahAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
				qiblahAlarmManager.set(AlarmManager.RTC_WAKEUP, currentcalendar.getTimeInMillis(), sender);
				
				Log.v(getClass().getSimpleName()," alarm time "+currentcalendar.getTime());
				uniqueIdvalue++;
				
			}else{
				
			}
			dayincrement++;  
		}
		
		editor.putInt("mAlarmTimes_size", uniqueIds.size());
		editor.commit();
		
	}
	
	
	/**
	 *  canceling alarms and reminders based on unique id's.
	 * @param uniqueIds for different alarms, size ----> alarms size, type ----> alarm or reminder.
	 */
	public void cancelAlarm(int uniqueId, int size,  int type) {

		int iCTR = uniqueId;
		
		Log.v(getClass().getSimpleName(), "value "+uniqueId+" size "+size);
		
		for (; iCTR < size; iCTR++)
		{

			Intent intent = new Intent(QiblahSettingsActivity.this,PrayerTimesReceiver.class);
			
			if(type == 0)
			intent.setAction("fromQiblahsettings");
			else
			intent.setAction("fromQiblahsettingsReminder");
			
			final PendingIntent pIntent = PendingIntent.getBroadcast(this,iCTR,intent,0);

			Log.v(getClass().getSimpleName(), "unique id "+iCTR);
			// And cancel the alarm.
	         AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
	         alarmManager.cancel(pIntent);
	         
		}
	
	}
	
	/**
	 *  after completion of song this method will get called
	 */
	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		mBTNPlay.setBackgroundResource(R.drawable.play);
		Log.v(getClass().getSimpleName(), " play status4 "+playstatus);

		playstatus = false;
		Log.v(getClass().getSimpleName(), " on completion ");
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		player.release();
	}

	
	
//  ****************************
//  Get Prayer Time
   
 
 private ArrayList<PrayerTimeModel> prayerTimes = new ArrayList<PrayerTimeModel>();

 Calendar today ;
 
 ProgressDialog mProgressDialog;
 
 private void setTimings() {
     
        
     mProgressDialog = ProgressDialog.show(QiblahSettingsActivity.this, null, "Please wait..");
     new AsynPrayerTimeGetter().execute();        
     
 }                
             


 class AsynPrayerTimeGetter extends AsyncTask<Void, Void, Void> {

     @Override
     protected Void doInBackground(Void... params) {
         // TODO Auto-generated method stub
         try {
                         
          	 
        	 String prayerCountryCode = prayTimeStatus.getString("countryshortname", "");
        	 String prayerCity = prayTimeStatus.getString("city", "");
        	 String prayerStateCode = prayTimeStatus.getString("statename", "");
        	 String prayerLat = prayTimeStatus.getString("lat", "");
        	 String prayerLong = prayTimeStatus.getString("long", "");
        	 String prayerTimeZone = prayTimeStatus.getString("timezone", "0.0");
                
                     
                     new PrayerTimeParser(String.format(Urls.PRAYER_TIMING_URL,
                             URLEncoder.encode(prayerCountryCode,"UTF-8"), 
                             URLEncoder.encode(prayerCity, "UTF-8"),
                             URLEncoder.encode(prayerStateCode,"UTF-8"), 
                             prayerLat,
                             prayerLong, 
                             prayerTimeZone, 
                             String.valueOf(local_calmethodtype)),
                             showReaminingTimeHandler).parser();        
                     

         } catch (Exception e) {
             // TODO: handle exception

             e.printStackTrace();

         }
         return null;
     }

 }

 
 	Handler showReaminingTimeHandler = new Handler() {
     PrayerTimeModel mTimeModel;
     @Override
     public void handleMessage(Message msg) {
         // TODO Auto-generated method stub
         super.handleMessage(msg);

         if (msg.what == PrayerTimeParser.FOUND_RESULT) {
             mDatabaseHelper.deletPrayTimes();
             prayerTimes = (ArrayList<PrayerTimeModel>) msg.getData().get("data");
            
             for(int i = 0; i<prayerTimes.size(); i++){
                 mTimeModel = prayerTimes.get(i);
                 long status = mDatabaseHelper.insertPrayerTimings(mTimeModel.getDate(), mTimeModel.getMonth(), mTimeModel.getYear(), mTimeModel.prayerTimings.get(0), mTimeModel.prayerTimings.get(1), mTimeModel.prayerTimings.get(2), mTimeModel.prayerTimings.get(3), mTimeModel.prayerTimings.get(4), mTimeModel.prayerTimings.get(5));
                 Log.e("status", status+" null");
             }
             
             mProgressDialog.dismiss();
             new SetAlarmAsync(QiblahSettingsActivity.this).execute();             
         
         }
     }

 };
 
 
 
 

 class SetAlarmAsync extends AsyncTask<Void, Void, Void>{

	Context mContext;
	ProgressDialog mDialog;
	public  SetAlarmAsync(Context context){
		
		mContext = context;
		mDialog = ProgressDialog.show(context, null, "Please wait..");
		mDialog.setCancelable(true);
	}
	@Override
	protected Void doInBackground(Void... params) {

		 deletePrevious(); // deleting previous alarms
			resetAlarm();  // resetting the new alarms
			
		return null;
	}
	 
	public void onPostExecute(Void result) 
	{
		
		 try 
		 {
			
			mDialog.dismiss();
			mDialog = null;
			finish();
			 
		 } catch (Exception e) {
		        // nothing
		    }
			
	}
 }

 	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try {
			
			mDatabaseHelper.closeDataBase();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
