package com.acomminos.morlunk.http;

import java.io.IOException;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;

public class MorlunkRequestTask extends AsyncTask<MorlunkRequest, Void, MorlunkResponse> {
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}
	
	/**
	 * Executes the request with either a GET or POST request to the specified URL, depending on the RequestType property.
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	@Override
	protected MorlunkResponse doInBackground(MorlunkRequest... arg0) {
		MorlunkRequest morlunkRequest = arg0[0];
		MorlunkResponse morlunkResponse = new MorlunkResponse();
		HttpClient client = new DefaultHttpClient();
		HttpRequest request = null;
		try {
			switch (morlunkRequest.getRequestType()) {
			case REQUEST_GET:
				request = morlunkRequest.makePostRequest();
				break;
			case REQUEST_POST:
				request = morlunkRequest.makeGetRequest();
				break;
			}
			HttpResponse response = client.execute((HttpUriRequest) request);
			response.
		} catch (Exception e) {
			// TODO handle exception
			return null;
		}
	}
	
}
