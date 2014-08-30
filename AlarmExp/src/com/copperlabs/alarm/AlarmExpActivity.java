package com.copperlabs.alarm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

public class AlarmExpActivity extends Activity implements OnCompletionListener{
	/** Called when the activity is first created. */
	Toast mToast;
	Calendar currentCalender;
	String[] alarmTimes = { "00:01", "00:02", "00:03", "00:04", "00:05","00:06", "00:07", "00:08", "00:09" };
	
	String[] checkExp = {"00:01"};
	ArrayList<Integer> uniqueid;
	MediaPlayer player;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		currentCalender = Calendar.getInstance();
		uniqueid = new ArrayList<Integer>();
		SeekBar sk=(SeekBar)findViewById(R.id.seekBar1);
		
		player =  new MediaPlayer();
		player.setOnCompletionListener(this);
	}

	public void clicktostart(View v) {

		int uniqueId = 0;
		
		for (int iCTR = 0; iCTR < alarmTimes.length; iCTR++)
		{

			String time = alarmTimes[iCTR];
			String[] arrDate = time.split(":");
			String hours = arrDate[0];
			String minutes = arrDate[1];
			currentCalender = Calendar.getInstance();
			
			int duration_int = Integer.parseInt(hours);
			int minutes_int = Integer.parseInt(minutes);
			currentCalender.add(Calendar.HOUR, duration_int);
			currentCalender.add(Calendar.MINUTE, minutes_int);

			Intent intent = new Intent(AlarmExpActivity.this, ReceiverTostart.class);
	        PendingIntent sender = PendingIntent.getBroadcast(AlarmExpActivity.this,uniqueId, intent, 0);
			uniqueid.add(uniqueId);
			Log.v(getClass().getSimpleName(),"unique id " + uniqueid.get(iCTR));

			// Schedule the alarm!
			AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
			am.set(AlarmManager.RTC_WAKEUP, currentCalender.getTimeInMillis(), sender);

			Log.v(getClass().getSimpleName()," date time2 " + currentCalender.getTime());
			uniqueId++;
		}
		Log.v(getClass().getSimpleName(), " size " + uniqueid.size());
		if (mToast != null) {
			mToast.cancel();
		}
		mToast = Toast.makeText(AlarmExpActivity.this, "alarm started",
				Toast.LENGTH_SHORT);
		mToast.show();
	}

	public void onCancelClick(View v) {

		for (int iCTR = 0; iCTR < 9; iCTR++) {

			Intent intent = new Intent(AlarmExpActivity.this,ReceiverTostart.class);
			final PendingIntent pIntent = PendingIntent.getBroadcast(this,iCTR,intent,0);

			Log.v(getClass().getSimpleName(), "unique id "+uniqueid.get(iCTR));
			// And cancel the alarm.
	         AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
	         alarmManager.cancel(pIntent);
		}

		if (mToast != null) {
			mToast.cancel();
		}
		mToast = Toast.makeText(AlarmExpActivity.this, "alarm canceled",
				Toast.LENGTH_LONG);
		mToast.show();
	
	}

	public void onPlayClick(View v){
		
		AssetFileDescriptor afd;
		try {
			afd = getAssets().openFd("abdul_basit1.mp3");
			    player.setDataSource(afd.getFileDescriptor());
			    player.prepare();
			    player.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   
//
//		player = MediaPlayer.create(AlarmExpActivity.this, R.raw.abdul_basit1);
//		player.start();
//		player.setLooping(false);
		Log.v(getClass().getSimpleName(), " click ");
	}

	public void onCancelPressed(View v){
		player.stop();
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		Log.v(getClass().getSimpleName(), " cancel ");
	}
	

}