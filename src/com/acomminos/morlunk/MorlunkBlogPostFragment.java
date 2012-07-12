package com.acomminos.morlunk;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.acomminos.morlunk.http.response.MorlunkBlogPost;
import com.actionbarsherlock.app.SherlockFragment;

public class MorlunkBlogPostFragment extends SherlockFragment {

	private MorlunkBlogPost post;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(savedInstanceState != null &&
				savedInstanceState.containsKey("post")) {
			this.post = savedInstanceState.getParcelable("post");
		}
		
		if(getArguments().containsKey("post")) {
			this.post = getArguments().getParcelable("post");
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View postView = inflater.inflate(R.layout.fragment_morlunk_blog_post, container, false); // Do NOT attach to root view so we can replace the fragment later.
		
		TextView title = (TextView) postView.findViewById(R.id.post_title);
		TextView description = (TextView) postView.findViewById(R.id.post_description);
		TextView body = (TextView) postView.findViewById(R.id.post_body);
		
		title.setText(post.postTitle);
		description.setText(post.postDescription);
		body.setText(Html.fromHtml(post.postBody));
		
		return postView;
	}
}
