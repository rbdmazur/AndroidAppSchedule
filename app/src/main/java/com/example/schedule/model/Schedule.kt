package com.example.schedule.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "schedule",
    indices = [Index("id")])
data class Schedule(
    @PrimaryKey val id: UUID,
    val title: String,
)
