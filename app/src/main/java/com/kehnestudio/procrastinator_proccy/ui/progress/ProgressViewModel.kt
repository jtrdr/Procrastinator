package com.kehnestudio.procrastinator_proccy.ui.progress

import android.os.Build
import android.provider.Contacts
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.kehnestudio.procrastinator_proccy.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ProgressViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    val scoreHistory = userRepository.getDailyScores().asLiveData()

}
