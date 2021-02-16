package com.kehnestudio.procrastinator_proccy.ui.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.databinding.Observable
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.kehnestudio.procrastinator_proccy.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val mAuth: FirebaseAuth
) : ViewModel(), Observable {

    private var uid = mAuth.currentUser?.uid

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
}