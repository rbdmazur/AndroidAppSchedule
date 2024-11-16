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
    private val addScheduleListener: () -> Unit,
    private val changeSchedule: (Int) -> Unit
) : RecyclerView.Adapter<ProfileScheduleAdapter.ScheduleViewHolder>() {


    inner class ScheduleViewHolder(
        private val binding: ScheduleTitleItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                selectedItemPos = adapterPosition
                if (lastSelectedItemPos == -1) {
                    lastSelectedItemPos = selectedItemPos
                } else {
                    notifyItemChanged(lastSelectedItemPos)
                    lastSelectedItemPos = selectedItemPos
                }
                notifyItemChanged(selectedItemPos)
            }
        }

        fun bind(schedule: Schedule) {
            binding.scheduleTitleTextView.text = schedule.title
        }

        fun makeSelected() {
            binding.isCheckedImageView.visibility = View.VISIBLE
            changeSchedule(selectedItemPos)
        }

        fun makeUnselected() {
            binding.isCheckedImageView.visibility = View.INVISIBLE
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

    private var selectedItemPos = checkedScheduleIndex
    private var lastSelectedItemPos = selectedItemPos

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
            if (selectedItemPos == position) {
                holder.makeSelected()
            } else {
                holder.makeUnselected()
            }
            holder.bind(schedule)
        } else {
            holder.makeAddView()
        }
    }


}