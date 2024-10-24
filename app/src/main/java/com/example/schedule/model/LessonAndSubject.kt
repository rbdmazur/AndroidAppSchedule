package com.example.schedule.model

import androidx.room.Embedded
import androidx.room.Relation

data class LessonAndSubject(
    @Embedded val lesson: Lesson,
    @Relation(
        parentColumn = "subject_id",
        entityColumn = "id"
    )
    val subject: Subject,
)
