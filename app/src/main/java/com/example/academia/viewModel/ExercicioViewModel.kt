package com.example.academia.viewModel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.academia.model.Exercicio
import com.example.academia.repository.ExercicioRepository
import kotlinx.coroutines.launch

class ExercicioViewModel : ViewModel() {

    private val _exercicios = MutableLiveData<List<Exercicio>>()
    val exercicios: LiveData<List<Exercicio>> get() = _exercicios

    private val repositoryExec = ExercicioRepository()


    fun carregarExercicios() {
        viewModelScope.launch {
            try {
                // Utilize o repositório para obter a lista de treinos
                val listaExercicios = repositoryExec.getExercicio()

                // Atualize o LiveData com a lista de treinos
                _exercicios.value = listaExercicios
            } catch (e: Exception) {
                // Lide com qualquer exceção, por exemplo, emitindo um log ou mostrando um erro
                Log.e(ContentValues.TAG, "Erro ao carregar exercicios", e)
            }
        }
    }
}