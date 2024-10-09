package com.example.schedule.schedulefragment

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.schedule.R
import com.example.schedule.databinding.CalendarDayBinding
import java.util.Calendar
import java.util.Date

class CalendarHolder(
    private val binding: CalendarDayBinding,
    private val context: Context,
    private val isSelected: Boolean
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(day: Date) {
        val cal = Calendar.getInstance()
        cal.time = day
        Log.d("CALENDAR", cal.time.toString())
        binding.apply {
            dayNumber.text = cal.get(Calendar.DAY_OF_MONTH).toString()
            dayOfWeek.text = daysOfWeekParser(cal.get(Calendar.DAY_OF_WEEK))
        }

        if (isSelected) {
            makeSelected()
        }
    }

    private fun makeSelected() {
        val res = context.resources
        binding.apply {
            dayCardView.setCardBackgroundColor(res.getColor(R.color.coal))
            dayNumber.setTextColor(res.getColor(R.color.white))
        }
    }

    private fun daysOfWeekParser(day: Int): String {
        val res = context.resources
        return when(day) {
            2 -> res.getString(R.string.monday)
            3 -> res.getString(R.string.tuesday)
            4 -> res.getString(R.string.wednesday)
            5 -> res.getString(R.string.thursday)
            6 -> res.getString(R.string.friday)
            7 -> res.getString(R.string.saturday)
            else -> res.getString(R.string.sunday)
        }
    }
}
class CalendarAdapter(
    private val dates: List<Date>,
    private val selectedDayId: Int
) : RecyclerView.Adapter<CalendarHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CalendarDayBinding.inflate(inflater, parent, false)
        val isSelected = viewType == 1
        return CalendarHolder(binding, parent.context, isSelected)
    }

    override fun getItemCount(): Int = dates.size

    override fun onBindViewHolder(holder: CalendarHolder, position: Int) {
        val date = dates[position]
        holder.bind(date)
    }

    // 1 - selected, 0 - not selected
    override fun getItemViewType(position: Int): Int {
        return if (position == selectedDayId) {
            1
        } else {
            0
        }
    }
}