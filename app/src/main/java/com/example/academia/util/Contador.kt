package com.example.academia.util

import android.os.CountDownTimer

class Contador(
    private var totalMillis: Long,
    private val intervalMillis: Long,
    private val onTick: (String) -> Unit,
    private val onFinish: () -> Unit,
) {
    private var isPaused = false
    private var millisUntilFinished: Long = totalMillis
    private var countdownTimer: CountDownTimer? = null

    fun start() {
        isPaused = false
        createTimer()
        countdownTimer?.start()
    }

    fun pause() {
        isPaused = true
        countdownTimer?.cancel()
    }

    fun stop() {
        isPaused = false
        countdownTimer?.cancel()
        millisUntilFinished = totalMillis
        updateUI()
    }

    fun setNewTime(newTotalMillis: Long) {
        totalMillis = newTotalMillis
        millisUntilFinished = newTotalMillis
        updateUI()
    }

    fun getTempoAtual(): Long {
        return millisUntilFinished
    }

    private fun createTimer() {
        countdownTimer = object : CountDownTimer(millisUntilFinished, intervalMillis) {
            override fun onTick(millisUntilFinished: Long) {
                if (!isPaused) {
                    this@Contador.millisUntilFinished = millisUntilFinished
                    updateUI()
                }
            }

            override fun onFinish() {
                onFinish.invoke()
            }
        }
    }

    fun setTempoInicial(tempoInicialMillis: Long) {
        this.totalMillis = tempoInicialMillis
        this.millisUntilFinished = tempoInicialMillis
        updateUI()
    }

    private fun updateUI() {
        val formattedTime = formatMillis(millisUntilFinished)
        onTick.invoke(formattedTime)
    }

    private fun formatMillis(millis: Long): String {
        val totalSeconds = millis / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
}
