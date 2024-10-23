package com.example.schedule.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.schedule.model.Subject
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface SubjectDao {

    @Query("SELECT * FROM subjects")
    suspend fun getSubjects(): List<Subject>

    @Query("SELECT * FROM subjects WHERE id=(:id)")
    suspend fun getSubject(id: UUID): Subject

    @Insert
    suspend fun addSubject(subject: Subject)
}