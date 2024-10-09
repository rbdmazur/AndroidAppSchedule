package com.example.schedule.repositories

import android.content.Context
import com.example.schedule.model.Schedule

class ScheduleRepository private constructor(context: Context) {

    private val list: ArrayList<Schedule> = ArrayList()

    fun isEmpty() = list.isEmpty()
    fun getScheduleForDay(day: Int): Schedule = list.stream().filter { p ->
        p.dayOfWeek == day
    }.findFirst().get()

    fun addSchedule(schedule: Schedule) {
        list.add(schedule)
    }

    companion object {
        private var INSTANCE: ScheduleRepository? = null

        fun init(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = ScheduleRepository(context)
            }
        }

        fun get(): ScheduleRepository {
            return INSTANCE ?:
            throw IllegalStateException("ScheduleRepository is not initialized")
        }
    }
}