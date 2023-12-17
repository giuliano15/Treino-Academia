package com.example.academia.util

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import com.example.academia.R


class DialogConfigExercicio {
    companion object {
        fun showConfigurarExercicioDialog(
            context: Context,
            title: String,
            onPositiveClick: (String, String, String,String,String) -> Unit,
            onNegativeClick: () -> Unit
        ) {
            val dialogView =
                LayoutInflater.from(context).inflate(R.layout.dialog_configurar_exercicio, null)

            val nomeTreino = dialogView.findViewById<EditText>(R.id.editTextNomeTreino)
            val cargaEditText = dialogView.findViewById<EditText>(R.id.editTextCarga)
            val tempoEditText = dialogView.findViewById<EditText>(R.id.editTextTempo)
            val quantidadeSeriesEditText = dialogView.findViewById<EditText>(R.id.editTextQuantidadeSeries)
            val observacacao = dialogView.findViewById<EditText>(R.id.editTextObservacao)

            cargaEditText.setFilterOnlyNumbers()
            tempoEditText.setFilterOnlyNumbers()
            quantidadeSeriesEditText.setFilterOnlyNumbers()

            val builder = AlertDialog.Builder(context)
                .setView(dialogView)
                .setPositiveButton("Adicionar", null)  // Ouve o clique, mas não fecha o diálogo

            val alertDialog = builder.create()

            alertDialog.setOnShowListener {
                val positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                positiveButton.setOnClickListener {
                    val nomeTreino = nomeTreino.text.toString()
                    val carga = cargaEditText.text.toString()
                    val tempo = tempoEditText.text.toString()
                    val quantidadeSeries = quantidadeSeriesEditText.text.toString()
                    val observacao = observacacao.text.toString()

                    // Verifica se os campos não estão vazios
                    if (carga.isNotEmpty() && tempo.isNotEmpty() && quantidadeSeries.isNotEmpty()) {
                        onPositiveClick(nomeTreino,carga, tempo, quantidadeSeries,observacao)
                        alertDialog.dismiss()  // Fecha o diálogo após o clique positivo
                    } else {
                        // Exibe uma mensagem de erro se algum campo estiver vazio
                        Toast.makeText(context, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            builder.setNegativeButton("Cancelar") { _, _ ->
                onNegativeClick()
            }

            alertDialog.show()
        }
    }
}


