package com.example.schedule.schedulefragment.createschedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.schedule.R
import com.example.schedule.Utils
import com.example.schedule.databinding.CreateScheduleDialogBinding
import com.example.schedule.model.Lesson
import com.example.schedule.model.ScheduleForDay
import com.example.schedule.model.Subject
import com.example.schedule.repositories.ScheduleRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.UUID

class CreateScheduleFragment : Fragment() {


    private val scheduleRepository = ScheduleRepository.get()
    private val args: CreateScheduleFragmentArgs by navArgs()

    private val createViewModel: CreateScheduleViewModel by viewModels {
        CreateScheduleViewModelFactory(args.scheduleId)
    }

    private var _binding: CreateScheduleDialogBinding? = null
    private val binding: CreateScheduleDialogBinding
        get() = checkNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CreateScheduleDialogBinding.inflate(inflater, container, false)
        binding.apply {
            createViewModel.days = listOf(
                cardViewMon,
                cardViewTue,
                cardViewWed,
                cardViewThu,
                cardViewFri,
                cardViewSat
            )

            createViewModel.daysText = listOf(
                dayCardMon,
                dayCardTue,
                dayCardWed,
                dayCardThu,
                dayCardFri,
                dayCardSat
            )
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createViewModel.types = listOf(resources.getString(R.string.lecture), resources.getString(R.string.practise))

        val res = resources
        binding.apply {
            lessonCounterText.text = res.getString(R.string.lesson_counter, createViewModel.lessonsCounter)
            timeSpinner.adapter = getSpinnerAdapter(createViewModel.times)
            viewLifecycleOwner.lifecycleScope.launch {
                subjectsSpinner.adapter = getSpinnerAdapterFromSubjects(scheduleRepository.getSubjects())
            }
            typeSpinner.adapter = getSpinnerAdapter(createViewModel.types)

            nextDayButton.isEnabled = createViewModel.scheduleForDay != null

            binding.apply {
                addLessonButton.setOnClickListener {
                    viewLifecycleOwner.lifecycleScope.launch {
                        addLesson()
                    }
                }

                addFreeButton.setOnClickListener {
                    viewLifecycleOwner.lifecycleScope.launch {
                        addFree()
                    }
                }

                nextDayButton.setOnClickListener {
                    viewLifecycleOwner.lifecycleScope.launch {
                        nextDay()
                    }
                }
            }
        }

        makeSelected(createViewModel.currentDay)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun makeSelected(day: Int) {
        val dayCard = createViewModel.days[day]
        dayCard.setCardBackgroundColor(resources.getColor(R.color.coal))
        val dayText = createViewModel.daysText[day]
        dayText.setTextColor(resources.getColor(R.color.white))
    }

    private suspend fun addLesson() {
        if (createViewModel.scheduleForDay == null) {
            createViewModel.scheduleForDay = ScheduleForDay(
                UUID.randomUUID(),
                createViewModel.currentDayOfWeek,
                createViewModel.scheduleId)
        }

        val subject = binding.subjectsSpinner.selectedItem as Subject
        val type = binding.typeSpinner.selectedItemId.toInt()
        val time = binding.timeSpinner.selectedItem.toString()
        createViewModel.lastTime = binding.timeSpinner.selectedItemId.toInt()
        val aud = binding.auditoriumTextField.text.toString()

        val lesson = Lesson(
            UUID.randomUUID(),
            aud,
            time,
            type,
            subject.id,
            createViewModel.scheduleForDay!!.id
        )

        createViewModel.lessons.add(lesson)

        createViewModel.lessonsCounter++
        createViewModel.lastTime++

        if (createViewModel.currentTimes.size == 1) {
            nextDay()
        }

        updateUIAfterAddLesson()
    }

    private suspend fun addFree() {
        createViewModel.scheduleForDay = ScheduleForDay(
            UUID.randomUUID(),
            createViewModel.currentDayOfWeek,
            createViewModel.scheduleId)
        createViewModel.lessons = ArrayList()
        if (createViewModel.currentDay < 5) {
            nextDay()
        } else {
            endDialog()
        }
    }

    private suspend fun nextDay() {
        createViewModel.scheduleForDay?.let { scheduleRepository.addScheduleForDay(it) }
        if (createViewModel.lessons.isNotEmpty()) {
            createViewModel.lessons.forEach {
                scheduleRepository.addLesson(it)
            }
        }

        createViewModel.currentDay++
        createViewModel.currentDayOfWeek++
        createViewModel.lessonsCounter = 0
        createViewModel.scheduleForDay = null
        createViewModel.lessons = ArrayList()
        updateUIForNextDay()
    }

    private fun updateUIAfterAddLesson() {
        createViewModel.currentTimes = Utils.subList(createViewModel.currentTimes, createViewModel.lastTime)
        val timeAdapter = getSpinnerAdapter(createViewModel.currentTimes)
        createViewModel.lastTime = 0

        binding.apply {
            lessonCounterText.text = resources.getString(R.string.lesson_counter, createViewModel.lessonsCounter)
            auditoriumTextField.text.clear()
            timeSpinner.adapter = timeAdapter
            typeSpinner.setSelection(0)

            nextDayButton.isEnabled = createViewModel.scheduleForDay != null
        }
    }

    private fun updateUIForNextDay() {
        makeSelected(createViewModel.currentDay)
        val timeAdapter = getSpinnerAdapter(createViewModel.times)
        createViewModel.lastTime = 0
        createViewModel.currentTimes = createViewModel.times
        binding.apply {
            lessonCounterText.text = resources.getString(R.string.lesson_counter, createViewModel.lessonsCounter)
            auditoriumTextField.text.clear()
            subjectsSpinner.setSelection(0)
            timeSpinner.adapter = timeAdapter
            typeSpinner.setSelection(0)

            nextDayButton.isEnabled = createViewModel.scheduleForDay != null
        }

        if (createViewModel.currentDay == 5) {
            binding.nextDayButton.text = resources.getString(R.string.create_sch_btn)
            binding.nextDayButton.setOnClickListener {
                lifecycleScope.launch {
                    endDialog()
                }
            }
        }
    }

    private suspend fun endDialog() {
        createViewModel.scheduleForDay?.let { scheduleRepository.addScheduleForDay(it) }
        scheduleRepository.addScheduleForDay(ScheduleForDay(UUID.randomUUID(), 1, createViewModel.scheduleId))
        findNavController().navigate(
            CreateScheduleFragmentDirections.actionCreateToSchedule()
        )
    }

    private fun getSpinnerAdapter(data: List<String>): ArrayAdapter<String>? {
        return this.context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_dropdown_item, data) }
    }

    private fun getSpinnerAdapterFromSubjects(data: List<Subject>): ArrayAdapter<Subject>? {
        return this.context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_dropdown_item, data) }
    }

}