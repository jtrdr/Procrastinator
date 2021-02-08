package com.kehnestudio.procrastinator_proccy.utilities

import android.content.Context
import android.os.Build
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.kehnestudio.procrastinator_proccy.repositories.FireStoreRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.text.SimpleDateFormat
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
        return try {

            val time = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            val currentTime = time.format(Date())

            val outputData: Data = Data.Builder()
                .putString(KEY_WORKER, currentTime)
                .build()

            fireStoreRepository.updateUserInFireStore()

            Result.success(outputData)
        } catch (e: Exception) {
            Result.failure()
        }
    }
}