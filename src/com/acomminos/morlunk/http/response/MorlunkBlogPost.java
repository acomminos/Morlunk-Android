package com.acomminos.morlunk.http.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class MorlunkBlogPost implements Parcelable {
	
	@SerializedName("title")
	public String postTitle;
	
	@SerializedName("description")
	public String postDescription;
	
	@SerializedName("body")
	public String postBody;

	// Parcelable implementation
	
	public static final Parcelable.Creator<MorlunkBlogPost> CREATOR = new Parcelable.Creator<MorlunkBlogPost>() {

		public MorlunkBlogPost createFromParcel(Parcel in) {
			return new Gson().fromJson(in.readString(), MorlunkBlogPost.class);
		}

		@Override
		public MorlunkBlogPost[] newArray(int arg0) {
			return new MorlunkBlogPost[arg0];
		}
	};
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		Gson gson = new Gson();
		dest.writeString(gson.toJson(this));
	}

}
