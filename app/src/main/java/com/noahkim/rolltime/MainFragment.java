package com.noahkim.rolltime;

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
import com.noahkim.rolltime.data.Match;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);
        setHasOptionsMenu(true);

        // Initialize Firebase
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        // Set up reference to database
        mDatabaseReference = mFirebaseDatabase.getReference();

        // Limit query to last 10 matches
        mRecentMatches = mDatabaseReference.limitToLast(10);

        // Initialize LayoutManager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mMatchesRecyclerView.setLayoutManager(mLayoutManager);

        // Create subclass of FirebaseRecyclerAdapter
        mRecyclerAdapter = new FirebaseRecyclerAdapter<Match, MatchHolder>(
                Match.class,
                R.layout.list_item_matches,
                MatchHolder.class,
                mRecentMatches) {
            @Override
            protected void populateViewHolder(MatchHolder holder, Match match, int position) {
                holder.setName(match.getOpponentName());
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
}
