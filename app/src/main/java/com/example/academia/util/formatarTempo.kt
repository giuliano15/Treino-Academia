package com.example.academia.util

fun formatarTempo(tempo: String): String {
    try {
        val segundos = tempo.toInt()
        val minutos = segundos / 60
        val segundosRestantes = segundos % 60
        return String.format("%02d:%02d", minutos, segundosRestantes)
    } catch (e: NumberFormatException) {
        // Lida com o caso em que a conversão para Int falha
        return "00:00"
    }
}