package com.noahkim.rolltime;

import com.github.mikephil.charting.data.BarData;

public class StatsPresenter {
    private View view;


    public StatsPresenter(View view) {
        this.view = view;
    }

    public void setNoDataText(String emptyString){
        view.setNoData(emptyString);
    }

    public void setBarData(BarData barData) {
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
    }

}
