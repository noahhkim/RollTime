package com.noahkim.rolltime;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * Created by noahkim on 8/16/17.
 */

public class RecordMatchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_match);
        ButterKnife.bind(this);

        // inflate DetailFragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.record_match_container, new DetailFragment())
                    .commit();
        }
    }
}
