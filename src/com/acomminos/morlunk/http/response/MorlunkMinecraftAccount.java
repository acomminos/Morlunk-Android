package com.acomminos.morlunk.http.response;

import com.google.gson.annotations.SerializedName;

public class MorlunkMinecraftAccount {
	
	@SerializedName("screen_name")
	public String screenName;
	
	@SerializedName("banned")
	public boolean banned;
	
	@SerializedName("minecraft_username")
	public String minecraftUsername;
	
	@SerializedName("paosos")
	public int paosos;
	
}
