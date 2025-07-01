package edu.ifsp.com.br.confortdiary

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import edu.ifsp.com.br.confortdiary.databinding.ActivityViewDaysBinding
import edu.ifsp.com.br.confortdiary.model.SQLiteHelper
import java.text.SimpleDateFormat
import java.util.*

class ViewDaysActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewDaysBinding
    private lateinit var dbHelper: SQLiteHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewDaysBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = SQLiteHelper(this)

        val dates = dbHelper.getAllDates()
        val calendar = Calendar.getInstance()


        val yearSet = sortedSetOf<Int>()
        val monthMap = mutableMapOf<Int, SortedSet<Int>>()

        for (timestamp in dates) {
            calendar.timeInMillis = timestamp
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)

            yearSet.add(year)
            monthMap.getOrPut(year) { sortedSetOf() }.add(month)
        }


        val years = yearSet.toList()
        val yearAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, years)
        binding.spinnerYear.adapter = yearAdapter

        binding.spinnerYear.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedYear = years[position]
                val availableMonths = monthMap[selectedYear]?.toList() ?: emptyList()

                val monthNames = availableMonths.map { monthNumber ->
                    SimpleDateFormat("MMMM", Locale("pt", "BR")).format(
                        GregorianCalendar(2000, monthNumber, 1).time
                    ).replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
                }

                val monthAdapter = ArrayAdapter(this@ViewDaysActivity, android.R.layout.simple_spinner_item, monthNames)
                monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                binding.spinnerMonth.adapter = monthAdapter
                binding.spinnerMonth.tag = availableMonths
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }


        binding.btnShowCalendar.setOnClickListener {
            val selectedYear = binding.spinnerYear.selectedItem as? Int
            val selectedMonthIndex = binding.spinnerMonth.selectedItemPosition
            val availableMonths = binding.spinnerMonth.tag as? List<Int>

            if (selectedYear != null && availableMonths != null && selectedMonthIndex in availableMonths.indices) {
                val selectedMonth = availableMonths[selectedMonthIndex]

                val intent = Intent(this, CalendarActivity::class.java).apply {
                    putExtra("year", selectedYear)
                    putExtra("month", selectedMonth)
                }
                startActivity(intent)
            }else {
                Toast.makeText(this, "Selecione um ano e mês válidos.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
