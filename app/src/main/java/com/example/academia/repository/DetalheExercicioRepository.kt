package com.example.ademia.repository

import android.os.Build
import android.provider.Settings.Global.getString
import androidx.annotation.RequiresApi
import com.example.academia.R
import com.example.academia.model.DetalheExercicio

class DetalheExercicioRepository {
    private val listaDetalhe = mutableListOf<DetalheExercicio>()

    // Lista estática de treinos (mock de dados)
    @RequiresApi(Build.VERSION_CODES.O)
    private val detalheExercicio: List<DetalheExercicio> = listOf(
        DetalheExercicio("Supino sentado na maquina", R.drawable.supino_sentado_na_maquina,"Indicado para a realização de exercícios que trabalham a musculatura do bíceps" ),
        DetalheExercicio("Extensão de triceps deitado", R.drawable.extensao_triceps,"Ele permite que seus braços girem naturalmente, reduzindo qualquer dor no pulso"),
        DetalheExercicio("Rotação extera da polia", R.drawable.rotacao_de_polia,"Esse treino visa especialmente a ativação do músculo infraespinhal e redondo menor."),
        DetalheExercicio("Elevação lateral em pé", R.drawable.elevacao_lateral_em_pe, "Ideal para o fortalecimento dos músculos dos ombros além de melhorar a postura do atleta"),
        DetalheExercicio("Esteira", R.drawable.esteira,"Melhora a força e a resistência dos membros superiores" ),
        DetalheExercicio("Flexão de pernas", R.drawable.flexao_de_pernas,"Fortifica os musculos da perna" ),
        DetalheExercicio("Crucifixo em banco reto", R.drawable.crucifixo_de_banco_reto,"Trabalha as cabeças esternais dos seus principais músculos peitorais" )

    )

    // Método para obter a lista de treinos
    fun getDetalheExercicio(): List<DetalheExercicio> {
        // Retorna a lista dinâmica se não estiver vazia, caso contrário, retorna a lista estática
        return if (listaDetalhe.isNotEmpty()) {
            listaDetalhe
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                detalheExercicio
            } else {
                TODO("VERSION.SDK_INT < O")
            }
        }
    }
}