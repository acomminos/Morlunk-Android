package com.acomminos.morlunk.account;

import android.content.Context;
import android.graphics.Typeface;
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
import android.widget.TextView;

import com.acomminos.morlunk.MorlunkLoadingHandler;
import com.acomminos.morlunk.R;
import com.acomminos.morlunk.account.MorlunkAccountManager.MorlunkAccountListener;
import com.acomminos.morlunk.http.MorlunkRequest;
import com.acomminos.morlunk.http.MorlunkRequest.MorlunkRequestType;
import com.acomminos.morlunk.http.MorlunkRequestLoader;
import com.acomminos.morlunk.http.MorlunkResponse;
import com.acomminos.morlunk.http.MorlunkResponse.MorlunkRequestResult;
import com.acomminos.morlunk.http.response.MorlunkMinecraftAccountResponse;

public class MorlunkMinecraftAccountFragment extends ListFragment implements OnItemClickListener, LoaderCallbacks<MorlunkResponse>, MorlunkAccountListener {
	
	private static final String MINECRAFT_ACCOUNT_API_URL = "http://www.morlunk.com/minecraft/account/json";
	private static final int MINECRAFT_ACCOUNT_LOADER_ID = 235; // RANDINT!
	
	String[] listItems = {
			"Morlunk Co. Store",
			"Paoso Conversion Rates",
			"Redeem Paoso Coupon",
			"Buy Paosos"
	};
	
	private MorlunkAccountManager accountManager;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		accountManager = new MorlunkAccountManager(getActivity(), getLoaderManager());
		accountManager.setAccountListener(MorlunkMinecraftAccountFragment.this);
		
		loadMinecraftAccount(false);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MorlunkMinecraftArrayAdapter arrayAdapter = new MorlunkMinecraftArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, listItems);
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
		case 3:
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
			accountManager.login();
		} else {
			// Popup with generic error
		}
		
		// Hide loading screen
		MorlunkLoadingHandler loadingHandler = (MorlunkLoadingHandler) getActivity(); // TODO make cleaner
		loadingHandler.dismissLoadingScreen();
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
	
	class MorlunkMinecraftArrayAdapter extends ArrayAdapter<String> {

		public MorlunkMinecraftArrayAdapter(Context context,
				int textViewResourceId, String[] objects) {
			super(context, textViewResourceId, objects);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = super.getView(position, convertView, parent);
			
			// Don't allow buying paosos yet
			if(position == 3) {
				view.setEnabled(false);
			}
			
			return view;
		}
		
	}

}
