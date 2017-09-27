package com.noahkim.rolltime.widget;

import android.content.Context;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.noahkim.rolltime.R;
import com.noahkim.rolltime.data.Match;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Noah on 9/26/2017.
 */

public class RTWidgetViewFactory implements RemoteViewsService.RemoteViewsFactory {
    private static final String LOG_TAG = RTWidgetViewFactory.class.getName();

    // Firebase global variables
    private FirebaseDatabase mFirebaseDB;
    private DatabaseReference mDatabaseReference;

    private Context mContext;
    private List<Match> mMatches = new ArrayList<>();
    private CountDownLatch mCountDownLatch;


    public RTWidgetViewFactory(Context context) {
        mContext = context;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        mCountDownLatch = new CountDownLatch(1);
        populateListItem();
        try {
            mCountDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mMatches.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        final Match currentMatch = mMatches.get(position);
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item);

//        // Inflate widget layout with matches
//        remoteViews.removeAllViews(R.id.widget_recent_matches_list);
//        for (int i = 0; i < mMatches.size(); i++) {
//            RemoteViews matchRemoteView = new RemoteViews(mContext.getPackageName(), R.layout.list_item_matches);
//            matchRemoteView.setTextViewText(R.id.opponent_name, currentMatch.getOppName());
//            remoteViews.addView(R.id.widget_recent_matches_list, matchRemoteView);
//        }

        for (int i = 0; i < mMatches.size(); i++) {
            remoteViews.setTextViewText(R.id.widget_opp_name, currentMatch.getOppName());
        }
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private void populateListItem() {
        mFirebaseDB = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDB.getReference().child("matches");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Match match = postSnapshot.getValue(Match.class);
                    mMatches.add(match);
                    mCountDownLatch.countDown();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
