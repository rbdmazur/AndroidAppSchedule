package com.example.schedule.schedulefragment

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import androidx.lifecycle.ViewModel
import com.example.schedule.model.Schedule
import com.example.schedule.repositories.ScheduleRepository
import java.util.Date
import java.util.Locale

class ScheduleViewModel : ViewModel() {
    private val sdf = SimpleDateFormat("MMMM yyyy", Locale.ENGLISH)
    //This is a day in the dates array
    var selectedDayId = 0
    val dates: List<Date> = initDates()
    var scheduleRepository = ScheduleRepository.get()



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