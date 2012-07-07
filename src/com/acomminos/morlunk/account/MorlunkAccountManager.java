package com.acomminos.morlunk.account;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;

import com.acomminos.morlunk.http.MorlunkRequest;
import com.acomminos.morlunk.http.MorlunkRequest.MorlunkRequestType;
import com.acomminos.morlunk.http.MorlunkRequestTask;
import com.acomminos.morlunk.http.MorlunkResponse;
import com.acomminos.morlunk.http.MorlunkResponse.MorlunkRequestResult;

public class MorlunkAccountManager implements LoaderCallbacks<MorlunkResponse>{
	
	interface MorlunkAccountListener {
		public void authenticationSuccess(MorlunkResponse response);
		public void authenticationFailure(MorlunkResponse response);
	}
	
	private static final String LOGIN_API_URL = "http://www.morlunk.com/account/login/json";
	private static final int LOGIN_LOADER_ID = 1337;
	
	private FragmentActivity context;
	
	private MorlunkAccountListener accountListener;
	
	public MorlunkAccountManager(FragmentActivity context) {
		this.context = context;
	}
	
	public void login(String username, String password) {
		LoaderManager loaderManager = context.getSupportLoaderManager();
		
		Bundle args = new Bundle();
		args.putString("username", username);
		args.putString("password", password);
		
		loaderManager.initLoader(LOGIN_LOADER_ID, args, this);
	}

	@Override
	public Loader<MorlunkResponse> onCreateLoader(int arg0, Bundle arg1) {
		String username = arg1.getString("username");
		String password = arg1.getString("password");
		
		MorlunkRequest loginRequest = new MorlunkRequest(LOGIN_API_URL, MorlunkRequestType.REQUEST_POST, MorlunkResponse.class);
		loginRequest.addArgument("username", username);
		loginRequest.addArgument("password", password);
		
		MorlunkRequestTask requestTask = new MorlunkRequestTask(context, loginRequest);
		requestTask.forceLoad();
		return requestTask;
	}

	@Override
	public void onLoadFinished(Loader<MorlunkResponse> arg0,
			MorlunkResponse arg1) {
		if(accountListener != null) {
			if(arg1.result == MorlunkRequestResult.SUCCESS) {
				accountListener.authenticationSuccess(arg1);
			} else {
				accountListener.authenticationFailure(arg1);
			}
		}
	}

	@Override
	public void onLoaderReset(Loader<MorlunkResponse> arg0) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @return the accountListener
	 */
	public MorlunkAccountListener getAccountListener() {
		return accountListener;
	}

	/**
	 * @param accountListener the accountListener to set
	 */
	public void setAccountListener(MorlunkAccountListener accountListener) {
		this.accountListener = accountListener;
	}
}
