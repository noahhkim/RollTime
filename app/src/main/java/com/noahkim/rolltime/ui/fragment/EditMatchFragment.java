package com.noahkim.rolltime.ui.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.noahkim.rolltime.R;
import com.noahkim.rolltime.SpinnerAdapter;
import com.noahkim.rolltime.data.Match;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.noahkim.rolltime.ui.fragment.HomeFragment.FIREBASE_DB_REF;

/**
 * Created by noahkim on 8/16/17.
 */

public class EditMatchFragment extends Fragment {
    public static final String LOG_TAG = EditMatchFragment.class.getName();

    // Initialize fields
    @BindView(R.id.edit_opponent_name)
    EditText mNameEditText;
    @BindView(R.id.belts_spinner)
    Spinner mBeltSpinner;
    @BindView(R.id.edit_user_choke)
    EditText mUserChokeEditText;
    @BindView(R.id.edit_user_armlock)
    EditText mUserArmlockEditText;
    @BindView(R.id.edit_user_leglock)
    EditText mUserLeglockEditText;
    @BindView(R.id.edit_opp_choke)
    EditText mOppChokeEditText;
    @BindView(R.id.edit_opp_armlock)
    EditText mOppArmlockEditText;
    @BindView(R.id.edit_opp_leglock)
    EditText mOppLeglockEditText;

    private View rootView;

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

    // Uri of selected match
    private Uri mCurrentMatchUri;

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
        rootView = inflater.inflate(R.layout.fragment_edit_match, container, false);
        ButterKnife.bind(this, rootView);
        setHasOptionsMenu(true);
        setUpBeltSpinner();

        Intent intent = getActivity().getIntent();
        mCurrentMatchUri = intent.getData();

        if (mCurrentMatchUri == null) {
            // This is a new match, so change the app bar to say "Add a Match"
            getActivity().setTitle(getString(R.string.edit_activity_title_new_match));
        } else {
            // This is a previously saved match, so change app bar to say "Edit Match"
            // and also display saved info
            getActivity().setTitle(getString(R.string.edit_activity_title_edit_match));
            final String matchKey = mCurrentMatchUri.toString();
            Log.v(LOG_TAG, "Firebase key: " + matchKey);
            FIREBASE_DB_REF.child(matchKey).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Match match = dataSnapshot.getValue(Match.class);
                    mNameEditText.setText(match.getOppName());
                    mBeltSpinner.setSelection(match.getOppBeltLevel());
                    mUserChokeEditText.setText(String.valueOf(match.getUserChokeCount()));
                    mUserArmlockEditText.setText(String.valueOf(match.getUserArmlockCount()));
                    mUserLeglockEditText.setText(String.valueOf(match.getUserLeglockCount()));
                    mOppChokeEditText.setText(String.valueOf(match.getOppChokeCount()));
                    mOppArmlockEditText.setText(String.valueOf(match.getOppArmlockCount()));
                    mOppLeglockEditText.setText(String.valueOf(match.getOppLeglockCount()));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        // Setup OnTouchListeners on all input fields
        mNameEditText.setOnTouchListener(mTouchListener);
        mUserChokeEditText.setOnTouchListener(mTouchListener);

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
        String oppNameString = mNameEditText.getText().toString().trim();
        String userChokeString = mUserChokeEditText.getText().toString().trim();
        String userArmlockString = mUserArmlockEditText.getText().toString().trim();
        String userLeglockString = mUserLeglockEditText.getText().toString().trim();
        String oppChokeString = mOppChokeEditText.getText().toString().trim();
        String oppArmlockString = mOppArmlockEditText.getText().toString().trim();
        String oppLeglockString = mOppLeglockEditText.getText().toString().trim();

        // Check if the name field is blank
        if (TextUtils.isEmpty(oppNameString)) {
            // If name field is blank, exit without saving to database
            return;
        }

        // If the submission count is not provided by the user, don't try to parse the string into an
        // integer value. Use 0 by default.
        int userChokeCount = 0;
        if (!TextUtils.isEmpty(userChokeString)) {
            userChokeCount = Integer.parseInt(userChokeString);
        }

        int userArmlockCount = 0;
        if (!TextUtils.isEmpty(userArmlockString)) {
            userArmlockCount = Integer.parseInt(userArmlockString);
        }

        int userLeglockCount = 0;
        if (!TextUtils.isEmpty(userLeglockString)) {
            userLeglockCount = Integer.parseInt(userLeglockString);
        }

        int oppChokeCount = 0;
        if (!TextUtils.isEmpty(oppChokeString)) {
            oppChokeCount = Integer.parseInt(oppChokeString);
        }

        int oppArmlockCount = 0;
        if (!TextUtils.isEmpty(oppArmlockString)) {
            oppArmlockCount = Integer.parseInt(oppArmlockString);
        }

        int oppLeglockCount = 0;
        if (!TextUtils.isEmpty(oppLeglockString)) {
            oppLeglockCount = Integer.parseInt(oppLeglockString);
        }

        Match matchDetails = new Match(
                oppNameString,
                mBeltLevel,
                userChokeCount,
                userArmlockCount,
                userLeglockCount,
                oppChokeCount,
                oppArmlockCount,
                oppLeglockCount);

        FIREBASE_DB_REF.push().setValue(matchDetails);
    }
}
