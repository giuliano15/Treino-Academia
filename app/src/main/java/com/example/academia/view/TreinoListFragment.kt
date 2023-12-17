package com.example.academia.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.academia.OnEditClickListener
import com.example.academia.OnTreinoItemClickListener
import com.example.academia.R
import com.example.academia.adapters.TreinoListAdapter
import com.example.academia.databinding.FragmentTreinoListBinding
import com.example.academia.model.Treino
import com.google.firebase.auth.FirebaseAuth

class TreinoListFragment : Fragment(), OnTreinoItemClickListener, OnEditClickListener {

    private lateinit var treinoViewModel: TreinoViewModel
    private lateinit var treinoListAdapter: TreinoListAdapter

    private var _binding: FragmentTreinoListBinding? = null

    private val binding get() = _binding!!

    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTreinoListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicialize o ViewModel e o RecyclerView
        treinoViewModel = ViewModelProvider(requireActivity()).get(TreinoViewModel::class.java)

        treinoListAdapter = TreinoListAdapter(
            emptyList(),
            this,
            onDeleteClickListener = { treino ->
                treinoViewModel.confirmarExclusao(treino)
            },
            onEditClickListener = { treino ->
                treinoViewModel.editarTreino(treino, treino.documentId)
                DialogUtils.showEditDialog(requireContext(), treino, treinoListAdapter, treinoViewModel,treino.documentId)
            }
        )

        val recyclerViewTreinos: RecyclerView = view.findViewById(R.id.recyclerViewTreinos)
        recyclerViewTreinos.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewTreinos.adapter = treinoListAdapter

        // Carrega os treinos do Firestore associados ao usuário logado
        firebaseAuth.currentUser?.uid?.let { uidUsuario ->
            treinoViewModel.carregarTreinosDoFirestore()
        }

        // Observa as mudanças na lista de treinos
        treinoViewModel.treinosNovos.observe(viewLifecycleOwner, Observer { treinosNovos ->
            if (treinosNovos.isEmpty()) {
                // Mostra um Toast informando que não há treinos criados
                Toast.makeText(requireContext(), "Ainda não há treinos criados.", Toast.LENGTH_SHORT).show()
            }

            treinoListAdapter.treinos = treinosNovos
            treinoListAdapter.notifyDataSetChanged()
        })

        treinoViewModel.deleteConfirmation.observe(viewLifecycleOwner, Observer { status ->
            when (status) {
                1 -> showDeleteConfirmationDialog()
                -1 -> {
                    // Lógica quando o usuário cancela a exclusão
                }
            }
        })
    }

    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Confirmação")
            .setMessage("Deseja excluir este treino?")
            .setPositiveButton("Sim") { _, _ ->
                treinoViewModel.excluirTreinoPendente()
            }
            .setNegativeButton("Cancelar") { _, _ ->

            }
            .show()
    }

    override fun onTreinoItemClick(treino: Treino) {
        val bundle = Bundle().apply {
            putSerializable("treino", treino)
        }

        if (findNavController().currentDestination?.id == R.id.container_treino_list) {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment, bundle)
        }
    }

    override fun onEditClicked(treino: Treino) {
        // Em qualquer fragmento onde você deseja chamar a função
        DialogUtils.showEditDialog(requireContext(), treino, treinoListAdapter, treinoViewModel, treino.documentId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
