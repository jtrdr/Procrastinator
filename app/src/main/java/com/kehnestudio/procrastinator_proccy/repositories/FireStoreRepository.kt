package com.kehnestudio.procrastinator_proccy.repositories

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.SetOptions
import timber.log.Timber
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.TemporalQueries.localDate
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap


class FireStoreRepository @Inject constructor(
    private val userRef: CollectionReference
) {

    fun saveOrUpdateUser(id: String, name: String) {
        val user: MutableMap<String, Any> = HashMap()
        user["id"] = id
        user["name"] = name

        userRef.document(id)
            .set(user, SetOptions.merge())
            .addOnSuccessListener {
                Timber.d("Successfully added user to Firestore")
            }
            .addOnFailureListener {
                Timber.d("Failed to add user to Firestore%s", it.toString())
            }
    }

    fun saveOrUpdateDailyScore(id: String, dailyScore: Long, dateToday: Date, localDateToday: String) {

        val dateWithDailyScore = hashMapOf(
            "score" to dailyScore,
            "date" to Timestamp(dateToday)
        )

        userRef
            .document(id)
            .collection("score_history_collection")
            .document(localDateToday)
            .set(dateWithDailyScore, SetOptions.merge())
            .addOnSuccessListener {
                Timber.d("Successfully added dailyScore to Firestore")
            }
            .addOnFailureListener {
                Timber.d("Failed to add dailyScore to Firestore%s", it.toString())
            }

    }

}