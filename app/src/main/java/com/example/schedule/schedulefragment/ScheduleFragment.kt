package com.example.schedule.schedulefragment

import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import kotlinx.coroutines.launch
import java.util.Date

private const val TAG = "VIEWMODEL"
class ScheduleFragment : Fragment() {

    private var _binding: FragmentScheduleBinding? = null
    val binding: FragmentScheduleBinding
    get() = checkNotNull(_binding)

    private val args: ScheduleFragmentArgs by navArgs()

    private val viewModel: ScheduleViewModel by viewModels {
        ScheduleViewModelFactory(args.scheduleId)
    }
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
        binding.monthYearTextView.text = monthParser(cal.get(Calendar.MONTH))
        binding.calendarRecyclerView.adapter = CalendarAdapter(viewModel.dates, viewModel.selectedDayId, updateUiAfterChangeDate)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                if (args.scheduleId == null) {
                    Log.d("SCH_ID", "randomFirst: ${args.scheduleId}")
                    binding.scheduleRecyclerView.adapter = ScheduleAddAdapter(showDialog)
                } else {
                    val scheduleForDay = viewModel.scheduleRepository.getScheduleForDayOfWeek(
                        args.scheduleId!!,
                        cal.get(Calendar.DAY_OF_WEEK)
                    )
                    binding.scheduleRecyclerView.adapter = ScheduleLessonAdapter(
                        viewModel.scheduleRepository.getLessonsWithSubjects(scheduleForDay.id)
                    )

                }

                if (viewModel.scheduleRepository.getSubjects().isEmpty()) {
                    viewModel.subjectsRepository.addSubjectsToDataBase(viewModel.scheduleRepository)
//                    findNavController().navigate(ScheduleFragmentDirections.actionScheduleToAddSubjects())
                }
            }
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

    private fun getScheduleForDay(dayOfWeek: Int, list: List<ScheduleForDay>): ScheduleForDay {
        return list.stream().filter { p ->
            p.dayOfWeek == dayOfWeek
        }.findFirst().get()
    }

    private val showDialog: () -> Unit = {
        findNavController().navigate(ScheduleFragmentDirections.actionScheduleToInit())
    }


    private val updateUiAfterChangeDate: (day: Date, dayId: Int) -> Unit = { day, dayId ->
        cal.time = day
        viewModel.selectedDayId = dayId

        if (args.scheduleId != null) {
            viewLifecycleOwner.lifecycleScope.launch {
                val scheduleForDay = viewModel.scheduleRepository.getScheduleForDayOfWeek(
                    args.scheduleId!!,
                    cal.get(Calendar.DAY_OF_WEEK)
                )
                binding.scheduleRecyclerView.adapter = ScheduleLessonAdapter(
                    viewModel.scheduleRepository.getLessonsWithSubjects(scheduleForDay.id)
                )
            }
        }
    }
}