package com.kehnestudio.procrastinator_proccy.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.kehnestudio.procrastinator_proccy.data.offline.ScoreHistory
import com.kehnestudio.procrastinator_proccy.repositories.DataStoreRepository
import com.kehnestudio.procrastinator_proccy.repositories.UserRepository
import com.kehnestudio.procrastinator_proccy.services.TimerService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val mAuth: FirebaseAuth,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    private var uid = mAuth.currentUser?.uid

    fun setTimerIsDoneState(state: Boolean) {
        TimerService.mTimerIsDone.value = state
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateDailyScore() = CoroutineScope(Dispatchers.Default).launch {

        val newScore = dataStoreRepository.readNewScoreFromDataStore.first()
        val localDate = LocalDate.now()
        val date: Date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
        uid?.let { ScoreHistory(date, it, newScore.toLong()) }?.let {
            userRepository.insertScore(it)
        }
    }
}
