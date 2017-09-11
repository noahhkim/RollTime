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

    private String mOppName;
    private int mOppBeltLevel;
    private int mUserChokeCount;
    private int mUserArmlockCount;
    private int mUserLeglockCount;
    private int mOppChokeCount;
    private int mOppArmlockCount;
    private int mOppLeglockCount;

    public Match() {
        // Needed for Firebase
    }

    public Match(String oppName, int oppBeltLevel, int userChokeCount, int userArmlockCount,
                 int userLeglockCount, int oppChokeCount, int oppArmlockCount, int oppLeglockCount) {
        mOppName = oppName;
        mOppBeltLevel = oppBeltLevel;
        mUserChokeCount = userChokeCount;
        mUserArmlockCount = userArmlockCount;
        mUserLeglockCount = userLeglockCount;
        mOppChokeCount = oppChokeCount;
        mOppArmlockCount = oppArmlockCount;
        mOppLeglockCount = oppLeglockCount;
    }

    public String getOppName() {
        return mOppName;
    }

    public void setOppName(String name) {
        mOppName = name;
    }

    public int getOppBeltLevel() {
        return mOppBeltLevel;
    }

    public void setOppBeltLevel(int oppBeltLevel) {
        mOppBeltLevel = oppBeltLevel;
    }

    public int getUserChokeCount() {
        return mUserChokeCount;
    }

    public void setUserChokeCount(int userChokeCount) {
        mUserChokeCount = userChokeCount;
    }

    public int getUserArmlockCount() {
        return mUserArmlockCount;
    }

    public void setUserArmlockCount(int userArmlockCount) {
        mUserArmlockCount = userArmlockCount;
    }

    public int getUserLeglockCount() {
        return mUserLeglockCount;
    }

    public void setUserLeglockCount(int userLeglockCount) {
        mUserLeglockCount = userLeglockCount;
    }

    public int getOppChokeCount() {
        return mOppChokeCount;
    }

    public void setOppChokeCount(int oppChokeCount) {
        mOppChokeCount = oppChokeCount;
    }

    public int getOppArmlockCount() {
        return mOppArmlockCount;
    }

    public void setOppArmlockCount(int oppArmlockCount) {
        mOppArmlockCount = oppArmlockCount;
    }

    public int getOppLeglockCount() {
        return mOppLeglockCount;
    }

    public void setOppLeglockCount(int oppLeglockCount) {
        mOppLeglockCount = oppLeglockCount;
    }

}
