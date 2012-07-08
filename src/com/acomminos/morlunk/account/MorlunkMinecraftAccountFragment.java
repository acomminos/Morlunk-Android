package com.acomminos.morlunk.account;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.acomminos.morlunk.MorlunkLoadingHandler;
import com.acomminos.morlunk.R;
import com.acomminos.morlunk.account.MorlunkAccountManager.MorlunkAccountListener;
import com.acomminos.morlunk.http.MorlunkRequest;
import com.acomminos.morlunk.http.MorlunkRequest.MorlunkRequestType;
import com.acomminos.morlunk.http.MorlunkRequestTask;
import com.acomminos.morlunk.http.MorlunkResponse;
import com.acomminos.morlunk.http.MorlunkResponse.MorlunkRequestResult;
import com.acomminos.morlunk.http.response.MorlunkMinecraftAccountResponse;

public class MorlunkMinecraftAccountFragment extends ListFragment implements OnItemClickListener, LoaderCallbacks<MorlunkResponse>, MorlunkAccountListener {
	
	private static final String MINECRAFT_ACCOUNT_API_URL = "http://www.morlunk.com/minecraft/account/json";
	private static final int MINECRAFT_ACCOUNT_LOADER_ID = 235; // RANDINT!
	
	String[] listItems = {
			"Morlunk Co. Store",
			"Paoso Conversion Rates",
			"Redeem Paoso Coupon"
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listItems);
		setListAdapter(arrayAdapter);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View minecraftView = inflater.inflate(R.layout.fragment_minecraft_account, container, false);
		return minecraftView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		getListView().setOnItemClickListener(this);
		loadMinecraftAccount();
	}
	
	/**
	 * Loads the user's minecraft account and populates the various fields of the view.
	 */
	private void loadMinecraftAccount() {
		LoaderManager loaderManager = getLoaderManager();
		loaderManager.initLoader(MINECRAFT_ACCOUNT_LOADER_ID, null, this);
	}
	
	/**
	 * Show login alert and sets up hooks.
	 */
	private void showLoginAlert() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
		alertDialog.setTitle("Login to Morlunk Co.");
		View alertView = getLayoutInflater(null).inflate(R.layout.account_text_alert, null);
		final EditText usernameField = (EditText) alertView.findViewById(R.id.account_alert_username);
		final EditText passwordField = (EditText) alertView.findViewById(R.id.account_alert_password);
		
		alertDialog.setView(alertView);
		alertDialog.setPositiveButton("Login", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				MorlunkAccountManager accountManager = new MorlunkAccountManager(getActivity());
				accountManager.setAccountListener(MorlunkMinecraftAccountFragment.this);
				accountManager.login(usernameField.getText().toString(), passwordField.getText().toString());
			}
		});
		alertDialog.setNegativeButton("Cancel", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				getActivity().finish(); // Quit account activity TODO make cleaner
			}
		});
		alertDialog.show();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		switch (position) {
		case 0:
		{
			
		}
			break;
		case 1:
		{
			
		}
			break;
		case 2:
		{
			
		}
			break;
		}
	}

	@Override
	public Loader<MorlunkResponse> onCreateLoader(int arg0, Bundle arg1) {
		// Show loading screen
		MorlunkLoadingHandler loadingHandler = (MorlunkLoadingHandler) getActivity(); // TODO make cleaner
		loadingHandler.showLoadingScreen();
		
		MorlunkRequest request = new MorlunkRequest(MINECRAFT_ACCOUNT_API_URL, MorlunkRequestType.REQUEST_GET, MorlunkMinecraftAccountResponse.class);
		MorlunkRequestTask task = new MorlunkRequestTask(getActivity(), request);
		task.forceLoad(); // TODO fix this
		return task;
	}

	@Override
	public void onLoadFinished(Loader<MorlunkResponse> arg0,
			MorlunkResponse arg1) {
		
		if(arg1.result == MorlunkRequestResult.SUCCESS) {
			// Populate fields
			MorlunkMinecraftAccountResponse accountResponse = (MorlunkMinecraftAccountResponse) arg1;
			TextView accountView = (TextView) getView().findViewById(R.id.minecraft_account_text);
			accountView.setText(accountResponse.account.minecraftUsername);
			TextView paososView = (TextView) getView().findViewById(R.id.minecraft_account_paosos);
			paososView.setText("$"+accountResponse.account.paosos+" Paosos");
			
			// Hide loading screen
			MorlunkLoadingHandler loadingHandler = (MorlunkLoadingHandler) getActivity(); // TODO make cleaner
			loadingHandler.dismissLoadingScreen();
		} else if(arg1.result == MorlunkRequestResult.NO_USER) {
			// No minecraft user
		} else if(arg1.result == MorlunkRequestResult.NOT_AUTHENTICATED) {
			// Popup with login field
			showLoginAlert();
		} else {
			// Popup with generic error
		}
	}

	@Override
	public void onLoaderReset(Loader<MorlunkResponse> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void authenticationSuccess(MorlunkResponse response) {
		loadMinecraftAccount(); // Reload minecraft account
	}

	@Override
	public void authenticationFailure(MorlunkResponse response) {
		showLoginAlert(); // Reshow after failed attempt
	}

}
