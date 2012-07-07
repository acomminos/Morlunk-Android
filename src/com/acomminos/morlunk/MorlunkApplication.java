package com.acomminos.morlunk;

import android.app.Application;
import android.webkit.CookieSyncManager;

public class MorlunkApplication extends Application {
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		// Create cookie sync manager
		CookieSyncManager.createInstance(this);
	}

}
