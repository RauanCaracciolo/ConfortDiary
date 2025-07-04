package edu.ifsp.com.br.confortdiary

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import edu.ifsp.com.br.confortdiary.databinding.ActivityViewSingleDayBinding
import edu.ifsp.com.br.confortdiary.model.Mood
import edu.ifsp.com.br.confortdiary.viewModel.DayViewModel

class ViewSingleDayActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewSingleDayBinding
    private lateinit var viewModel: DayViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewSingleDayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[DayViewModel::class.java]

        val id = intent.getStringExtra("day_id")
        if (id == null) {
            Toast.makeText(this, "Erro: ID do dia não encontrado", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val day = viewModel.getDayById(this, id)
        if (day == null) {
            Toast.makeText(this, "Dia não encontrado", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val bitmap = BitmapFactory.decodeFile(day.photo)
        binding.imageView.setImageBitmap(bitmap)

        binding.textView.text = day.text

        binding.moodView.text = when (day.mood) {
            Mood.verySad -> "😢"
            Mood.sad -> "🙁"
            Mood.normal -> "😐"
            Mood.happy -> "🙂"
            Mood.veryHappy -> "😁"
        }
    }
}
