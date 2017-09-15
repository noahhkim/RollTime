package com.noahkim.rolltime;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.noahkim.rolltime.data.Match;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.data;
import static android.R.attr.entries;
import static com.noahkim.rolltime.HomeFragment.FIREBASE_DB_REF;


/**
 * Created by noahkim on 9/13/17.
 */

public class HistoryFragment extends Fragment {
    @BindView(R.id.bar_chart)
    BarChart mBarChart;

    private static final int TOTAL_CHOKES = 0;
    private static final int TOTAL_ARMLOCKS = 1;
    private static final int TOTAL_LEGLOCKS = 2;

    private int mUserChokes = 0;

    private static final String LOG_TAG = HistoryFragment.class.getName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        ButterKnife.bind(this, rootView);

        setUpBarChart();

        return rootView;
    }

    private void setUpBarChart() {
        ArrayList<String> labels = new ArrayList<>();
        labels.add(getResources().getString(R.string.choke));
        labels.add(getResources().getString(R.string.armlock));
        labels.add(getResources().getString(R.string.leglock));

        ArrayList<BarEntry> userBarGroup = new ArrayList<>();
        float userChokeFloat = (float) getUserChokes();
        Log.d(LOG_TAG, "Int value = " + getUserChokes());
        Log.d(LOG_TAG, "Float value = " + userChokeFloat);
        userBarGroup.add(new BarEntry(userChokeFloat, TOTAL_CHOKES));
        userBarGroup.add(new BarEntry(14f, TOTAL_ARMLOCKS));
        userBarGroup.add(new BarEntry(23f, TOTAL_LEGLOCKS));
        BarDataSet barDataSet1 = new BarDataSet(userBarGroup, "You");
        barDataSet1.setColor(Color.rgb(104, 241, 175));

        ArrayList<BarEntry> oppBarGroup = new ArrayList<>();
        oppBarGroup.add(new BarEntry(22f, TOTAL_CHOKES));
        oppBarGroup.add(new BarEntry(15f, TOTAL_ARMLOCKS));
        oppBarGroup.add(new BarEntry(21f, TOTAL_LEGLOCKS));
        BarDataSet barDataSet2 = new BarDataSet(oppBarGroup, "Opponent");
        barDataSet2.setColor(Color.rgb(255, 102, 0));

        ArrayList<BarDataSet> dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        dataSets.add(barDataSet2);

        BarData theData = new BarData(labels, dataSets);
        mBarChart.setData(theData);

        mBarChart.setTouchEnabled(false);
        mBarChart.setPinchZoom(false);
    }

    private int getUserChokes() {
        FIREBASE_DB_REF.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Match match = postSnapshot.getValue(Match.class);
                    mUserChokes += match.getUserChokeCount();
                    Log.d(LOG_TAG, "Total user chokes = " + mUserChokes);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return mUserChokes;
    }
}
