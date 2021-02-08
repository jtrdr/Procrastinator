package com.kehnestudio.procrastinator_proccy.data.offline.online

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import timber.log.Timber
import java.lang.Exception

class UploadWorker(context: Context, params: WorkerParameters) : Worker(context, params)
{
    override fun doWork(): Result {

        return try {
            for (i in 0..600) {
                Timber.d("Uploading $i")
            }
            Result.success()
        }catch (e: Exception){
            Result.failure()
        }
    }
}