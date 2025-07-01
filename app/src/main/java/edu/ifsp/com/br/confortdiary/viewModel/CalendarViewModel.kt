package edu.ifsp.com.br.confortdiary.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.ifsp.com.br.confortdiary.model.DayCalendarItem
import edu.ifsp.com.br.confortdiary.model.SQLiteHelper
import java.util.*

class CalendarViewModel : ViewModel() {

    private val _daysWithEntry = MutableLiveData<List<DayCalendarItem>>()
    val daysWithEntry: LiveData<List<DayCalendarItem>> = _daysWithEntry

    fun loadDays(context: Context, year: Int, month: Int) {
        val db = SQLiteHelper(context)
        val timestamps = db.getAllDates()

        val filteredDays = timestamps.filter {
            val c = Calendar.getInstance().apply { timeInMillis = it }
            c.get(Calendar.YEAR) == year && c.get(Calendar.MONTH) == month
        }.map {
            Calendar.getInstance().apply { timeInMillis = it }.get(Calendar.DAY_OF_MONTH)
        }.toSet()

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        val totalDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        val days = (1..totalDays).map { day ->
            DayCalendarItem(day, day in filteredDays)
        }
        Log.d("CalendarViewModel", "Carregando dias de $month/$year")
        timestamps.forEach {
            val c = Calendar.getInstance().apply { timeInMillis = it }
            Log.d("CalendarViewModel", "Data salva: ${c.get(Calendar.DAY_OF_MONTH)}/${c.get(Calendar.MONTH)}/${c.get(Calendar.YEAR)}")
        }

        _daysWithEntry.value = days
    }
}
