package com.kehnestudio.procrastinator_proccy.repositories

import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import timber.log.Timber
import javax.inject.Inject

class FireStoreRepository @Inject constructor(
    private val userRef: CollectionReference
) {

    fun saveOrUpdateUser(id: String, name: String) {
        val user: MutableMap<String, Any> = HashMap()
        user["id"] = id
        user["name"] = name

        userRef.document(id)
            .set(user)
            .addOnSuccessListener {
                Timber.d("Successfully added user to Firestore")
            }
            .addOnFailureListener {
                Timber.d("Failed to add user to Firestore%s", it.toString())
            }
    }

    fun saveOrUpdateDailyScore(id: String, date: Timestamp, dailyScore: Int) {

    }

}