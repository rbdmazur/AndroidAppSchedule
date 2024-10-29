package com.example.schedule.profilefragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.schedule.databinding.ScheduleTitleItemBinding
import com.example.schedule.model.Schedule

class ProfileScheduleAdapter(
    private val list: List<Schedule>
) : RecyclerView.Adapter<ProfileScheduleAdapter.ScheduleViewHolder>() {


    inner class ScheduleViewHolder(
        private val binding: ScheduleTitleItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(schedule: Schedule) {
            binding.scheduleTitleTextView.text = schedule.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ScheduleTitleItemBinding.inflate(inflater, parent, false)
        return ScheduleViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        val schedule = list[position]
        holder.bind(schedule)
    }
}