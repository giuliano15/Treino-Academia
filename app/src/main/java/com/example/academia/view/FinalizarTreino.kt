package com.example.academia.view

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
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

    private var nomeTreino: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        onBackPressedDispatcher.addCallback(this , object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

            }
        })

        // recuperarDadosIniciaisUsuarios()

//        binding.btnRestart.setOnClickListener {
//            // Navegar de volta para o fragmento de lista de treinos
//            finish()
//        }

        binding.btnFinish.setOnClickListener {
            firebaseAuth.signOut()
            finishAffinity()
        }

        nomeTreino = intent.getStringExtra("nomeTreino")

        val mensagem =
            " ${nomeTreino ?: ""}"
        binding.displayResult.text = mensagem


    }

}





