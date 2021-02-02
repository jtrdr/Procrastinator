package com.kehnestudio.procrastinator_proccy.ui.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.kehnestudio.procrastinator_proccy.data.DailyScoreDao
import com.kehnestudio.procrastinator_proccy.data.User
import com.kehnestudio.procrastinator_proccy.data.UserDao
import com.kehnestudio.procrastinator_proccy.repositories.MainRepository
import javax.inject.Inject

class HomeViewModel @ViewModelInject constructor(
    val mainRepository:MainRepository
) : ViewModel() {

}