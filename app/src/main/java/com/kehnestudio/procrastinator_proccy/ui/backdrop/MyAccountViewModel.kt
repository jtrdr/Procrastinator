package com.kehnestudio.procrastinator_proccy.ui.backdrop

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kehnestudio.procrastinator_proccy.repositories.UserRepository
import kotlinx.coroutines.launch

class MyAccountViewModel @ViewModelInject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private fun delete() = viewModelScope.launch {
        userRepository.delete()
    }

    fun deleteUserOnLogout() {
        delete()
    }
}