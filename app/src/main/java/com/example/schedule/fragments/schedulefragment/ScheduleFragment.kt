package com.example.schedule.fragments.schedulefragment

import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.currentCompositionErrors
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.schedule.R
import com.example.schedule.databinding.FragmentScheduleBinding
import com.example.schedule.model.ScheduleForDay
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

class ScheduleFragment : Fragment() {

    private var _binding: FragmentScheduleBinding? = null
    val binding: FragmentScheduleBinding
    get() = checkNotNull(_binding)
    private var currentScheduleId: UUID? = null

    private val TAG = "ScheduleFragment"

    private val viewModel: ScheduleViewModel by viewModels()

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
        Log.d(TAG, "onCreateView: ${viewModel.scheduleId.value}")
        observe()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cal.time = viewModel.run { dates[selectedDayId] }
        binding.monthYearTextView.text = monthParser(cal.get(Calendar.MONTH))
        binding.calendarRecyclerView.adapter = CalendarAdapter(viewModel.dates, viewModel.selectedDayId, updateUiAfterChangeDate)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                delay(100L)
                if (currentScheduleId != null) {
                    hideEmptyScheduleText()
                    val scheduleForDay = viewModel.scheduleRepository.getScheduleForDayOfWeek(
                        currentScheduleId!!,
                        cal.get(Calendar.DAY_OF_WEEK)
                    )
                    setupScheduleLessonAdapter(scheduleForDay)
                } else {
                    showEmptyScheduleText()
                }

                if (viewModel.scheduleRepository.getSubjects().isEmpty()) {
                    viewModel.subjectsRepository.addSubjectsToDataBase(viewModel.scheduleRepository)
//                    findNavController().navigate(ScheduleFragmentDirections.actionScheduleToAddSubjects())
                }
            }
        }
        Log.d(TAG, "onViewCreated")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.d(TAG, "onDestroyView")
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

    private val updateUiAfterChangeDate: (day: Date, dayId: Int) -> Unit = { day, dayId ->
        cal.time = day
        viewModel.selectedDayId = dayId

        if (currentScheduleId != null) {
            viewLifecycleOwner.lifecycleScope.launch {
                val scheduleForDay = viewModel.scheduleRepository.getScheduleForDayOfWeek(
                    currentScheduleId!!,
                    cal.get(Calendar.DAY_OF_WEEK)
                )
                setupScheduleLessonAdapter(scheduleForDay)
            }
        }
    }

    private suspend fun setupScheduleLessonAdapter(scheduleForDay: ScheduleForDay) {
        val adapter = ScheduleLessonAdapter()
        adapter.lessons.submitList(
            viewModel.scheduleRepository.getLessonsWithSubjects(scheduleForDay.id)
        )
        binding.scheduleRecyclerView.adapter = adapter
    }

    private fun hideEmptyScheduleText() {
        binding.apply {
            emptyScheduleTextView.visibility = View.INVISIBLE
            scheduleRecyclerView.visibility = View.VISIBLE
        }
    }

    private fun showEmptyScheduleText() {
        binding.apply {
            emptyScheduleTextView.visibility = View.VISIBLE
            scheduleRecyclerView.visibility = View.INVISIBLE
        }
    }

    private fun observe() {
        viewModel.updateScheduleId()
        lifecycleScope.launch {
            viewModel.scheduleId.collect {
                currentScheduleId = it
            }
        }
    }
}