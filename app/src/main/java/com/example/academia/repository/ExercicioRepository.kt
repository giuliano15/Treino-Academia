package com.example.academia.repository

import com.example.academia.R
import com.example.academia.model.Exercicio


class ExercicioRepository {
    private val listaExercicios = mutableListOf<Exercicio>()


    // Lista estática de treinos (mock de dados)
    private val exercicios: List<Exercicio> = listOf(
        Exercicio("Supino sentado na maquina", R.drawable.peito,"Peito","30" ),
        Exercicio("Extensão de triceps deitado", R.drawable.abdomen, "Triceps","45"),
        Exercicio("Rotação externa da polia", R.drawable.peito, "Peito","45"),
        Exercicio("Elevação lateral em pé", R.drawable.braco, "Braços","50"),
        Exercicio("Esteira", R.drawable.costas,"costas","60" ),
        Exercicio("Flexão de pernas", R.drawable.todo_corpo,"Gluteos","30" ),
        Exercicio("Crucifixo em banco reto", R.drawable.costas,"Costas","45" )
        // Adicione mais treinos conforme necessário
    )

    // Método para obter a lista de treinos
    fun getExercicio(): List<Exercicio> {
        // Retorna a lista dinâmica se não estiver vazia, caso contrário, retorna a lista estática
        return if (listaExercicios.isNotEmpty()) {
            listaExercicios
        } else {
            exercicios
        }
    }
}