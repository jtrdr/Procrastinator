package com.kehnestudio.procrastinator_proccy.ui.goals

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.kehnestudio.procrastinator_proccy.Constants.ACTION_START_SERVICE
import com.kehnestudio.procrastinator_proccy.Constants.ACTION_STOP_SERVICE
import com.kehnestudio.procrastinator_proccy.R
import com.kehnestudio.procrastinator_proccy.TimerUtility
import com.kehnestudio.procrastinator_proccy.services.TimerService
import com.kehnestudio.procrastinator_proccy.ui.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_goals.*
import timber.log.Timber

@AndroidEntryPoint
class GoalsFragment : Fragment(R.layout.fragment_goals) {

    private val viewModel: HomeViewModel by viewModels()

    private var timeLeftInMillis = 0L

    private var isRunning = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button_start.setOnClickListener {
            toggleTimer()
        }
        subscribeToObservers()
    }

    private fun subscribeToObservers() {

        TimerService.mTimerIsRunning.observe(viewLifecycleOwner, Observer {
            updateTracking(it)
        })

        TimerService.timeLeftInMillies.observe(viewLifecycleOwner, Observer {
            timeLeftInMillis = it
            val formattedTime: String = (TimerUtility.getFormattedTimerTime(timeLeftInMillis, true))
            textView_timeLeft.text = formattedTime
        })
    }

    private fun toggleTimer() {
        if(isRunning) {
            sendCommandToService(ACTION_STOP_SERVICE)
        } else {
            sendCommandToService(ACTION_START_SERVICE)
        }
    }

    private fun updateTracking(isTracking: Boolean) {
        this.isRunning = isTracking
        if(!isTracking) {
            button_reset.visibility = View.VISIBLE
        } else {
            button_reset.visibility = View.GONE
        }
    }

    private fun sendCommandToService(action: String) =
        Intent(requireContext(), TimerService::class.java).also{
            it.action = action
            requireContext().startService(it)
        }
}
