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
import android.widget.CheckBox;
import android.widget.EditText;

import com.acomminos.morlunk.R;
import com.acomminos.morlunk.http.MorlunkRequest;
import com.acomminos.morlunk.http.MorlunkRequest.MorlunkRequestType;
import com.acomminos.morlunk.http.MorlunkRequestLoader;
import com.acomminos.morlunk.http.MorlunkResponse;
import com.acomminos.morlunk.http.MorlunkResponse.MorlunkRequestResult;

public class MorlunkAccountManager {
	
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
	
	public class MorlunkAuthenticator implements LoaderCallbacks<MorlunkResponse> {
		
		private Context context;
		private ProgressDialog progressDialog;
		private MorlunkAccountListener accountListener;
		private LoaderManager loaderManager;
		
		public MorlunkAuthenticator(MorlunkAccountListener accountListener, Context context, LoaderManager loaderManager) {
			this.context = context;
			this.accountListener = accountListener;
			this.loaderManager = loaderManager;
			
			this.progressDialog = new ProgressDialog(context);
			this.progressDialog.setTitle("Please Wait");
			this.progressDialog.setMessage("Connecting to Morlunk.com...");
			this.progressDialog.setIndeterminate(true);
			this.progressDialog.setCancelable(false);
		}
		
		public void start(String username, String password) {
			Bundle args = new Bundle();
			args.putString(LOGIN_USERNAME_KEY, username);
			args.putString(LOGIN_PASSWORD_KEY, password);
			// Restart to get new post data
			loaderManager.restartLoader(LOGIN_LOADER_ID, args, this);
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
			final CheckBox rememberBox = (CheckBox) alertView.findViewById(R.id.account_remember_checkbox);
			
			alertDialog.setView(alertView);
			alertDialog.setPositiveButton("Login", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					login(usernameField.getText().toString(), passwordField.getText().toString(), rememberBox.isChecked(), accountListener, context, loaderManager);
					dialog.dismiss();
				}
			});
			alertDialog.setOnCancelListener(new OnCancelListener() {
				
				@Override
				public void onCancel(DialogInterface arg0) {
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
			return requestTask;
		}
		
		@Override
		public void onLoadFinished(Loader<MorlunkResponse> arg0,
				MorlunkResponse arg1) {
			if(arg0.getId() == LOGIN_LOADER_ID) {
				// Close loading dialog
				this.progressDialog.dismiss();

				if (arg1.result == MorlunkRequestResult.SUCCESS) {
					accountListener.onLoginSuccess(arg1);
				} else {
					showLoginAlert();
					accountListener.onLoginFailure(arg1);
				}
			}
		}

		@Override
		public void onLoaderReset(Loader<MorlunkResponse> arg0) {
			
		}
	}

	private static final String LOGIN_API_URL = "http://www.morlunk.com/account/login/json";
	private static final String LOGIN_PREFERENCES_FILE = "morlunk_login.xml";
	private static final String LOGIN_USERNAME_KEY = "username";
	private static final String LOGIN_PASSWORD_KEY = "password";
	
	private static final int LOGIN_LOADER_ID = 42;
	
	private Context context;
	private SharedPreferences loginPreferences;
	
	private static MorlunkAccountManager accountManager;
	
	/**
	 * Creates a new singleton instance of the account manager.
	 * Needs to be called once before getInstance can be used.
	 * @param context
	 * @param loaderManager
	 * @return
	 */
	public static MorlunkAccountManager createInstance(Context context) {
		accountManager = new MorlunkAccountManager(context);
		return accountManager;
	}
	
	/**
	 * Retrieves the singleton instance of MorlunkAccountManager. Returns null if createInstance was not called.
	 * @return
	 */
	public static MorlunkAccountManager getInstance() {
		return accountManager;
	}
	
	private MorlunkAccountManager(Context context) {
		this.context = context;
		this.loginPreferences = this.context.getSharedPreferences(LOGIN_PREFERENCES_FILE, 0);
	}
	
	/**
	 * Attempts to login with the stored username and password, if any.
	 */
	public void login(MorlunkAccountListener listener, Context context, LoaderManager loaderManager) {
		String username = loginPreferences.getString(LOGIN_USERNAME_KEY, "");
		String password = loginPreferences.getString(LOGIN_PASSWORD_KEY, "");
		login(username, password, false, listener, context, loaderManager);
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
	 * The credentials passed will also be stored in XML if remember is true.
	 * @param username
	 * @param password
	 * @param remember
	 * @return The authenticator object performing authentication.
	 */
	private MorlunkAuthenticator login(String username, String password, boolean remember, MorlunkAccountListener listener, Context context, LoaderManager loaderManager) {
		if(remember) {
			storeCredentials(username, password);
		}
		
		MorlunkAuthenticator authenticator = new MorlunkAuthenticator(listener, context, loaderManager);
		authenticator.start(username, password);
		return authenticator;
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
}
