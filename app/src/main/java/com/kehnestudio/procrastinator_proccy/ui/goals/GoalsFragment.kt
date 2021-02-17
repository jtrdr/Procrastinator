package com.kehnestudio.procrastinator_proccy.ui.goals

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.slider.Slider
import com.kehnestudio.procrastinator_proccy.Constants.ACTION_START_SERVICE
import com.kehnestudio.procrastinator_proccy.Constants.ACTION_STOP_SERVICE
import com.kehnestudio.procrastinator_proccy.Constants.EXTRA_TIMER_LENGTH
import com.kehnestudio.procrastinator_proccy.R
import com.kehnestudio.procrastinator_proccy.databinding.FragmentGoalsBinding
import com.kehnestudio.procrastinator_proccy.services.TimerService
import com.kehnestudio.procrastinator_proccy.utilities.TimerUtility
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class GoalsFragment : Fragment() {

    private var _binding: FragmentGoalsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GoalsViewModel by viewModels()

    private var timeLeftInMillis = 0L
    private var startTime = 300000L
    private var mTimerRunning = false

    private var currentScore = 0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGoalsBinding.inflate(inflater, container, false)

        //binding.root is property on all automatic generated viewbinding classes. Root return whole layout
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.buttonStart.setOnClickListener {
            startTimer()
        }

        binding.buttonReset.setOnClickListener {
            stopTimer()
        }

        binding.buttonRefresh.setOnClickListener {
            updateGoals()
        }

        binding.seekBarTimer.addOnChangeListener { _, value, _ ->
            startTime = (value.toLong()) * 60000
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

    private fun updateGoals() {
        val arrayEnvironment: Array<String> =
            resources.getStringArray(R.array.questions_environment)
        val arrayTasks: Array<String> = resources.getStringArray(R.array.questions_tasks)
        val randomEnvironment: MutableList<String> = arrayEnvironment.toMutableList()
        val randomTasks: MutableList<String> = arrayTasks.toMutableList()

        randomEnvironment.shuffle()
        randomTasks.shuffle()

        binding.apply {
            checkBox.text = randomEnvironment[0]
            checkBox2.text = randomEnvironment[1]
            checkBox3.text = randomTasks[0]
            checkBox4.text = randomTasks[1]

            checkBox.isChecked = false
            checkBox2.isChecked = false
            checkBox3.isChecked = false
            checkBox4.isChecked = false

        }
        saveCheckBoxStates()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun subscribeToObservers() {

        TimerService.mTimerIsRunning.observe(viewLifecycleOwner, {
            mTimerRunning = it
            updateButtons()
        })

        TimerService.timeLeftInMillies.observe(viewLifecycleOwner, {
            timeLeftInMillis = it
            val formattedTime: String = (TimerUtility.getFormattedTimerTime(timeLeftInMillis, true))

            if (mTimerRunning) {
                binding.tvTimeLeft.text = formattedTime
            } else {
                binding.tvTimeLeft.text = TimerUtility.getFormattedTimerTime(startTime, true)
            }
        })

        viewModel.getSpecificDailyScore()?.observe(viewLifecycleOwner, {
            currentScore = it
        })

        viewModel.loadCheckBoxStates().observe(viewLifecycleOwner, {
            binding.apply {
                checkBox.isChecked = it[0]
                checkBox2.isChecked = it[1]
                checkBox3.isChecked = it[2]
                checkBox4.isChecked = it[3]
            }
        })

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startTimer() {

        if (!mTimerRunning) {
            saveCheckBoxStates()
            calculateNewScore()
            val timeToPassToService = startTime
            val intent = Intent(requireContext(), TimerService::class.java)
            intent.putExtra(EXTRA_TIMER_LENGTH, timeToPassToService)
            intent.putExtra(ACTION_START_SERVICE, ACTION_START_SERVICE)
            requireActivity().startService(intent)
        } else {
            return
        }
    }

    private fun calculateNewScore(){
        val scoreBase = startTime.toInt() / 60000
        var newScore = currentScore + scoreBase

        Timber.d("ScoreBase: $scoreBase NewScore: $newScore")

        if(binding.checkBox.isChecked){
            newScore += (3..5).random()
        }
        if(binding.checkBox2.isChecked){
            newScore += (3..5).random()
        }
        if(binding.checkBox3.isChecked){
            newScore += (6..8).random()
        }
        if(binding.checkBox4.isChecked){
            newScore += (6..8).random()
        }
        viewModel.saveNewScore(newScore)
        Timber.d("Current Score: $currentScore ScoreBase: $scoreBase NewScore: $newScore")
    }

    private fun stopTimer() {
        if (mTimerRunning) {
            sendCommandToService(ACTION_STOP_SERVICE)
            viewModel.setTimerIsDoneState(false)
        } else {
            return
        }
    }

    private fun saveCheckBoxStates() {
        viewModel.saveCheckBoxStates(
            binding.checkBox.isChecked,
            binding.checkBox2.isChecked,
            binding.checkBox3.isChecked,
            binding.checkBox4.isChecked
        )
    }

    private fun updateButtons() {

        if (!mTimerRunning) {
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
        Intent(requireContext(), TimerService::class.java).also {
            it.action = action
            requireContext().startService(it)
        }

    override fun onDestroyView() {
        super.onDestroyView()
        //clears reference to binding, view is cleaned up in memory
        _binding = null
    }

}
