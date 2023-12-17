package com.example.academia.viewModel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.academia.model.DetalheExercicio
import com.example.ademia.repository.DetalheExercicioRepository
import kotlinx.coroutines.launch

class DetalheExercicioViewModel : ViewModel(){
    private val _detalhesExercicios = MutableLiveData<List<DetalheExercicio>>()

    val detalhesExercicios: LiveData<List<DetalheExercicio>> get() = _detalhesExercicios
    private val repositoryDetalhe = DetalheExercicioRepository()

    fun carregarDetalhesExercicios() {
        viewModelScope.launch {
            try {
                // Utilize o repositório para obter a lista de detalhes dos exercícios
                val listaDetalhesExercicio = repositoryDetalhe.getDetalheExercicio()

                // Atualize o LiveData com a lista de detalhes dos exercícios
                _detalhesExercicios.value = listaDetalhesExercicio
            } catch (e: Exception) {
                // Lide com qualquer exceção, por exemplo, emitindo um log ou mostrando um erro
                Log.e(ContentValues.TAG, "Erro ao carregar detalhes exercicios", e)
            }
        }
    }

}