package edu.ifsp.com.br.confortdiary.model

data class Day (
    val id: String,
    val photo: String?,
    val text: String,
    val mood: Mood,
    val time: Long
)