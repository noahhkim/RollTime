package com.noahkim.rolltime.stats;

import com.github.mikephil.charting.data.BarData;
import com.google.firebase.database.DataSnapshot;

public class StatsPresenter {
    private View view;


    StatsPresenter(View view) {
        this.view = view;

    }

    void setNoDataText(String emptyString){
        view.setNoData(emptyString);
    }

    void setBarData(BarData barData) {
        view.setBarData(barData);
    }

    public interface View {
        void setNoData(String string);
        void showBarChart();
        void hideBarChart();
        void showEmptyView();
        void hideEmptyView();
        void invalidateBarChart();
        void setBarData(BarData barData);
        void subscribe();
    }

}
