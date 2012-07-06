package com.acomminos.morlunk.account.minecraft;

import java.util.List;

import com.acomminos.morlunk.R;
import com.acomminos.morlunk.http.MorlunkRequest;
import com.acomminos.morlunk.http.MorlunkRequestTask;
import com.acomminos.morlunk.http.MorlunkResponse;
import com.acomminos.morlunk.http.MorlunkRequest.MorlunkRequestType;

import android.content.Context;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.Loader.OnLoadCompleteListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

public class MorlunkMinecraftStoreFragment extends ListFragment implements OnLoadCompleteListener<MorlunkResponse> {
	
	private static final String STORE_BUY_API_URL = "http://www.morlunk.com/minecraft/store/buy/";
	
	private static final int BUY_REQUEST_LOADER_ID = 0;
	
	protected void buyItem(MorlunkMinecraftStoreItem item) {
		MorlunkRequest request = new MorlunkRequest(STORE_BUY_API_URL, MorlunkRequestType.REQUEST_GET, MorlunkResponse.class);
		MorlunkRequestTask task = new MorlunkRequestTask(getActivity(), request);
		LoaderManager loaderManager = getLoaderManager();
		loaderManager.initLoader(BUY_REQUEST_LOADER_ID, null, this);
	}
	
	@Override
	public void onLoadComplete(Loader<MorlunkResponse> arg0,
			MorlunkResponse arg1) {
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
