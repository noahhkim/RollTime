package com.noahkim.rolltime;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.noahkim.rolltime.data.Match;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by noahkim on 8/16/17.
 */

public class MainFragment extends Fragment {
    @BindView(R.id.rv_matches)
    RecyclerView mMatchesRecyclerView;

    private LinearLayoutManager mLayoutManager;

    // Firebase instance variables
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
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
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);
        setHasOptionsMenu(true);

        // Initialize Firebase
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        // Set up reference to database
        mDatabaseReference = mFirebaseDatabase.getReference().child("matches");

        // Limit query to last 10 matches
        mRecentMatches = mDatabaseReference.limitToLast(5);

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
                holder.setName(match.getOpponentName());
                holder.setBeltLevel(beltArray[match.getBeltLevel()]);
                holder.setUserChokeCount(match.getChokeCount());
            }

            @Override
            public MatchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                MatchHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                viewHolder.setOnClickListener(new MatchHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getActivity(), RecordMatchActivity.class);
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
