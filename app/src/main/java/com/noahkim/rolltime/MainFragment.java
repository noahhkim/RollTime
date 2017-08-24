package com.noahkim.rolltime;

import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.noahkim.rolltime.data.Match;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by noahkim on 8/16/17.
 */

public class MainFragment extends Fragment {
    @BindView(R.id.rv_matches)
    RecyclerView mMatchesRecyclerView;

    private List<Match> mMatches;
    private MatchAdapter mMatchAdapter;
    private String mId;

    // Firebase instance variables
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEventListener;
    private FirebaseRecyclerAdapter mRecyclerAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);
        setHasOptionsMenu(true);

        // Initialize Firebase
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();

        mMatches = new ArrayList<>();
        mId = Settings.Secure.getString(getActivity().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        mMatchesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mMatchAdapter = new MatchAdapter(mMatches, mId);


//        mRecyclerAdapter = new FirebaseRecyclerAdapter<Match, MatchAdapter.MatchHolder>(Match.class,
//                R.layout.fragment_main,
//                MatchAdapter.MatchHolder.class,
//                mDatabaseReference) {
//            @Override
//            protected void populateViewHolder(MatchAdapter.MatchHolder holder, Match match, int position) {
//                holder.setName(match.getOpponentName());
//            }
//        };

        mMatchesRecyclerView.setAdapter(mMatchAdapter);

        attachDatabaseReadListener();

        return rootView;
    }

    private void attachDatabaseReadListener() {
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                        Match match = dataSnapshot.getValue(Match.class);
                        mMatches.add(match);
                        mMatchesRecyclerView.scrollToPosition(mMatches.size() - 1);
                        mMatchAdapter.notifyItemInserted(mMatches.size() - 1);
                    }
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
            mDatabaseReference.addChildEventListener(mChildEventListener);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mRecyclerAdapter.cleanup();
    }
}
