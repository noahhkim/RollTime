package com.noahkim.rolltime.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Noah on 10/5/2017.
 */

@IgnoreExtraProperties
public class Video implements Parcelable {

    private String mTitle;
    private String mThumbnail;
    private String mVideoId;

    protected Video(Parcel in) {
        mTitle = in.readString();
        mThumbnail = in.readString();
        mVideoId = in.readString();
    }

    public Video() {
    }

    public Video(String title, String thumbnail,String videoId) {
        mTitle = title;
        mThumbnail = thumbnail;
        mVideoId = videoId;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("title", mTitle);
        result.put("thumbnail", mThumbnail);
        result.put("videoId", mVideoId);

        return result;
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
        parcel.writeString(mThumbnail);
        parcel.writeString(mVideoId);
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getThumbnail() {
        return mThumbnail;
    }

    public void setThumbnail(String thumbnail) {
        mThumbnail = thumbnail;
    }

    public String getVideoId() {
        return mVideoId;
    }

    public void setVideoId(String videoId) {
        mVideoId = videoId;
    }
}
