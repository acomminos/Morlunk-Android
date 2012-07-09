package com.acomminos.morlunk.account;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.acomminos.morlunk.R;
import com.acomminos.morlunk.http.MorlunkRequest;
import com.acomminos.morlunk.http.MorlunkRequest.MorlunkRequestType;
import com.acomminos.morlunk.http.MorlunkRequestLoader;
import com.acomminos.morlunk.http.MorlunkResponse;
import com.acomminos.morlunk.http.MorlunkResponse.MorlunkRequestResult;

public class MorlunkAccountManager implements LoaderCallbacks<MorlunkResponse>{
	
	/**
	 * Interface for fragments and activities that need to listen to login requests to implement.
	 * Typically used to create a request after the login request where authentication is required.
	 * @author andrew
	 *
	 */
	public interface MorlunkAccountListener {
		public void onLoginSuccess(MorlunkResponse response);
		public void onLoginFailure(MorlunkResponse response);
		public void onLoginCancel();
	}
	
	private static final String LOGIN_PREFERENCES_FILE = "morlunk_login.xml";
	private static final String LOGIN_USERNAME_KEY = "username";
	private static final String LOGIN_PASSWORD_KEY = "password";
	
	private static final String LOGIN_API_URL = "http://www.morlunk.com/account/login/json";
	private static final int LOGIN_LOADER_ID = 1337;
	
	private Context context;
	private LoaderManager loaderManager;
	private SharedPreferences loginPreferences;
	private ProgressDialog progressDialog;
	
	private MorlunkAccountListener accountListener;
	
	public MorlunkAccountManager(Context context, LoaderManager loaderManager) {
		this.context = context;
		this.loaderManager = loaderManager;
		this.loginPreferences = context.getSharedPreferences(LOGIN_PREFERENCES_FILE, 0);
		
		this.progressDialog = new ProgressDialog(context);
		this.progressDialog.setTitle("Please Wait");
		this.progressDialog.setMessage("Connecting to Morlunk.com...");
		this.progressDialog.setIndeterminate(true);
		this.progressDialog.setCancelable(false);
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
	 * Clears the HttpClient's cookies and removes the user's data from the login data file.
	 */
	public void logout() {
		// Clear cookies
		MorlunkRequestLoader.clearCookies();
		// Clear data
		loginPreferences.edit()
						.remove(LOGIN_USERNAME_KEY)
						.remove(LOGIN_PASSWORD_KEY)
						.commit();
	}
	
	/**
	 * This login method is only called when the stored username and password are confirmed to be invalid,
	 * hence why it is private. To require a login event, @see login().
	 * The credentials passed will also be stored in XML.
	 * @param username
	 * @param password
	 */
	private void login(String username, String password) {
		storeCredentials(username, password); // TODO make credentials only stored on a success.
		
		Bundle args = new Bundle();
		args.putString("username", username);
		args.putString("password", password);
		
		// Restart to get new post data
		loaderManager.restartLoader(LOGIN_LOADER_ID, args, this);
	}
	
	/**
	 * Stores the passed credentials in the preferences XML file.
	 * @param username
	 * @param password
	 */
	private void storeCredentials(String username, String password) {
		Editor preferenceEditor = loginPreferences.edit();
		preferenceEditor.putString(LOGIN_USERNAME_KEY, username);
		preferenceEditor.putString(LOGIN_PASSWORD_KEY, password);
		preferenceEditor.commit();
	}
	
	/**
	 * Show login alert and sets up hooks.
	 */
	private void showLoginAlert() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
		alertDialog.setTitle("Login to Morlunk Co.");
		View alertView = ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.account_text_alert, null);
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
		alertDialog.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface arg0) {
				// Tell listener to cancel
				accountListener.onLoginCancel();
				
			}
		});
		alertDialog.create().show();
	}

	@Override
	public Loader<MorlunkResponse> onCreateLoader(int arg0, Bundle arg1) {
		// Show loading dialog
		this.progressDialog.show();
		
		String username = arg1.getString("username");
		String password = arg1.getString("password");
		
		MorlunkRequest loginRequest = new MorlunkRequest(LOGIN_API_URL, MorlunkRequestType.REQUEST_POST, MorlunkResponse.class);
		loginRequest.addArgument("username", username);
		loginRequest.addArgument("password", password);
		
		MorlunkRequestLoader requestTask = new MorlunkRequestLoader(context, loginRequest);
		requestTask.forceLoad();
		return requestTask;
	}

	@Override
	public void onLoadFinished(Loader<MorlunkResponse> arg0,
			MorlunkResponse arg1) {
		// Close loading dialog
		this.progressDialog.dismiss();
		
		if(accountListener != null) {
			if(arg1.result == MorlunkRequestResult.SUCCESS) {
				// TODO store credentials here
				accountListener.onLoginSuccess(arg1);
			} else {
				showLoginAlert();
				accountListener.onLoginFailure(arg1);
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
