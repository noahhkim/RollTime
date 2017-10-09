package com.noahkim.rolltime.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.noahkim.rolltime.R;
import com.noahkim.rolltime.adapters.VideoAdapter;
import com.noahkim.rolltime.data.Video;
import com.noahkim.rolltime.util.RecyclerItemClickListener;
import com.noahkim.rolltime.webservice.FetchVideosTask;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Noah on 10/5/2017.
 */

public class VideosFragment extends Fragment {
    @BindView(R.id.rv_videos)
    RecyclerView mVideosRecyclerView;
    private VideoAdapter mVideoAdapter;
    private List<Video> mVideos = new ArrayList<>();
    private Parcelable mState;
    private final String SAVED_RECYCLER_VIEW_STATUS_ID = "saved_id";
    private LinearLayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_videos, container, false);
        ButterKnife.bind(this, rootView);

        // Initialize LayoutManager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mVideosRecyclerView.setLayoutManager(mLayoutManager);
        mVideoAdapter = new VideoAdapter(getContext(), mVideos);

        // Retrieve data from Firebase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference videoReference = firebaseDatabase.getReference().child("videos");
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Video video = dataSnapshot.getValue(Video.class);
                mVideos.add(video);
                // Attach an adapter
                mVideosRecyclerView.setAdapter(mVideoAdapter);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        videoReference.addChildEventListener(childEventListener);

        setUpOnItemClick();

        return rootView;
    }

    @Override
    public void onStart() {
        getYoutubeVideos();
        super.onStart();
    }

    private void getYoutubeVideos() {
        FetchVideosTask videosTask = new FetchVideosTask(getActivity(), mVideoAdapter);
        videosTask.execute();
    }

    private void setUpOnItemClick() {
        mVideosRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(
                getContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                final Video currentVideo = mVideos.get(position);

                Uri uri = new Uri.Builder()
                        .scheme("https")
                        .authority("www.youtube.com")
                        .appendPath("watch")
                        .appendQueryParameter("v", currentVideo.getVideoId())
                        .build();

                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        }
        ));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mVideosRecyclerView != null) {
            mVideosRecyclerView.clearOnScrollListeners();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mState != null) {
            mLayoutManager.onRestoreInstanceState(mState);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mState = mLayoutManager.onSaveInstanceState();
        outState.putParcelable(SAVED_RECYCLER_VIEW_STATUS_ID, mState);
        super.onSaveInstanceState(outState);
    }
}
