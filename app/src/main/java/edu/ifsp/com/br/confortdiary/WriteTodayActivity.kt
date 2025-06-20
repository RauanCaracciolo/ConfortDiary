package edu.ifsp.com.br.confortdiary

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import edu.ifsp.com.br.confortdiary.databinding.ActivityWriteTodayBinding
import java.util.*

class WriteTodayActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWriteTodayBinding
    private var capturedImage: Bitmap? = null

    companion object {
        const val REQUEST_CAMERA_PERMISSION = 100
        const val REQUEST_MIC_PERMISSION = 101
    }

    // Câmera
    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        if (bitmap != null) {
            capturedImage = bitmap
            binding.imagePreview.setImageBitmap(bitmap)
        } else {
            Toast.makeText(this, "Imagem não capturada", Toast.LENGTH_SHORT).show()
        }
    }

    // Reconhecimento de voz
    private val speechResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            val spokenText = result.data!!
                .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.get(0)
            binding.etTextEntry.append(" $spokenText")
        } else {
            Toast.makeText(this, "Não entendi o que você disse", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWriteTodayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkAndRequestPermissions()

        binding.btnCamera.setOnClickListener {
            if (hasCameraPermission()) {
                cameraLauncher.launch(null)
            } else {
                requestCameraPermission()
            }
        }

        binding.btnMicrophone.setOnClickListener {
            if (hasMicPermission()) {
                startVoiceRecognition()
            } else {
                requestMicPermission()
            }
        }
    }

    private fun startVoiceRecognition() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Fale agora...")
        }

        speechResultLauncher.launch(intent)
    }

    private fun getSelectedMood(): String? {
        val selectedId = binding.emojiGroup.checkedRadioButtonId
        return if (selectedId != -1) {
            findViewById<RadioButton>(selectedId).text.toString()
        } else null
    }

    // Permissões

    private fun hasCameraPermission(): Boolean =
        ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

    private fun hasMicPermission(): Boolean =
        ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
    }

    private fun requestMicPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), REQUEST_MIC_PERMISSION)
    }

    private fun checkAndRequestPermissions() {
        val permissionsToRequest = mutableListOf<String>()
        if (!hasCameraPermission()) permissionsToRequest.add(Manifest.permission.CAMERA)
        if (!hasMicPermission()) permissionsToRequest.add(Manifest.permission.RECORD_AUDIO)
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
            REQUEST_MIC_PERMISSION -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permissão de microfone negada", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
