package com.noahkim.rolltime.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.noahkim.rolltime.FirebaseHelper;
import com.noahkim.rolltime.R;
import com.noahkim.rolltime.fragments.HomeFragment;
import com.noahkim.rolltime.stats.StatsFragment;
import com.noahkim.rolltime.fragments.TimerFragment;
import com.noahkim.rolltime.fragments.VideosFragment;
import com.noahkim.rolltime.util.BottomNavigationViewHelper;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    // Initialize toolbar and fab
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.fab)
    FloatingActionButton mFab;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView mBottomNavView;

    public static final int RC_SIGN_IN = 1;

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
//    public static DatabaseReference FIREBASE_DB_REF;
    private FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        // Set up timber
        Timber.plant(new Timber.DebugTree());

//        firebaseHelper = FirebaseHelper.getFirebaseInstance();

        // Initialize Firebase
        mFirebaseAuth = FirebaseAuth.getInstance();

        // Create login authentication page
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // user is signed in
//                    FIREBASE_DB_REF = FIREBASE_DB.getReference().child("users/" + FIREBASE_USER.getUid());
                    if (savedInstanceState == null) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, new HomeFragment())
                                .commit();
                    }
                } else {
                    // user is signed out
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setTheme(R.style.GreenTheme)
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(
                                            Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build(),
                                                    new AuthUI.IdpConfig.FacebookBuilder().build(),
                                                    new AuthUI.IdpConfig.GoogleBuilder().build()))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };

        BottomNavigationViewHelper.removeShiftMode(mBottomNavView);

        // set bottom nav views to inflate fragments upon selection
        mBottomNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Fragment fragment = null;
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.container);
                switch (item.getItemId()) {
                    case R.id.action_home:
                        if (currentFragment instanceof HomeFragment) {
                            return false;
                        } else {
                            fragment = new HomeFragment();
                            mFab.setVisibility(View.VISIBLE);
                        }
                        break;
                    case R.id.action_stats:
                        fragment = new StatsFragment();
                        mFab.setVisibility(View.GONE);
                        break;
                    case R.id.action_timer:
                        fragment = new TimerFragment();
                        mFab.setVisibility(View.VISIBLE);
                        break;
                    case R.id.action_videos:
                        fragment = new VideosFragment();
                        mFab.setVisibility(View.VISIBLE);
                        break;
                }
                if (fragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, fragment, "TAG").commit();
                }
                return true;
            }
        });
    }

    // Close app if sign-in is cancelled
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // Sign-in succeeded, set up the UI
                Toast.makeText(this, getString(R.string.signed_in), Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                // Sign in was canceled by the user, finish the activity
                Toast.makeText(this, getString(R.string.sign_in_cancelled), Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    // FAB opens EditMatchActivity
    @OnClick(R.id.fab)
    public void onFabClick(View view) {
        Intent intent = new Intent(MainActivity.this, EditMatchActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_sign_out:
                AuthUI.getInstance().signOut(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }
}
