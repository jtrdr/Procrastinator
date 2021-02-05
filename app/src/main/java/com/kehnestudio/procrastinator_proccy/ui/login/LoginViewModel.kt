package com.kehnestudio.procrastinator_proccy.ui.login

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kehnestudio.procrastinator_proccy.data.offline.User
import com.kehnestudio.procrastinator_proccy.repositories.FireStoreRepository
import com.kehnestudio.procrastinator_proccy.repositories.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel @ViewModelInject constructor(
    private val userRepository: UserRepository,
    private val fireStoreRepository: FireStoreRepository
) : ViewModel() {


    private fun updateFireStore(id: String, name: String){
        fireStoreRepository.saveOrUpdateUser(id, name)
    }

    fun saveOrUpdateUserToFireStore(id: String, name: String){
        updateFireStore(id, name)
    }

    private fun insert(user: User) = viewModelScope.launch {
        userRepository.insert(user)
    }
    fun saveOrUpdateUser(id:String, name: String){
        insert(User(id, name))
    }
}