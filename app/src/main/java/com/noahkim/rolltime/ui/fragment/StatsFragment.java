package com.noahkim.rolltime.ui.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.noahkim.rolltime.R;
import com.noahkim.rolltime.data.Match;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.noahkim.rolltime.ui.activity.MainActivity.FIREBASE_DB_REF;


/**
 * Created by noahkim on 9/13/17.
 */

public class StatsFragment extends Fragment {
    @BindView(R.id.bar_chart)
    BarChart mBarChart;

    private static final int TOTAL_CHOKES = 0;
    private static final int TOTAL_ARMLOCKS = 1;
    private static final int TOTAL_LEGLOCKS = 2;

    private ValueEventListener mValueEventListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_stats, container, false);
        ButterKnife.bind(this, rootView);

        attachDatabaseReadListener();

        return rootView;
    }

    private void attachDatabaseReadListener() {
        if (mValueEventListener == null) {
            mValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Retrieve data from Firebase
                    int userChokes = 0;
                    int userArmlocks = 0;
                    int userLeglocks = 0;
                    int oppChokes = 0;
                    int oppArmlocks = 0;
                    int oppLeglocks = 0;
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Match match = postSnapshot.getValue(Match.class);
                        userChokes += match.getUserChokeCount();
                        userArmlocks += match.getUserArmlockCount();
                        userLeglocks += match.getUserLeglockCount();
                        oppChokes += match.getOppChokeCount();
                        oppArmlocks += match.getOppArmlockCount();
                        oppLeglocks += match.getOppLeglockCount();

                        // Convert int to float values
                        float userChokeFloat = (float) userChokes;
                        float userArmlockFloat = (float) userArmlocks;
                        float userLeglockFloat = (float) userLeglocks;
                        float oppChokeFloat = (float) oppChokes;
                        float oppArmlockFloat = (float) oppArmlocks;
                        float oppLeglockFloat = (float) oppLeglocks;

                        // Bar group data for user
                        final ArrayList<BarEntry> userBarGroup = new ArrayList<>();
                        userBarGroup.add(new BarEntry(userChokeFloat, TOTAL_CHOKES));
                        userBarGroup.add(new BarEntry(userArmlockFloat, TOTAL_ARMLOCKS));
                        userBarGroup.add(new BarEntry(userLeglockFloat, TOTAL_LEGLOCKS));
                        BarDataSet userBarDataSet = new BarDataSet(userBarGroup, "You");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            userBarDataSet.setColor(getActivity().getColor(R.color.colorPrimary));
                        }

                        // Bar group data for opponent
                        ArrayList<BarEntry> oppBarGroup = new ArrayList<>();
                        oppBarGroup.add(new BarEntry(oppChokeFloat, TOTAL_CHOKES));
                        oppBarGroup.add(new BarEntry(oppArmlockFloat, TOTAL_ARMLOCKS));
                        oppBarGroup.add(new BarEntry(oppLeglockFloat, TOTAL_LEGLOCKS));
                        BarDataSet oppBarDataSet = new BarDataSet(oppBarGroup, "Opponent");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            oppBarDataSet.setColor(getActivity().getColor(R.color.colorOrange));
                        }

                        // y-value data
                        ArrayList<BarDataSet> dataSets = new ArrayList<>();
                        dataSets.add(userBarDataSet);
                        dataSets.add(oppBarDataSet);

                        // x-value labels
                        ArrayList<String> labels = new ArrayList<>();
                        labels.add(getResources().getString(R.string.choke));
                        labels.add(getResources().getString(R.string.armlock));
                        labels.add(getResources().getString(R.string.leglock));

                        // Set data to bar chart
                        BarData barData = new BarData(labels, dataSets);
                        barData.setDrawValues(false);
                        mBarChart.setData(barData);

                        // Customize bar chart settings
                        mBarChart.setTouchEnabled(false);
                        mBarChart.setPinchZoom(false);
                        mBarChart.setDescription("");
                        mBarChart.getLegend().setTextSize(16f);
                        mBarChart.getXAxis().setTextSize(12f);

                        // Remove horizontal gridlines and numbers on the right
                        YAxis yAxis = mBarChart.getAxisLeft();
                        yAxis.setDrawGridLines(false);
                        yAxis = mBarChart.getAxisRight();
                        yAxis.setDrawGridLines(false);
                        yAxis.setDrawLabels(false);
                    }
                    mBarChart.invalidate();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            mBarChart.setNoDataText("");
            FIREBASE_DB_REF.addValueEventListener(mValueEventListener);
        }
    }

    private void detachDatabaseReadListener() {
        if (mValueEventListener != null) {
            FIREBASE_DB_REF.removeEventListener(mValueEventListener);
            mValueEventListener = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        detachDatabaseReadListener();
    }

    @Override
    public void onStop() {
        super.onStop();
        detachDatabaseReadListener();
    }
}
