package com.noahkim.rolltime.stats;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.noahkim.rolltime.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by noahkim on 9/13/17.
 */

public class StatsFragment extends Fragment implements StatsPresenter.View {
    @BindView(R.id.bar_chart)
    BarChart mBarChart;
    @BindView(R.id.stats_empty_view)
    View mEmptyStatsView;

    private StatsPresenter statsPresenter;
    private String choke, armlock, leglock;
    private int colorPrimary, colorGrey;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_stats, container, false);
        ButterKnife.bind(this, rootView);

        choke = getResources().getString(R.string.choke);
        armlock = getResources().getString(R.string.armlock);
        leglock = getResources().getString(R.string.leglock);

        colorPrimary = ContextCompat.getColor(getActivity(), R.color.colorPrimary);
        colorGrey = ContextCompat.getColor(getActivity(), R.color.colorGrey);

        statsPresenter = new StatsPresenter(this, choke, armlock, leglock, colorPrimary, colorGrey);

        // Customize bar chart settings
        mBarChart.setTouchEnabled(false);
        mBarChart.setPinchZoom(false);
        mBarChart.setDescription("");
        mBarChart.getLegend().setTextSize(16f);
        mBarChart.getXAxis().setTextSize(12f);

        // Remove horizontal gridlines and numbers on the right
        YAxis yAxisLeft = mBarChart.getAxisLeft();
        YAxis yAxisRight = mBarChart.getAxisRight();
        yAxisLeft.setDrawGridLines(false);
        yAxisRight.setDrawGridLines(false);
        yAxisRight.setDrawLabels(false);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        statsPresenter.detachDatabaseReadListener();
    }

    @Override
    public void setNoData(String string) {
        mBarChart.setNoDataText(string);
    }

    @Override
    public void showBarChart() {
        mBarChart.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideBarChart() {
        mBarChart.setVisibility(View.GONE);
    }

    @Override
    public void showEmptyView() {
        mEmptyStatsView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmptyView() {
        mEmptyStatsView.setVisibility(View.GONE);
    }

    @Override
    public void invalidateBarChart() {
        mBarChart.invalidate();
    }

    @Override
    public void setBarData(BarData barData) {
        mBarChart.setData(barData);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        statsPresenter.onDestroyView();
    }
}
