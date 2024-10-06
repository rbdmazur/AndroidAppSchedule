package com.example.schedule.model

import java.util.Date
// type: 0 - lecture, 1 - practical
data class Lesson(
    val id: Int,
    val auditorium: String,
    val time: Date,
    val type: Int,
    val subject: Subject
)
