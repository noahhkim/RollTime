package com.noahkim.rolltime.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber

// object in Kotlin is the same as Java singleton pattern
object MatchesRepository {
  // Initialize Firebase
  private val firebaseAuth = FirebaseAuth.getInstance()

  private val currentUser = firebaseAuth.currentUser

  private val subject = BehaviorSubject.create<List<Match>>()

  //this observable is what the next class consumes
  val observable: Observable<List<Match>> = subject.hide().subscribeOn(Schedulers.io())

  //this is essentially a constructor, the init method in Kotlin gets called when the
  //    class is first created
  init {
    FirebaseDatabase.getInstance().reference
        .child("users/" + currentUser!!.uid)
        .apply {
          addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
              Timber.e("error hooking up database")
            }

            override fun onDataChange(p0: DataSnapshot) {
              val matches = p0.children.map {
                it.getValue(Match::class.java)!!
              }
              subject.onNext(matches)
            }
          })
        }
  }
}
