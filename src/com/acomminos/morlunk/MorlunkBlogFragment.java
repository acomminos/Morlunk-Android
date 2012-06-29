package com.acomminos.morlunk;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MorlunkBlogFragment extends ListFragment {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		loadPosts();
	}
	
	/**
	 * Loads initial blog posts.
	 */
	public void loadPosts() {
		List<MorlunkBlogPost> blogPosts = new ArrayList<MorlunkBlogPost>();
		MorlunkBlogPost post = new MorlunkBlogPost();
		post.setPostTitle("Title!");
		post.setPostDescription("Description!");
		post.setPostBody("Body!");
		blogPosts.add(post);
		setListAdapter(new MorlunkBlogArrayAdapter(getActivity(), blogPosts));
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
