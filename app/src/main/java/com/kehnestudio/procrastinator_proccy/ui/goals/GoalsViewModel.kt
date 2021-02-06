package com.kehnestudio.procrastinator_proccy.ui.goals

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.kehnestudio.procrastinator_proccy.data.offline.ScoreHistory
import com.kehnestudio.procrastinator_proccy.repositories.UserRepository
import com.kehnestudio.procrastinator_proccy.services.TimerService
import kotlinx.coroutines.launch

class GoalsViewModel @ViewModelInject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var uid = firebaseAuth.currentUser?.uid

    fun setTimerIsDoneState(state: Boolean){
        TimerService.mTimerIsDone.value = state
    }

    private fun insertScore(scoreHistory: ScoreHistory) = viewModelScope.launch {
        userRepository.insertScore(scoreHistory)
    }
    fun updateDailyScore(score: Int){
        uid?.let { ScoreHistory("today", it, score) }?.let { insertScore(it) }
    }
}