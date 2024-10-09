package com.example.schedule.model

import java.util.Date
import java.util.UUID

// type: 0 - lecture, 1 - practical
data class Lesson(
    val id: UUID,
    val auditorium: String,
    val time: String,
    val type: Int,
    val subject: Subject
)
