package com.kehnestudio.procrastinator_proccy.ui.home

import androidx.databinding.Observable
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.kehnestudio.procrastinator_proccy.repositories.UserRepository


class HomeViewModel @ViewModelInject constructor(
    private val userRepository: UserRepository
) : ViewModel(), Observable {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var uid = firebaseAuth.currentUser?.uid

    fun getSpecificUser() = uid?.let { userRepository.getSpecificUser(it) }

    fun getUserWithScoreHistory() = uid?.let { userRepository.getUserWithScoreHistory(it) }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

}