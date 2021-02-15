package com.kehnestudio.procrastinator_proccy.ui.backdrop.about

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.work.*
import com.kehnestudio.procrastinator_proccy.utilities.UploadWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class AboutViewModel @Inject constructor(
) : ViewModel() {

    fun sendPeriodicWorkRequest(context: Context) {

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val periodicWorkRequest = PeriodicWorkRequest
            .Builder(UploadWorker::class.java, 15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

        val workManager: WorkManager = WorkManager
            .getInstance(context)

        workManager.enqueueUniquePeriodicWork(
            "com.kehnestudio.procrastinator_proccy.ui.backdrop.about",
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWorkRequest)
    }
}