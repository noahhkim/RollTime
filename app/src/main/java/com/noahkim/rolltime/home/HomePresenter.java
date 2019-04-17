package com.noahkim.rolltime.home;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomePresenter {
    private View view;
    private DatabaseReference mDatabaseReference;
    private ValueEventListener mValueEventListener;

    public interface View {
        void showEmptyView();

        void hideEmptyView();

        void showRecyclerView();

        void hideRecyclerView();
    }

    HomePresenter(final View view) {
        this.view = view;
        // Initialize Firebase
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser mCurrentUser = mFirebaseAuth.getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users/" + mCurrentUser.getUid());

        setEmptyView();
    }

    private void setEmptyView() {
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChildren()) {
                    view.showEmptyView();
                    view.hideRecyclerView();
                } else {
                    view.hideEmptyView();
                    view.showRecyclerView();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void onDestroyView() {
        view = null;
    }

}
