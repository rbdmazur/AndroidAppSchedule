package com.example.schedule.schedulefragment

import android.icu.util.Calendar
import androidx.lifecycle.ViewModel
import com.example.schedule.repositories.ScheduleRepository
import com.example.schedule.repositories.SubjectsRepository
import java.util.Date

class ScheduleViewModel() : ViewModel() {
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
