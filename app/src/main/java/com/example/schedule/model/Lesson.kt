package com.example.schedule.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

// type: 0 - lecture, 1 - practical

@Entity(tableName = "lessons",
    indices = [Index("id")],
    foreignKeys = [
        ForeignKey(
            entity = Subject::class,
            parentColumns = ["id"],
            childColumns = ["subject_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ScheduleForDay::class,
            parentColumns = ["id"],
            childColumns = ["schedule_for_day_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ])
data class Lesson(
    @PrimaryKey val id: UUID,
    val auditorium: String,
    val time: String,
    val type: Int,
    @ColumnInfo(name = "subject_id") val subjectId: UUID,
    @ColumnInfo(name = "schedule_for_day_id") val scheduleForDayId: UUID
)
