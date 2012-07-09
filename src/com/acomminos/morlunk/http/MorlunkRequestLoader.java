package com.acomminos.morlunk.http;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import android.webkit.CookieSyncManager;

import com.acomminos.morlunk.http.MorlunkResponse.MorlunkRequestResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MorlunkRequestLoader extends AsyncTaskLoader<MorlunkResponse>{
	
	private static HttpClient client; // TODO make cleaner, we need a singleton for cookie saving
	
	private MorlunkRequest morlunkRequest;
	
	/**
	 * Clears all cookies in the active httpclient.
	 */
	public static void clearCookies() {
		((DefaultHttpClient)client).getCookieStore().clear();
	}
	
	public MorlunkRequestLoader(Context context, MorlunkRequest request) {
		super(context);
		this.morlunkRequest = request;
		
		if(client == null) {
			// Make a thread-safe singleton client
		    DefaultHttpClient httpClient = new DefaultHttpClient();
		    ClientConnectionManager mgr = httpClient.getConnectionManager();
			ThreadSafeClientConnManager connectionManager = 
		      		new ThreadSafeClientConnManager(httpClient.getParams(), mgr.getSchemeRegistry());
		    client = new DefaultHttpClient(connectionManager, httpClient.getParams());
		}
	}
	
	@Override
	public MorlunkResponse loadInBackground() {
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
			HttpResponse response = client.execute(request);
			CookieSyncManager.getInstance().sync(); // Sync cookies TODO fix, does nothing
			
			String jsonResponse = handler.handleResponse(response);
			
			// Begin parsing with GSON
			GsonBuilder gsonBuilder = new GsonBuilder();
		    gsonBuilder.registerTypeAdapter(MorlunkRequestResult.class, new MorlunkRequestResultDeserializer());
		    Gson gson = gsonBuilder.create();
		    
		    MorlunkResponse morlunkResponse = gson.fromJson(jsonResponse, morlunkRequest.getResponseClass());
		    
		    Log.i("HTTP", "Response: "+morlunkResponse.result);
		    
		    if(request instanceof HttpPost) {
		    	// Handle closing POST requests
		    	((HttpPost)request).getEntity().consumeContent();
		    }
		    
			return morlunkResponse;
		} catch (Exception e) {
			// TODO: handle exception and return a generic error.
			e.printStackTrace();
			MorlunkResponse response = new MorlunkResponse();
			response.result = MorlunkRequestResult.UNKNOWN;
			return response;
		}
	}
}
