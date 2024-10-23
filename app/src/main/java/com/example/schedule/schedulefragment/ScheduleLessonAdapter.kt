package com.example.schedule.schedulefragment

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.schedule.R
import com.example.schedule.databinding.LessonScheduleCardBinding
import com.example.schedule.model.Lesson
import com.example.schedule.model.ScheduleForDay
import com.example.schedule.model.Subject

class ScheduleLessonHolder(
    private val binding: LessonScheduleCardBinding,
    private val context: Context
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(lesson: Lesson) {
        val res = context.resources
        binding.apply {
//            subjectTextView.text = lesson.subject.name
            typeTextView.text = typeParser(lesson.type)
            auditoriumTextView.text = res.getString(R.string.auditorium, lesson.auditorium)

            val times = timeParser(lesson.time)
            timeTextView1.text = times[0]
            timeTextView2.text = times[1]
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

    private fun timeParser(timeStr: String): List<String> {
        return timeStr.split('-')
    }
}

class ScheduleLessonAdapter(
    private val lessons: Map<Lesson, Subject>
) : RecyclerView.Adapter<ScheduleLessonHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleLessonHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LessonScheduleCardBinding.inflate(inflater, parent, false)
        return ScheduleLessonHolder(binding, parent.context)
    }

    override fun getItemCount(): Int = lessons.size

    override fun onBindViewHolder(holder: ScheduleLessonHolder, position: Int) {
//        val lesson = lessons[position]
//        holder.bind(lesson)
    }
}