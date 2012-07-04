package com.acomminos.morlunk.account;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;

import com.acomminos.morlunk.R;

public class MorlunkMinecraftAccountFragment extends ListFragment implements OnItemClickListener {
	
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

}
