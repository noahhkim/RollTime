package com.noahkim.rolltime;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by noahkim on 8/24/17.
 */

public class MatchHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.opponent_name)
    TextView mOpponentNameTextView;

    public MatchHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setName(String name) {
        mOpponentNameTextView.setText(name);
    }
}

