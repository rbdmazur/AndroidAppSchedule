package com.example.schedule.fragments.schedulefragment

import android.icu.util.Calendar
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.schedule.repositories.ScheduleRepository
import com.example.schedule.repositories.SubjectsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

class ScheduleViewModel : ViewModel() {
    //This is a day in the dates array
    var selectedDayId = 0
    val dates: List<Date> = initDates()
    val scheduleRepository = ScheduleRepository.get()
    val subjectsRepository = SubjectsRepository.get()

    private val TAG = "ScheduleFragment"

    private val _scheduleId: MutableStateFlow<UUID?> = MutableStateFlow(null)
    val scheduleId: StateFlow<UUID?> = _scheduleId.asStateFlow()
    private var _checkedScheduleIndex = 0
    val checkedScheduleIndex = _checkedScheduleIndex



    init {
        updateScheduleId()
    }

    private fun initDates(): List<Date> {
        val list = ArrayList<Date>()
        val calendar = Calendar.getInstance()
        for (i in 0 until 14) {
            val date = calendar.time
            list.add(date)
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }
        return list
    }

    fun updateScheduleId(id: UUID, index: Int) {
        _scheduleId.value = id
        _checkedScheduleIndex = index
    }

    fun updateScheduleId() {
        viewModelScope.launch {
            val schedules = scheduleRepository.getSchedules()
            if (schedules.isNotEmpty()) {
                _scheduleId.value = schedules[checkedScheduleIndex].id
            }
            else {
                _scheduleId.value = null
            }
        }
    }
}
