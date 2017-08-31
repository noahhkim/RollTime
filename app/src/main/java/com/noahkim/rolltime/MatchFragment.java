package com.noahkim.rolltime;


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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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

    // Initialize fields
    @BindView(R.id.edit_opponent_name)
    EditText mNameEditText;
    @BindView(R.id.belts_spinner)
    Spinner mBeltSpinner;
    @BindView(R.id.edit_choke)
    EditText mChokeEditText;
    @BindView(R.id.edit_armlock)
    EditText mArmlockEditText;
    @BindView(R.id.edit_leglock)
    EditText mLeglockEditText;

    private View rootView;

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

    // Submission type. Default is choke
//    private int mSubmissionType = Match.CHOKE;

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
        rootView = inflater.inflate(R.layout.fragment_input_details, container, false);
        ButterKnife.bind(this, rootView);
        setHasOptionsMenu(true);

        // Setup OnTouchListeners on all input fields
        mNameEditText.setOnTouchListener(mTouchListener);

        // Initialize Firebase components
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        // Set up reference to database
        mDatabaseReference = mFirebaseDatabase.getReference().child("matches");

        // Set up spinners
        setUpBeltSpinner();
//        setUpSubmissionSpinner();

        return rootView;
    }

    private void setUpBeltSpinner() {
        // Set up belt spinner
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

//    // Set up submission type spinner
//    private void setUpSubmissionSpinner() {
//        // Create adapter and attach to spinner
//        ArrayAdapter submissionSpinnerAdapter = ArrayAdapter.createFromResource(
//                getActivity(), R.array.array_submssion_types, android.R.layout.simple_spinner_item);
//        submissionSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
//        mSubmissionTypeTextView.setAdapter(submissionSpinnerAdapter);
//
//        // Set integer mSelected to constant values
//        mSubmissionTypeTextView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                String selection = (String) adapterView.getItemAtPosition(i);
//                if (!TextUtils.isEmpty(selection)) {
//                    if (selection.equals(getString(R.string.armlock))) {
//                        mSubmissionType = Match.ARM_LOCK;
//                    } else if (selection.equals(getString(R.string.leglock))) {
//                        mSubmissionType = Match.LEG_LOCK;
//                    } else  {
//                        mSubmissionType = Match.CHOKE;
//                    }
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//                mSubmissionType = Match.CHOKE;
//            }
//        });
//    }

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

    private void saveMatch() {
        // Read from input fields
        String nameString = mNameEditText.getText().toString().trim();
        String chokeString = mChokeEditText.getText().toString().trim();
        String armlockString = mArmlockEditText.getText().toString().trim();
        String leglockString = mLeglockEditText.getText().toString().trim();

        // If the submission count is not provided by the user, don't try to parse the string into an
        // integer value. Use 0 by default.
        int chokeCount = 0;
        if (!TextUtils.isEmpty(chokeString)) {
            chokeCount = Integer.parseInt(chokeString);
        }
        // If the submission count is not provided by the user, don't try to parse the string into an
        // integer value. Use 0 by default.
        int armlockCount = 0;
        if (!TextUtils.isEmpty(chokeString)) {
            armlockCount = Integer.parseInt(chokeString);
        }
        // If the submission count is not provided by the user, don't try to parse the string into an
        // integer value. Use 0 by default.
        int leglockCount = 0;
        if (!TextUtils.isEmpty(chokeString)) {
            leglockCount = Integer.parseInt(chokeString);
        }

        Match matchDetails = new Match(
                nameString, mBeltLevel, chokeCount);

        mDatabaseReference.push().setValue(matchDetails);
    }

    private void addEditText() {

    }
}
