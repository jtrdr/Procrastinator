package com.kehnestudio.procrastinator_proccy.ui.backdrop.myaccount

import android.os.Build
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.kehnestudio.procrastinator_proccy.repositories.DataStoreRepository
import com.kehnestudio.procrastinator_proccy.repositories.FireStoreRepository
import com.kehnestudio.procrastinator_proccy.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyAccountViewModel @Inject constructor(
    dataStoreRepository: DataStoreRepository
) : ViewModel() {

    val readFromDataStore = dataStoreRepository.readFromDataStore.asLiveData()

}