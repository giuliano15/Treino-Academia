package com.example.academia.model

import java.io.Serializable

data class Treino(
    var documentId: String = "",
    var nomeTreino: String = "",
    val nomeExercicio: String = "",
    val data: String = "",
    var carga: String = "",
    var quantidadeSeries: String = "",
    var tempoExercicio: String = "",
    var observacao: String = "",
    val userId: String = ""
) : Serializable
