package com.acomminos.morlunk.account.minecraft;

import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.acomminos.morlunk.R;
import com.acomminos.morlunk.account.MorlunkAccountManager;
import com.acomminos.morlunk.account.MorlunkAccountManager.MorlunkAccountListener;
import com.acomminos.morlunk.http.MorlunkRequest;
import com.acomminos.morlunk.http.MorlunkRequest.MorlunkRequestType;
import com.acomminos.morlunk.http.MorlunkRequestLoader;
import com.acomminos.morlunk.http.MorlunkResponse;
import com.acomminos.morlunk.http.MorlunkResponse.MorlunkRequestResult;
import com.acomminos.morlunk.http.response.MorlunkMinecraftAccount;
import com.acomminos.morlunk.http.response.MorlunkMinecraftAccountResponse;
import com.acomminos.morlunk.http.response.MorlunkMinecraftStashItem;
import com.acomminos.morlunk.http.response.MorlunkMinecraftStashResponse;

public class MorlunkMinecraftAccountFragment extends Fragment implements LoaderCallbacks<MorlunkResponse>, MorlunkAccountListener {
	
	private static final String MINECRAFT_ACCOUNT_API_URL = "http://www.morlunk.com/minecraft/account/json";
	private static final String MINECRAFT_STASH_API_URL = "http://www.morlunk.com/minecraft/stash/get";
	private static final int MINECRAFT_ACCOUNT_LOADER_ID = 235;
	private static final int MINECRAFT_STASH_LOADER_ID = 236;
	
	private MorlunkMinecraftAccount account;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		loadMinecraftAccount(false);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
	
	private void loadMinecraftStash(boolean reload) {
		LoaderManager loaderManager = getLoaderManager();
		if(reload) {
			loaderManager.restartLoader(MINECRAFT_STASH_LOADER_ID, null, this);
		} else {
			loaderManager.initLoader(MINECRAFT_STASH_LOADER_ID, null, this);
		}
	}
	
	@Override
	public Loader<MorlunkResponse> onCreateLoader(int arg0, Bundle arg1) {
		if(arg0 == MINECRAFT_ACCOUNT_LOADER_ID) {
			MorlunkRequest request = new MorlunkRequest(MINECRAFT_ACCOUNT_API_URL, MorlunkRequestType.REQUEST_GET, MorlunkMinecraftAccountResponse.class);
			MorlunkRequestLoader task = new MorlunkRequestLoader(getActivity(), request);
			return task;
		} else if(arg0 == MINECRAFT_STASH_LOADER_ID) {
			MorlunkRequest request = new MorlunkRequest(MINECRAFT_STASH_API_URL, MorlunkRequestType.REQUEST_GET, MorlunkMinecraftStashResponse.class);
			request.addArgument("username", account.minecraftUsername);
			MorlunkRequestLoader task = new MorlunkRequestLoader(getActivity(), request);
			return task;
		}
		return null;
	}

	@Override
	public void onLoadFinished(Loader<MorlunkResponse> arg0,
			MorlunkResponse arg1) {
		if(arg0.getId() == MINECRAFT_ACCOUNT_LOADER_ID) {
			if(arg1.result == MorlunkRequestResult.SUCCESS) {
				// Populate fields
				MorlunkMinecraftAccountResponse accountResponse = (MorlunkMinecraftAccountResponse) arg1;
				TextView accountView = (TextView) getView().findViewById(R.id.minecraft_account_text);
				accountView.setText(accountResponse.account.minecraftUsername);
				TextView paososView = (TextView) getView().findViewById(R.id.minecraft_account_paosos);
				paososView.setText(accountResponse.account.paosos+"P");
				
				account = accountResponse.account;
				
				// Load stash now
				loadMinecraftStash(false);
			} else if(arg1.result == MorlunkRequestResult.NO_USER) {
				// No minecraft user
			} else if(arg1.result == MorlunkRequestResult.NOT_AUTHENTICATED) {
				// Login, the minecraft account will be retrieved after initial auth
				MorlunkAccountManager.getInstance().login(this, getActivity(), getLoaderManager());
			}
		} else if(arg0.getId() == MINECRAFT_STASH_LOADER_ID) {
			if(arg1.result == MorlunkRequestResult.SUCCESS) {
				MorlunkMinecraftStashResponse stashResponse = (MorlunkMinecraftStashResponse) arg1;
				// Populate items
				GridView gridView = (GridView) getView().findViewById(R.id.inventory_gridview);
				gridView.setAdapter(new MorlunkMinecraftStashAdapter(getActivity(), stashResponse.stash.items));
			}
		}
		
		if(arg1.result == MorlunkRequestResult.NO_MINECRAFT_ACCOUNT) {
			// Boot them out if they don't have a minecraft account
			Toast.makeText(getActivity(), "You need to link a Minecraft account at Morlunk.com to use Minecraft services.", Toast.LENGTH_SHORT).show();
			getActivity().finish();
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
	
	class MorlunkMinecraftStashAdapter extends ArrayAdapter<MorlunkMinecraftStashItem> {

		public MorlunkMinecraftStashAdapter(Context context, List<MorlunkMinecraftStashItem> objects) {
			super(context, 0, objects);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View itemView = convertView;
			if(itemView == null) {
				LayoutInflater inflater = getLayoutInflater(null);
				itemView = inflater.inflate(R.layout.list_item_stash, parent, false);
			}
			
			MorlunkMinecraftStashItem item = getItem(position);
			
			TextView titleView = (TextView)itemView.findViewById(R.id.stash_item_name);
			titleView.setText(item.name);
			
			TextView quantityView = (TextView)itemView.findViewById(R.id.stash_item_amount);
			quantityView.setText("Quantity: "+item.amount);
			
			return itemView;
		}
		
	}
}
