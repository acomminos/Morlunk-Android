package com.acomminos.morlunk.http.response;

import java.util.List;

import com.acomminos.morlunk.http.MorlunkResponse;
import com.google.gson.annotations.SerializedName;

public class MorlunkMinecraftStoreResponse extends MorlunkResponse {
	
	@SerializedName("items")
	public List<MorlunkMinecraftStoreItem> items;

}
