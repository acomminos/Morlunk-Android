package com.acomminos.morlunk.notify;

import com.acomminos.morlunk.http.MorlunkRequest;
import com.acomminos.morlunk.http.MorlunkRequest.MorlunkRequestType;
import com.acomminos.morlunk.http.MorlunkResponse;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MorlunkNotificationService extends Service {

	private static final String NOTIFICATION_API_URL = "http://www.morlunk.com/notifications/json";
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		MorlunkNotificationManager notificationManager = MorlunkNotificationManager.getInstance();
		
		MorlunkRequest notificationRequest = new MorlunkRequest(NOTIFICATION_API_URL, MorlunkRequestType.REQUEST_POST, MorlunkResponse.class);
	}
}
