package com.kehnestudio.procrastinator_proccy.ui.progress

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kehnestudio.procrastinator_proccy.R
import com.kehnestudio.procrastinator_proccy.data.offline.ScoreHistoryLocalDate
import com.kehnestudio.procrastinator_proccy.databinding.CalendarDayLayoutBinding
import com.kehnestudio.procrastinator_proccy.databinding.CalendarMonthHeaderLayoutBinding
import com.kehnestudio.procrastinator_proccy.databinding.FragmentLoginBinding
import com.kehnestudio.procrastinator_proccy.databinding.FragmentProgressBinding
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.*

@AndroidEntryPoint
class ProgressFragment : Fragment(R.layout.fragment_progress) {

    private var _binding: FragmentProgressBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProgressViewModel by viewModels()

    private var selectedDate: LocalDate? = null
    @RequiresApi(Build.VERSION_CODES.O)
    private val today = LocalDate.now()

    private val scoreAdapter = ScoreAdapter()
    private var list = listOf<ScoreHistoryLocalDate>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProgressBinding.inflate(inflater, container, false)

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currentMonth = YearMonth.now()
        //TODO: Months to substract should be calculated on latest date in DB
        // monthsToSubstract = (Current month - months since first month)
        val firstMonth = currentMonth.minusMonths(1)
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek

        binding.apply {
            recyclerView.apply {
                adapter = scoreAdapter
                layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            }
            calendarView.setup(firstMonth, currentMonth, firstDayOfWeek)
            calendarView.scrollToMonth(currentMonth)
        }

        viewModel.scoreHistory.observe(viewLifecycleOwner){ it ->
            Timber.d("ScoreHistoryLocalData: $it")
            list = it
            Timber.d("MAP $list")
        }
        scoreAdapter.notifyDataSetChanged()

        class DayViewContainer(view: View) : ViewContainer(view) {
            lateinit var day: CalendarDay
            val textView = CalendarDayLayoutBinding.bind(view).calendarDayText
            val scoreIndicator = CalendarDayLayoutBinding.bind(view).scoreIndicator

            init {
                textView.setOnClickListener {
                    if (day.owner == DayOwner.THIS_MONTH) {
                        if (selectedDate == day.date) {
                            selectedDate = null
                            binding.calendarView.notifyDayChanged(day)
                        } else {
                            val oldDate = selectedDate
                            selectedDate = day.date
                            binding.calendarView.notifyDateChanged(day.date)
                            oldDate?.let { binding.calendarView.notifyDateChanged(oldDate) }
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
                scoreIndicator.background = null

                container.textView.text = day.date.dayOfMonth.toString()


                val scores = list.map { it.date to it.score }.toMap()
                val scoress = scores[day.date]
                if (day.owner == DayOwner.THIS_MONTH) {

                    if(scoress != null){
                        scoreIndicator.setBackgroundColor(R.color.purple_800)
                    }

                    when (day.date) {
                        selectedDate -> {
                            textView.setBackgroundResource(R.drawable.example_1_selected_bg)
                        }
                        today -> {
                            textView.setBackgroundResource(R.drawable.example_1_today_bg)
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

        binding.calendarView.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(view)
            override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                val locale = Locale.getDefault()
                val monthYear = month.yearMonth.month.getDisplayName(TextStyle.FULL, locale).toLowerCase(locale).capitalize(locale) + " " + month.year
                container.textView.text = monthYear
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateAdapterForDate(date: LocalDate?) {
        val scores = list.map { it.date to it.score }.toMap()
        val scoreHistoryLocalDate = ScoreHistoryLocalDate(date, scores[date])
        val list = listOf(scoreHistoryLocalDate)
        scoreAdapter.submitList(list)
        scoreAdapter.notifyDataSetChanged()
    }
}