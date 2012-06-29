package com.acomminos.morlunk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

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
	
	String url;
	Map<String, String> arguments;
	
	public MorlunkRequest(String url, Map<String, String> arguments) {
		this.url = url;
		this.arguments = arguments;
	}
	
	/**
	 * Gets the JSON string retrieved when querying
	 * 
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public String fetchJSON() throws ClientProtocolException, IOException {
		HttpClient httpClient = new DefaultHttpClient();
		HttpUriRequest request = null;
		
		if(getRequestMethod() == DucatRequestMethod.GET) {
			String loginUrl = makeGetURL(); // Get the URL that we'll ask.
			request = new HttpGet(loginUrl);
		} else if(getRequestMethod() == DucatRequestMethod.POST) {
			// NOTE: IN ORDER FOR POST TO WORK, WE NEED TO USE @csrf_exempt IN THE VIEW WE WANT TO CALL.
			request = new HttpPost(getRequestUrl());
			HttpPost postRequest = (HttpPost) request;
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(arguments.size());
			for(Entry<String, String> entry : arguments.entrySet()) {
				nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			postRequest.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		}
		// Execute and handle
		ResponseHandler<String> handler = new BasicResponseHandler();
		String json = httpClient.execute(request, handler);

		httpClient.getConnectionManager().shutdown();

		return json;
	}
	
	/**
	 * Creates a queryable URL for the values in hashmap arguments with the URL requestUrl.
	 * @return A queryable URL.
	 */
	private String makeGetURL() {
		String url = this.url;
		
		int index = 0;
		// Append keys and values to URL.
		for (Map.Entry<String, String> entry : this.arguments.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			
			value = value.replaceAll(" ", "+"); // Remove spaces and replace with pluses
			
			// Add a question mark or ampersand depending on the index
			url += (index == 0) ? "?" : "&";
			
			url	+= key
				+ "="
				+ value;
			index++;
		}
		
		System.out.println(url);
		
		return url;
	}
	
}
