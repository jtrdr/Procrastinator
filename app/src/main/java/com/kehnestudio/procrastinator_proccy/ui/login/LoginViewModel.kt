package com.kehnestudio.procrastinator_proccy.ui.login

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kehnestudio.procrastinator_proccy.data.User
import com.kehnestudio.procrastinator_proccy.repositories.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel @ViewModelInject constructor(
    private val userRepository: UserRepository
) : ViewModel() {


    fun insert(user: User) = viewModelScope.launch {
        userRepository.insert(user)
    }
    fun saveOrUpdateUser(id:String, name: String){
        insert(User(id, name))
    }
}