package com.example.schedule.schedulefragment

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.schedule.R
import com.example.schedule.databinding.LessonScheduleCardBinding
import com.example.schedule.model.Lesson
import com.example.schedule.model.Schedule

class ScheduleLessonHolder(
    private val binding: LessonScheduleCardBinding,
    private val context: Context
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(lesson: Lesson) {
        val res = context.resources
        binding.apply {
            subjectTextView.text = lesson.subject.name
            typeTextView.text = typeParser(lesson.type)
            auditoriumTextView.text = res.getString(R.string.auditorium, lesson.auditorium)
        }
    }

    //0 - lecture, 1 - practise
    private fun typeParser(type: Int): String {
        val res = context.resources
        return if (type == 0) {
            res.getString(R.string.lecture)
        } else {
            res.getString(R.string.practise)
        }
    }
}

class ScheduleLessonAdapter(
    private val schedule: Schedule
) : RecyclerView.Adapter<ScheduleLessonHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleLessonHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LessonScheduleCardBinding.inflate(inflater, parent, false)
        return ScheduleLessonHolder(binding, parent.context)
    }

    override fun getItemCount(): Int = schedule.lessons.size

    override fun onBindViewHolder(holder: ScheduleLessonHolder, position: Int) {
        val lesson = schedule.lessons[position]
        holder.bind(lesson)
    }
}