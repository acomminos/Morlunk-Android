package com.acomminos.morlunk;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.acomminos.morlunk.http.MorlunkRequest;
import com.acomminos.morlunk.http.MorlunkRequest.MorlunkRequestType;
import com.acomminos.morlunk.http.MorlunkRequestLoader;
import com.acomminos.morlunk.http.MorlunkResponse;
import com.acomminos.morlunk.http.MorlunkResponse.MorlunkRequestResult;
import com.acomminos.morlunk.http.response.MorlunkPage;
import com.acomminos.morlunk.http.response.MorlunkPageResponse;

public class MorlunkPageFragment extends Fragment implements LoaderCallbacks<MorlunkResponse> {
	
	private static final String PAGE_API_URL = "http://www.morlunk.com/page/";
	
	private static final int PAGE_LOADER_ID = 1;
	
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
		loadPage(pageName, false);
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
		return view;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		webView = (WebView) view.findViewById(R.id.webview_morlunk_page);
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}
	
	/**
	 * Loads the MorlunkPage required into the webview. The JSON parsing should go into its own class, TODO.
	 * @param pageName
	 */
	private void loadPage(String pageName, boolean reload) {
		getActivity().setProgressBarIndeterminateVisibility(true);
		LoaderManager loaderManager = getLoaderManager();
		if(reload) {
			loaderManager.restartLoader(PAGE_LOADER_ID, null, this);
		} else {
			loaderManager.initLoader(PAGE_LOADER_ID, null, this);
		}
	}
	
	@Override
	public Loader<MorlunkResponse> onCreateLoader(int arg0, Bundle arg1) {
		MorlunkRequest request = new MorlunkRequest(PAGE_API_URL+pageName+"/json", MorlunkRequestType.REQUEST_GET, MorlunkPageResponse.class);
		MorlunkRequestLoader loader = new MorlunkRequestLoader(getActivity(), request);
		loader.forceLoad(); // Not sure why this is needed. But it doesn't work without it. TODO fix. -AC
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<MorlunkResponse> loader,
			MorlunkResponse response) {
		getActivity().setProgressBarIndeterminateVisibility(false);
		if(response != null &&
				response.result == MorlunkRequestResult.SUCCESS) {
			// Get response object
			MorlunkPageResponse pageResponse = (MorlunkPageResponse) response;
			page = pageResponse.page;

			// Load holo.light stylesheet, morlunk stylesheet, as well as html
			String htmlData = "<link rel=\"stylesheet\" type=\"text/css\" href=\"http://www.morlunk.com/static/css/stylesheet.css\" />"+
					"<link rel=\"stylesheet\" type=\"text/css\" href=\"http://www.morlunk.com/static/css/stylesheet_mobile.css\" />"+
					"<link rel=\"stylesheet\" type=\"text/css\" href=\"holo-light.css\" />" + page.pageBody;
			webView.loadDataWithBaseURL("file:///android_asset/", htmlData, "text/html", "UTF-8", null);
		} else {
			AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
			dialog.setTitle("Error");
			dialog.setMessage("Failed to communicate with Morlunk Co. servers!");
			dialog.setNeutralButton("Retry", new AlertDialog.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					loadPage(pageName, true);
				}
			});
			dialog.setNegativeButton("Cancel", null);
			dialog.create().show();
		}
	}

	@Override
	public void onLoaderReset(Loader<MorlunkResponse> arg0) {
		// TODO Auto-generated method stub
		
	}
}
