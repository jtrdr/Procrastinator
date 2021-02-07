package com.kehnestudio.procrastinator_proccy.ui.login

import androidx.annotation.Nullable
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase
import com.kehnestudio.procrastinator_proccy.data.offline.User
import com.kehnestudio.procrastinator_proccy.repositories.FireStoreRepository
import com.kehnestudio.procrastinator_proccy.repositories.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel @ViewModelInject constructor(
    private val userRepository: UserRepository,
    private val fireStoreRepository: FireStoreRepository,
    private val mAuth: FirebaseAuth
) : ViewModel() {


    private var uid = mAuth.currentUser?.uid

    private fun insert(user: User) = viewModelScope.launch {
        userRepository.insert(user)
    }
    fun saveOrUpdateUser(id:String, name: String){
        insert(User(id, name))
    }

    private fun getUser(){
        uid?.let { fireStoreRepository.getUser(it) }
    }

    fun loadDataFromFireStore(){
        getUser()
    }

}