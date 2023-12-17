package com.example.academia.util

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    private const val DEFAULT_DATE_FORMAT = "dd/MM/yyyy HH:mm:ss"

    fun getCurrentFormattedDate(dateFormat: String = DEFAULT_DATE_FORMAT, timeZone: String? = null): String {
        val currentTimeMillis = System.currentTimeMillis()
        val currentDate = Date(currentTimeMillis)

        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())

        timeZone?.let {
            formatter.timeZone = TimeZone.getTimeZone(it)
        }

        return formatter.format(currentDate)
    }
}
