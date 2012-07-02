package com.acomminos.morlunk;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;
import android.support.v4.content.Loader.OnLoadCompleteListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.acomminos.morlunk.http.MorlunkRequest;
import com.acomminos.morlunk.http.MorlunkRequest.MorlunkRequestType;
import com.acomminos.morlunk.http.MorlunkRequestTask;
import com.acomminos.morlunk.http.MorlunkResponse;
import com.acomminos.morlunk.http.MorlunkResponse.MorlunkRequestResult;
import com.acomminos.morlunk.http.response.MorlunkPage;
import com.acomminos.morlunk.http.response.MorlunkPageResponse;

public class MorlunkPageFragment extends Fragment implements OnLoadCompleteListener<MorlunkResponse> {
	
	private static final String PAGE_API_URL = "http://www.morlunk.com/page/";
	
	private MorlunkPage page;
	
	private String pageName;
	private WebView webView;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		// If the page name is passed as an argument, load it
		if(getArguments().containsKey("pageName")) {
			pageName = getArguments().getString("pageName");
		}
		
		// If the page name is found in a saved instance state, load it (usually after orientation change)
		if(savedInstanceState != null &&
				savedInstanceState.containsKey("pageName")) {
			pageName = savedInstanceState.getString("pageName");
		}
		
		// Load page data
		loadPage(pageName);
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("pageName", pageName); // Put page name in bundle
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_morlunk_page, container, false);
		webView = (WebView) view.findViewById(R.id.webview_morlunk_page);
		return view;
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}
	
	/**
	 * Loads the MorlunkPage required into the webview. The JSON parsing should go into its own class, TODO.
	 * @param pageName
	 */
	private void loadPage(String pageName) {
		MorlunkRequest request = new MorlunkRequest(PAGE_API_URL+pageName+"/json", MorlunkRequestType.REQUEST_GET, MorlunkPageResponse.class); // The format is morlunk.com/page/PAGE_NAME/json for API access
		MorlunkRequestTask task = new MorlunkRequestTask(getActivity(), request);
		task.registerListener(getId(), this);
		task.forceLoad();
	}

	@Override
	public void onLoadComplete(Loader<MorlunkResponse> loader,
			MorlunkResponse response) {
		if(response != null &&
				response.result == MorlunkRequestResult.SUCCESS) {
			// Get response object
			MorlunkPageResponse pageResponse = (MorlunkPageResponse) response;
			page = pageResponse.page;
			
			webView.loadData(page.pageBody, "text/html", "utf-8");
		} else {
			AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
			dialog.setTitle("Error");
			dialog.setMessage("Failed to communicate with Morlunk Co. servers!");
			dialog.setNeutralButton("Retry", new AlertDialog.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					loadPage(pageName);
				}
			});
			dialog.setNegativeButton("Cancel", null);
			dialog.create().show();
		}
	}
}
