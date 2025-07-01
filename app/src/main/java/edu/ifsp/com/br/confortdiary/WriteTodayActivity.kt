package edu.ifsp.com.br.confortdiary

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import edu.ifsp.com.br.confortdiary.databinding.ActivityWriteTodayBinding
import edu.ifsp.com.br.confortdiary.model.Day
import edu.ifsp.com.br.confortdiary.model.Mood
import edu.ifsp.com.br.confortdiary.viewModel.DayViewModel
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class WriteTodayActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWriteTodayBinding
    private lateinit var viewModel: DayViewModel
    private var capturedImage: Bitmap? = null

    companion object {
        const val REQUEST_CAMERA_PERMISSION = 100
    }

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        if (bitmap != null) {
            capturedImage = bitmap
            binding.imagePreview.setImageBitmap(bitmap)
        } else {
            Toast.makeText(this, "Imagem não capturada", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWriteTodayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[DayViewModel::class.java]

        checkAndRequestPermissions()

        binding.btnCamera.setOnClickListener {
            if (hasCameraPermission()) {
                cameraLauncher.launch(null)
            } else {
                requestCameraPermission()
            }
        }


        binding.btnSave.setOnClickListener {
            saveDay()
        }
    }


    private fun getSelectedMood(): Mood? {
        val selectedId = binding.emojiGroup.checkedRadioButtonId
        return if (selectedId != -1) {
            when (findViewById<RadioButton>(selectedId).text.toString().lowercase()) {
                "😢" -> Mood.verySad
                "🙁" -> Mood.sad
                "😐" -> Mood.normal
                "🙂" -> Mood.happy
                "😁" -> Mood.veryHappy
                else -> Mood.normal
            }
        } else null
    }

    private fun saveBitmapToInternalStorage(bitmap: Bitmap, filename: String): String {
        val file = File(filesDir, filename)
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }
        return file.absolutePath
    }

    private fun getTodayId(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun saveDay() {
        val text = binding.etTextEntry.text.toString()
        val mood = getSelectedMood()
        val bitmap = capturedImage

        if (text.isBlank() || mood == null || bitmap == null) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            return
        }

        val fileName = "${getTodayId()}.png"
        val photoPath = saveBitmapToInternalStorage(bitmap, fileName)

        val day = Day(
            id = getTodayId(),
            photo = photoPath,
            text = text,
            mood = mood,
            time = System.currentTimeMillis()
        )

        viewModel.insertDay(this, day)

        Toast.makeText(this, "Dia salvo com sucesso!", Toast.LENGTH_SHORT).show()
        finish()
    }


    private fun hasCameraPermission(): Boolean =
        ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED


    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
    }


    private fun checkAndRequestPermissions() {
        val permissionsToRequest = mutableListOf<String>()
        if (!hasCameraPermission()) permissionsToRequest.add(Manifest.permission.CAMERA)
        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest.toTypedArray(), 999)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CAMERA_PERMISSION -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permissão de câmera negada", Toast.LENGTH_SHORT).show()
                }
            }
            }
        }
}
