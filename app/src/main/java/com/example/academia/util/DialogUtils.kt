
import android.content.Context
import android.view.LayoutInflater
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.example.academia.R
import com.example.academia.adapters.TreinoListAdapter
import com.example.academia.model.Treino
import com.example.academia.view.TreinoViewModel

object DialogUtils {

    fun showEditDialog(context: Context, treino: Treino, treinoListAdapter: TreinoListAdapter, treinoViewModel: TreinoViewModel, documentId: String ) {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_editar_treino, null)

        val editTextNomeTreino = view.findViewById<EditText>(R.id.editTextNomeTreino)
        val editTextCarga = view.findViewById<EditText>(R.id.editTextCarga)
        val editTextTempo = view.findViewById<EditText>(R.id.editTextTempo)
        val editTextQuantidadeSeries = view.findViewById<EditText>(R.id.editTextQuantidadeSeries)
        val editTObs = view.findViewById<EditText>(R.id.editObs)

        // Preencha os campos com os valores atuais do treino
        editTextNomeTreino.setText(treino.nomeTreino)
        editTextCarga.setText(treino.carga)
        editTextTempo.setText(treino.tempoExercicio)
        editTextQuantidadeSeries.setText(treino.quantidadeSeries)
        editTObs.setText(treino.observacao)

        AlertDialog.Builder(context)
            .setTitle("Editar Treino")
            .setView(view)
            .setPositiveButton("Salvar") { _, _ ->
                // Atualize os dados do treino com os valores inseridos nos EditTexts
                treino.nomeTreino = editTextNomeTreino.text.toString()
                treino.carga = editTextCarga.text.toString()
                treino.tempoExercicio = editTextTempo.text.toString()
                treino.quantidadeSeries = editTextQuantidadeSeries.text.toString()
                treino.observacao = editTObs.text.toString()

                // Atualize a lista, se necessário
                treinoListAdapter.notifyDataSetChanged()

                // Limpe o treino a ser editado no ViewModel
                treinoViewModel.limparTreinoParaEditar()

                treinoViewModel.salvarEdicaoTreinoNoFirestore(treino)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}


