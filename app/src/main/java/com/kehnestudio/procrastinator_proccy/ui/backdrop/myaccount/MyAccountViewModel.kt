package com.kehnestudio.procrastinator_proccy.ui.backdrop.myaccount

import android.os.Build
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.kehnestudio.procrastinator_proccy.repositories.FireStoreRepository
import com.kehnestudio.procrastinator_proccy.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyAccountViewModel @Inject constructor(
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