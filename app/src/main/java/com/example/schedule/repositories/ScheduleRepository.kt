package com.example.schedule.repositories

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.schedule.database.ScheduleDatabase
import com.example.schedule.model.Lesson
import com.example.schedule.model.LessonAndSubject
import com.example.schedule.model.Schedule
import com.example.schedule.model.ScheduleForDay
import com.example.schedule.model.Subject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.UUID
import kotlin.coroutines.coroutineContext

private const val DATABASE_NAME = "schedule-database"

class ScheduleRepository private constructor(context: Context) {

    private val database: ScheduleDatabase = Room
        .databaseBuilder(
            context.applicationContext,
            ScheduleDatabase::class.java,
            DATABASE_NAME
        )
        .build()

    suspend fun getSubjects(): List<Subject> = database.getSubjectDao().getSubjects()
    suspend fun getSubjectById(subjectId: UUID): Subject =
        database.getSubjectDao().getSubject(subjectId)
    suspend fun addSubject(subject: Subject) = withContext(Dispatchers.IO) {
        database.getSubjectDao().addSubject(subject)
    }

    suspend fun getLessons(): List<Lesson> = database.getLessonDao().getLessons()
    suspend fun getLessonById(lessonId: UUID): Lesson = database.getLessonDao().getLessonById(lessonId)
    suspend fun getSubjectName(subjectId: UUID): String = database.getLessonDao().getSubjectName(subjectId)
    suspend fun addLesson(lesson: Lesson) {
        database.getLessonDao().addLesson(lesson)
    }

    suspend fun getLessonsFromSchedule(scheduleId: UUID): List<Lesson> =
        database.getScheduleForDayDao().getLessons(scheduleId)
    suspend fun addScheduleForDay(scheduleForDay: ScheduleForDay) {
        database.getScheduleForDayDao().addScheduleForDay(scheduleForDay)
    }
    suspend fun getLessonsWithSubjects(scheduleForDay: UUID): List<LessonAndSubject> = withContext(Dispatchers.IO) {
        database.getScheduleForDayDao().getLessonsAndSubjects(scheduleForDay)
    }

    suspend fun getScheduleForWeek(scheduleId: UUID): List<ScheduleForDay> = withContext(Dispatchers.IO) {
        database.getScheduleDao().getSchedulesForDays(scheduleId)
    }
    suspend fun getSchedules(): List<Schedule> = withContext(Dispatchers.IO) {
        database.getScheduleDao().getSchedules()
    }
    suspend fun addSchedule(schedule: Schedule) = withContext(Dispatchers.IO) {
        database.getScheduleDao().addSchedule(schedule)
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