package com.example.academia.view

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.academia.R
import com.example.academia.util.setFilterOnlyNumbers

class DialogConfigExercicio {
    companion object {
        fun showConfigurarExercicioDialog(
            context: Context,
            title: String,
            onPositiveClick: (String, String, String, String, String) -> Unit,
            onNegativeClick: () -> Unit
        ) {
            val dialogView =
                LayoutInflater.from(context).inflate(R.layout.dialog_configurar_exercicio, null)

            val nomeTreino = dialogView.findViewById<EditText>(R.id.editTextNomeTreino)
            val cargaEditText = dialogView.findViewById<EditText>(R.id.editTextCarga)
            val tempoEditText = dialogView.findViewById<EditText>(R.id.editTextTempo)
            val quantidadeSeriesEditText =
                dialogView.findViewById<EditText>(R.id.editTextQuantidadeSeries)
            val observacacao = dialogView.findViewById<EditText>(R.id.editTextObservacao)
            val titleTextView = dialogView.findViewById<TextView>(R.id.title)
            val btnAdd = dialogView.findViewById<Button>(R.id.btnAdd)
            val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)

            cargaEditText.setFilterOnlyNumbers()
            tempoEditText.setFilterOnlyNumbers()
            quantidadeSeriesEditText.setFilterOnlyNumbers()

            val builder = AlertDialog.Builder(context)
                .setView(dialogView)

            val alertDialog = builder.create()

            alertDialog.setOnShowListener {
                val positiveButton = btnAdd
                positiveButton.setOnClickListener {
                    val nomeTreino = nomeTreino.text.toString()
                    val carga = cargaEditText.text.toString()
                    val tempo = tempoEditText.text.toString()
                    val quantidadeSeries = quantidadeSeriesEditText.text.toString()
                    val observacao = observacacao.text.toString()

                    // Verifica se os campos não estão vazios
                    if (carga.isNotEmpty() && tempo.isNotEmpty() && quantidadeSeries.isNotEmpty()) {
                        onPositiveClick(nomeTreino, carga, tempo, quantidadeSeries, observacao)
                        alertDialog.dismiss()  // Fecha o diálogo após o clique positivo
                    } else {
                        // Exibe uma mensagem de erro se algum campo estiver vazio
                        Toast.makeText(
                            context,
                            "Preencha todos os campos",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                val negativeButton = btnCancel
                negativeButton.setOnClickListener {
                    onNegativeClick()
                    alertDialog.dismiss()
                }
            }

            // Configurar o título principal
            titleTextView.text = title

            alertDialog.show()
        }
    }
}
