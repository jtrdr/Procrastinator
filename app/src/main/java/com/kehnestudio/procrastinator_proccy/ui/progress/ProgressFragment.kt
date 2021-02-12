package com.kehnestudio.procrastinator_proccy.ui.progress

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kehnestudio.procrastinator_proccy.R
import com.kehnestudio.procrastinator_proccy.databinding.CalendarDayLayoutBinding
import com.kehnestudio.procrastinator_proccy.databinding.CalendarMonthHeaderLayoutBinding
import com.kehnestudio.procrastinator_proccy.databinding.FragmentProgressBinding
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.*

class ProgressFragment : Fragment(R.layout.fragment_progress) {

    private var _binding: FragmentProgressBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProgressViewModel by viewModels()

    private var selectedDate: LocalDate? = null
    @RequiresApi(Build.VERSION_CODES.O)
    private val today = LocalDate.now()

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

        class DayViewContainer(view: View) : ViewContainer(view) {
            lateinit var day: CalendarDay
            val textView = CalendarDayLayoutBinding.bind(view).calendarDayText

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
                        }
                    }
                }
            }

        }

        binding.calendarView.dayBinder = object : DayBinder<DayViewContainer> {
            // Called only when a new container is needed.
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                val textView = container.textView
                container.textView.text = day.date.dayOfMonth.toString()
                if (day.owner == DayOwner.THIS_MONTH) {
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

        val currentMonth = YearMonth.now()
        //TODO: Months to substract should be calculated on latest date in DB
        // monthsToSubstract = (Current month - months since first month)
        val firstMonth = currentMonth.minusMonths(1)
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
        binding.calendarView.setup(firstMonth, currentMonth, firstDayOfWeek)
        binding.calendarView.scrollToMonth(currentMonth)

    }



    override fun onDestroyView() {
        super.onDestroyView()
        //clears reference to binding, view is cleaned up in memory
        _binding = null
    }
}