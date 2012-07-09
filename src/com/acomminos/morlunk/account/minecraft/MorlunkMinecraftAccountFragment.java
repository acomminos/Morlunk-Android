package com.acomminos.morlunk.account.minecraft;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.acomminos.morlunk.R;
import com.acomminos.morlunk.account.MorlunkAccountManager;
import com.acomminos.morlunk.account.MorlunkAccountManager.MorlunkAccountListener;
import com.acomminos.morlunk.http.MorlunkRequest;
import com.acomminos.morlunk.http.MorlunkRequest.MorlunkRequestType;
import com.acomminos.morlunk.http.MorlunkRequestLoader;
import com.acomminos.morlunk.http.MorlunkResponse;
import com.acomminos.morlunk.http.MorlunkResponse.MorlunkRequestResult;
import com.acomminos.morlunk.http.response.MorlunkMinecraftAccountResponse;

public class MorlunkMinecraftAccountFragment extends Fragment implements LoaderCallbacks<MorlunkResponse>, MorlunkAccountListener {
	
	private static final String MINECRAFT_ACCOUNT_API_URL = "http://www.morlunk.com/minecraft/account/json";
	private static final int MINECRAFT_ACCOUNT_LOADER_ID = 235; // RANDINT!
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		MorlunkAccountManager.getInstance().registerAccountListener(this);
		loadMinecraftAccount(false);
	}
	
	@Override
	public void onDestroy() {
		MorlunkAccountManager.getInstance().unregisterAccountListener(this);
		super.onDestroy();
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
		
		// Set custom username font for title
		Typeface mineType = Typeface.createFromAsset(getActivity().getAssets(),"fonts/minecraft_font.ttf"); 
		((TextView)getView().findViewById(R.id.minecraft_account_text)).setTypeface(mineType);
	}
	
	/**
	 * Loads the user's minecraft account and populates the various fields of the view.
	 */
	private void loadMinecraftAccount(boolean reload) {
		LoaderManager loaderManager = getLoaderManager();
		if(reload) {
			loaderManager.restartLoader(MINECRAFT_ACCOUNT_LOADER_ID, null, this);
		} else {
			loaderManager.initLoader(MINECRAFT_ACCOUNT_LOADER_ID, null, this);
		}
	}
	
	@Override
	public Loader<MorlunkResponse> onCreateLoader(int arg0, Bundle arg1) {
		MorlunkRequest request = new MorlunkRequest(MINECRAFT_ACCOUNT_API_URL, MorlunkRequestType.REQUEST_GET, MorlunkMinecraftAccountResponse.class);
		MorlunkRequestLoader task = new MorlunkRequestLoader(getActivity(), request);
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
		} else if(arg1.result == MorlunkRequestResult.NO_USER) {
			// No minecraft user
		} else if(arg1.result == MorlunkRequestResult.NOT_AUTHENTICATED) {
			// Login, the minecraft account will be retrieved after initial auth
			MorlunkAccountManager.getInstance().login();
		} else {
			// Popup with generic error
		}
	}


	@Override
	public void onLoaderReset(Loader<MorlunkResponse> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoginSuccess(MorlunkResponse response) {
		loadMinecraftAccount(true); // Load minecraft account after account authentication
	}

	@Override
	public void onLoginFailure(MorlunkResponse response) {
		
	}
	
	@Override
	public void onLoginCancel() {
		getActivity().finish();
	}
}
