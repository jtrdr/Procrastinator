package com.kehnestudio.procrastinator_proccy.ui.backdrop.myaccount

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.kehnestudio.procrastinator_proccy.repositories.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyAccountViewModel @Inject constructor(
    dataStoreRepository: DataStoreRepository
) : ViewModel() {

    val readFromDataStore = dataStoreRepository.readFromDataStore.asLiveData()

}