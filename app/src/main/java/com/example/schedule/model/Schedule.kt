package com.example.schedule.model

import java.util.UUID

data class Schedule(
    val id: UUID,
    val dayOfWeek: Int,
    val lessons: ArrayList<Lesson> = ArrayList()
)
