package com.example.schedule.fragments.profilefragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.schedule.databinding.ScheduleTitleItemBinding
import com.example.schedule.model.Schedule

class ProfileScheduleAdapter(
    private val checkedScheduleIndex: Int,
    private val addScheduleListener: () -> Unit
) : RecyclerView.Adapter<ProfileScheduleAdapter.ScheduleViewHolder>() {


    inner class ScheduleViewHolder(
        private val binding: ScheduleTitleItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(schedule: Schedule, isChecked: Boolean) {
            binding.scheduleTitleTextView.text = schedule.title
            if (isChecked) {
                binding.isCheckedImageView.visibility = View.VISIBLE
            } else {
                binding.isCheckedImageView.visibility = View.INVISIBLE
            }
        }

        fun makeAddView() {
            binding.apply {
                isCheckedImageView.visibility = View.INVISIBLE
                scheduleTitleTextView.visibility = View.INVISIBLE
                addScheduleImageView.visibility = View.VISIBLE

                root.setOnClickListener {
                    addScheduleListener()
                }
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Schedule>() {
        override fun areItemsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
            return oldItem == newItem
        }
    }

    val diffList = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ScheduleTitleItemBinding.inflate(inflater, parent, false)
        return ScheduleViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return diffList.currentList.size + 1
    }

    //1 - simple 0 - add view
    override fun getItemViewType(position: Int): Int {
        return if (position < diffList.currentList.size) 1 else 0
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        if (position < diffList.currentList.size) {
            val schedule = diffList.currentList[position]
            val isChecked = checkedScheduleIndex == position
            holder.bind(schedule, isChecked)
        } else {
            holder.makeAddView()
        }
    }


}