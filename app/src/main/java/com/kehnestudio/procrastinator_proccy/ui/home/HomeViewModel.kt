package com.kehnestudio.procrastinator_proccy.ui.home

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.databinding.Observable
import androidx.lifecycle.ViewModel
import androidx.work.*
import com.google.firebase.auth.FirebaseAuth
import com.kehnestudio.procrastinator_proccy.Constants
import com.kehnestudio.procrastinator_proccy.repositories.UserRepository
import com.kehnestudio.procrastinator_proccy.utilities.UploadWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val mAuth: FirebaseAuth
) : ViewModel(), Observable {

    private var uid = mAuth.currentUser?.uid

    fun getSpecificUser() = uid?.let { userRepository.getSpecificUser(it) }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getSpecificDailyScore() = uid?.let {
        val localDate = LocalDate.now()
        val date: Date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
        userRepository.getSpecificDailyScore(it, date)
    }

    fun getSumOfDailyScore() =
        uid?.let { userRepository.getSumOfDailyScore(it) }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    fun sendPeriodicWorkRequest(context: Context) {

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val data = Data.Builder()
        data.putString(Constants.WORKER_INPUT_SOURCE, Constants.WORKER_INPUT_SENDER_PERIODIC)

        val periodicWorkRequest = PeriodicWorkRequest
            .Builder(UploadWorker::class.java, 1, TimeUnit.HOURS)
            .addTag("UploadJob")
            .setConstraints(constraints)
            .setInputData(data.build())
            .build()

        val workManager: WorkManager = WorkManager
            .getInstance(context)

        workManager.enqueueUniquePeriodicWork(
            Constants.PERIODIC_WORK_UPLOAD_DATA,
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWorkRequest
        )
    }
}