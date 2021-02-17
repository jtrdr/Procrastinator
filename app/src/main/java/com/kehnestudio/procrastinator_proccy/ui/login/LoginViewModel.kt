package com.kehnestudio.procrastinator_proccy.ui.login

import androidx.annotation.Nullable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.kehnestudio.procrastinator_proccy.data.offline.User
import com.kehnestudio.procrastinator_proccy.repositories.FireStoreRepository
import com.kehnestudio.procrastinator_proccy.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    @Nullable private val fireStoreRepository: FireStoreRepository
) : ViewModel() {

    fun saveOrUpdateUser(id:String, name: String) = viewModelScope.launch {
        userRepository.insert(User(id, name))
    }

    fun loadDataFromFireStore(uid: String){

        fireStoreRepository.loadUserFromFireStore(uid)
    }

    fun getUserId() = userRepository.getUserId()

    fun deleteAll(uid: String) = CoroutineScope(Dispatchers.IO).launch {
        userRepository.delete(uid)
    }

}