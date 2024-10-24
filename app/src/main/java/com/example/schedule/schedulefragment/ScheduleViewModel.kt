package com.example.schedule.schedulefragment

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.schedule.model.ScheduleForDay
import com.example.schedule.repositories.ScheduleRepository
import com.example.schedule.repositories.SubjectsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Locale
import java.util.UUID

class ScheduleViewModel(scheduleId: UUID?) : ViewModel() {
    //This is a day in the dates array
    var selectedDayId = 0
    val dates: List<Date> = initDates()
    val scheduleRepository = ScheduleRepository.get()
    val subjectsRepository = SubjectsRepository.get()

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

}

class ScheduleViewModelFactory(
    private val scheduleId: UUID?
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ScheduleViewModel(scheduleId) as T
    }
}