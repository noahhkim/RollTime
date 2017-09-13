package com.noahkim.rolltime;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by noahkim on 8/24/17.
 */

public class MatchHolder extends RecyclerView.ViewHolder {
    private MatchHolder.ClickListener mClickListener;

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
        mUserChokeTextView.setText("Chokes: " + Integer.toString(userChokeCount));
    }

    public void setUserArmlockCount(int userArmlockCount) {
        mUserArmlockTextView.setText("Armlocks: " + Integer.toString(userArmlockCount));
    }

    public void setUserLeglockCount(int userLeglockCount) {
        mUserLeglockTextView.setText("Leglocks: " + Integer.toString(userLeglockCount));
    }

    public void setOppChokeCount(int oppChokeCount) {
        mOppChokeTextView.setText("Chokes: " + Integer.toString(oppChokeCount));
    }

    public void setOppArmlockCount(int oppArmlockCount) {
        mOppArmlockTextView.setText("Armlocks: " + Integer.toString(oppArmlockCount));
    }

    public void setOppLeglockCount(int oppLeglockCount) {
        mOppLeglockTextView.setText("Leglocks: " + Integer.toString(oppLeglockCount));
    }
}

