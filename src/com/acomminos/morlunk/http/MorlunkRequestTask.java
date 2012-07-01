package com.acomminos.morlunk.http;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;

public class MorlunkRequestTask extends AsyncTask<MorlunkRequest, Void, MorlunkResponse> {
	
	public interface MorlunkRequestListener {
		public void requestCompleted(MorlunkRequest request, MorlunkResponse response);
		public void requestFailed(MorlunkRequest request);
	}

	private MorlunkRequest request;
	private MorlunkRequestListener listener;
	
	public MorlunkRequestTask(MorlunkRequestListener listener) {
		this.listener = listener;
	}
	
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
		this.request = morlunkRequest; // TODO make this cleaner?
		
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
	
	@Override
	protected void onPostExecute(MorlunkResponse result) {
		// If a response value is set, call requestCompleted
		if(result != null) {
			listener.requestCompleted(request, result);
		} else {
			// Something else happened, no result returned
			listener.requestFailed(request);
		}
	}
	
}
