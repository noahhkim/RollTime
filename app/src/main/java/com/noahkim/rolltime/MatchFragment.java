package com.noahkim.rolltime;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.noahkim.rolltime.data.Match;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by noahkim on 8/16/17.
 */

public class MatchFragment extends Fragment {

    // Initialize fields
    @BindView(R.id.edit_opponent_name)
    EditText mNameEditText;
    @BindView(R.id.belts_spinner)
    Spinner mBeltSpinner;

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

    // Belt level. Default level is white
    private int mBeltLevel = Match.WHITE_BELT;

    // Array of belt levels
    private int[] beltArray = {
            R.drawable.ic_bjj_white_belt,
            R.drawable.ic_bjj_blue_belt,
            R.drawable.ic_bjj_purple_belt,
            R.drawable.ic_bjj_brown_belt,
            R.drawable.ic_bjj_black_belt
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

        // Set up reference to database
        mDatabaseReference = mFirebaseDatabase.getReference();

        setUpSpinner();

        return rootView;
    }

    // Set up custom spinner
    private void setUpSpinner() {
        mBeltSpinner.setAdapter(new SpinnerAdapter(getActivity(), beltArray));
        mBeltSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                int selection = parent.getSelectedItemPosition();
                switch (selection) {
                    case 0:
                        mBeltLevel = Match.WHITE_BELT;
                        break;
                    case 1:
                        mBeltLevel = Match.BLUE_BELT;
                        break;
                    case 2:
                        mBeltLevel = Match.PURPLE_BELT;
                        break;
                    case 3:
                        mBeltLevel = Match.BROWN_BELT;
                        break;
                    case 4:
                        mBeltLevel = Match.BLACK_BELT;
                        break;
                    default:
                        mBeltLevel = Match.WHITE_BELT;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mBeltLevel = Match.WHITE_BELT;
            }
        });
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
        Match matchDetails = new Match(nameString, mBeltLevel);
        mDatabaseReference.push().setValue(matchDetails);
    }


}
