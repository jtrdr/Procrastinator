package com.kehnestudio.procrastinator_proccy.repositories

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.SetOptions
import com.kehnestudio.procrastinator_proccy.data.offline.ScoreHistory
import com.kehnestudio.procrastinator_proccy.data.offline.User
import com.kehnestudio.procrastinator_proccy.data.offline.UserWithScoreHistory
import com.kehnestudio.procrastinator_proccy.data.offline.online.ScoreHistoryFirestore
import com.kehnestudio.procrastinator_proccy.data.offline.online.UserFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate
import javax.inject.Inject

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
            val scoreHistory: List<ScoreHistoryFirestore>? = uid?.let {
                userRepository.getDailyScoreHistoryForFireStore(it)
            }
            Timber.d("ScoreHistory: %s", scoreHistory)
            val user: User? = uid?.let {
                userRepository.getUserWithScoreHistoryFirestore(it)
            }

            val userFirestore = UserFirestore(
                userId = user?.userId,
                name = user?.name,
                score_history = scoreHistory
            )

            if (user != null) {
                uid?.let {
                    userRef.document(it)
                        .set(userFirestore, SetOptions.merge())
                        .addOnSuccessListener {
                            Timber.d(
                                "Successfully added user to Firestore %s %s",
                                user.name,
                                user.userId
                            )
                        }
                        .addOnFailureListener {
                            Timber.d("Failed to add user to Firestore%s", it.toString())
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
                    val userFirestore: UserFirestore? = document.toObject(UserFirestore::class.java)


                        CoroutineScope(Dispatchers.IO).launch {
                            userFirestore?.score_history?.forEach { e ->
                                Timber.d("Score History: $e")
                                var scoreHistory = e.date?.let {
                                    ScoreHistory(
                                        it, id, e.score
                                    )
                                }
                                if (scoreHistory != null) {
                                    userRepository.insertScore(scoreHistory)
                                }
                                Timber.d("Score History: $scoreHistory")
                        }
                    }

                    /**
                    val user: User? = userFirestore?.userId?.let {
                        userFirestore.name?.let { it1 ->
                            User(
                                it,
                                it1
                            )
                        }
                    }


                    if (userFirestore != null) {
                        CoroutineScope(Dispatchers.IO).launch {
                            if (user != null) {
                                userRepository.insert(user)
                            }
                        }
                    }
                     */
                } else {
                    Timber.d("No such document")
                }
            }.addOnFailureListener{ exception ->
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