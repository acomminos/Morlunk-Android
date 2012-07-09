package com.acomminos.morlunk;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import com.acomminos.morlunk.http.MorlunkRequest;
import com.acomminos.morlunk.http.MorlunkRequest.MorlunkRequestType;
import com.acomminos.morlunk.http.MorlunkRequestLoader;
import com.acomminos.morlunk.http.MorlunkResponse;
import com.acomminos.morlunk.http.MorlunkResponse.MorlunkRequestResult;
import com.acomminos.morlunk.http.response.MorlunkBlogPost;
import com.acomminos.morlunk.http.response.MorlunkBlogResponse;

public class MorlunkBlogFragment extends ListFragment implements LoaderCallbacks<MorlunkResponse>, OnItemClickListener {
	
	interface MorlunkBlogFragmentListener {
		public void onBlogPostSelected(MorlunkBlogPost post);
	}
	
	private static final String BLOG_API_URL = "http://www.morlunk.com/blog/json";
	
	private static final int BLOG_LOADER_ID = 0;
	
	private MorlunkBlogFragmentListener blogFragmentListener;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {;
		super.onActivityCreated(savedInstanceState);
		//setRetainInstance(true); // Retain fragment so it doesn't reload on orientation change
		loadPosts(false);
		getListView().setOnItemClickListener(this);
	}
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // This is google's way of adding a callback to an activity.
        try {
            blogFragmentListener = (MorlunkBlogFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement MorlunkBlogFragmentListener");
        }
    }
	
	/**
	 * Loads initial blog posts.
	 */
	public void loadPosts(boolean reload) {
		LoaderManager loaderManager = getLoaderManager();
		if(reload) {
			loaderManager.restartLoader(BLOG_LOADER_ID, null, this);
		} else {
			loaderManager.initLoader(BLOG_LOADER_ID, null, this);
		}
	}

	@Override
	public Loader<MorlunkResponse> onCreateLoader(int arg0, Bundle arg1) {
		MorlunkRequest request = new MorlunkRequest(BLOG_API_URL, MorlunkRequestType.REQUEST_GET, MorlunkBlogResponse.class);
		MorlunkRequestLoader requestTask = new MorlunkRequestLoader(getActivity(), request);
		requestTask.forceLoad(); // Not sure why this is needed. But it doesn't work without it. TODO fix. -AC
		return requestTask;
	}

	@Override
	public void onLoadFinished(Loader<MorlunkResponse> loader,
			MorlunkResponse response) {
		if(response != null &&
				response.result == MorlunkRequestResult.SUCCESS) {
			MorlunkBlogResponse blogResponse = (MorlunkBlogResponse) response;
			List<MorlunkBlogPost> posts = blogResponse.posts;
			MorlunkBlogArrayAdapter adapter = new MorlunkBlogArrayAdapter(getActivity(), posts);
			setListAdapter(adapter);
		} else {
			AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
			dialog.setTitle("Error");
			dialog.setMessage("Failed to communicate with Morlunk Co. servers!");
			dialog.setNeutralButton("Retry", new AlertDialog.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					loadPosts(true);
				}
			});
			dialog.setNegativeButton("Cancel", null);
			dialog.create().show();
		}
	}

	@Override
	public void onLoaderReset(Loader<MorlunkResponse> arg0) {
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		MorlunkBlogPost post = (MorlunkBlogPost) getListView().getItemAtPosition(arg2);
		blogFragmentListener.onBlogPostSelected(post);
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
			
			title.setText(post.postTitle);
			description.setText(post.postDescription);
			
			return itemView;
		}
		
	}
}
