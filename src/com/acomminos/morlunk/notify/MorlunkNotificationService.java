package com.acomminos.morlunk.notify;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MorlunkNotificationService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}

}
