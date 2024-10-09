package com.example.schedule.schedulefragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.schedule.databinding.AddScheduleCardBinding

class ScheduleAddHolder(
    private val binding: AddScheduleCardBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun setClick(show: () -> Unit) {
        binding.root.setOnClickListener {
            show()
        }
    }
}
class ScheduleAddAdapter(
    private val showDialog: () -> Unit
) : RecyclerView.Adapter<ScheduleAddHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleAddHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AddScheduleCardBinding.inflate(inflater, parent, false)
        return ScheduleAddHolder(binding)
    }

    override fun getItemCount(): Int = 1

    override fun onBindViewHolder(holder: ScheduleAddHolder, position: Int) {
        holder.setClick(showDialog)
    }

}