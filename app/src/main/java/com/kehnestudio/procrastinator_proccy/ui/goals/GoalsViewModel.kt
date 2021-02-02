package com.kehnestudio.procrastinator_proccy.ui.goals

import android.util.Log
import androidx.core.app.Person
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.kehnestudio.procrastinator_proccy.data.DailyScore
import com.kehnestudio.procrastinator_proccy.data.DailyScoreDao
import com.kehnestudio.procrastinator_proccy.data.UserDao
import com.kehnestudio.procrastinator_proccy.repositories.MainRepository
import java.util.*

class GoalsViewModel @ViewModelInject constructor(
    val mainRepository: MainRepository
) : ViewModel() {


}