package com.kehnestudio.procrastinator_proccy.ui.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.databinding.Observable
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.kehnestudio.procrastinator_proccy.repositories.FireStoreRepository
import com.kehnestudio.procrastinator_proccy.repositories.UserRepository
import java.time.LocalDate
import java.time.ZoneId
import java.util.*


class HomeViewModel @ViewModelInject constructor(
    private val userRepository: UserRepository,
    private val fireStoreRepository: FireStoreRepository
) : ViewModel(), Observable {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var uid = firebaseAuth.currentUser?.uid

    fun getSpecificUser() = uid?.let { userRepository.getSpecificUser(it) }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getSpecificDailyScore() = uid?.let {
        val localDate = LocalDate.now()
        val date: Date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
        userRepository.getSpecificDailyScore(it, date) }

    fun getSumOfDailyScore() =
        uid?.let { userRepository.getSumOfDailyScore(it) }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    fun getUser(){
        uid?.let { fireStoreRepository.getUser(it) }
    }

}