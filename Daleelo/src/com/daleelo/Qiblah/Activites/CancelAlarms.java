package com.daleelo.Qiblah.Activites;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.daleelo.QiblahReceiver.PrayerTimesReceiver;
import com.daleelo.Utilities.Utils;

public class CancelAlarms {

	Context mContext;
	
	int
		mCheck_fajrStatus,
		mCheck_shuruqStatus,
		mCheck_dhuhrStatus,
		mCheck_asrStatus,				
		mCheck_maghribStatus, 
		mCheck_ishaStatus,
		mReminderTimes_size, mAlarmTimes_size,
		selected_adhaan, alarm_size, mCheckReminder_SpinnerStatus, reminder_size = 0;
		private SharedPreferences mQiblahpreferences;
		private SharedPreferences.Editor editor;

	
	
	public void deletePrevious(Context context)
	{
	
		try{
			mContext = context;
			mQiblahpreferences = mContext.getSharedPreferences("qiblahstatus", Context.MODE_PRIVATE);
	
			editor = mQiblahpreferences.edit();
			
			mCheck_fajrStatus    = mQiblahpreferences.getInt("fajr", 0);
			mCheck_shuruqStatus  = mQiblahpreferences.getInt("shuruq", 0);
			mCheck_dhuhrStatus   = mQiblahpreferences.getInt("dhuhr", 0);
			mCheck_asrStatus     = mQiblahpreferences.getInt("asr", 0);
			mCheck_maghribStatus = mQiblahpreferences.getInt("maghrib", 0);
			mCheck_ishaStatus    = mQiblahpreferences.getInt("isha", 0);
			
	
			mCheckReminder_SpinnerStatus = mQiblahpreferences.getInt("reminder_status", 0);
			
			mAlarmTimes_size = mQiblahpreferences.getInt("mAlarmTimes_size", 0);
			mReminderTimes_size = mQiblahpreferences.getInt("mReminderTimes_size", 0);
			
			if (mCheck_fajrStatus == 1) 
			{

				Log.v(getClass().getSimpleName(), "fajr click delete");

				alarm_size = mAlarmTimes_size;
				cancelAlarm(0, alarm_size,0);

				if (mCheckReminder_SpinnerStatus == 0) 
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
				
				if (mCheckReminder_SpinnerStatus == 0) 
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

				if (mCheckReminder_SpinnerStatus == 0)
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

				if (mCheckReminder_SpinnerStatus == 0) 
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

				if (mCheckReminder_SpinnerStatus == 0)
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

				if (mCheckReminder_SpinnerStatus == 0)
				{
					
					Log.v(getClass().getSimpleName(), " no reminder to cancel ");
				
				}else{
					
					Log.v(getClass().getSimpleName(), " no reminder to cancel size "+reminder_size);
					reminder_size = mReminderTimes_size + 550;
					cancelAlarm(550, reminder_size,1);
				}	
				
			}
			
			editor.clear();
			editor.commit();
		}catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	/**
	 *  canceling alarms based on unique id's.
	 * @param uniqueIds
	 */
	public void cancelAlarm(int uniqueId, int size,  int type) {

		int iCTR = uniqueId;
		
		Log.v(getClass().getSimpleName(), "value "+uniqueId+" size "+size);
		
		for (; iCTR < size; iCTR++)
		{

			Intent intent = new Intent(mContext,PrayerTimesReceiver.class);
			
			if(type == 0)
			intent.setAction("fromQiblahsettings");
			else
			intent.setAction("fromQiblahsettingsReminder");
			
			final PendingIntent pIntent = PendingIntent.getBroadcast(mContext,iCTR,intent,0);

			Log.v(getClass().getSimpleName(), "unique id "+iCTR);
			// And cancel the alarm.
	         AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
	         alarmManager.cancel(pIntent);
	         
		}
	
	}
	
}
