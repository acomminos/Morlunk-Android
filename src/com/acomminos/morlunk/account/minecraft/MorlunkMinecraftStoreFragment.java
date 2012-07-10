package com.acomminos.morlunk.account.minecraft;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.acomminos.morlunk.R;
import com.acomminos.morlunk.account.MorlunkAccountManager;
import com.acomminos.morlunk.account.MorlunkAccountManager.MorlunkAccountListener;
import com.acomminos.morlunk.http.MorlunkRequest;
import com.acomminos.morlunk.http.MorlunkRequest.MorlunkRequestType;
import com.acomminos.morlunk.http.MorlunkResponse.MorlunkRequestResult;
import com.acomminos.morlunk.http.response.MorlunkMinecraftStoreItem;
import com.acomminos.morlunk.http.response.MorlunkMinecraftStoreResponse;
import com.acomminos.morlunk.http.MorlunkRequestLoader;
import com.acomminos.morlunk.http.MorlunkResponse;

public class MorlunkMinecraftStoreFragment extends ListFragment implements LoaderCallbacks<MorlunkResponse>, MorlunkAccountListener {
	
	private static final String STORE_BUY_API_URL = "http://www.morlunk.com/minecraft/store/buy/";
	private static final String STORE_ITEMS_API_URL = "http://www.morlunk.com/minecraft/store/json";
	private static final String ITEM_ID_KEY = "item";
	
	private static final int BUY_REQUEST_LOADER_ID = 716;
	private static final int ITEMS_REQUEST_LOADER_ID = 251;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		loadItems(false);
	}
	
	private void loadItems(boolean refresh) {
		LoaderManager loaderManager = getLoaderManager();
		if(refresh) {
			loaderManager.restartLoader(ITEMS_REQUEST_LOADER_ID, null, this);
		} else {
			loaderManager.initLoader(ITEMS_REQUEST_LOADER_ID, null, this);
		}
	}
	
	protected void buyItem(MorlunkMinecraftStoreItem item) {
		Bundle arguments = new Bundle();
		arguments.putInt(ITEM_ID_KEY, item.id);
		LoaderManager loaderManager = getLoaderManager();
		loaderManager.restartLoader(BUY_REQUEST_LOADER_ID, arguments, this);
	}
	
	@Override
	public Loader<MorlunkResponse> onCreateLoader(int arg0, Bundle arg1) {
		if (arg0 == ITEMS_REQUEST_LOADER_ID) {
			MorlunkRequest request = new MorlunkRequest(STORE_ITEMS_API_URL,
					MorlunkRequestType.REQUEST_GET, MorlunkMinecraftStoreResponse.class);
			MorlunkRequestLoader task = new MorlunkRequestLoader(getActivity(),
					request);
			return task;
		} else if (arg0 == BUY_REQUEST_LOADER_ID) {
			MorlunkRequest request = new MorlunkRequest(STORE_BUY_API_URL,
					MorlunkRequestType.REQUEST_GET, MorlunkResponse.class);
			request.addArgument(ITEM_ID_KEY,
					String.valueOf(arg1.getInt(ITEM_ID_KEY)));
			MorlunkRequestLoader task = new MorlunkRequestLoader(getActivity(),
					request);
			return task;
		}
		return null;
	}


	@Override
	public void onLoadFinished(Loader<MorlunkResponse> arg0,
			MorlunkResponse arg1) {
		if(arg0.getId() == ITEMS_REQUEST_LOADER_ID) {
			if(arg1.result == MorlunkRequestResult.SUCCESS) {
				MorlunkMinecraftStoreResponse storeResponse = (MorlunkMinecraftStoreResponse) arg1;
				setListAdapter(new MorlunkMinecraftStoreAdapter(getActivity(), storeResponse.items));
			} else {
				// TODO popup error
			}
		} else if(arg0.getId() == BUY_REQUEST_LOADER_ID) {
			if(arg1.result == MorlunkRequestResult.SUCCESS) {
				Toast.makeText(getActivity(), "Success! Your item has been sent to your stash.", Toast.LENGTH_SHORT).show();
			} else if(arg1.result == MorlunkRequestResult.NOT_AUTHENTICATED) {
				MorlunkAccountManager.getInstance().login(this, getActivity(), getLoaderManager());
			} else if(arg1.result == MorlunkRequestResult.INSUFFICIENT_FUNDS) {
				Toast.makeText(getActivity(), "You don't have enough Paosos!", Toast.LENGTH_SHORT).show();
			} else if(arg1.result == MorlunkRequestResult.NO_MINECRAFT_ACCOUNT) {
				// Boot them out if they don't have a minecraft account
				Toast.makeText(getActivity(), "You need to link a Minecraft account at Morlunk.com to use Minecraft services.", Toast.LENGTH_SHORT).show();
				getActivity().finish();
			}
		}
	}


	@Override
	public void onLoaderReset(Loader<MorlunkResponse> arg0) {
		// TODO Auto-generated method stub
		
	}
	
	class MorlunkMinecraftStoreAdapter extends ArrayAdapter<MorlunkMinecraftStoreItem> {

		public MorlunkMinecraftStoreAdapter(Context context, List<MorlunkMinecraftStoreItem> objects) {
			super(context, 0, objects);
		}
		
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View itemView = convertView;
			
			if(itemView == null) {
				LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				itemView = inflater.inflate(R.layout.list_item_store, parent, false);
			}
			
			final MorlunkMinecraftStoreItem item = getItem(position);
			
			TextView titleView = (TextView) itemView.findViewById(R.id.store_item_name);
			titleView.setText(item.name);
			TextView quantityView = (TextView) itemView.findViewById(R.id.store_item_quantity);
			quantityView.setText("Quantity: "+item.buySellQuantity);
			Button buyButton = (Button) itemView.findViewById(R.id.store_buy);
			buyButton.setText(item.buyValue+"P");
			buyButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					buyItem(item);
				}
			});
			
			return itemView;
		}
	}

	@Override
	public void onLoginSuccess(MorlunkResponse response) {
		Toast.makeText(getActivity(), "Authenticated. Please retry your purchase.", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onLoginFailure(MorlunkResponse response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoginCancel() {
		// TODO Auto-generated method stub
		
	}

}
