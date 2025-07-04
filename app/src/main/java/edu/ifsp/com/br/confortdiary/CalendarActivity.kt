package edu.ifsp.com.br.confortdiary

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import edu.ifsp.com.br.confortdiary.adapters.CalendarAdapter
import edu.ifsp.com.br.confortdiary.databinding.ActivityCalendarBinding
import edu.ifsp.com.br.confortdiary.viewModel.CalendarViewModel
import java.text.SimpleDateFormat
import java.util.*

class CalendarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalendarBinding
    private val viewModel: CalendarViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val year = intent.getIntExtra("year", -1)
        val month = intent.getIntExtra("month", -1)

        if (year == -1 || month == -1) {
            Toast.makeText(this, "Erro ao carregar mês/ano", Toast.LENGTH_SHORT).show()
            finish()
            return
        }


        val calendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
        }

        val sdf = SimpleDateFormat("MMMM 'de' yyyy", Locale("pt", "BR"))
        binding.tvMonthYear.text = sdf.format(calendar.time).replaceFirstChar { it.uppercase() }


        viewModel.daysWithEntry.observe(this, Observer { days ->
            binding.calendarRecyclerView.layoutManager = GridLayoutManager(this, 7)
            binding.calendarRecyclerView.adapter = CalendarAdapter(days) { selectedDay ->
                val selectedDateId = String.format("%04d-%02d-%02d", year, month + 1, selectedDay)

                val intent = Intent(this, ViewSingleDayActivity::class.java)
                intent.putExtra("day_id", selectedDateId)
                startActivity(intent)
            }
        })

        viewModel.loadDays(applicationContext, year, month)
    }
}
