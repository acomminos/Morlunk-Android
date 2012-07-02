package com.acomminos.morlunk;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.content.Loader;
import android.support.v4.content.Loader.OnLoadCompleteListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.acomminos.morlunk.http.MorlunkRequest;
import com.acomminos.morlunk.http.MorlunkRequestTask;
import com.acomminos.morlunk.http.MorlunkResponse;

public class MorlunkBlogFragment extends ListFragment implements OnLoadCompleteListener<MorlunkResponse> {
	
	private static final String BLOG_API_URL = "http://www.morlunk.com/blog/json";
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {;
		super.onActivityCreated(savedInstanceState);
		//setRetainInstance(true); // Retain fragment so it doesn't reload on orientation change
		loadPosts();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	/**
	 * Loads initial blog posts.
	 */
	public void loadPosts() {
		MorlunkRequest request = new MorlunkRequest(BLOG_API_URL);
		MorlunkRequestTask requestTask = new MorlunkRequestTask(getActivity(), request);
		requestTask.registerListener(requestTask.getId(), this);
		requestTask.forceLoad();
	}
	
	@Override
	public void onLoadComplete(Loader<MorlunkResponse> loader,
			MorlunkResponse response) {
		if(response != null) {
			// TODO load
			// Configure list adapter and parse JSON posts
			List<MorlunkBlogPost> blogPosts = new ArrayList<MorlunkBlogPost>();
			/*
			try {
				// TODO parse JSON in other class
				JsonArray posts = response.getResponseJSON().("posts");
				
				Gson gson = new Gson();
				
				gson.fromJson(json, classOfT)
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
			
			setListAdapter(new MorlunkBlogArrayAdapter(getActivity(), blogPosts));
		} else {
			AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
			dialog.setTitle("Error");
			dialog.setMessage("Failed to communicate with Morlunk Co. servers!");
			dialog.setNeutralButton("Retry", new AlertDialog.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					loadPosts();
				}
			});
			dialog.setNegativeButton("Cancel", null);
			dialog.create().show();
		}
	}
	
	class MorlunkBlogArrayAdapter extends ArrayAdapter<MorlunkBlogPost> {

		public MorlunkBlogArrayAdapter(Context context, List<MorlunkBlogPost> objects) {
			super(context, 0, objects);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View itemView = convertView;
			if(itemView == null) {
				LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				itemView = inflater.inflate(R.layout.list_item_blog, parent, false);
			}
			
			// Get blog item
			MorlunkBlogPost post = getItem(position);
			
			TextView title = (TextView) itemView.findViewById(R.id.title);
			TextView description = (TextView) itemView.findViewById(R.id.description);
			
			title.setText(post.getPostTitle());
			description.setText(post.getPostDescription());
			
			return itemView;
		}
		
	}
}
