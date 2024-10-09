package com.example.schedule.schedulefragment

import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.schedule.R
import com.example.schedule.databinding.FragmentScheduleBinding
private const val TAG = "VIEWMODEL"
class ScheduleFragment : Fragment() {

    private var _binding: FragmentScheduleBinding? = null
    val binding: FragmentScheduleBinding
    get() = checkNotNull(_binding)

    val viewModel: ScheduleViewModel by viewModels()
    val cal = Calendar.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScheduleBinding.inflate(inflater, container, false)
        binding.calendarRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.scheduleRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cal.time = viewModel.run { dates[selectedDayId] }
        Log.d(TAG, cal.time.toString())
        binding.monthYearTextView.text = monthParser(cal.get(Calendar.MONTH))
        binding.calendarRecyclerView.adapter = CalendarAdapter(viewModel.dates, viewModel.selectedDayId)
        if (viewModel.scheduleRepository.isEmpty()) {
            binding.scheduleRecyclerView.adapter = ScheduleAddAdapter(showDialog)
        } else {
            binding.scheduleRecyclerView.adapter =
                ScheduleLessonAdapter(viewModel.scheduleRepository.getScheduleForDay(cal.get(Calendar.DAY_OF_WEEK)))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun monthParser(month: Int): String {
        return when (month) {
            0 -> getString(R.string.january)
            1 -> getString(R.string.february)
            2 -> getString(R.string.march)
            3 -> getString(R.string.april)
            4 -> getString(R.string.may)
            5 -> getString(R.string.june)
            6 -> getString(R.string.july)
            7 -> getString(R.string.august)
            8 -> getString(R.string.september)
            9 -> getString(R.string.october)
            10 -> getString(R.string.november)
            else -> getString(R.string.december)
        }
    }

    private val showDialog: () -> Unit = {
        val dialog = CreateScheduleDialog(updateScheduleRecycler)
        dialog.show(childFragmentManager, "dialog")
    }

    private val updateScheduleRecycler: () -> Unit = {
        Log.d(TAG, cal.time.toString())
        binding.scheduleRecyclerView.adapter =
            ScheduleLessonAdapter(viewModel.scheduleRepository.getScheduleForDay(cal.get(Calendar.DAY_OF_WEEK)))
    }
}