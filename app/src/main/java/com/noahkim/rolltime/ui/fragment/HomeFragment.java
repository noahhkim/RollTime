package com.noahkim.rolltime.ui.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.noahkim.rolltime.R;
import com.noahkim.rolltime.data.Match;
import com.noahkim.rolltime.ui.activity.EditMatchActivity;
import com.noahkim.rolltime.util.MatchHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.noahkim.rolltime.ui.activity.MainActivity.FIREBASE_DB;

/**
 * Created by noahkim on 8/16/17.
 */

public class HomeFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    @BindView(R.id.rv_matches)
    RecyclerView mMatchesRecyclerView;
//    @BindView(R.id.empty_view)
//    View mEmptyView;

    // Firebase instance variables
//    public static FirebaseDatabase FIREBASE_DB;
//    public static DatabaseReference FIREBASE_DB_REF;
//    private FirebaseUser FIREBASE_USER;
    private FirebaseRecyclerAdapter mRecyclerAdapter;
    private Query mRecentMatches;
    private String userBeltPreference;
    private RecyclerView.AdapterDataObserver mObserver;
    public static DatabaseReference FIREBASE_DB_REF;

    // Array of belt levels
    private int[] beltArray = {
            R.drawable.ic_bjj_white_belt,
            R.drawable.ic_bjj_blue_belt,
            R.drawable.ic_bjj_purple_belt,
            R.drawable.ic_bjj_brown_belt,
            R.drawable.ic_bjj_black_belt
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, rootView);
        setHasOptionsMenu(true);

        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser mCurrentUser = mFirebaseAuth.getCurrentUser();
        FIREBASE_DB_REF = FIREBASE_DB.getReference().child("users/" + mCurrentUser.getUid());

        // Initialize LayoutManager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mMatchesRecyclerView.setLayoutManager(layoutManager);

        // Limit query to last 5 matches
        mRecentMatches = FIREBASE_DB_REF.limitToLast(5);

        setUpFirebaseRecyclerViewAdapter();

        // Attach adapter to recyclerview
        mMatchesRecyclerView.setAdapter(mRecyclerAdapter);

        // Remove emptyView if there is data
//        mObserver = new RecyclerView.AdapterDataObserver() {
//            @Override
//            public void onItemRangeInserted(int positionStart, int itemCount) {
//                super.onItemRangeInserted(positionStart, itemCount);
//                if (itemCount == 0) {
//                    mEmptyView.setVisibility(View.VISIBLE);
//                    mMatchesRecyclerView.setVisibility(View.GONE);
//                } else {
//                    mEmptyView.setVisibility(View.GONE);
//                    mMatchesRecyclerView.setVisibility(View.VISIBLE);
//                }
//            }
//
//            @Override
//            public void onItemRangeRemoved(int positionStart, int itemCount) {
//                super.onItemRangeRemoved(positionStart, itemCount);
//                if (itemCount == 0) {
//                    mEmptyView.setVisibility(View.VISIBLE);
//                    mMatchesRecyclerView.setVisibility(View.GONE);
//                } else {
//                    mEmptyView.setVisibility(View.GONE);
//                    mMatchesRecyclerView.setVisibility(View.VISIBLE);
//                }
//            }
//        };
//
//        mRecyclerAdapter.registerAdapterDataObserver(mObserver);


        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mRecyclerAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mRecyclerAdapter.stopListening();

        PreferenceManager.getDefaultSharedPreferences(getActivity())
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Scroll to top of the list when returning to HomeFragment
        mMatchesRecyclerView.smoothScrollToPosition(5);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        // Update user belt level from preference
        if (key.equals(getString(R.string.belt_level_key))) {
            userBeltPreference = sharedPreferences.getString(getString(R.string.belt_level_key),
                    getString(R.string.pref_belt_white));
        }
            mRecyclerAdapter.notifyDataSetChanged();
    }

    private void setUpFirebaseRecyclerViewAdapter() {
        // Get user belt preference
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        userBeltPreference = sharedPreferences.getString(getString(R.string.belt_level_key),
                getString(R.string.pref_belt_white));

        // Build FirebaseRecyclerOptions
        FirebaseRecyclerOptions<Match> options =
                new FirebaseRecyclerOptions.Builder<Match>()
                        .setQuery(mRecentMatches, Match.class)
                        .build();


        // Attach FirebaseRecyclerAdapter to recyclerview
        mRecyclerAdapter = new FirebaseRecyclerAdapter<Match, MatchHolder>(
                options) {
            @Override
            public void onDataChanged() {
                super.onDataChanged();
                mRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            protected void onBindViewHolder(MatchHolder holder, int position, Match match) {
                // Populate views in ViewHolder
                holder.setUserBeltLevel(beltArray[Integer.valueOf(userBeltPreference)]);
                holder.setOppName(match.getOppName());
                holder.setOppBeltLevel(beltArray[match.getOppBeltLevel()]);
                holder.setUserChokeCount(match.getUserChokeCount());
                holder.setUserArmlockCount(match.getUserArmlockCount());
                holder.setUserLeglockCount(match.getUserLeglockCount());
                holder.setOppChokeCount(match.getOppChokeCount());
                holder.setOppArmlockCount(match.getOppArmlockCount());
                holder.setOppLeglockCount(match.getOppLeglockCount());
            }

            @Override
            public MatchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//                MatchHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.matches_list_item, parent, false);
                MatchHolder matchHolder = new MatchHolder(view);
                matchHolder.setOnClickListener(new MatchHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // Attach onClickListener to recyclerview list items
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

                return matchHolder;
            }
        };
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }
}
