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

class CalendarHolderContainer {
    companion object {
        var selectedHolder: CalendarHolder? = null
        var selectedDayId: Int = 0
    }
}

class CalendarHolder(
    private val binding: CalendarDayBinding,
    private val context: Context,
    private val isSelected: Boolean,
    private val updateUI: (Date, Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(day: Date, position: Int) {
        val cal = Calendar.getInstance()
        cal.time = day
        binding.apply {
            dayNumber.text = cal.get(Calendar.DAY_OF_MONTH).toString()
            dayOfWeek.text = daysOfWeekParser(cal.get(Calendar.DAY_OF_WEEK))
        }

        if (isSelected) {
            makeSelected()
        }

        binding.dayCardView.setOnClickListener {
            CalendarHolderContainer.selectedHolder?.let {
                makeUnselected()
            }
            updateUI(day, position)
            this.makeSelected()
        }
    }

    private fun makeUnselected() {
        val res = context.resources
        CalendarHolderContainer.selectedHolder?.let {
            it.binding.apply {
                dayCardView.setCardBackgroundColor(res.getColor(R.color.white))
                dayNumber.setTextColor(res.getColor(R.color.coal))
            }
        }
    }

    private fun makeSelected() {
        val res = context.resources
        binding.apply {
            dayCardView.setCardBackgroundColor(res.getColor(R.color.coal))
            dayNumber.setTextColor(res.getColor(R.color.white))
        }

        CalendarHolderContainer.selectedHolder = this
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
    private val updateUI: (Date, Int) -> Unit
) : RecyclerView.Adapter<CalendarHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CalendarDayBinding.inflate(inflater, parent, false)
        val isSelected = viewType == 1
        return CalendarHolder(binding, parent.context, isSelected, updateUI)
    }

    override fun getItemCount(): Int = dates.size

    override fun onBindViewHolder(holder: CalendarHolder, position: Int) {
        val date = dates[position]
        holder.bind(date, position)
    }

    // 1 - selected, 0 - not selected
    override fun getItemViewType(position: Int): Int {
        return if (position == CalendarHolderContainer.selectedDayId) {
            1
        } else {
            0
        }
    }
}