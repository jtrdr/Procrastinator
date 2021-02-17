package com.kehnestudio.procrastinator_proccy.utilities

import android.content.Context
import android.os.Build
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.hilt.work.HiltWorker
import androidx.lifecycle.asLiveData
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.auth.FirebaseAuth
import com.kehnestudio.procrastinator_proccy.Constants
import com.kehnestudio.procrastinator_proccy.Constants.WORKER_INPUT_SENDER_LOGOUT
import com.kehnestudio.procrastinator_proccy.Constants.WORKER_INPUT_SENDER_PERIODIC
import com.kehnestudio.procrastinator_proccy.Constants.WORKER_INPUT_SOURCE
import com.kehnestudio.procrastinator_proccy.repositories.DataStoreRepository
import com.kehnestudio.procrastinator_proccy.repositories.FireStoreRepository
import com.kehnestudio.procrastinator_proccy.repositories.UserRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

@HiltWorker
class UploadWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    @Nullable private val fireStoreRepository: FireStoreRepository
) : Worker(appContext, workerParams) {

    companion object {
        const val KEY_WORKER = "key_worker"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun doWork(): Result {

        val sender = inputData.getString(WORKER_INPUT_SOURCE)

        return try {
            when (sender) {
                WORKER_INPUT_SENDER_LOGOUT -> fireStoreRepository.updateUserViaLogout()
                WORKER_INPUT_SENDER_PERIODIC -> fireStoreRepository.updateUserViaWorker()
            }
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}