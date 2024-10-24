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



class CalendarAdapter(
    private val dates: List<Date>,
    private val selectedDayId: Int,
    private val updateUI: (Date, Int) -> Unit
) : RecyclerView.Adapter<CalendarAdapter.CalendarHolder>() {
    var selectedItemPos = selectedDayId
    var lastItemSelectedPos = selectedItemPos

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CalendarDayBinding.inflate(inflater, parent, false)
        return CalendarHolder(binding, parent.context)
    }

    override fun getItemCount(): Int = dates.size

    override fun onBindViewHolder(holder: CalendarHolder, position: Int) {
        val date = dates[position]
        if (selectedItemPos == position) {
            holder.makeSelected(date, position)
        } else {
            holder.makeUnselected()
        }
        holder.bind(date)
    }

    inner class CalendarHolder(
        private val binding: CalendarDayBinding,
        private val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.dayCardView.setOnClickListener {
                selectedItemPos = adapterPosition
                if (lastItemSelectedPos == -1) {
                    lastItemSelectedPos = selectedItemPos
                } else {
                    notifyItemChanged(lastItemSelectedPos)
                    lastItemSelectedPos = selectedItemPos
                }
                notifyItemChanged(selectedItemPos)
            }
        }

        fun bind(day: Date) {
            val cal = Calendar.getInstance()
            cal.time = day
            binding.apply {
                dayNumber.text = cal.get(Calendar.DAY_OF_MONTH).toString()
                dayOfWeek.text = daysOfWeekParser(cal.get(Calendar.DAY_OF_WEEK))
            }
        }

        fun makeUnselected() {
            val res = context.resources
            binding.apply {
                dayCardView.setCardBackgroundColor(res.getColor(R.color.white))
                dayNumber.setTextColor(res.getColor(R.color.coal))
            }
        }

        fun makeSelected(day: Date, position: Int) {
            val res = context.resources
            binding.apply {
                dayCardView.setCardBackgroundColor(res.getColor(R.color.coal))
                dayNumber.setTextColor(res.getColor(R.color.white))
            }

            updateUI(day, position)
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
}