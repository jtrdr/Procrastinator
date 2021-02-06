package com.kehnestudio.procrastinator_proccy.ui.goals

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.kehnestudio.procrastinator_proccy.data.offline.ScoreHistory
import com.kehnestudio.procrastinator_proccy.repositories.FireStoreRepository
import com.kehnestudio.procrastinator_proccy.repositories.UserRepository
import com.kehnestudio.procrastinator_proccy.services.TimerService
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

class GoalsViewModel @ViewModelInject constructor(
    private val userRepository: UserRepository,
    private val fireStoreRepository: FireStoreRepository
) : ViewModel() {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var uid = firebaseAuth.currentUser?.uid

    fun setTimerIsDoneState(state: Boolean) {
        TimerService.mTimerIsDone.value = state
    }

    private fun insertScore(scoreHistory: ScoreHistory) = viewModelScope.launch {
        userRepository.insertScore(scoreHistory)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateDailyScore(score: Long) {
        uid?.let { ScoreHistory( LocalDate.now(),it, score) }?.let { insertScore(it) }
    }

    private fun updateDailyScoreFireStore() {
        val score = 50L
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val localDate = LocalDate.now()
            val date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
            val today: String = LocalDate.now().toString()
            uid?.let { fireStoreRepository.saveOrUpdateDailyScore(it, score, date, today) }
        }
    }

    fun saveDailyScoreFireStore() {
        updateDailyScoreFireStore()
    }
}