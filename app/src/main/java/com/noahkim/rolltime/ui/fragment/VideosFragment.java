package com.noahkim.rolltime.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.noahkim.rolltime.R;
import com.noahkim.rolltime.adapters.VideoAdapter;
import com.noahkim.rolltime.data.Video;
import com.noahkim.rolltime.webservice.FetchVideosTask;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Noah on 10/5/2017.
 */

public class VideosFragment extends Fragment {
    private static final String LOG_TAG = VideosFragment.class.getName();
    @BindView(R.id.rv_videos)
    RecyclerView mVideosRecyclerView;
    private VideoAdapter mVideoAdapter;
    private List<Video> mVideos;
    private List<Video> videoList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_videos, container, false);
        ButterKnife.bind(this, rootView);

        getYoutubeVideos();

        // Initialize LayoutManager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mVideosRecyclerView.setLayoutManager(layoutManager);

        // Specify an adapter
//        mVideosRecyclerView.setAdapter(mVideoAdapter);

        return rootView;
    }

    private void getYoutubeVideos() {
        FetchVideosTask videosTask = new FetchVideosTask(getActivity(), mVideoAdapter);
        videosTask.execute();
        Log.d(LOG_TAG, "Videos: " + videoList.size());
        mVideoAdapter = new VideoAdapter(videoList);

//        Retrofit retrofit =  new Retrofit.Builder()
//                .baseUrl(getString(R.string.youtube_base_url))
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        final Api videoApi = retrofit.create(Api.class);
//        Call<List<Video>> videoCall = videoApi.getVideos();
//        videoCall.enqueue(new Callback<List<Video>>() {
//            @Override
//            public void onResponse(Call<List<Video>> call, Response<List<Video>> response) {
//                mVideos = response.body();
//                Log.d(LOG_TAG, "Videos: " + mVideos);
//                mVideoAdapter = new VideoAdapter(mVideos);
//                mVideosRecyclerView.setAdapter(mVideoAdapter);
//            }
//
//            @Override
//            public void onFailure(Call<List<Video>> call, Throwable t) {
//
//            }
//        });
    }
}
