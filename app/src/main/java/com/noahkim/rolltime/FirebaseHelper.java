package com.noahkim.rolltime;

import android.content.Context;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class FirebaseHelper {

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private DatabaseReference mDatabaseReference;
    private static FirebaseHelper INSTANCE;


    public static FirebaseHelper getFirebaseInstance(){
        if (INSTANCE == null) {
            INSTANCE = new FirebaseHelper();
        }
        return INSTANCE;
    }

    public FirebaseHelper(){
        mFirebaseAuth = FirebaseAuth.getInstance();
    }


    public void signOut(Context context) {
        AuthUI.getInstance().signOut(context);
    }
}
