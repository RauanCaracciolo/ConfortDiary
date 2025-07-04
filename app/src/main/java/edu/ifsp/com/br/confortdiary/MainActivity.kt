package edu.ifsp.com.br.confortdiary

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import edu.ifsp.com.br.confortdiary.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding  // ViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnWriteToday.setOnClickListener {
            val intent = Intent(this, WriteTodayActivity::class.java)
            startActivity(intent)
        }

        binding.btnViewDays.setOnClickListener {
            val intent = Intent(this, ViewDaysActivity::class.java)
            startActivity(intent)
        }
    }
}
