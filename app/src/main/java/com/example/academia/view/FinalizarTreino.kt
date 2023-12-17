package com.example.academia.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.academia.databinding.ActivityFinalizarTreinoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FinalizarTreino : AppCompatActivity() {

    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val firestore by lazy {
        FirebaseFirestore.getInstance()
    }

    private val binding by lazy {
        ActivityFinalizarTreinoBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        recuperarDadosIniciaisUsuarios()

        binding.btnRestart.setOnClickListener {
            // Navegar de volta para o fragmento de lista de treinos
            finish()
        }

        binding.btnFinish.setOnClickListener {
            firebaseAuth.signOut()
            finishAffinity()
        }
    }

    private fun recuperarDadosIniciaisUsuarios() {
        val idUsuario = firebaseAuth.currentUser?.uid
        if (idUsuario != null) {

            firestore
                .collection("usuarios")
                .document(idUsuario)
                .get()
                .addOnSuccessListener { documentSnapshot ->

                    val dadosUsuarios = documentSnapshot.data
                    if (dadosUsuarios != null) {

                        val nome = dadosUsuarios["nome"] as String

                        val mensagem =
                            "Parabéns, ${nome ?: ""}, você concluiu mais um treino com sucesso!"
                        binding.displayResult.text = mensagem

                    }

                }

        }

    }

}