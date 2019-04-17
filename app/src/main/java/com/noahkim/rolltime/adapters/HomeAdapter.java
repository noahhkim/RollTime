package com.noahkim.rolltime.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.noahkim.rolltime.R;
import com.noahkim.rolltime.model.Match;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    private List<Match> matches;

    private int[] beltArray = {
            R.drawable.ic_bjj_white_belt,
            R.drawable.ic_bjj_blue_belt,
            R.drawable.ic_bjj_purple_belt,
            R.drawable.ic_bjj_brown_belt,
            R.drawable.ic_bjj_black_belt
    };

    public HomeAdapter(List<Match> matches) {
        this.matches = matches;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.matches_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Match match = matches.get(position);
        holder.mUserBeltView.setImageResource(beltArray[2]);
        holder.mOpponentNameTextView.setText(match.getOppName());
        holder.mOpponentBeltView.setImageResource(beltArray[match.getOppBeltLevel()]);
        holder.mUserChokeTextView.setText(String.valueOf(match.getUserChokeCount()));
        holder.mUserArmlockTextView.setText(String.valueOf(match.getUserArmlockCount()));
        holder.mUserLeglockTextView.setText(String.valueOf(match.getUserLeglockCount()));
        holder.mOppChokeTextView.setText(String.valueOf(match.getOppChokeCount()));
        holder.mOppArmlockTextView.setText(String.valueOf(match.getOppArmlockCount()));
        holder.mOppLeglockTextView.setText(String.valueOf(match.getOppLeglockCount()));
    }


    @Override
    public int getItemCount() {
        return matches.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.user_belt_level)
        ImageView mUserBeltView;
        @BindView(R.id.opponent_name)
        TextView mOpponentNameTextView;
        @BindView(R.id.opponent_belt)
        ImageView mOpponentBeltView;
        @BindView(R.id.user_choke_count)
        TextView mUserChokeTextView;
        @BindView(R.id.user_armlock_count)
        TextView mUserArmlockTextView;
        @BindView(R.id.user_leglock_count)
        TextView mUserLeglockTextView;
        @BindView(R.id.opp_choke_count)
        TextView mOppChokeTextView;
        @BindView(R.id.opp_armlock_count)
        TextView mOppArmlockTextView;
        @BindView(R.id.opp_leglock_count)
        TextView mOppLeglockTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
