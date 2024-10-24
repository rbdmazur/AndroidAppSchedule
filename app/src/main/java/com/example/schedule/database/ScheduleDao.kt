package com.example.schedule.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.schedule.model.Schedule
import com.example.schedule.model.ScheduleForDay
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface ScheduleDao {

    @Query("SELECT * FROM schedules_for_day WHERE schedule_id=(:scheduleId)")
    suspend fun getSchedulesForDays(scheduleId: UUID): List<ScheduleForDay>

    @Query("SELECT * FROM schedule")
    suspend fun getSchedules(): List<Schedule>

    @Query("SELECT * FROM schedules_for_day WHERE schedule_id=(:scheduleId) AND day_of_week=(:dayOfWeek)")
    suspend fun getScheduleForDayOfWeek(scheduleId: UUID, dayOfWeek: Int): ScheduleForDay

    @Insert
    suspend fun addSchedule(schedule: Schedule)
}