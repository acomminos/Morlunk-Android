package com.acomminos.morlunk.http;

import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import com.acomminos.morlunk.http.MorlunkResponse.MorlunkRequestResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import android.webkit.CookieSyncManager;

public class MorlunkRequestTask extends AsyncTaskLoader<MorlunkResponse>{
	
	private MorlunkRequest morlunkRequest;
	
	public MorlunkRequestTask(Context context, MorlunkRequest request) {
		super(context);
		this.morlunkRequest = request;
	}
	
	@Override
	public MorlunkResponse loadInBackground() {
		// Instantiate CookieSyncManager if not already
		CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(getContext());
		CookieStore cookieStore = new BasicCookieStore();
		
		HttpContext context = new BasicHttpContext();
	    context.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
		
		HttpClient client = new DefaultHttpClient();
		HttpUriRequest request = null;
		try {
			Log.i("HTTP", "Loading URL "+morlunkRequest.getUrl());
			switch (morlunkRequest.getRequestType()) {
			case REQUEST_GET:
				request = morlunkRequest.makeGetRequest();
				break;
			case REQUEST_POST:
				request = morlunkRequest.makePostRequest();
				break;
			}
			ResponseHandler<String> handler = new BasicResponseHandler();
			HttpResponse response = client.execute(request, context);
			cookieSyncManager.sync(); // Sync cookies
			
			String jsonResponse = handler.handleResponse(response);
			
			// Begin parsing with GSON
			GsonBuilder gsonBuilder = new GsonBuilder();
		    gsonBuilder.registerTypeAdapter(MorlunkRequestResult.class, new MorlunkRequestResultDeserializer());
		    Gson gson = gsonBuilder.create();
		    
		    MorlunkResponse morlunkResponse = gson.fromJson(jsonResponse, morlunkRequest.getResponseClass());
			return morlunkResponse;
		} catch (Exception e) {
			// TODO: handle exception and return a generic error.
			e.printStackTrace();
			return null;
		}
	}
}
