package com.example.schedule.model

data class Schedule(
    val id: Int,
    val dayOfWeek: Int,
    val lessons: List<Lesson>
)
