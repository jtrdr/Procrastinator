package com.kehnestudio.procrastinator_proccy.repositories

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.SetOptions
import com.kehnestudio.procrastinator_proccy.data.offline.ScoreHistory
import com.kehnestudio.procrastinator_proccy.data.offline.User
import com.kehnestudio.procrastinator_proccy.data.online.ScoreHistoryFirestore
import com.kehnestudio.procrastinator_proccy.data.online.UserFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import javax.inject.Inject


class FireStoreRepository @Inject constructor(
    private val userRepository: UserRepository,
    private val userRef: CollectionReference,
    private val mAuth: FirebaseAuth,
    private val dataStoreRepository: DataStoreRepository
) {


    private var uid = mAuth.currentUser?.uid


    @RequiresApi(Build.VERSION_CODES.O)
    fun updateUserInFireStore() {

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
                            saveTimeToDataStore()
                        }
                        .addOnFailureListener {
                            Timber.d("Failed to add user to Firestore%s", it.toString())
                        }
                }
            }
        }
    }


    fun loadUserFromFireStore(id: String) {
        userRef.document(id).get()
            .addOnSuccessListener { document ->
                if (document.data != null) {
                    Timber.d(
                        "Successfully retrieved User from with guid $id, content: %s",
                        document.data
                    )
                    val userFirestore: UserFirestore? = document.toObject(UserFirestore::class.java)

                    //Insert into local Room database
                    CoroutineScope(Dispatchers.IO).launch {
                        userFirestore?.score_history?.forEach { e ->
                            var scoreHistory = e.date?.let {
                                ScoreHistory(
                                    it, id, e.score
                                )
                            }
                            if (scoreHistory != null) {
                                userRepository.insertScore(scoreHistory)
                            }
                        }
                    }
                } else {
                    Timber.d("No such document with guid: $id")
                }
            }.addOnFailureListener { exception ->
                Timber.d("FirestoreRepository getUser failed with %s", exception.toString())
            }
    }

    private fun saveTimeToDataStore() = CoroutineScope(Dispatchers.IO)
        .launch {
            val time = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            val currentTime = time.format(Date())
            dataStoreRepository.saveToDataStore(currentTime)
        }
}