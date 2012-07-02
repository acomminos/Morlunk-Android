package com.acomminos.morlunk.http;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

/**
 * MorlunkRequest boilerplate.
 * @author andrew
 *
 */
public class MorlunkRequest {
	
	// TODO add authentication headers to GET and POST requests. This is important so that the user can access their Paoso balance and such.
	
	public enum MorlunkRequestType {
		REQUEST_POST,
		REQUEST_GET
	}
	
	private String url;
	private Map<String, String> arguments = new HashMap<String, String>();
	private MorlunkRequestType requestType;
	private Class<? extends MorlunkResponse> responseClass;
	
	/**
	 * Creates a new MorlunkRequest with the following parameters.
	 * @param url The URL to make a request to.
	 * @param requestType The type of request (GET or POST)
	 * @param responseClass The class that the response will be deserialized into from JSON via GSON.
	 */
	public MorlunkRequest(String url, MorlunkRequestType requestType, Class<? extends MorlunkResponse> responseClass) {
		this.url = url;
		this.requestType = requestType;
		this.responseClass = responseClass;
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
		String getUrl = getUrl();
		
		int index = 0;
		
		for(Entry<String, String> entry : arguments.entrySet()) {
			getUrl += (index == 0 ? "?" : "&") + entry.getKey() + "="
					+ entry.getValue();
			index++;
		}
		
		HttpGet getRequest = new HttpGet(getUrl);
		return getRequest;
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
	 * @return the responseClass
	 */
	public Class<? extends MorlunkResponse> getResponseClass() {
		return responseClass;
	}

	/**
	 * @param responseClass the responseClass to set
	 */
	public void setResponseClass(Class<MorlunkResponse> responseClass) {
		this.responseClass = responseClass;
	}
}
