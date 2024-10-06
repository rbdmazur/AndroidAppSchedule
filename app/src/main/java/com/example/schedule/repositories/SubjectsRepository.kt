package com.example.schedule.repositories

import android.content.Context
import com.example.schedule.model.Subject

class SubjectsRepository private constructor(context: Context) {
    private val names = listOf(
        "������� ������� � ������������",
        "������������� �������� ���������",
        "������ ����������������� ������� ������",
        "������ ���������� ���������������� ��������������",
        "������������ � �������������� ����������",
        "������������� ����������������",
        "���������� ������������ �������������",
        "������������� ���������",
        "������������ ��������",
        "�������������� �������������"
    )

    private val subjects = initSubjects()

    private fun initSubjects(): List<Subject> {
        val list = ArrayList<Subject>()
        for (i in names.indices) {
            list.add(Subject(i, names[i]))
        }

        return list
    }

    fun getSubjects(): List<Subject> = subjects
    fun getSubject(id: Int): Subject = subjects[id]

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