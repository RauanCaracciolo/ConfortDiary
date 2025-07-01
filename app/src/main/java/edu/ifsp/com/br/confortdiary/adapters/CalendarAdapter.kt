package edu.ifsp.com.br.confortdiary.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.ifsp.com.br.confortdiary.R
import edu.ifsp.com.br.confortdiary.model.DayCalendarItem

class CalendarAdapter(
    private val days: List<DayCalendarItem>,
    private val onDayClick: (Int) -> Unit
) : RecyclerView.Adapter<CalendarAdapter.DayViewHolder>() {

    inner class DayViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dayText: TextView = view.findViewById(R.id.tvDay)

        init {
            view.setOnClickListener {
                val day = days[adapterPosition].dayNumber
                onDayClick(day)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_day_calendar, parent, false)
        return DayViewHolder(view)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val item = days[position]
        holder.dayText.text = item.dayNumber.toString()

        if (item.hasEntry) {
            holder.dayText.setBackgroundResource(R.drawable.day_with_entry_background)
            holder.dayText.setTextColor(Color.WHITE)
        } else {
            holder.dayText.setBackgroundColor(Color.TRANSPARENT)
            holder.dayText.setTextColor(Color.DKGRAY)
        }
    }

    override fun getItemCount() = days.size
}