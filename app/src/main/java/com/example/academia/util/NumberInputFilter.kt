package com.example.academia.util

import android.text.InputFilter
import android.text.Spanned


import android.widget.EditText

fun EditText.setFilterOnlyNumbers() {
    filters = arrayOf(NumberInputFilter())
}

class NumberInputFilter : InputFilter {
    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        source?.let {
            for (i in start until end) {
                if (!Character.isDigit(source[i])) {
                    return "" // Ignorar o caractere não numérico
                }
            }
        }
        return null // Aceitar a sequência de entrada
    }
}
