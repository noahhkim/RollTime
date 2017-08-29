package com.noahkim.rolltime.data;

/**
 * Created by noahkim on 8/23/17.
 */

public class Match {
    public static int WHITE_BELT = 0;
    public static int BLUE_BELT = 1;
    public static int PURPLE_BELT = 2;
    public static int BROWN_BELT = 3;
    public static int BLACK_BELT = 4;

    private String mOpponentName;
    private int mBeltLevel;


    public Match() {
        // Needed for Firebase
    }

    public Match(String opponentName, int beltLevel) {
        mOpponentName = opponentName;
        mBeltLevel = beltLevel;
    }

    public String getOpponentName() {
        return mOpponentName;
    }

    public void setOpponentName(String name) {
        mOpponentName = name;
    }

    public int getBeltLevel() {
        return mBeltLevel;
    }

    public void setBeltLevel(int beltLevel) {
        mBeltLevel = beltLevel;
    }
}
