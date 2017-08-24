package com.noahkim.rolltime;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.noahkim.rolltime.data.Match;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by noahkim on 8/24/17.
 */

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.MatchHolder> {
    private List<Match> mMatches;
    private String mId;

    MatchAdapter(List<Match> matchDetails, String id) {
        mMatches = matchDetails;
        mId = id;
    }

    @Override
    public MatchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_matches, parent, false);
        return new MatchHolder(view);
    }

    @Override
    public void onBindViewHolder(MatchHolder holder, int position) {
        Match match = mMatches.get(position);
        holder.mOpponentNameTextView.setText(match.getOpponentName());
    }

    @Override
    public int getItemCount() {
        return mMatches.size();
    }

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
}
