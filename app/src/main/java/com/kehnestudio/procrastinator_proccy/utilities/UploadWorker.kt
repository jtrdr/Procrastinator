package com.kehnestudio.procrastinator_proccy.utilities

import android.content.Context
import android.os.Build
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.kehnestudio.procrastinator_proccy.repositories.DataStoreRepository
import com.kehnestudio.procrastinator_proccy.repositories.FireStoreRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
            fireStoreRepository.updateUserInFireStore()
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}