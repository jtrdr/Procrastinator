package com.kehnestudio.procrastinator_proccy.ui.backdrop.about

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.work.*
import com.kehnestudio.procrastinator_proccy.Constants.PERIODIC_WORK_UPLOAD_DATA
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
            .Builder(UploadWorker::class.java, 1, TimeUnit.HOURS)
            .addTag("UploadJob")
            .setConstraints(constraints)
            .build()

        val workManager: WorkManager = WorkManager
            .getInstance(context)

        workManager.enqueueUniquePeriodicWork(
            PERIODIC_WORK_UPLOAD_DATA,
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWorkRequest)
    }
}