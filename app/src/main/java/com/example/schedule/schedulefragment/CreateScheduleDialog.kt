package com.example.schedule.schedulefragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.schedule.R
import com.example.schedule.databinding.CreateScheduleDialogBinding
import com.example.schedule.model.Lesson
import com.example.schedule.model.Schedule
import com.example.schedule.repositories.ScheduleRepository
import com.example.schedule.repositories.SubjectsRepository
import com.google.android.material.card.MaterialCardView
import java.util.UUID

private const val TAG = "DIALOG"

class CreateScheduleDialog(
    private val updateScheduleRecycler: () -> Unit
) : DialogFragment() {

    private val times = listOf(
        "8:15-9:35",
        "9:45-11:05",
        "11:15-12:35",
        "13:00-14:20",
        "14:30-15:50",
        "16:00-17:20",
        "17:40-19:00",
        "19:10-20:30",
        "20:40-22:00"
    )
    private lateinit var currentTimes: List<String>
    private val subjectsRepository = SubjectsRepository.get()
    private val subjectNames = initSubjects()
    private lateinit var types: List<String>

    private var lastTime = 0
    private var lessonsCounter = 0
    //day in daysView arrays
    private var currentDay = 0

    private var currentDayOfWeek = 2

    private lateinit var days: List<MaterialCardView>
    private lateinit var daysText: List<TextView>

    private var schedule: Schedule? = null

    private val scheduleRepository = ScheduleRepository.get()

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
            days = listOf(
                cardViewMon,
                cardViewTue,
                cardViewWed,
                cardViewThu,
                cardViewFri,
                cardViewSat
            )

            daysText = listOf(
                dayCardMon,
                dayCardTue,
                dayCardWed,
                dayCardThu,
                dayCardFri,
                dayCardSat
            )
        }
        types = listOf(resources.getString(R.string.lecture), resources.getString(R.string.practise))
        Log.d(TAG, "OnCreateView works")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        val timeAdapter = getSpinnerAdapter(times)
        val subjectAdapter = getSpinnerAdapter(subjectNames)
        val typesAdapter = getSpinnerAdapter(types)
        val res = resources
        binding.apply {
            lessonCounterText.text = res.getString(R.string.lesson_counter, lessonsCounter)
            timeSpinner.adapter = timeAdapter
            subjectsSpinner.adapter = subjectAdapter
            typeSpinner.adapter = typesAdapter

            nextDayButton.isEnabled = schedule != null

            addLessonButton.setOnClickListener {
                addLesson()
            }

            addFreeButton.setOnClickListener {
                addFree()
            }

            nextDayButton.setOnClickListener {
                nextDay()
            }
        }

        makeSelected(currentDay)
        Log.d(TAG, "onViewCreated works")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initSubjects(): List<String> {
        val result = ArrayList<String>()
        val rep = subjectsRepository.getSubjects()
        rep.forEach {
            result.add(it.name)
        }
        return result
    }

    private fun makeSelected(day: Int) {
        val dayCard = days[day]
        dayCard.setCardBackgroundColor(resources.getColor(R.color.coal))
        val dayText = daysText[day]
        dayText.setTextColor(resources.getColor(R.color.white))
    }

    private fun addLesson() {
        if (schedule == null) {
            schedule = Schedule(UUID.randomUUID(), currentDayOfWeek)
        }

        val subjectId = binding.subjectsSpinner.selectedItemId.toInt()
        val type = binding.typeSpinner.selectedItemId.toInt()
        val time = binding.timeSpinner.selectedItem.toString()
        lastTime = binding.timeSpinner.selectedItemId.toInt()
        val aud = binding.auditoriumTextField.text.toString()

        val lesson = Lesson(
            UUID.randomUUID(),
            aud,
            time,
            type,
            subjectsRepository.getSubject(subjectId)
        )

        schedule!!.lessons.add(lesson)

        lessonsCounter++
        lastTime++

        updateUIAfterAddLesson()
    }

    private fun addFree() {
        schedule = Schedule(UUID.randomUUID(), currentDayOfWeek)
        if (currentDay < 5) {
            nextDay()
        } else {
            endDialog()
        }
    }

    private fun nextDay() {
        schedule?.let { scheduleRepository.addSchedule(it) }

        currentDay++
        currentDayOfWeek++
        lessonsCounter = 0
        schedule = null
        updateUIForNextDay()
    }

    private fun updateUIAfterAddLesson() {
        currentTimes = times.subList(lastTime, times.lastIndex)
        val timeAdapter = getSpinnerAdapter(currentTimes)

        binding.apply {
            lessonCounterText.text = resources.getString(R.string.lesson_counter, lessonsCounter)
            auditoriumTextField.text.clear()
            subjectsSpinner.setSelection(0)
            timeSpinner.adapter = timeAdapter
            typeSpinner.setSelection(0)

            nextDayButton.isEnabled = schedule != null
        }
    }

    private fun updateUIForNextDay() {
        makeSelected(currentDay)
        val timeAdapter = getSpinnerAdapter(times)
        binding.apply {
            lessonCounterText.text = resources.getString(R.string.lesson_counter, lessonsCounter)
            auditoriumTextField.text.clear()
            subjectsSpinner.setSelection(0)
            timeSpinner.adapter = timeAdapter
            typeSpinner.setSelection(0)

            nextDayButton.isEnabled = schedule != null
        }

        if (currentDay == 5) {
            binding.nextDayButton.text = resources.getString(R.string.create_sch_btn)
            binding.nextDayButton.setOnClickListener {
                endDialog()
            }
        }
    }

    private fun endDialog() {
        schedule?.let { scheduleRepository.addSchedule(it) }
        updateScheduleRecycler()
        this.dismiss()
    }

    private fun getSpinnerAdapter(data: List<String>): ArrayAdapter<String>? {
        return this.context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_dropdown_item, data) }
    }
}