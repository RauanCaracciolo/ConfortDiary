package edu.ifsp.com.br.confortdiary.viewModel


import android.content.Context
import androidx.lifecycle.ViewModel
import edu.ifsp.com.br.confortdiary.model.SQLiteHelper
import edu.ifsp.com.br.confortdiary.model.Day

class DayViewModel : ViewModel() {

    fun insertDay(context: Context, day: Day): Boolean {
        val dbHelper = SQLiteHelper(context)
        return dbHelper.insertDay(day)
    }
}
