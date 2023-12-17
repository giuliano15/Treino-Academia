package com.example.academia.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.academia.OnTreinoItemClickListener
import com.example.academia.R
import com.example.academia.model.Treino
import java.io.Serializable


class TreinoListAdapter( var treinos: List<Treino>,
                         private val itemClickListener: OnTreinoItemClickListener,
                         private val onDeleteClickListener: (Treino) -> Unit,
                         private val onEditClickListener: (Treino) -> Unit
) :
    RecyclerView.Adapter<TreinoListAdapter.TreinoViewHolder>(), Serializable {

    inner class TreinoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nomeTextView: TextView = itemView.findViewById(R.id.nomeTextView)
        val descricaoTextView: TextView = itemView.findViewById(R.id.descricaoTextView)
        val dateTextView: TextView = itemView.findViewById(R.id.dateTime)
        val carga: TextView = itemView.findViewById(R.id.carga)
        val serie: TextView = itemView.findViewById(R.id.qtdSerie)
        val btnOverflow: ImageButton = itemView.findViewById(R.id.ic_menu)

        private lateinit var treino: Treino

        fun bind(treino: Treino) {
            this.treino = treino
            nomeTextView.text = treino.nomeTreino
            descricaoTextView.text = treino.nomeExercicio
            dateTextView.text = treino.data.toString()
            //val cargaformatada = "$carga sec"
            carga.text = treino.carga
            serie.text = treino.quantidadeSeries
            btnOverflow.setImageResource(R.drawable.ic_novo)

            btnOverflow.setOnClickListener {
                showPopupMenu()
            }

            itemView.setOnClickListener {
                itemClickListener.onTreinoItemClick(treino)
            }
        }

        private fun showPopupMenu() {
            val popupMenu = PopupMenu(itemView.context, btnOverflow)
            popupMenu.inflate(R.menu.menu_overflow)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_excluir -> {
                        deleteItem()
                        true
                    }
                    R.id.action_editar -> {
                        // Lógica para editar
                        editItem()
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }

        private fun deleteItem() {
            val adapterPosition = adapterPosition
            if (adapterPosition != RecyclerView.NO_POSITION) {
                val treino = treinos[adapterPosition]
                onDeleteClickListener.invoke(treino)
            }

        }
        private fun editItem() {
            val adapterPosition = adapterPosition
            if (adapterPosition != RecyclerView.NO_POSITION) {
                val treino = treinos[adapterPosition]
                onEditClickListener.invoke(treino)
            }
        }
    }

    override fun getItemCount(): Int = treinos.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TreinoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_treino, parent, false)
        return TreinoViewHolder(view)
    }


    override fun onBindViewHolder(holder: TreinoViewHolder, position: Int) {
        val treino = treinos[position]
        holder.bind(treino)
    }
}


