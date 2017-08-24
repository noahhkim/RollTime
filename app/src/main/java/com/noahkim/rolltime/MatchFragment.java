package com.noahkim.rolltime;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.noahkim.rolltime.data.Match;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by noahkim on 8/16/17.
 */

public class MatchFragment extends Fragment {

    // Initialize EditText fields
    @BindView(R.id.edit_opponent_name)
    EditText mNameEditText;

    // Firebase instance variables
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    // Keep track of whether match info has been edited or not
    private boolean mInfoHasChanged = false;

    // If user is modifying the view, change the mInfoHasChanged boolean to true.
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mInfoHasChanged = true;
            return false;
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_details, container, false);
        ButterKnife.bind(this, rootView);
        setHasOptionsMenu(true);

        // Setup OnTouchListeners on all input fields
        mNameEditText.setOnTouchListener(mTouchListener);

        // Initialize Firebase components
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        mDatabaseReference = mFirebaseDatabase.getReference();

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_details, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_save:
                saveMatch();
                getActivity().finish();
                Toast.makeText(getActivity(), "Match saved!", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Get user input and save match into database
    private void saveMatch() {
        String nameString = mNameEditText.getText().toString().trim();
        Match matchDetails = new Match(nameString);
        mDatabaseReference.push().setValue(matchDetails);
    }

}
