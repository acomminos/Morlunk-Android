package com.acomminos.morlunk.notify;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;

public class MorlunkNotificationManager {

	// TODO make this an option in settings
	private static final long notificationPeriod = AlarmManager.INTERVAL_FIFTEEN_MINUTES;
	
	// TODO check whether or not the user has enabled notifications.
	
	private Context context;
	
	public MorlunkNotificationManager(Context context) {
		this.context = context;
	}
	
	/**
	 * Register this app for notifications.
	 * If the user has disabled notifications, don't do anything.
	 */
	public void registerNotifications() {
		PendingIntent intent = PendingIntent.getService(context, 0, null, PendingIntent.FLAG_CANCEL_CURRENT);
		
		// Register alarm
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, 0, notificationPeriod, intent);
	}
}
