package edu.ifsp.com.br.confortdiary.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory

object BitmapUtils {
    fun loadBitmap(path: String): Bitmap? {
        return try {
            BitmapFactory.decodeFile(path)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
