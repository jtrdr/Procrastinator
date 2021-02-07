package com.kehnestudio.procrastinator_proccy.ui.backdrop.myaccount

import android.os.Build
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kehnestudio.procrastinator_proccy.repositories.FireStoreRepository
import com.kehnestudio.procrastinator_proccy.repositories.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyAccountViewModel @ViewModelInject constructor(
    private val userRepository: UserRepository,
    @Nullable
    private val fireStoreRepository: FireStoreRepository
) : ViewModel() {

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveDataIntoFirestore(){
        fireStoreRepository.saveOrUpdateUser()
    }

    private fun delete() = CoroutineScope(Dispatchers.IO).launch {
        userRepository.delete()
    }

    fun deleteAll() = delete()

}