package com.acomminos.morlunk.notify;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class MorlunkNotificationManager {

	private static MorlunkNotificationManager notificationManager;
	
	// TODO respect the notification toggle option in settings
	private static final long notificationPeriod = AlarmManager.INTERVAL_FIFTEEN_MINUTES;
	private Context context;
	private PendingIntent notificationPendingIntent;
	private boolean notificationsOn = false;
	
	/**
	 * Creates a new instance of a notification manager. To be called when the app starts.
	 */
	public static MorlunkNotificationManager createInstance(Context context) {
		notificationManager = new MorlunkNotificationManager(context);
		return notificationManager;
	}
	
	public static MorlunkNotificationManager getInstance() {
		return notificationManager;
	}
	
	private MorlunkNotificationManager(Context context) {
		this.context = context;
		
		// Create notification intent
		Intent notificationIntent = new Intent(context, MorlunkNotificationService.class);
		notificationPendingIntent = PendingIntent.getService(context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
	}
	
	/**
	 * Register this app for notifications.
	 * If the user has disabled notifications, don't do anything.
	 */
	public void registerNotifications() {
		if(!notificationsOn) {
			// Register alarm
			AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
			alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, 0, notificationPeriod, notificationPendingIntent);
			notificationsOn = true;
		}
	}
	
	/**
	 * Un-registers the app from receiving notifications.
	 */
	public void deregisterNotifications() {
		if(notificationsOn) {
			AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
			alarmManager.cancel(notificationPendingIntent);
			notificationsOn = false;
		}
	}
	
	/**
	 * @return the notificationsOn
	 */
	public boolean areNotificationsOn() {
		return notificationsOn;
	}

}
