package com.noahkim.rolltime.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.noahkim.rolltime.R;
import com.noahkim.rolltime.adapters.SpinnerAdapter;
import com.noahkim.rolltime.data.Match;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.noahkim.rolltime.ui.activity.MainActivity.FIREBASE_DB_REF;

/**
 * Created by noahkim on 8/16/17.
 */

public class EditMatchActivity extends AppCompatActivity {

    // Initialize fields
    @BindView(R.id.edit_opponent_name)
    EditText mNameEditText;
    @BindView(R.id.belts_spinner)
    Spinner mBeltSpinner;
    @BindView(R.id.user_choke_increase_button)
    ImageButton mUserChokeIncreaseButton;
    @BindView(R.id.user_choke_decrease_button)
    ImageButton mUserChokeDecreaseButton;
    @BindView(R.id.user_choke_quantity)
    TextView mUserChokeQuantity;
    @BindView(R.id.user_armlock_increase_button)
    ImageButton mUserArmlockIncreaseButton;
    @BindView(R.id.user_armlock_decrease_button)
    ImageButton mUserArmlockDecreaseButton;
    @BindView(R.id.user_armlock_quantity)
    TextView mUserArmlockQuantity;
    @BindView(R.id.user_leglock_increase_button)
    ImageButton mUserLeglockIncreaseButton;
    @BindView(R.id.user_leglock_decrease_button)
    ImageButton mUserLeglockDecreaseButton;
    @BindView(R.id.user_leglock_quantity)
    TextView mUserLeglockQuantity;
    @BindView(R.id.opp_choke_increase_button)
    ImageButton mOppChokeIncreaseButton;
    @BindView(R.id.opp_choke_decrease_button)
    ImageButton mOppChokeDecreaseButton;
    @BindView(R.id.opp_choke_quantity)
    TextView mOppChokeQuantity;
    @BindView(R.id.opp_armlock_increase_button)
    ImageButton mOppArmlockIncreaseButton;
    @BindView(R.id.opp_armlock_decrease_button)
    ImageButton mOppArmlockDecreaseButton;
    @BindView(R.id.opp_armlock_quantity)
    TextView mOppArmlockQuantity;
    @BindView(R.id.opp_leglock_increase_button)
    ImageButton mOppLeglockIncreaseButton;
    @BindView(R.id.opp_leglock_decrease_button)
    ImageButton mOppLeglockDecreaseButton;
    @BindView(R.id.opp_leglock_quantity)
    TextView mOppLeglockQuantity;

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

    private int mQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_match);
        ButterKnife.bind(this);

        setUpBeltSpinner();

        Intent intent = getIntent();
        mCurrentMatchUri = intent.getData();

        if (mCurrentMatchUri == null) {
            // This is a new match, so change the app bar to say "Add a Match"
            setTitle(getString(R.string.edit_activity_title_new_match));
        } else {
            // This is a previously saved match, so change app bar to say "Edit Match"
            // and also display saved info
            setTitle(getString(R.string.edit_activity_title_edit_match));
            final String matchKey = mCurrentMatchUri.toString();
            FIREBASE_DB_REF.child(matchKey).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Match match = dataSnapshot.getValue(Match.class);
                    mNameEditText.setText(match.getOppName());
                    mBeltSpinner.setSelection(match.getOppBeltLevel());
                    mUserChokeQuantity.setText(String.valueOf(match.getUserChokeCount()));
                    mUserArmlockQuantity.setText(String.valueOf(match.getUserArmlockCount()));
                    mUserLeglockQuantity.setText(String.valueOf(match.getUserLeglockCount()));
                    mOppChokeQuantity.setText(String.valueOf(match.getOppChokeCount()));
                    mOppArmlockQuantity.setText(String.valueOf(match.getOppArmlockCount()));
                    mOppLeglockQuantity.setText(String.valueOf(match.getOppLeglockCount()));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        setUpButtonClickListeners();

        // Set up OnTouchListeners on input fields
        mNameEditText.setOnTouchListener(mTouchListener);


    }

    private void setUpButtonClickListeners() {
        // Set up onClickListeners for increase and decrease buttons
        mUserChokeIncreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mQuantity = Integer.valueOf(String.valueOf(mUserChokeQuantity.getText()));
                mQuantity++;
                String quantity = "" + mQuantity;
                mUserChokeQuantity.setText(quantity);
            }
        });
        mUserChokeDecreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mQuantity = Integer.valueOf(String.valueOf(mUserChokeQuantity.getText()));
                if (mQuantity == 0) {
                    return;
                }
                mQuantity--;
                String quantity = "" + mQuantity;
                mUserChokeQuantity.setText(quantity);
            }
        });
        mUserArmlockIncreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mQuantity = Integer.valueOf(String.valueOf(mUserArmlockQuantity.getText()));
                mQuantity++;
                String quantity = "" + mQuantity;
                mUserArmlockQuantity.setText(quantity);
            }
        });
        mUserArmlockDecreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mQuantity = Integer.valueOf(String.valueOf(mUserArmlockQuantity.getText()));
                if (mQuantity == 0) {
                    return;
                }
                mQuantity--;
                String quantity = "" + mQuantity;
                mUserArmlockQuantity.setText(quantity);
            }
        });
        mUserLeglockIncreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mQuantity = Integer.valueOf(String.valueOf(mUserLeglockQuantity.getText()));
                mQuantity++;
                String quantity = "" + mQuantity;
                mUserLeglockQuantity.setText(quantity);
            }
        });
        mUserLeglockDecreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mQuantity = Integer.valueOf(String.valueOf(mUserLeglockQuantity.getText()));
                if (mQuantity == 0) {
                    return;
                }
                mQuantity--;
                String quantity = "" + mQuantity;
                mUserLeglockQuantity.setText(quantity);
            }
        });
        mOppChokeIncreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mQuantity = Integer.valueOf(String.valueOf(mOppChokeQuantity.getText()));
                mQuantity++;
                String quantity = "" + mQuantity;
                mOppChokeQuantity.setText(quantity);
            }
        });
        mOppChokeDecreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mQuantity = Integer.valueOf(String.valueOf(mOppChokeQuantity.getText()));
                if (mQuantity == 0) {
                    return;
                }
                mQuantity--;
                String quantity = "" + mQuantity;
                mOppChokeQuantity.setText(quantity);
            }
        });
        mOppArmlockIncreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mQuantity = Integer.valueOf(String.valueOf(mOppArmlockQuantity.getText()));
                mQuantity++;
                String quantity = "" + mQuantity;
                mOppArmlockQuantity.setText(quantity);
            }
        });
        mOppArmlockDecreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mQuantity = Integer.valueOf(String.valueOf(mOppArmlockQuantity.getText()));
                if (mQuantity == 0) {
                    return;
                }
                mQuantity--;
                String quantity = "" + mQuantity;
                mOppArmlockQuantity.setText(quantity);
            }
        });
        mOppLeglockIncreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mQuantity = Integer.valueOf(String.valueOf(mOppLeglockQuantity.getText()));
                mQuantity++;
                String quantity = "" + mQuantity;
                mOppLeglockQuantity.setText(quantity);
            }
        });
        mOppLeglockDecreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mQuantity = Integer.valueOf(String.valueOf(mOppLeglockQuantity.getText()));
                if (mQuantity == 0) {
                    return;
                }
                mQuantity--;
                String quantity = "" + mQuantity;
                mOppLeglockQuantity.setText(quantity);
            }
        });
    }

    private void setUpBeltSpinner() {
        // Set up belt spinner
        mBeltSpinner.setAdapter(new SpinnerAdapter(this, beltArray));
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_match, menu);
        return true;
    }

    /**
     * This method is called after invalidateOptionsMenu(), so that the
     * menu can be updated (some menu items can be hidden or made visible).
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mCurrentMatchUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_save:
                saveMatch();
                finish();
                return true;
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;
            case android.R.id.home:
                if (!mInfoHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditMatchActivity.this);
                    return true;
                }
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavUtils.navigateUpFromSameTask(EditMatchActivity.this);
                            }
                        };
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // This method is called when the back button is pressed.
    @Override
    public void onBackPressed() {
        if (!mInfoHasChanged) {
            super.onBackPressed();
            return;
        }

        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                };
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    // save match to FRD
    private void saveMatch() {
        // Read from input fields
        String oppNameString = mNameEditText.getText().toString().trim();
        String userChokeString = mUserChokeQuantity.getText().toString().trim();
        String userArmlockString = mUserArmlockQuantity.getText().toString().trim();
        String userLeglockString = mUserLeglockQuantity.getText().toString().trim();
        String oppChokeString = mOppChokeQuantity.getText().toString().trim();
        String oppArmlockString = mOppArmlockQuantity.getText().toString().trim();
        String oppLeglockString = mOppLeglockQuantity.getText().toString().trim();

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

        if (mCurrentMatchUri == null) {
            // New match, so add data to database
            FIREBASE_DB_REF.push().setValue(matchDetails);
        } else {
            // Previously stored match, so update data to database
            Map<String, Object> matchValues = matchDetails.toMap();
            Map<String, Object> matchUpdates = new HashMap<>();
            matchUpdates.put(mCurrentMatchUri.toString(), matchValues);
            FIREBASE_DB_REF.updateChildren(matchUpdates);
        }
    }

    /**
     * Show a dialog that warns the user there are unsaved changes that will be lost
     * if they continue leaving the editor.
     *
     * @param discardButtonClickListener is the click listener for what to do when
     *                                   the user confirms they want to discard their changes
     */
    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    // Prompt user to confirm that they want to delete this entry
    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteMatch();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    // delete match from FRD
    private void deleteMatch() {
        if (mCurrentMatchUri != null) {
            FIREBASE_DB_REF.child(mCurrentMatchUri.toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    FIREBASE_DB_REF.child(mCurrentMatchUri.toString()).removeValue();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        finish();
    }
}
