package com.daleelo.QiblahReceiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.daleelo.R;

public class PrayerTimesReceiver extends BroadcastReceiver{

	private NotificationManager mNotificationManager;
	private static final int NOTIFICATION = 123;
	String fileName = "";
	String messagefor = "";
	String text = "";
	@Override
	public void onReceive(final Context context, Intent intent) {
		// TODO Auto-generated method stub
		
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		String title = "Daleelo";

		int icon = R.drawable.logo_d;

		long time = System.currentTimeMillis();
		
		String fileName = "android.resource://" + context.getPackageName() + "/" + R.raw.abdul_basit1;
		
		if (intent.getAction().equalsIgnoreCase("fromQiblahsettings")) {
			fileName = intent.getStringExtra("selectedaudio");
			messagefor = intent.getStringExtra("messageFor");
			text = "This is the time to Pray for "+messagefor;
		}
		
		if (intent.getAction().equalsIgnoreCase("fromQiblahsettingsReminder")) {
			fileName = "android.resource://" + context.getPackageName() + "/" + R.raw.default1;
			messagefor = intent.getStringExtra("messagefor");
			String reminde_time = intent.getStringExtra("time");
			text = messagefor+" Prayer time startes at "+reminde_time;
		}
		
		Toast.makeText(context, "Your alarm started"+"file name "+fileName, Toast.LENGTH_SHORT).show();
		
		Notification notification = new Notification(icon, text, time);
		notification.sound = Uri.parse(fileName);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, new Intent(context, PrayerTimesReceiver.class), 0);
		notification.setLatestEventInfo(context, title, text, contentIntent);
		
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
	
		// Send the notification to the system.
		mNotificationManager.notify(NOTIFICATION, notification);
		
	}

}
