package com.example.schedule.schedulefragment

import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.schedule.model.Lesson
import com.example.schedule.model.ScheduleForDay
import com.example.schedule.model.Subject
import com.example.schedule.repositories.ScheduleRepository
import com.example.schedule.repositories.SubjectsRepository
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.util.UUID

class CreateScheduleViewModel(val scheduleId: UUID) : ViewModel() {
    val times = listOf(
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
    var currentTimes = times

    val scheduleRepository = ScheduleRepository.get()

    var lastTime = 0
    var lessonsCounter = 0
    //day in daysView arrays
    var currentDay = 0

    var currentDayOfWeek = 2

    var scheduleForDay: ScheduleForDay? = null
    //initialized in onCreateView
    var subjects: List<Subject> = ArrayList()


    var types: List<String> = emptyList()

    var days: List<MaterialCardView> = emptyList()
    var daysText: List<TextView> = emptyList()

    var lessons: ArrayList<Lesson> = ArrayList()
}

class CreateScheduleViewModelFactory(
    private val scheduleId: UUID
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CreateScheduleViewModel(scheduleId) as T
    }
}