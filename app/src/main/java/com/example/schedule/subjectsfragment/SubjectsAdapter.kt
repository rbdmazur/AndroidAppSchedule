package com.example.schedule.subjectsfragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.schedule.databinding.SubjectCardItemBinding
import com.example.schedule.model.Subject
import java.util.zip.Inflater

class SubjectsAdapter(
    private val list: List<Subject>
) : RecyclerView.Adapter<SubjectsAdapter.SubjectsViewHolder>()  {

    inner class SubjectsViewHolder(
        private val binding: SubjectCardItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(subject: Subject) {
            binding.subjectTitleTextView.text = subject.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SubjectCardItemBinding.inflate(inflater, parent, false)
        return SubjectsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: SubjectsViewHolder, position: Int) {
        val subj = list[position]
        holder.bind(subj)
    }
}