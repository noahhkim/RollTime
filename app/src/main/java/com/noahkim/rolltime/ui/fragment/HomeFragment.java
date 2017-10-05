package com.noahkim.rolltime.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.noahkim.rolltime.util.MatchHolder;
import com.noahkim.rolltime.R;
import com.noahkim.rolltime.data.Match;
import com.noahkim.rolltime.ui.activity.EditMatchActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by noahkim on 8/16/17.
 */

public class HomeFragment extends Fragment {
    @BindView(R.id.rv_matches)
    RecyclerView mMatchesRecyclerView;

    private LinearLayoutManager mLayoutManager;

    // Firebase instance variables
    public static FirebaseDatabase FIREBASE_DB;
    public static DatabaseReference FIREBASE_DB_REF;
    private FirebaseRecyclerAdapter mRecyclerAdapter;
    private Query mRecentMatches;

    // Array of belt levels
    private int[] beltArray = {
            R.drawable.ic_bjj_white_belt,
            R.drawable.ic_bjj_blue_belt,
            R.drawable.ic_bjj_purple_belt,
            R.drawable.ic_bjj_brown_belt,
            R.drawable.ic_bjj_black_belt
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, rootView);
        setHasOptionsMenu(true);


        // Initialize Firebase
        FIREBASE_DB = FirebaseDatabase.getInstance();

        // Set up reference to database
        FIREBASE_DB_REF = FIREBASE_DB.getReference().child("matches");

        // Limit query to last 10 matches
        mRecentMatches = FIREBASE_DB_REF.limitToLast(5);

        // Initialize LayoutManager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mMatchesRecyclerView.setLayoutManager(mLayoutManager);

        // Attach FirebaseRecyclerAdapter to recyclerview
        mRecyclerAdapter = new FirebaseRecyclerAdapter<Match, MatchHolder>(
                Match.class,
                R.layout.list_item_matches,
                MatchHolder.class,
                mRecentMatches) {
            @Override
            protected void populateViewHolder(MatchHolder holder, Match match, int position) {
                holder.setName(match.getOppName());
                holder.setBeltLevel(beltArray[match.getOppBeltLevel()]);
                holder.setUserChokeCount(match.getUserChokeCount());
                holder.setUserArmlockCount(match.getUserArmlockCount());
                holder.setUserLeglockCount(match.getUserLeglockCount());
                holder.setOppChokeCount(match.getOppChokeCount());
                holder.setOppArmlockCount(match.getOppArmlockCount());
                holder.setOppLeglockCount(match.getOppLeglockCount());
            }

            @Override
            public MatchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                MatchHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                viewHolder.setOnClickListener(new MatchHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        DatabaseReference itemRef = mRecyclerAdapter.getRef(position);
                        Intent intent = new Intent(getActivity(), EditMatchActivity.class);
                        String postId = itemRef.getKey();
                        intent.setData(Uri.parse(postId));
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                });

                return viewHolder;
            }
        };
        mMatchesRecyclerView.setAdapter(mRecyclerAdapter);


        return rootView;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mRecyclerAdapter.cleanup();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMatchesRecyclerView.smoothScrollToPosition(5);
    }
}
