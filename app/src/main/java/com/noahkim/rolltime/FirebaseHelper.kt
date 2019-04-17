package com.noahkim.rolltime

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

class FirebaseHelper {

    // Firebase instance variables
    private val mFirebaseAuth: FirebaseAuth

    init {
        mFirebaseAuth = FirebaseAuth.getInstance()
    }
}
