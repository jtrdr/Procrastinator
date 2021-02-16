package com.kehnestudio.procrastinator_proccy.ui.goals

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.kehnestudio.procrastinator_proccy.data.offline.ScoreHistory
import com.kehnestudio.procrastinator_proccy.repositories.DataStoreRepository
import com.kehnestudio.procrastinator_proccy.repositories.UserRepository
import com.kehnestudio.procrastinator_proccy.services.TimerService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import javax.inject.Inject

@HiltViewModel
class GoalsViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val mAuth: FirebaseAuth,
    private val dataStoreRepository: DataStoreRepository
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

    fun loadCheckBoxStates() = dataStoreRepository.readCheckBoxStates.asLiveData()

    fun saveCheckBoxStates(
        checkBox1: Boolean,
        checkBox2: Boolean,
        checkBox3: Boolean,
        checkBox4: Boolean
    ) = CoroutineScope(Dispatchers.IO).launch {
        dataStoreRepository.saveCheckBoxStates(
            checkBox1,
            checkBox2,
            checkBox3,
            checkBox4
        )
    }
}