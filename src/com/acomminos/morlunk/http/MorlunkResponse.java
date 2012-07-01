package com.acomminos.morlunk.http;

import org.json.JSONException;
import org.json.JSONObject;

public class MorlunkResponse {
	
	public enum MorlunkRequestError {
		ERROR_NOT_AUTHENTICATED,
		ERROR_INVALID_REQUEST,
		ERROR_GENERIC,
		ERROR_NONE
	}
	private String responseJSON;
	private MorlunkRequestError error;
	
	private MorlunkResponse() {}
	
	public static MorlunkResponse parseResponse(String JSON) throws JSONException {
		// Get error information
		JSONObject object = new JSONObject(JSON);
		
	}
}
