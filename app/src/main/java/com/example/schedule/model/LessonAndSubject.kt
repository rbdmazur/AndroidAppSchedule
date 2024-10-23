package com.example.schedule.model

import androidx.room.Embedded
import androidx.room.Relation

data class LessonAndSubject(
    @Embedded val subject: Subject,
    @Relation(
        parentColumn = "id",
        entityColumn = "subject_id"
    )
    val lesson: Lesson
)
