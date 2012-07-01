package com.acomminos.morlunk;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

/**
 * MorlunkRequest boilerplate.
 * @author andrew
 *
 */
public class MorlunkRequest {
	
	interface MorlunkRequestListener {
		
		public void onRequestSuccess(String response);
		
		public void onRequestFailure(MorlunkRequestError error);
		
	}
	
	public enum MorlunkRequestError {
		ERROR_NOT_AUTHENTICATED,
		ERROR_INVALID_REQUEST,
		ERROR_GENERIC
	}
	
	public enum MorlunkRequestType {
		REQUEST_POST,
		REQUEST_GET
	}
	
	private String url;
	private Map<String, String> arguments = new HashMap<String, String>();
	private MorlunkRequestType requestType;
	private MorlunkRequestListener listener;
	
	public MorlunkRequest(String url) {
		this.url = url;
	}
	
	/**
	 * Executes the request with either a GET or POST request to the specified URL, depending on the RequestType property.
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public void execute() throws ClientProtocolException, IOException {
		HttpClient client = new DefaultHttpClient();
		HttpRequest request = null;
		switch (requestType) {
		case REQUEST_GET:
			request = makePostRequest();
			break;
		case REQUEST_POST:
			request = makeGetRequest();
			break;
		}
		client.execute((HttpUriRequest) request);
	}
	
	public HttpPost makePostRequest() throws UnsupportedEncodingException {
		HttpPost postRequest = new HttpPost(getUrl());
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		for(Entry<String, String> entry : arguments.entrySet()) {
			nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		postRequest.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		
		return postRequest;
	}
	
	public HttpGet makeGetRequest() {
		HttpGet getRequest = new HttpGet(getUrl());
		getRequest.
	}
	
	/**
	 * Adds a new argument to be submitted via a POST or GET request.
	 * @param key
	 * @param value
	 */
	public void addArgument(String key, String value) {
		this.arguments.put(key, value);
	}
	
	public void removeArgument(String key) {
		this.arguments.remove(key);
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the requestType
	 */
	public MorlunkRequestType getRequestType() {
		return requestType;
	}

	/**
	 * @param requestType the requestType to set
	 */
	public void setRequestType(MorlunkRequestType requestType) {
		this.requestType = requestType;
	}

	/**
	 * @return the listener
	 */
	public MorlunkRequestListener getListener() {
		return listener;
	}

	/**
	 * @param listener the listener to set
	 */
	public void setListener(MorlunkRequestListener listener) {
		this.listener = listener;
	}
	
	
	
}
