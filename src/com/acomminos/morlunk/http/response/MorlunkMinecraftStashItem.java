package com.acomminos.morlunk.http.response;

import com.google.gson.annotations.SerializedName;

public class MorlunkMinecraftStashItem {
	
	@SerializedName("name")
	public String name;
	
	@SerializedName("data_value")
	public int dataValue;
	
	@SerializedName("damage_value")
	public int damageValue;
	
	@SerializedName("amount")
	public int amount;

}
