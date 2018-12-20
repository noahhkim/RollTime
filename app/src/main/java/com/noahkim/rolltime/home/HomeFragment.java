package com.noahkim.rolltime.home;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.hannesdorfmann.mosby3.mvi.MviFragment;
import com.noahkim.rolltime.R;
import com.noahkim.rolltime.adapters.MatchAdapter;
import com.noahkim.rolltime.data.Match;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * Created by noahkim on 8/16/17.
 */

public class HomeFragment extends MviFragment<HomeView, HomePresenter>
    implements SharedPreferences.OnSharedPreferenceChangeListener, HomeView {
  @BindView(R.id.rv_matches) RecyclerView mMatchesRecyclerView;
  @BindView(R.id.empty_view) View mEmptyView;

  private String userBeltPreference;

  private MatchAdapter matchAdapter;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_home, container, false);
    ButterKnife.bind(this, rootView);
    setHasOptionsMenu(true);

    // Initialize LayoutManager
    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
    layoutManager.setReverseLayout(true);
    layoutManager.setStackFromEnd(true);
    mMatchesRecyclerView.setLayoutManager(layoutManager);

    setUpRecyclerViewAdapter();

    return rootView;
  }

  @NonNull @Override public HomePresenter createPresenter() {
    return new HomePresenter();
  }

  @Override public void onStop() {
    super.onStop();

    PreferenceManager.getDefaultSharedPreferences(getActivity())
        .unregisterOnSharedPreferenceChangeListener(this);
  }

  @Override public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    // Update user belt level from preference
    if (key.equals(getString(R.string.belt_level_key))) {
      userBeltPreference = sharedPreferences.getString(getString(R.string.belt_level_key),
          getString(R.string.pref_belt_white));
    }
    matchAdapter.setUserBeltPreference(userBeltPreference);
    matchAdapter.notifyDataSetChanged();
  }

  private void setUpRecyclerViewAdapter() {
    // Get user belt preference
    SharedPreferences sharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(getContext());
    userBeltPreference = sharedPreferences.getString(getString(R.string.belt_level_key),
        getString(R.string.pref_belt_white));

    matchAdapter = new MatchAdapter(userBeltPreference);
    mMatchesRecyclerView.setAdapter(matchAdapter);
  }

  @Override public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    setRetainInstance(true);
  }

  @Override public void render(@NotNull HomeViewState homeViewState) {
    List<Match> matchesList = homeViewState.getMatchesList();
    if (matchesList.isEmpty()) {
      mEmptyView.setVisibility(View.VISIBLE);
      mMatchesRecyclerView.setVisibility(View.GONE);
    } else {
      mEmptyView.setVisibility(View.GONE);
      mMatchesRecyclerView.setVisibility(View.VISIBLE);
      matchAdapter.setMatchesList(homeViewState.getMatchesList());
    }
  }
}
