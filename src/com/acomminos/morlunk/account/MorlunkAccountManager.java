package com.acomminos.morlunk.account;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.EditText;

import com.acomminos.morlunk.R;
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
	
	private static final String LOGIN_PREFERENCES_FILE = "morlunk_login.xml";
	private static final String LOGIN_USERNAME_KEY = "username";
	private static final String LOGIN_PASSWORD_KEY = "password";
	
	private static final String LOGIN_API_URL = "http://www.morlunk.com/account/login/json";
	private static final int LOGIN_LOADER_ID = 1337;
	
	private FragmentActivity context;
	private SharedPreferences loginPreferences;
	
	private MorlunkAccountListener accountListener;
	
	public MorlunkAccountManager(FragmentActivity context) {
		this.context = context;
		SharedPreferences prefs = context.getSharedPreferences(LOGIN_PREFERENCES_FILE, 0);
		
	}
	
	/**
	 * Attempts to login with the stored username and password, if any.
	 */
	public void login() {
		String username = loginPreferences.getString(LOGIN_USERNAME_KEY, "");
		String password = loginPreferences.getString(LOGIN_PASSWORD_KEY, "");
		login(username, password);
	}
	
	/**
	 * This login method is only called when the stored username and password are confirmed to be invalid,
	 * hence why it is private. To require a login event, @see login().
	 * In the case of a successful authentication, the credentials will be stored in XML.
	 * @param username
	 * @param password
	 */
	private void login(String username, String password) {
		LoaderManager loaderManager = context.getSupportLoaderManager();
		
		Bundle args = new Bundle();
		args.putString("username", username);
		args.putString("password", password);
		
		// Restart to get new post data
		loaderManager.restartLoader(LOGIN_LOADER_ID, args, this);
	}
	
	/**
	 * Show login alert and sets up hooks.
	 */
	private void showLoginAlert() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
		alertDialog.setTitle("Login to Morlunk Co.");
		View alertView = context.getLayoutInflater().inflate(R.layout.account_text_alert, null);
		final EditText usernameField = (EditText) alertView.findViewById(R.id.account_alert_username);
		final EditText passwordField = (EditText) alertView.findViewById(R.id.account_alert_password);
		
		alertDialog.setView(alertView);
		alertDialog.setPositiveButton("Login", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				login(usernameField.getText().toString(), passwordField.getText().toString());
				dialog.dismiss();
			}
		});
		alertDialog.setNegativeButton("Cancel", null);
		alertDialog.create().show();
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
				showLoginAlert();
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
