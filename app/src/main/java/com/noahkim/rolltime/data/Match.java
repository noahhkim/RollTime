package com.noahkim.rolltime.data;

/**
 * Created by noahkim on 8/23/17.
 */

public class Match {

    private String mOpponentName;

    public Match(String opponentName) {
        this.mOpponentName = opponentName;
    }

    public String getOpponentName() {
        return mOpponentName;
    }

    public void setOpponentName(String name) {
        mOpponentName = name;
    }
}
