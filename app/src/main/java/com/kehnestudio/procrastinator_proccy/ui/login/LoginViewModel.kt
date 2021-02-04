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


    var name = MutableLiveData<String?>()
    val totalScore = MutableLiveData<Int>()

    fun insert(user: User) = viewModelScope.launch {
        userRepository.insert(user)
    }
    fun saveOrUpdateUser(displayname: String, score: Int, id:String){
        insert(User(displayname, score, id))
        name.value = null
        totalScore.value = null
    }

}