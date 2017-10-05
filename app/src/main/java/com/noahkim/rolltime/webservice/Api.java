package com.noahkim.rolltime.webservice;

import com.noahkim.rolltime.BuildConfig;
import com.noahkim.rolltime.data.Video;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Noah on 10/5/2017.
 */

public interface Api {
    @GET("search?part=snippet&maxResults=10&q=brazilian+jiu+jitsu&key=" + BuildConfig.YOUTUBE_API_KEY)
    Call<List<Video>> getVideos();
}
