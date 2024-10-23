package com.example.schedule.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "schedule")
data class Schedule(
    @PrimaryKey val id: UUID,
    val title: String,
)
