package com.acomminos.morlunk.http.response;

import java.util.List;

import com.acomminos.morlunk.http.MorlunkResponse;
import com.google.gson.annotations.SerializedName;

public class MorlunkMinecraftStashResponse extends MorlunkResponse {
	
	@SerializedName("stash")
	public MorlunkMinecraftStash stash;
	
	public class MorlunkMinecraftStash {
		@SerializedName("name")
		public String stashName;
		
		@SerializedName("size")
		public int stashSize;
		
		@SerializedName("items")
		public List<MorlunkMinecraftStashItem> items;
	}
}
