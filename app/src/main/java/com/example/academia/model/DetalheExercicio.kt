package com.example.academia.model

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate

data class DetalheExercicio @RequiresApi(Build.VERSION_CODES.O) constructor(
    var nome: String  = "",
    var imagemUrl: Int = 0,
    var descricao: String  = "",
    var TextTime: String = "",
    var nomeTreino: String = "",
    var carga: Int = 0,
    var tempo: String = "",
    val quantidadeSeries: Int? = 0,
    var data: LocalDate = LocalDate.now(),
    var observacao: String = ""
)
