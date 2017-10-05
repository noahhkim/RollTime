package com.noahkim.rolltime.util;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.noahkim.rolltime.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by noahkim on 8/24/17.
 */

public class MatchHolder extends RecyclerView.ViewHolder {
    private MatchHolder.ClickListener mClickListener;
    private Context mContext;

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

    // Interface to send callbacks
    public interface ClickListener {
        public void onItemClick(View view, int position);
        public void onItemLongClick(View view, int position);
    }

    public void setOnClickListener(MatchHolder.ClickListener clickListener) {
        mClickListener = clickListener;
    }

    public MatchHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();
        ButterKnife.bind(this, itemView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mClickListener.onItemLongClick(view, getAdapterPosition());
                return true;
            }
        });
    }

    public void setName(String name) {
        mOpponentNameTextView.setText(name);
    }

    public void setBeltLevel(int beltImage) {
        mOpponentBeltView.setImageResource(beltImage);
    }

    public void setUserChokeCount(int userChokeCount) {
        String userChokeText = mContext.getString(R.string.choke) + ": " + Integer.toString(userChokeCount);
        mUserChokeTextView.setText(userChokeText);
    }

    public void setUserArmlockCount(int userArmlockCount) {
        String userArmlockText = mContext.getString(R.string.armlock) + ": " + Integer.toString(userArmlockCount);
        mUserArmlockTextView.setText(userArmlockText);
    }

    public void setUserLeglockCount(int userLeglockCount) {
        String userLeglockText = mContext.getString(R.string.leglock) + ": " + Integer.toString(userLeglockCount);
        mUserLeglockTextView.setText(userLeglockText);
    }

    public void setOppChokeCount(int oppChokeCount) {
        String oppChokeText = mContext.getString(R.string.choke) + ": " + Integer.toString(oppChokeCount);
        mOppChokeTextView.setText(oppChokeText);
    }

    public void setOppArmlockCount(int oppArmlockCount) {
        String oppArmlockText = mContext.getString(R.string.armlock) + ": " + Integer.toString(oppArmlockCount);
        mOppArmlockTextView.setText(oppArmlockText);
    }

    public void setOppLeglockCount(int oppLeglockCount) {
        String oppLeglockText = mContext.getString(R.string.leglock) + ": " + Integer.toString(oppLeglockCount);
        mOppLeglockTextView.setText(oppLeglockText);
    }
}
