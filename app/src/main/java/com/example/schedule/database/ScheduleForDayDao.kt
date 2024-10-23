package com.example.schedule.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.schedule.model.Lesson
import com.example.schedule.model.LessonAndSubject
import com.example.schedule.model.ScheduleForDay
import com.example.schedule.model.Subject
import java.util.UUID

@Dao
interface ScheduleForDayDao {

    @Query("SELECT * FROM lessons WHERE schedule_for_day_id=(:scheduleId)")
    suspend fun getLessons(scheduleId: UUID): List<Lesson>

    @Query("SELECT * FROM lessons " +
            "JOIN subjects ON lessons.subject_id=subjects.id WHERE lessons.schedule_for_day_id=(:scheduleId)")
    suspend fun getLessonsWithSubjects(scheduleId: UUID): Map<Lesson, Subject>

    @Insert
    suspend fun addScheduleForDay(scheduleForDay: ScheduleForDay)

    @Transaction
    @Query("SELECT * FROM lessons WHERE schedule_for_day_id=(:scheduleForDayId)")
    suspend fun getLessonsAndSubjects(scheduleForDayId: UUID): List<LessonAndSubject>
}