package com.example.academia.view

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.academia.model.Treino
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TreinoViewModel : ViewModel() {
    private val _treinos = MutableLiveData<List<Treino>>()
    val treinosNovos: LiveData<List<Treino>> get() = _treinos

    private val _treinoEditado = MutableLiveData<Treino>()

    private var treinoToDelete: Treino? = null
    private var treinoParaEditar: Treino? = null

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun adicionarExercicio(exercicio: Treino) {
        // Chama a função para adicionar treino no Firestore
        adicionarTreinoNoFirestore(exercicio)
    }

    private fun adicionarTreinoNoFirestore(treino: Treino) {
        // Verifica se o usuário está autenticado
        auth.currentUser?.let { user ->
            // Associa o UID do usuário ao treino
            val treinoComUsuario = treino.copy(userId = user.uid)

            // Referência à coleção "treinos"
            val treinosRef = db.collection("treinos")

            // Adiciona o treino ao Firestore
            treinosRef.add(treinoComUsuario)
                .addOnSuccessListener { documentReference ->
                    // Obtém o ID do documento recém-adicionado
                    val treinoId = documentReference.id
                    treinosRef.document(treinoId)
                        .update(
                            "documentId",
                            treinoId
                        ) // Atualiza o campo documentId com o ID do documento
                        .addOnSuccessListener {
                            Log.d("Firestore", "Treino adicionado com ID: $treinoId")
                        }
                        .addOnFailureListener { e ->
                            Log.w("Firestore", "Erro ao atualizar documentId", e)
                        }
                }
                .addOnFailureListener { e ->
                    Log.w("Firestore", "Erro ao adicionar treino", e)
                }
        }
    }

    fun carregarTreinosDoFirestore() {
        // Verifica se o usuário está autenticado
        auth.currentUser?.let { user ->
            // UID do usuário
            val uidUsuario = user.uid

            val treinosRef = db.collection("treinos")

            // Carrega treinos específicos do usuário
            treinosRef.whereEqualTo("userId", uidUsuario)
                .get()
                .addOnSuccessListener { result ->
                    val treinos = mutableListOf<Treino>()

                    for (document in result) {
                        val treino = document.toObject(Treino::class.java)
                        treinos.add(treino)
                    }

                    _treinos.postValue(treinos)
                }
                .addOnFailureListener { exception ->
                    Log.w("Firestore", "Erro ao obter treinos", exception)
                }
        }
    }

    private val _deleteConfirmation = MutableLiveData<Int>()
    val deleteConfirmation: LiveData<Int> get() = _deleteConfirmation

    fun confirmarExclusao(treino: Treino) {
        treinoToDelete = treino
        _deleteConfirmation.value = 1
    }

    fun excluirTreinoPendente() {
        treinoToDelete?.let { treino ->
            // Verifica se o usuário está autenticado
            val listaAtual = _treinos.value ?: emptyList()
            val novaLista = listaAtual.toMutableList()
            novaLista.remove(treino)
            _treinos.postValue(novaLista)
            auth.currentUser?.let { user ->
                // Referência à coleção "treinos"
                val treinosRef = db.collection("treinos")

                // Cria uma consulta para encontrar o documento com base em alguns campos
                val query = treinosRef
                    .whereEqualTo("userId", user.uid)
                    .whereEqualTo("nomeTreino", treino.nomeTreino)
                    .whereEqualTo("nomeExercicio", treino.nomeExercicio)
                // Adicione outros campos relevantes para garantir a exclusão correta

                query.get()
                    .addOnSuccessListener { querySnapshot ->
                        // Itera sobre os documentos correspondentes (deveria ser apenas um)
                        for (document in querySnapshot.documents) {
                            // Obtém o ID do documento e exclui
                            val treinoId = document.id
                            treinosRef.document(treinoId)
                                .delete()
                                .addOnSuccessListener {
                                    Log.d("Firestore", "Treino excluído com ID: $treinoId")
                                }
                                .addOnFailureListener { e ->
                                    Log.w("Firestore", "Erro ao excluir treino", e)
                                }
                        }

                        // Atualize a lista após a exclusão
                        carregarTreinosDoFirestore()

                        treinoToDelete = null
                        _deleteConfirmation.value = -1
                    }
                    .addOnFailureListener { e ->
                        Log.w("Firestore", "Erro ao executar consulta para exclusão", e)
                        // Trate o erro conforme necessário
                    }
            }
        }
    }

    fun salvarEdicaoTreinoNoFirestore(treino: Treino) {
        // Verifica se o usuário está autenticado
        auth.currentUser?.let { user ->
            // Associa o UID do usuário ao treino
            val treinoComUsuario = treino.copy(userId = user.uid)

            // Referência à coleção "treinos"
            val treinosRef = db.collection("treinos")

            // Atualiza os dados do treino no Firestore usando o documentId
            treinosRef.document(treino.documentId)
                .set(treinoComUsuario)
                .addOnSuccessListener {
                    Log.d("Firestore", "Treino editado com ID: ${treino.documentId}")

                    // Atualiza a lista local
                    treinoParaEditar?.let {
                        val listaAtual = _treinos.value ?: emptyList()
                        val novaLista = listaAtual.toMutableList()
                        val index = novaLista.indexOf(it)
                        if (index != -1) {
                            novaLista[index] = treino
                            _treinos.postValue(novaLista)
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Log.w("Firestore", "Erro ao editar treino", e)
                }
        }
    }

    fun editarTreino(treino: Treino, documentId: String) {
        treinoParaEditar = treino.copy(documentId = documentId)
        _treinoEditado.value = treino // Emitir o treino a ser editado
    }

    fun limparTreinoParaEditar() {
        treinoParaEditar = null
    }
}
