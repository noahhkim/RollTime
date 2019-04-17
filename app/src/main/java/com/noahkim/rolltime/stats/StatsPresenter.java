package com.noahkim.rolltime.stats;

import android.os.Build;
import androidx.annotation.NonNull;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.noahkim.rolltime.model.Match;

import java.util.ArrayList;
import java.util.List;

public class StatsPresenter {
    private View view;
    private DatabaseReference mDatabaseReference;
    private ValueEventListener mValueEventListener;

    private static final int TOTAL_CHOKES = 0;
    private static final int TOTAL_ARMLOCKS = 1;
    private static final int TOTAL_LEGLOCKS = 2;
    private String chokes, armlocks, leglocks;
    private int colorPrimary, colorGrey;

    public interface View {
        void setNoData(String string);
        void showBarChart();
        void hideBarChart();
        void showEmptyView();
        void hideEmptyView();
        void invalidateBarChart();
        void setBarData(BarData barData);
    }

    StatsPresenter(final View view, String chokes, String armlocks,
                   String leglocks, int colorPrimary,
                   int colorGrey) {
        this.chokes = chokes;
        this.armlocks = armlocks;
        this.leglocks = leglocks;
        this.colorPrimary = colorPrimary;
        this.colorGrey = colorGrey;
        this.view = view;

        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser mCurrentUser = mFirebaseAuth.getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users/" + mCurrentUser.getUid());

        attachDatabaseReadListener();

        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChildren()) {
                    view.showEmptyView();
                    view.hideBarChart();
                } else {
                    view.hideEmptyView();
                    view.showBarChart();
                    view.setNoData("");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void detachDatabaseReadListener() {
        if (mValueEventListener != null) {
            mDatabaseReference.removeEventListener(mValueEventListener);
            mValueEventListener = null;
        }
    }

    void onDestroyView() {
        view = null;
    }


    private void attachDatabaseReadListener() {
        if (mValueEventListener == null) {
            mValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Retrieve data from Firebase
                    int userChokes = 0;
                    int userArmlocks = 0;
                    int userLeglocks = 0;
                    int oppChokes = 0;
                    int oppArmlocks = 0;
                    int oppLeglocks = 0;
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Match match = postSnapshot.getValue(Match.class);
                        assert match != null;
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
                        final List<BarEntry> userBarGroup = new ArrayList<>();
                        userBarGroup.add(new BarEntry(userChokeFloat, TOTAL_CHOKES));
                        userBarGroup.add(new BarEntry(userArmlockFloat, TOTAL_ARMLOCKS));
                        userBarGroup.add(new BarEntry(userLeglockFloat, TOTAL_LEGLOCKS));
                        BarDataSet userBarDataSet = new BarDataSet(userBarGroup, "You");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            userBarDataSet.setColor(colorPrimary);
                        }

                        // Bar group data for opponent
                        final List<BarEntry> oppBarGroup = new ArrayList<>();
                        oppBarGroup.add(new BarEntry(oppChokeFloat, TOTAL_CHOKES));
                        oppBarGroup.add(new BarEntry(oppArmlockFloat, TOTAL_ARMLOCKS));
                        oppBarGroup.add(new BarEntry(oppLeglockFloat, TOTAL_LEGLOCKS));
                        BarDataSet oppBarDataSet = new BarDataSet(oppBarGroup, "Opponent");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            oppBarDataSet.setColor(colorGrey);
                        }

                        // y-value data
                        List<BarDataSet> dataSets = new ArrayList<>();
                        dataSets.add(userBarDataSet);
                        dataSets.add(oppBarDataSet);

                        // x-value labels
                        ArrayList<String> labels = new ArrayList<>();
                        labels.add(chokes);
                        labels.add(armlocks);
                        labels.add(leglocks);

                        // Set data to bar chart
                        BarData barData = new BarData(labels, dataSets);
                        barData.setDrawValues(false);
                        view.setBarData(barData);

                    }
                    view.invalidateBarChart();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            view.setNoData("");
            mDatabaseReference.addValueEventListener(mValueEventListener);
        }
    }

}
