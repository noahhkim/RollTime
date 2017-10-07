package com.noahkim.rolltime.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Noah on 10/5/2017.
 */

public class Video implements Parcelable {

    @SerializedName("title")
    private String mTitle;

    @SerializedName("description")
    private String mDescription;

    @SerializedName("thumbnails")
    private String mThumbnail;

    @SerializedName("videoId")
    private String mVideoId;

    protected Video(Parcel in) {
        mTitle = in.readString();
        mDescription = in.readString();
        mThumbnail = in.readString();
        mVideoId = in.readString();
    }

    public Video() {
    }

    public Video(String title) {
        mTitle = title;
    }

    public static final Creator<Video> CREATOR = new Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel in) {
            return new Video(in);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mTitle);
        parcel.writeString(mDescription);
        parcel.writeString(mThumbnail);
        parcel.writeString(mVideoId);
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getThumbail() {
        return mThumbnail;
    }

    public String getVideoId() {
        return mVideoId;
    }
}
