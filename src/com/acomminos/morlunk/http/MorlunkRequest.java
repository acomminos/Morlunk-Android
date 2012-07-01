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
	
	public enum MorlunkRequestType {
		REQUEST_POST,
		REQUEST_GET
	}
	
	private String url;
	private Map<String, String> arguments = new HashMap<String, String>();
	private MorlunkRequestType requestType;
	
	public MorlunkRequest(String url) {
		this.url = url;
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
}
