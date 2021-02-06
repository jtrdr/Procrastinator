package com.kehnestudio.procrastinator_proccy.ui.goals

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.slider.Slider
import com.kehnestudio.procrastinator_proccy.Constants.ACTION_START_SERVICE
import com.kehnestudio.procrastinator_proccy.Constants.ACTION_STOP_SERVICE
import com.kehnestudio.procrastinator_proccy.Constants.EXTRA_TIMER_LENGTH
import com.kehnestudio.procrastinator_proccy.R
import com.kehnestudio.procrastinator_proccy.databinding.FragmentGoalsBinding
import com.kehnestudio.procrastinator_proccy.utilities.TimerUtility
import com.kehnestudio.procrastinator_proccy.services.TimerService
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class GoalsFragment : Fragment() {

    private var _binding: FragmentGoalsBinding? = null
    /*
    This property is only valid between onCreateView and onDestroyView -
    as those views will only be available when visible to user
    !! will enforce the type of binding property to non-null
     */
    private val binding get()= _binding!!

    private val viewModel: GoalsViewModel by viewModels()

    private var timeLeftInMillis = 0L
    private var startTime = 300000L
    private var mTimerRunning = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGoalsBinding.inflate(inflater, container, false)

        //binding.root is property on all automatic generated viewbinding classes. Root return whole layout
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.buttonStart.setOnClickListener {
            startTimer()
        }

        binding.buttonReset.setOnClickListener{
            stopTimer()
        }

        binding.buttonRefresh.setOnClickListener {
            updateGoals()
        }

        binding.seekBarTimer.addOnChangeListener { slider, value, fromUser ->
            startTime = (value.toLong())*60000
            binding.tvTimeLeft.text = TimerUtility.getFormattedTimerTime(startTime, true)
        }

        binding.seekBarTimer.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {

            }

            override fun onStopTrackingTouch(slider: Slider) {

            }
        })

        subscribeToObservers()
    }

    private fun updateGoals(){

        val arrayEnvironment: Array<String> = resources.getStringArray(R.array.questions_environment)
        val arrayTasks: Array<String> = resources.getStringArray(R.array.questions_tasks)
        val randomEnvironment: MutableList<String> = arrayEnvironment.toMutableList()
        val randomTasks: MutableList<String> = arrayTasks.toMutableList()

        randomEnvironment.shuffle()
        randomTasks.shuffle()

        binding.checkBox.text = randomEnvironment[0]
        binding.checkBox2.text = randomEnvironment[1]
        binding.checkBox3.text = randomTasks[0]
        binding.checkBox4.text = randomTasks[1]
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
                binding.tvTimeLeft.text = formattedTime
            } else {
                binding.tvTimeLeft.text = TimerUtility.getFormattedTimerTime(startTime, true)
            }
        })

        TimerService.mTimerIsDone.observe(viewLifecycleOwner, Observer {
            when (it) {
                //TODO Do something when Timer has finished the normal way

                true -> {
                    Timber.d("Timer is done")
                    viewModel.updateDailyScore(25)
                    viewModel.saveDailyScoreFireStore()
                }
                else -> Timber.d("Timer is not done")
            }

        })
    }

    private fun startTimer() {
        if(!mTimerRunning) {
            val timeToPassToService = startTime
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
            viewModel.setTimerIsDoneState(false)
        } else {
            Timber.d("Timer already stopped")
        }
    }

    private fun updateButtons(timerRunning: Boolean) {
        this.mTimerRunning = timerRunning
        if(!mTimerRunning) {
            binding.buttonStart.visibility = View.VISIBLE
            binding.buttonReset.visibility = View.GONE
            binding.buttonRefresh.visibility = View.VISIBLE
            binding.linearLayoutCheckboxes.visibility = View.VISIBLE
            binding.seekBarTimer.visibility = View.VISIBLE
            binding.tvTimeLeft.text = TimerUtility.getFormattedTimerTime(startTime, true)
        } else {
            binding.buttonStart.visibility = View.GONE
            binding.buttonReset.visibility = View.VISIBLE
            binding.buttonRefresh.visibility = View.GONE
            binding.linearLayoutCheckboxes.visibility = View.GONE
            binding.seekBarTimer.visibility = View.GONE
        }
    }

    private fun sendCommandToService(action: String) =
        Intent(requireContext(), TimerService::class.java).also{
            it.action = action
            requireContext().startService(it)
        }

    override fun onDestroyView() {
        super.onDestroyView()
        //clears reference to binding, view is cleaned up in memory
        _binding = null
    }

}
