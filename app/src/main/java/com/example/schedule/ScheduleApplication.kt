package com.example.schedule

import android.app.Application
import com.example.schedule.repositories.ScheduleRepository
import com.example.schedule.repositories.SubjectsRepository

class ScheduleApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        SubjectsRepository.init(this)
        ScheduleRepository.init(this)
    }
}