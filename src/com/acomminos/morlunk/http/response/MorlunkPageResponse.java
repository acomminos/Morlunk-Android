package com.acomminos.morlunk.http.response;

import com.acomminos.morlunk.http.MorlunkResponse;
import com.google.gson.annotations.SerializedName;

public class MorlunkPageResponse extends MorlunkResponse {
	
	@SerializedName("page")
	public MorlunkPage page;

}
