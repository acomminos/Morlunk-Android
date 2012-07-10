package com.acomminos.morlunk;

import com.acomminos.morlunk.account.MorlunkAccountManager;

import android.app.Application;

public class MorlunkApplication extends Application {
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		// Create account manager
    	MorlunkAccountManager.createInstance(this);
	}

}
