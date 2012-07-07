package com.acomminos.morlunk.http;

import java.lang.reflect.Type;

import com.acomminos.morlunk.http.MorlunkResponse.MorlunkRequestResult;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;

public class MorlunkResponse {
	
	public enum MorlunkRequestResult {
		NOT_AUTHENTICATED,
		NO_USER,
		INVALID_REQUEST,
		ERROR,
		UNKNOWN,
		SUCCESS
	}
	
	@SerializedName("result")
	public MorlunkRequestResult result;
}

/**
 * Class so we can deserialize the result string returned as part of the response into an enum.
 * @author andrew
 */
class MorlunkRequestResultDeserializer implements JsonDeserializer<MorlunkRequestResult> {

	@Override
	public MorlunkRequestResult deserialize(JsonElement arg0, Type arg1,
			JsonDeserializationContext arg2) throws JsonParseException {
		String resultCode = arg0.getAsString();
		
		MorlunkRequestResult error;
		if (resultCode.equals("success")) {
			error = MorlunkRequestResult.SUCCESS;
		} else if (resultCode.equals("invalid_request")) {
			error = MorlunkRequestResult.INVALID_REQUEST;
		} else if (resultCode.equals("error")) {
			error = MorlunkRequestResult.ERROR;
		} else if (resultCode.equals("no_user")) {
			error = MorlunkRequestResult.NO_USER;
		} else if (resultCode.equals("no_auth")) {
			error = MorlunkRequestResult.NOT_AUTHENTICATED;
		} else {
			error = MorlunkRequestResult.UNKNOWN;
		}
		return error;
	}
	
}
