package com.acomminos.morlunk.account.minecraft;

import com.google.gson.annotations.SerializedName;

public class MorlunkMinecraftStoreItem {
	
	@SerializedName("name")
	public String name;
	
	@SerializedName("id")
	public int id;
	
	@SerializedName("buy_value")
	public int buyValue;
	
	@SerializedName("sell_value")
	public int sellValue;
	
	@SerializedName("buy_sell_quantity")
	public int buySellQuantity;
}
