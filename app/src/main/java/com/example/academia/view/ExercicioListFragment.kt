package com.example.academia.view


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.academia.OnExercicioClickListener
import com.example.academia.R
import com.example.academia.adapters.ExercicioListAdapter
import com.example.academia.adapters.TreinoListAdapter
import com.example.academia.databinding.FragmentExercicioListBinding
import com.example.academia.model.Exercicio
import com.example.academia.model.Treino
import com.example.academia.util.DateUtils
import com.example.academia.util.DialogConfigExercicio
import com.example.academia.viewModel.DetalheExercicioViewModel
import com.example.academia.viewModel.ExercicioViewModel
import com.google.firebase.auth.FirebaseAuth

class ExercicioListFragment : Fragment(), OnExercicioClickListener {

    private lateinit var detalheViewModel: DetalheExercicioViewModel
    private lateinit var exercicioViewModel: ExercicioViewModel
    private lateinit var treinoViewModel: TreinoViewModel

    private lateinit var exercicioListAdapter: ExercicioListAdapter

    private var _binding: FragmentExercicioListBinding? = null

    private val binding get() = _binding!!

    private var nome: String? = null
    private var data: String? = null

    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentExercicioListBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Evita voltar para tela anterior ao clicar no botão voltar do aparelho
        //https://developer.android.com/guide/navigation/navigation-custom-back?hl=pt-br
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            // Handle the back button event
        }

        //callback.handleOnBackCancelled()

        setHasOptionsMenu(true)

            binding.buttonSecond.setOnClickListener {
                findNavController().navigate(R.id.action_ExercicioListFragment_to_TreinoListFragment)

            }

        detalheViewModel = ViewModelProvider(this).get(DetalheExercicioViewModel::class.java)
        exercicioViewModel = ViewModelProvider(this).get(ExercicioViewModel::class.java)
        treinoViewModel = ViewModelProvider(this).get(TreinoViewModel::class.java)

        exercicioListAdapter =
            ExercicioListAdapter(emptyList(), this) // Inicializa com uma lista vazia

        val recyclerViewExercicios: RecyclerView = view.findViewById(R.id.recyclerViewExercicios)
        recyclerViewExercicios.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewExercicios.adapter = exercicioListAdapter

        // Observa as mudanças na lista de exercicios
        exercicioViewModel.exercicios.observe(viewLifecycleOwner, Observer { exercicios ->
            exercicioListAdapter.exercicios = exercicios
            exercicioListAdapter.notifyDataSetChanged()
        })


        detalheViewModel.detalhesExercicios.observe(viewLifecycleOwner, Observer { detalhes ->

        })

        exercicioViewModel.carregarExercicios()
        detalheViewModel.carregarDetalhesExercicios()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_exercicio_list, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sair -> {

                deslogarUsuario()
               // requireActivity().onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onExercicioClick(exercicio: Exercicio) {

        DialogConfigExercicio.showConfigurarExercicioDialog(
            requireContext(),
            "Configurar Treino",
            { nomeTreino,carga, tempo, quantidadeSeries, observacao,  ->
                // Crie um novo treino com os detalhes fornecidos
                val detalheExercicio = detalheViewModel.detalhesExercicios.value?.find { it.nome == exercicio.nome

                }

                nome = detalheExercicio?.nome.toString()
                data = detalheExercicio?.data.toString()

                val cargaformatada = "$carga Kg"
                val serieFormatada = "$quantidadeSeries Rp"

                val formattedDate = DateUtils.getCurrentFormattedDate()

                val novoTreino = Treino(
                    nomeTreino = nomeTreino,
                    nomeExercicio = nome!!, // Você precisa fornecer o nome do exercício
                    data = formattedDate, // Aqui estou usando a data atual, ajuste conforme necessário
                    carga = cargaformatada,
                    tempoExercicio = tempo,
                    quantidadeSeries = serieFormatada,
                    observacao = observacao

                )

                treinoViewModel.adicionarExercicio(novoTreino)

                //Passando objeto Treino
                val bundle = Bundle().apply {
                    putSerializable("treino", novoTreino)
                }

                //Criar um Bundle e adicionar os detalhes do exercício
                if (findNavController().currentDestination?.id == R.id.FirstFragment) {
                    findNavController().navigate(
                        R.id.action_ExercicioListFragment_to_TreinoListFragment,bundle

                        )
                }
            },
            {

            }
        )
    }
    private fun deslogarUsuario() {
        AlertDialog.Builder(requireContext())
            .setTitle("Deslogar")
            .setMessage("Deseja realmente sair?")
            .setNeutralButton("Não"){dialog,posicao ->}
            .setPositiveButton("Sim"){dialog,posicao ->
                firebaseAuth.signOut()
                startActivity(
                    Intent(requireContext(), LoginActivity::class.java)
                )
            }
            .create()
            .show()
    }

}






