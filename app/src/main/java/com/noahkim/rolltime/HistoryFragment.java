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

    private ValueEventListener mValueEventListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
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
                        BarDataSet barDataSet1 = new BarDataSet(userBarGroup, "You");
                        barDataSet1.setColor(Color.rgb(104, 241, 175));

                        // Bar group data for opponent
                        ArrayList<BarEntry> oppBarGroup = new ArrayList<>();
                        oppBarGroup.add(new BarEntry(oppChokeFloat, TOTAL_CHOKES));
                        oppBarGroup.add(new BarEntry(oppArmlockFloat, TOTAL_ARMLOCKS));
                        oppBarGroup.add(new BarEntry(oppLeglockFloat, TOTAL_LEGLOCKS));
                        BarDataSet barDataSet2 = new BarDataSet(oppBarGroup, "Opponent");
                        barDataSet2.setColor(Color.rgb(255, 102, 0));

                        // y-value data
                        ArrayList<BarDataSet> dataSets = new ArrayList<>();
                        dataSets.add(barDataSet1);
                        dataSets.add(barDataSet2);

                        // x-value labels
                        ArrayList<String> labels = new ArrayList<>();
                        labels.add(getResources().getString(R.string.choke));
                        labels.add(getResources().getString(R.string.armlock));
                        labels.add(getResources().getString(R.string.leglock));

                        BarData theData = new BarData(labels, dataSets);
                        mBarChart.setData(theData);

                        mBarChart.setTouchEnabled(false);
                        mBarChart.setPinchZoom(false);

                        mBarChart.invalidate();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
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
