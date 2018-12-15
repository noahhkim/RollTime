package com.noahkim.rolltime;

import android.view.View;

public class StatsPresenter {
    private View view;


    public StatsPresenter(View view) {
        this.view = view;
    }

    public interface View {
        void showBarChart();
        void hideBarChart();
    }

}
