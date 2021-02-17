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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import javax.inject.Inject
import kotlin.random.Random.Default.nextInt

@HiltViewModel
class GoalsViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val mAuth: FirebaseAuth,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    private var uid = mAuth.currentUser?.uid

    fun loadCheckBoxStates() = dataStoreRepository.readCheckBoxStates.asLiveData()

    @RequiresApi(Build.VERSION_CODES.O)
    fun getSpecificDailyScore() = uid?.let {
        val localDate = LocalDate.now()
        val date: Date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
        userRepository.getSpecificDailyScore(it, date)
    }

    fun setTimerIsDoneState(state: Boolean) {
        TimerService.mTimerIsDone.value = state
    }

    fun saveCheckBoxStates(
        checkBox1: Boolean,
        checkBox2: Boolean,
        checkBox3: Boolean,
        checkBox4: Boolean
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            dataStoreRepository.saveCheckBoxStates(
                checkBox1,
                checkBox2,
                checkBox3,
                checkBox4
            )
        }
    }

    fun saveNewScore(newScore:Int) = CoroutineScope(Dispatchers.IO).launch {
        dataStoreRepository.saveNewScoreToDataStore(newScore)
    }
}
