package com.example.schedule.repositories

import android.content.Context
import android.util.Log
import com.example.schedule.model.Subject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID
const val SUBJECT_TAG = "SUBJECTS"
class SubjectsRepository private constructor(context: Context) {
    private val names = listOf(
        "Business communication and interactions",
        "Modeling of market processes",
        "Fundamentals of data mining",
        "Fundamentals of intellectual property management",
        "Parallel and distributed computations",
        "Integer programming",
        "E-government technologies",
        "Artificial intelligence",
        "Operations Research",
        "Mathematical modeling"
    )


    private val subjects = initSubjects()

    private fun initSubjects(): List<Subject> {
        val list = ArrayList<Subject>()
        for (i in names.indices) {
            list.add(Subject(UUID.randomUUID(), names[i]))
        }
        Log.d(SUBJECT_TAG, "From SubjectsRep: $subjects")
        return list
    }

    suspend fun addSubjectsToDataBase(scheduleRepository: ScheduleRepository) = withContext(Dispatchers.IO) {
        for (i in subjects.indices) {
            scheduleRepository.addSubject(subjects[i])
        }
    }

    fun getSubjects(): List<Subject> = subjects

    companion object {
        private var INSTANCE: SubjectsRepository? = null

        fun init(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = SubjectsRepository(context)
            }
        }

        fun get(): SubjectsRepository {
            return INSTANCE ?:
            throw IllegalStateException("SubjectRepository is not initialized")
        }
    }


}