package com.kehnestudio.procrastinator_proccy.ui.goals

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.kehnestudio.procrastinator_proccy.data.offline.ScoreHistory
import com.kehnestudio.procrastinator_proccy.repositories.UserRepository
import com.kehnestudio.procrastinator_proccy.services.TimerService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import javax.inject.Inject

@HiltViewModel
class GoalsViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val mAuth: FirebaseAuth
) : ViewModel() {

    private var uid = mAuth.currentUser?.uid

    fun setTimerIsDoneState(state: Boolean) {
        TimerService.mTimerIsDone.value = state
    }

    private fun insertScore(scoreHistory: ScoreHistory) = viewModelScope.launch {
        userRepository.insertScore(scoreHistory)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateDailyScore(score: Long) {
        val localDate = LocalDate.now()
        val date: Date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
        uid?.let { ScoreHistory(date, it, score) }?.let { insertScore(it) }
    }
}