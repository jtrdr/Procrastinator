package com.kehnestudio.procrastinator_proccy.ui.goals

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.slider.Slider
import com.kehnestudio.procrastinator_proccy.Constants.ACTION_START_SERVICE
import com.kehnestudio.procrastinator_proccy.Constants.ACTION_STOP_SERVICE
import com.kehnestudio.procrastinator_proccy.Constants.EXTRA_TIMER_LENGTH
import com.kehnestudio.procrastinator_proccy.R
import com.kehnestudio.procrastinator_proccy.utilities.TimerUtility
import com.kehnestudio.procrastinator_proccy.services.TimerService
import com.kehnestudio.procrastinator_proccy.ui.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_goals.*
import timber.log.Timber

@AndroidEntryPoint
class GoalsFragment : Fragment(R.layout.fragment_goals) {

    private val viewModel: HomeViewModel by viewModels()

    private var timeLeftInMillis = 0L
    private var startTime = 300000L
    private var mTimerRunning = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button_start.setOnClickListener {
            startTimer()
        }

        button_reset.setOnClickListener{
            stopTimer()
        }

        seekBar_timer.addOnChangeListener { slider, value, fromUser ->
            startTime = (value.toLong())*60000
            textView_timeLeft.text = TimerUtility.getFormattedTimerTime(startTime, true)
        }

        seekBar_timer.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {

            }

            override fun onStopTrackingTouch(slider: Slider) {

            }
        })
        subscribeToObservers()
    }

    private fun subscribeToObservers() {

        TimerService.mTimerIsRunning.observe(viewLifecycleOwner, Observer {
            updateButtons(it)
            if(!it){
                stopTimer()
            }
        })

        TimerService.timeLeftInMillies.observe(viewLifecycleOwner, Observer {
            timeLeftInMillis = it
            val formattedTime: String = (TimerUtility.getFormattedTimerTime(timeLeftInMillis, true))

            if(mTimerRunning) {
                textView_timeLeft.text = formattedTime
            } else {
                textView_timeLeft.text = TimerUtility.getFormattedTimerTime(startTime, true)
            }
        })
    }

    private fun startTimer() {
        if(!mTimerRunning) {
            val timeToPassToService = startTime / 60000
            val intent = Intent(requireContext(), TimerService::class.java)
            intent.putExtra(EXTRA_TIMER_LENGTH, timeToPassToService)
            intent.putExtra(ACTION_START_SERVICE, ACTION_START_SERVICE)
            requireActivity().startService(intent)
        } else {
            Timber.d("Timer already running")
        }
    }

    private fun stopTimer() {
        if(mTimerRunning) {
            sendCommandToService(ACTION_STOP_SERVICE)
        } else {
            Timber.d("Timer already stopped")
        }
    }

    private fun updateButtons(timerRunning: Boolean) {
        this.mTimerRunning = timerRunning
        if(!mTimerRunning) {
            button_start.visibility = View.VISIBLE
            button_reset.visibility = View.GONE
            button_refresh.visibility = View.VISIBLE
            linearLayoutCheckboxes.visibility = View.VISIBLE
            seekBar_timer.visibility = View.VISIBLE
            textView_timeLeft.text = TimerUtility.getFormattedTimerTime(startTime, true)
        } else {
            button_start.visibility = View.GONE
            button_reset.visibility = View.VISIBLE
            button_refresh.visibility = View.GONE
            linearLayoutCheckboxes.visibility = View.GONE
            seekBar_timer.visibility = View.GONE
        }
    }

    private fun sendCommandToService(action: String) =
        Intent(requireContext(), TimerService::class.java).also{
            it.action = action
            requireContext().startService(it)
        }
}
