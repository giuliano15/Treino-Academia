package com.example.academia.view


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.academia.R
import com.example.academia.adapters.TreinoListAdapter
import com.example.academia.databinding.FragmentDetalhesExercicioBinding
import com.example.academia.model.DetalheExercicio
import com.example.academia.model.Treino
import com.example.academia.util.Contador
import com.example.academia.util.formatarTempo
import com.example.academia.viewModel.DetalheExercicioViewModel

class DetalhesExercicioFragment() : Fragment() {

    private var _binding: FragmentDetalhesExercicioBinding? = null
    private lateinit var treinoViewModel: TreinoViewModel
    private lateinit var detalheViewModel: DetalheExercicioViewModel

    private lateinit var treino: Treino
    private lateinit var treinoListAdapter: TreinoListAdapter

    private val binding get() = _binding!!

    private lateinit var academiaTimer: Contador
    private val obs: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        _binding = FragmentDetalhesExercicioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
//            // Handle the back button event
//        }
//
//        callback.handleOnBackCancelled()


        val activity = requireActivity() as AppCompatActivity
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(false)

        detalheViewModel = ViewModelProvider(this).get(DetalheExercicioViewModel::class.java)
        treinoViewModel = ViewModelProvider(requireActivity()).get(TreinoViewModel::class.java)


        val nomeExercicio = arguments?.getString("nomeExercicio")
        //val obs = arguments?.getString("obs")


        arguments?.let {
            treino = it.getSerializable("treino") as Treino
            //treinoListAdapter = it.getSerializable("adapter") as TreinoListAdapter

        }

        binding.textObs.text = treino.observacao

        detalheViewModel.carregarDetalhesExercicios()


        //carregarDadosExercicio(nomeExercicio)
        detalheViewModel.detalhesExercicios.observe(viewLifecycleOwner, Observer { detalhes ->
            carregarDadosExercicio(nomeExercicio, detalhes)
        })

        binding.buttonSalvarTempo.setOnClickListener {

            val intent = Intent(requireContext(),FinalizarTreino::class.java)
            intent.putExtra("nomeTreino",treino.nomeTreino)
            startActivity(intent)

            Toast.makeText(
                requireContext(),
                "Treino finalizado!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun carregarDadosExercicio(
        nomeExercicio: String?,
        detalhesExercicios: List<DetalheExercicio>
    ) {
        // Encontrar o DetalheExercicio correspondente ao nome recebido
        val detalheExercicio = detalhesExercicios.find { it.nome == treino.nomeExercicio }
        val textTime = treino.tempoExercicio

        if (textTime != null) {
            iniciarContador(textTime)
        }

        val tempoDaLista = textTime  // Substitua isso pelo tempo real da lista
         val tempoFormatado = tempoDaLista?.let { formatarTempo(it) }
        binding.textTempoRestante.text = tempoDaLista

        detalheExercicio?.let {
            // Preencher os campos com os dados do exercício
            binding.textNomeExercicio.text = it.nome
            binding.textDetalhesDescricao.text = it.descricao
            binding.textTempoRestante.text = tempoFormatado
            binding.textCarga.text = treino.carga
            binding.textQtdSerie.text = treino.quantidadeSeries
            binding.textObs.text = treino.observacao

            // Carregar a imagem usando Glide
            Glide.with(this)
                .load(it.imagemUrl) // Substitua com a URL ou recurso da imagem
                .placeholder(R.drawable.foto_academia) // Recurso de placeholder enquanto carrega
                .error(com.google.android.material.R.drawable.mtrl_ic_error) // Recurso de erro, se a carga falhar
                .into(binding.imageDetalhesExercicio)
        }

    }

    private fun iniciarContador(textTime: String) {
        if (textTime.isNotBlank()) {
            try {
                val tempoInicialMillis = textTime.toLong() * 1000L  // Converter para milissegundos
                academiaTimer = Contador(
                    totalMillis = tempoInicialMillis,
                    intervalMillis = 1000,
                    onTick = { formattedTime ->
                        binding.textTempoRestante.text = formattedTime
                    },
                    onFinish = {
                        binding.textTempoRestante.text = "00:00"
                        Toast.makeText(requireContext(), "Treino finalizado!", Toast.LENGTH_SHORT)
                            .show()
                        // Aqui você pode reativar os botões se necessário
                        binding.buttonSalvarTempo.isEnabled = true
                        binding.btnPlay.isEnabled = true
                        binding.btnPause.isEnabled = true
                        binding.btnStop.isEnabled = true
                    }
                )

                binding.btnPlay.setOnClickListener {
                    academiaTimer.start()
                    binding.buttonSalvarTempo.isEnabled = false
                    // Desativar o botão Play
                    binding.btnPlay.isEnabled = false
                    // Reativar os botões Pause e Stop
                    binding.btnPause.isEnabled = true
                    binding.btnStop.isEnabled = true
                }

                binding.btnPause.setOnClickListener {
                    academiaTimer.pause()
                    // Desativar o botão Pause
                    binding.btnPause.isEnabled = false
                    // Reativar os botões Play e Stop
                    binding.btnPlay.isEnabled = true
                    binding.btnStop.isEnabled = true
                }

                val textTime = treino.tempoExercicio
                val tempoDaLista = textTime  // Substitua isso pelo tempo real da lista
                val tempoFormatado = tempoDaLista?.let { formatarTempo(it) }
                binding.btnStop.setOnClickListener {
                    academiaTimer.stop()
                    // Zerar os botões e reativar os botões Play e Pause
                    binding.buttonSalvarTempo.isEnabled = true
                    binding.textTempoRestante.text = tempoFormatado
                    binding.btnPlay.isEnabled = true
                    binding.btnPause.isEnabled = true
                    binding.btnStop.isEnabled = false
                }

            } catch (e: NumberFormatException) {
                // Lidar com a conversão mal-sucedida, por exemplo, mostrar um log
                Log.e(
                    "DetalhesExercicioFragment",
                    "Erro ao converter textTime para Long: $textTime",
                    e
                )
            }
        } else {
            // Lidar com o caso em que textTime está vazio
            Log.e("DetalhesExercicioFragment", "textTime está vazio ou nulo")
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
