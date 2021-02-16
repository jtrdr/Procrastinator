package com.kehnestudio.procrastinator_proccy.ui.backdrop.myaccount

import android.content.Context
import androidx.annotation.Nullable
import androidx.lifecycle.*
import androidx.work.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestoreSettings
import com.kehnestudio.procrastinator_proccy.repositories.DataStoreRepository
import com.kehnestudio.procrastinator_proccy.repositories.FireStoreRepository
import com.kehnestudio.procrastinator_proccy.repositories.UserRepository
import com.kehnestudio.procrastinator_proccy.utilities.UploadWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MyAccountViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    @Nullable private val fireStoreRepository: FireStoreRepository
) : ViewModel() {

    val readFromDataStore = dataStoreRepository.readFromDataStore.asLiveData()

    val result = fireStoreRepository.result

    fun resetResult() {
        result.value = null
    }

    fun saveDataOnLogout(context: Context) {

        val constraints = Constraints.Builder()
            .build()

        val oneTimeWorkRequest = OneTimeWorkRequest
            .Builder(UploadWorker::class.java)
            .setConstraints(constraints)
            .build()

        val workManager: WorkManager = WorkManager
            .getInstance(context)

        workManager.enqueueUniqueWork("Update", ExistingWorkPolicy.REPLACE, oneTimeWorkRequest)
    }

    lateinit var deleteAcountWorkRequest: OneTimeWorkRequest

    fun deleteAccount(context: Context){

        val constraints = Constraints.Builder()
            .build()

        deleteAcountWorkRequest = OneTimeWorkRequest
            .Builder(DeleteAccountWorker::class.java)
            .setConstraints(constraints)
            .build()

        val workManager: WorkManager = WorkManager
            .getInstance(context)

        workManager.enqueue(deleteAcountWorkRequest)
    }
}

