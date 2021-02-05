package com.kehnestudio.procrastinator_proccy.ui.goals

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.kehnestudio.procrastinator_proccy.data.offline.ScoreHistory
import com.kehnestudio.procrastinator_proccy.repositories.UserRepository
import kotlinx.coroutines.launch

class GoalsViewModel @ViewModelInject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    var uid = firebaseAuth.currentUser?.uid


    fun insertScore(scoreHistory: ScoreHistory) = viewModelScope.launch {
        userRepository.insertScore(scoreHistory)
    }
    fun updateScore(score: Int){
        uid?.let { ScoreHistory("today", it, score) }?.let { insertScore(it) }
    }
}