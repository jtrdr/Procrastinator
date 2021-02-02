package com.kehnestudio.procrastinator_proccy.ui.progress

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.kehnestudio.procrastinator_proccy.data.DailyScoreDao
import com.kehnestudio.procrastinator_proccy.data.UserDao

class ProgressViewModel @ViewModelInject constructor(
    private val userDao: UserDao,
    private val dailyScoreDao: DailyScoreDao
) : ViewModel() {

}