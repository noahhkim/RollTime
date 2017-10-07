package com.noahkim.rolltime.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Noah on 10/5/2017.
 */

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

    public Video(String title, String thumbnail) {
        mTitle = title;
        mThumbnail = thumbnail;
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
}
