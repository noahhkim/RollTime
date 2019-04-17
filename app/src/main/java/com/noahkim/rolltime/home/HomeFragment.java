package com.noahkim.rolltime.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.noahkim.rolltime.R;
import com.noahkim.rolltime.activities.EditMatchActivity;
import com.noahkim.rolltime.adapters.HomeAdapter;
import com.noahkim.rolltime.adapters.MatchHolder;
import com.noahkim.rolltime.model.Match;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.noahkim.rolltime.adapters.MatchHolder.ClickListener;

/**
 * Created by noahkim on 8/16/17.
 */

public class HomeFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener, HomePresenter.View {
    @BindView(R.id.rv_matches)
    RecyclerView mMatchesRecyclerView;
    @BindView(R.id.empty_view)
    View mEmptyView;

    // Firebase instance variables
    private FirebaseRecyclerAdapter mRecyclerAdapter;
    private HomeAdapter homeAdapter;
    private Query mRecentMatches;
    private String userBeltPreference;
    private DatabaseReference mDatabaseReference;

    // Array of belt levels
    private int[] beltArray = {
            R.drawable.ic_bjj_white_belt,
            R.drawable.ic_bjj_blue_belt,
            R.drawable.ic_bjj_purple_belt,
            R.drawable.ic_bjj_brown_belt,
            R.drawable.ic_bjj_black_belt
    };

    private List<Match> matches = new ArrayList<>();

    private HomePresenter homePresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, rootView);
        setHasOptionsMenu(true);


        // Initialize Firebase
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser mCurrentUser = mFirebaseAuth.getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users/" + mCurrentUser.getUid());

        // Limit query to last 5 matches
//        mRecentMatches = mDatabaseReference.limitToLast(5);

        // Initialize LayoutManager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mMatchesRecyclerView.setLayoutManager(layoutManager);

        // Get user belt preference
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        userBeltPreference = sharedPreferences.getString(getString(R.string.belt_level_key),
                getString(R.string.pref_belt_white));

//        setUpFirebaseRecyclerViewAdapter();

        initView();

//        setEmptyView();
        homePresenter = new HomePresenter(this);

//        mMatchesRecyclerView.setAdapter(mRecyclerAdapter);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
//        mRecyclerAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
//        mRecyclerAdapter.stopListening();

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

//    private void setEmptyView() {
//        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (!dataSnapshot.hasChildren()) {
//                    mEmptyView.setVisibility(View.VISIBLE);
//                    mMatchesRecyclerView.setVisibility(View.GONE);
//                } else {
//                    mEmptyView.setVisibility(View.GONE);
//                    mMatchesRecyclerView.setVisibility(View.VISIBLE);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }

    private void initView() {
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                matches.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Match match = snapshot.getValue(Match.class);
                    matches.add(match);
                }
                homeAdapter = new HomeAdapter(matches);
                mMatchesRecyclerView.setAdapter(homeAdapter);
                homeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setUpFirebaseRecyclerViewAdapter() {

        // Build FirebaseRecyclerOptions
        FirebaseRecyclerOptions<Match> options =
                new FirebaseRecyclerOptions.Builder<Match>()
                        .setQuery(mDatabaseReference, Match.class)
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
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.matches_list_item, parent, false);
                MatchHolder matchHolder = new MatchHolder(view);
                matchHolder.setOnClickListener(new ClickListener() {
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

    @Override
    public void showEmptyView() {
        mEmptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmptyView() {
        mEmptyView.setVisibility(View.GONE);
    }

    @Override
    public void showRecyclerView() {
        mMatchesRecyclerView.setVisibility(View.VISIBLE);

    }

    @Override
    public void hideRecyclerView() {
        mMatchesRecyclerView.setVisibility(View.GONE);

    }
}
