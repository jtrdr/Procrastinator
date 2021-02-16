package com.kehnestudio.procrastinator_proccy.repositories

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
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
import java.util.*
import javax.inject.Inject


class FireStoreRepository @Inject constructor(
    private val userRepository: UserRepository,
    private val userRef: CollectionReference,
    private val dataStoreRepository: DataStoreRepository,
)  {

    var result = MutableLiveData<Boolean>()

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateUserInFireStore() {

        var uid: String? = null
        CoroutineScope(Dispatchers.IO).launch {
            val scoreHistory: List<ScoreHistoryFirestore> =
                userRepository.getDailyScoreHistoryForFireStore()

            Timber.d("ScoreHistory: %s", scoreHistory)
            val user: User = userRepository.getUserWithScoreHistoryFirestore()
            uid = user.userId

            val userFirestore = UserFirestore(
                userId = user.userId,
                name = user.name,
                score_history = scoreHistory
            )
            userRef.document(uid!!)
                .set(userFirestore, SetOptions.merge())
                .addOnSuccessListener {
                    Timber.d(
                        "Successfully added user to Firestore %s %s",
                        user.name,
                        user.userId
                    )
                    saveTimeToDataStore()
                    result.postValue(true)

                }
                .addOnFailureListener {
                    Timber.d("Failed to add user to Firestore%s", it.toString())
                    result.postValue(false)
                }
        }
    }

    fun loadUserFromFireStore(uid: String) {
        Timber.d("LoadingUserFromFireStore")
        userRef.document(uid).get()
            .addOnSuccessListener { document ->
                if (document.data != null) {
                    Timber.d("STEP 3")
                    Timber.d(
                        "Successfully retrieved User from with guid $uid, content: %s",
                        document.data
                    )
                    val userFirestore: UserFirestore? = document.toObject(UserFirestore::class.java)

                    //Insert into local Room database
                    CoroutineScope(Dispatchers.IO).launch {
                        userFirestore?.score_history?.forEach { e ->
                            var scoreHistory = e.date?.let {
                                ScoreHistory(
                                    it, uid, e.score
                                )
                            }
                            if (scoreHistory != null) {
                                Timber.d("STEP 4")
                                userRepository.insertScore(scoreHistory)
                            }
                        }
                    }
                } else {
                    Timber.d("No such document with guid: $uid")
                }
            }.addOnFailureListener { exception ->
                Timber.d("FirestoreRepository getUser failed with %s", exception.toString())
            }
    }

    private fun saveTimeToDataStore() = CoroutineScope(Dispatchers.IO)
        .launch {
            val time = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            val currentTime = time.format(Date())
            dataStoreRepository.saveSyncDateToDataStore(currentTime)
        }

    fun deleteFireStoreUser(uid: String){
        userRef.document(uid)
            .delete()
            .addOnSuccessListener { Timber.d("DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Timber.d(e, "Error deleting document") }
    }

}