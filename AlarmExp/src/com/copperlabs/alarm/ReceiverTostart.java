package com.copperlabs.alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

public class ReceiverTostart extends BroadcastReceiver{

	private NotificationManager mNotificationManager;
	private static final int NOTIFICATION = 123;
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Toast.makeText(context, "Your alarm started", Toast.LENGTH_SHORT).show();
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		String title = "Reminder";

		String fileName = "android.resource://" + context.getPackageName() + "/" + R.raw.abdul_basit1;
		int icon = R.drawable.ic_launcher;

		String text = "this is repeating notification..";		

		long time = System.currentTimeMillis();
		
		Notification notification = new Notification(icon, text, time);
		notification.sound = Uri.parse(fileName);
		
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, new Intent(context, ReceiverTostart.class), 0);
		notification.setLatestEventInfo(context, title, text, contentIntent);
		
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
	
		// Send the notification to the system.
		mNotificationManager.notify(NOTIFICATION, notification);
	}

}
