package com.acomminos.morlunk.http.response;

import com.acomminos.morlunk.http.MorlunkResponse;
import com.google.gson.annotations.SerializedName;

public class MorlunkMinecraftAccountResponse extends MorlunkResponse {
	
	@SerializedName("user")
	public MorlunkMinecraftAccount account;
}
