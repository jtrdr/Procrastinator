package com.kehnestudio.procrastinator_proccy.ui.progress

import android.annotation.SuppressLint
import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kehnestudio.procrastinator_proccy.R
import com.kehnestudio.procrastinator_proccy.data.offline.ScoreHistoryLocalDate
import com.kehnestudio.procrastinator_proccy.databinding.*
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import timber.log.Timber
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.*

@AndroidEntryPoint
class ProgressFragment : Fragment(R.layout.fragment_progress) {

    private val viewModel: ProgressViewModel by viewModels()
    private var selectedDate: LocalDate? = null

    @RequiresApi(Build.VERSION_CODES.O)
    private val today = LocalDate.now()

    private val scoreAdapter = ScoreAdapter()
    private var list = listOf<ScoreHistoryLocalDate>()
    private var scoreMap: Map<LocalDate?, List<ScoreHistoryLocalDate>> = mapOf()

    private lateinit var binding: FragmentProgressBinding


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currentMonth = YearMonth.now()

        binding = FragmentProgressBinding.bind(view)

        //TODO: Months to substract should be calculated on latest date in DB
        // monthsToSubstract = (Current month - months since first month)
        val firstMonth = currentMonth.minusMonths(1)
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek

        startViewModelObservation()

        binding.apply {
            recyclerView.apply {
                adapter = scoreAdapter
                layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            }
            calendarView.setup(firstMonth, currentMonth, firstDayOfWeek)
            calendarView.scrollToMonth(currentMonth)
        }

        scoreAdapter.notifyDataSetChanged()

        class DayViewContainer(view: View) : ViewContainer(view) {
            lateinit var day: CalendarDay
            val textView = CalendarDayLayoutBinding.bind(view).calendarDayText
            val scoreIndicator = CalendarDayLayoutBinding.bind(view).scoreIndicator

            init {
                view.setOnClickListener {
                    if (day.owner == DayOwner.THIS_MONTH) {
                        if (selectedDate != day.date) {
                            val oldDate = selectedDate
                            selectedDate = day.date
                            binding.calendarView.notifyDateChanged(day.date)
                            oldDate?.let { binding.calendarView.notifyDateChanged(it) }
                            updateAdapterForDate(day.date)
                        }
                    }
                }
            }
        }



        binding.calendarView.dayBinder = object : DayBinder<DayViewContainer> {
            // Called only when a new container is needed.
            override fun create(view: View) = DayViewContainer(view)

            @SuppressLint("ResourceAsColor")
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                val textView = container.textView
                val scoreIndicator = container.scoreIndicator
                container.textView.text = day.date.dayOfMonth.toString()
                CoroutineScope(Dispatchers.Main).launch {
                    val scores = scoreMap[day.date]
                    if (day.owner == DayOwner.THIS_MONTH) {
                        if (scores != null) {
                            when (day.date) {
                                today -> scoreIndicator.visibility = View.INVISIBLE
                                selectedDate -> scoreIndicator.visibility = View.INVISIBLE
                                else -> scoreIndicator.visibility = View.VISIBLE
                            }
                        } else {
                            scoreIndicator.visibility = View.INVISIBLE
                        }
                    }
                }

                if (day.owner == DayOwner.THIS_MONTH) {
                    when (day.date) {
                        selectedDate -> {
                            textView.setBackgroundResource(R.drawable.example_1_today_bg)
                            scoreIndicator.visibility = View.INVISIBLE
                        }
                        today -> {
                            textView.setBackgroundResource(R.drawable.purple_circle)
                            scoreIndicator.visibility = View.INVISIBLE
                        }
                        else -> {
                            textView.background = null
                        }
                    }
                } else {
                    textView.background = null

                }
            }
        }

        class MonthViewContainer(view: View) : ViewContainer(view) {
            val textView = CalendarMonthHeaderLayoutBinding.bind(view).headerTextView
        }

        binding.calendarView.monthHeaderBinder =
            object : MonthHeaderFooterBinder<MonthViewContainer> {
                override fun create(view: View) = MonthViewContainer(view)
                override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                    val locale = Locale.getDefault()
                    val monthYear = month.yearMonth.month.getDisplayName(TextStyle.FULL, locale)
                        .toLowerCase(locale).capitalize(locale) + " " + month.year
                    container.textView.text = monthYear
                }
            }

        binding.calendarView.monthScrollListener = { month ->

            selectedDate?.let {
                selectedDate = null
                binding.calendarView.notifyDateChanged(it)
                updateAdapterForDate(null)
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startViewModelObservation() {
        viewModel.scoreHistory.observe(viewLifecycleOwner) { it ->
            list = it
            scoreMap = it.groupBy { it.date }
            binding.calendarView.notifyDateChanged(LocalDate.now())
        }

        viewModel.getSpecificDailyScore()?.observe(viewLifecycleOwner){
            binding.textViewProgress.text = getString(R.string.progress_fragment_progresscircle_text, it)
            binding.progressBar.progress = it.toInt()
        }
    }

    private fun updateAdapterForDate(date: LocalDate?) {
        scoreAdapter.scores.clear()
        scoreAdapter.scores.addAll(scoreMap[date].orEmpty())
        scoreAdapter.notifyDataSetChanged()
    }
}