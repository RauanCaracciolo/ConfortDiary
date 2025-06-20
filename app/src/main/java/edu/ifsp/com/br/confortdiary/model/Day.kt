package edu.ifsp.com.br.confortdiary.model

import android.graphics.Bitmap

data class Day (
    val id: String,
    val photo: Bitmap?,
    val text: String,
    val mood: Mood,
    val time: Long
)