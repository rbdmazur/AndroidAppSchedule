package com.example.schedule.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.schedule.model.Lesson
import com.example.schedule.model.LessonAndSubject
import java.util.UUID

@Dao
interface LessonDao {

    @Query("SELECT * FROM lessons")
    suspend fun getLessons(): List<Lesson>

    @Query("SELECT * FROM lessons WHERE id=(:id)")
    suspend fun getLessonById(id: UUID): Lesson

    @Insert()
    suspend fun addLesson(lesson: Lesson)

    @Query("SELECT subject_name FROM subjects WHERE id=(:subjectId)")
    suspend fun getSubjectName(subjectId: UUID): String


}