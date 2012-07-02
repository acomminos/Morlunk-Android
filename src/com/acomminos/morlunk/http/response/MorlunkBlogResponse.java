package com.acomminos.morlunk.http.response;

import java.util.List;

import com.acomminos.morlunk.http.MorlunkResponse;
import com.google.gson.annotations.SerializedName;

public class MorlunkBlogResponse extends MorlunkResponse {
	
	@SerializedName("posts")
	public List<MorlunkBlogPost> posts;
}
