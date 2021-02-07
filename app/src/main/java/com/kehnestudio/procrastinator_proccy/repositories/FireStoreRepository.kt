package com.kehnestudio.procrastinator_proccy.repositories

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.SetOptions
import com.kehnestudio.procrastinator_proccy.data.offline.ScoreHistory
import com.kehnestudio.procrastinator_proccy.data.offline.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap

class FireStoreRepository @Inject constructor(
    private val userRepository: UserRepository,
    private val userRef: CollectionReference
) {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var uid = firebaseAuth.currentUser?.uid


    @RequiresApi(Build.VERSION_CODES.O)
    fun saveOrUpdateUser() {

        val today: String = LocalDate.now().toString()

        CoroutineScope(Dispatchers.IO).launch {
            val scoreHistory: ScoreHistory? = uid?.let {
                userRepository.getDailyScoreHistoryForFireStore(it)
            }

            val user: User? = uid?.let {
                userRepository.getUserWithScoreHistoryFirestore(it)
            }

            if (user != null) {
                uid?.let {
                    userRef.document(it)
                        .set(user, SetOptions.merge())
                        .addOnSuccessListener {
                            Timber.d(
                                "Successfully added user to Firestore %s %s %s",
                                user.name,
                                user.userId,
                                user.totalScore
                            )
                        }
                        .addOnFailureListener {
                            Timber.d("Failed to add user to Firestore%s", it.toString())
                        }
                }
            }

            if(scoreHistory!=null){
                uid?.let { it ->
                    userRef
                        .document(it)
                        .collection("score_history_collection")
                        .document(today)
                        .set(scoreHistory, SetOptions.merge())
                        .addOnSuccessListener {
                            Timber.d("Successfully added dailyScore to Firestore")
                        }
                        .addOnFailureListener {
                            Timber.d("Failed to add dailyScore to Firestore%s", it.toString())
                        }
                }
            }

        }
    }


    fun getUser(id: String) {
        userRef.document(id).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Timber.d("Successfully retrieved User from Firestore:%s", document.data)
                    val user = document.toObject(User::class.java)
                    if (user != null) {
                        CoroutineScope(Dispatchers.IO).launch {
                            userRepository.insert(user)
                        }
                    }
                } else {
                    Timber.d("No such document")
                }
            }
            .addOnFailureListener { exception ->
                Timber.d("FirestoreRepository getUser failed with %s", exception.toString())
            }
    }

    fun getDailyScoreHistory(id: String) {
        userRef.document(id).collection("score_history_collection").get()
            .addOnSuccessListener { collection ->
                if (collection != null) {
                    Timber.d("Successfully retrieved User from Firestore")
                    Timber.d("collection: %s", collection)
                } else {
                    Timber.d("No such document")
                }
            }
            .addOnFailureListener { exception ->
                Timber.d(" FirestoreRepository getUser failed with %s", exception.toString())
            }
    }


}