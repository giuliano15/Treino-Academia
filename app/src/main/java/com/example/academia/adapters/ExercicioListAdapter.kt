package com.example.academia.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.academia.OnExercicioClickListener
import com.example.academia.R
import com.example.academia.model.Exercicio

class ExercicioListAdapter(var exercicios: List<Exercicio>,
                           private val onExercicioClickListener: OnExercicioClickListener
) :
    RecyclerView.Adapter<ExercicioListAdapter.ExercicioViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExercicioListAdapter.ExercicioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_exercicio2, parent, false)
        return ExercicioViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExercicioListAdapter.ExercicioViewHolder, position: Int) {
        val exercicio = exercicios[position]
        holder.bind(exercicio)
    }

    override fun getItemCount():
       Int = exercicios.size


    inner class ExercicioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),View.OnClickListener {
        private val textNomeExercicio: TextView = itemView.findViewById(R.id.textNomeExercicio)
        private val textCategoriaExercicio: TextView = itemView.findViewById(R.id.textCategoriaExercicio)
        private val imageExercicio: ImageView = itemView.findViewById(R.id.imageExercicio)
        private val textSec: TextView = itemView.findViewById(R.id.textSec)
        // Adicione outros elementos conforme necessário


        fun bind(exercicio: Exercicio) {
            val tempo = exercicio.textTime
            val tempoFormatado = "$tempo sec"
            val imageUrl = exercicio.imagemUrl

            textNomeExercicio.text = exercicio.nome
            textCategoriaExercicio.text = exercicio.categoria
            textSec.text = tempoFormatado

            Glide.with(itemView)
                .load(imageUrl)
                .placeholder(android.R.drawable.btn_default) // Imagem de placeholder enquanto carrega
                .error(android.R.drawable.stat_notify_error) // Imagem de erro, se não puder carregar
                .into(imageExercicio)

        }

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                onExercicioClickListener.onExercicioClick(exercicios[position])
            }
        }
    }

}