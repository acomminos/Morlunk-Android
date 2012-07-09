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

import com.acomminos.morlunk.R;
import com.acomminos.morlunk.http.MorlunkRequest;
import com.acomminos.morlunk.http.MorlunkRequest.MorlunkRequestType;
import com.acomminos.morlunk.http.MorlunkRequestLoader;
import com.acomminos.morlunk.http.MorlunkResponse;

public class MorlunkMinecraftStoreFragment extends ListFragment implements LoaderCallbacks<MorlunkResponse> {
	
	private static final String STORE_BUY_API_URL = "http://www.morlunk.com/minecraft/store/buy/";
	private static final String ITEM_ID_KEY = "item_id";
	
	private static final int BUY_REQUEST_LOADER_ID = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	protected void buyItem(MorlunkMinecraftStoreItem item) {
		Bundle arguments = new Bundle();
		arguments.putInt(ITEM_ID_KEY, item.id);
		LoaderManager loaderManager = getLoaderManager();
		loaderManager.initLoader(BUY_REQUEST_LOADER_ID, arguments, this);
	}
	
	@Override
	public Loader<MorlunkResponse> onCreateLoader(int arg0, Bundle arg1) {
		MorlunkRequest request = new MorlunkRequest(STORE_BUY_API_URL, MorlunkRequestType.REQUEST_GET, MorlunkResponse.class);
		request.addArgument(ITEM_ID_KEY, String.valueOf(arg1.getInt(ITEM_ID_KEY)));
		MorlunkRequestLoader task = new MorlunkRequestLoader(getActivity(), request);
		return task;
	}


	@Override
	public void onLoadFinished(Loader<MorlunkResponse> arg0,
			MorlunkResponse arg1) {
		// TODO Auto-generated method stub
		
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

}
