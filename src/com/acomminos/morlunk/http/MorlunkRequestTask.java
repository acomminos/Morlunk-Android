package com.acomminos.morlunk.http;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public class MorlunkRequestTask extends AsyncTaskLoader<MorlunkResponse>{

	private MorlunkRequest morlunkRequest;
	
	public MorlunkRequestTask(Context context, MorlunkRequest request) {
		super(context);
		this.morlunkRequest = request;
	}
	
	@Override
	public MorlunkResponse loadInBackground() {
		HttpClient client = new DefaultHttpClient();
		HttpUriRequest request = null;
		try {
			switch (morlunkRequest.getRequestType()) {
			case REQUEST_GET:
				request = morlunkRequest.makePostRequest();
				break;
			case REQUEST_POST:
				request = morlunkRequest.makeGetRequest();
				break;
			}
			ResponseHandler<String> handler = new BasicResponseHandler();
			HttpResponse response = client.execute(request);
			String jsonResponse = handler.handleResponse(response);
			MorlunkResponse morlunkResponse = MorlunkResponse.parseResponse(jsonResponse);
			return morlunkResponse;
		} catch (Exception e) {
			// TODO: handle exception and return a generic error.
			return null;
		}
	}
}
