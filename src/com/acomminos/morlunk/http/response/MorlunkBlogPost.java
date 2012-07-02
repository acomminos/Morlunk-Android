package com.acomminos.morlunk.http.response;

import com.google.gson.annotations.SerializedName;

public class MorlunkBlogPost {
	
	@SerializedName("title")
	public String postTitle;
	
	@SerializedName("description")
	public String postDescription;
	
	@SerializedName("body")
	public String postBody;

}
