package com.acomminos.morlunk.http.response;

import com.google.gson.annotations.SerializedName;

public class MorlunkPage {
	
	@SerializedName("id")
	public int id;
	
	@SerializedName("title")
	public String pageTitle;
	
	@SerializedName("identifier")
	public String pageIdentifier;
	
	@SerializedName("body")
	public String pageBody;
}
