package com.example.schedule.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.schedule.model.Lesson
import com.example.schedule.model.Schedule
import com.example.schedule.model.ScheduleForDay
import com.example.schedule.model.Subject

@Database(entities = [
    Subject::class,
    Lesson::class,
    ScheduleForDay::class,
    Schedule::class
], version = 1)
abstract class ScheduleDatabase : RoomDatabase() {
    abstract fun getSubjectDao(): SubjectDao
    abstract fun getLessonDao(): LessonDao
    abstract fun getScheduleForDayDao(): ScheduleForDayDao
    abstract fun getScheduleDao(): ScheduleDao
}