package com.acomminos.morlunk.http;

import org.json.JSONException;
import org.json.JSONObject;

public class MorlunkResponse {
	
	public enum MorlunkRequestResult {
		NOT_AUTHENTICATED,
		INVALID_REQUEST,
		ERROR,
		UNKNOWN,
		SUCCESS
	}
	private String responseJSON;
	private MorlunkRequestResult error;
	
	private MorlunkResponse() {}
	
	public static MorlunkResponse parseResponse(String JSON) throws JSONException {
		JSONObject resultObject = new JSONObject(JSON);

		// Get error information
		String resultCode = resultObject.getString("result");
		MorlunkResponse response = new MorlunkResponse();
		MorlunkRequestResult error = readResult(resultCode);
		
		response.setError(error);
		response.setResponseJSON(JSON);
		
		return response;
	}
	
	private static MorlunkRequestResult readResult(String resultCode) {
		// Parse the string returned in the result property.
		MorlunkRequestResult error;
		if (resultCode.equals("success")) {
			error = MorlunkRequestResult.SUCCESS;
		} else if (resultCode.equals("invalid_request")) {
			error = MorlunkRequestResult.INVALID_REQUEST;
		} else if (resultCode.equals("error")) {
			error = MorlunkRequestResult.ERROR;
		} else if (resultCode.equals("no_auth")) {
			error = MorlunkRequestResult.NOT_AUTHENTICATED;
		} else {
			error = MorlunkRequestResult.UNKNOWN;
		}
		return error;
	}

	public String getResponseJSON() {
		return responseJSON;
	}

	private void setResponseJSON(String responseJSON) {
		this.responseJSON = responseJSON;
	}

	public MorlunkRequestResult getError() {
		return error;
	}

	private void setError(MorlunkRequestResult error) {
		this.error = error;
	}
	
	
}
