package com.kehnestudio.procrastinator_proccy.ui.backdrop.myaccount

import android.content.Context
import android.os.Build
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.auth.FirebaseAuth
import com.kehnestudio.procrastinator_proccy.repositories.FireStoreRepository
import com.kehnestudio.procrastinator_proccy.repositories.UserRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltWorker
class DeleteAccountWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    @Nullable private val fireStoreRepository: FireStoreRepository,
    private val userRepository: UserRepository
) : Worker(appContext, workerParams) {

    companion object {
        const val KEY_WORKER = "key_worker"
    }

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var uid = firebaseAuth.currentUser?.uid

    @RequiresApi(Build.VERSION_CODES.O)
    override fun doWork(): Result {
        return try {
            CoroutineScope(Dispatchers.IO).launch {
                uid?.let { fireStoreRepository.deleteFireStoreUser(it) }
                uid?.let { userRepository.delete(it) }
                firebaseAuth.currentUser?.delete()
            }
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}