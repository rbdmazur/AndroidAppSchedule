package com.example.schedule.schedulefragment

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import androidx.lifecycle.ViewModel
import java.util.Date
import java.util.Locale

class ScheduleViewModel : ViewModel() {
    private val sdf = SimpleDateFormat("MMMM yyyy", Locale.ENGLISH)
    val dates: List<Date> = initDates()
    var selectedDayId = 0



    private fun initDates(): List<Date> {
        val list = ArrayList<Date>()
        val calendar = Calendar.getInstance()
        for (i in 0 until 21) {
            val date = calendar.time
            list.add(date)
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }
        return list
    }
}