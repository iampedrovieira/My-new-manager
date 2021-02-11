package Classes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mynewmanager.R
import com.example.mynewmanager.user_main_menu

class TarefaRecyclerAdapter(val tarefaList :ArrayList<Tarefa>):
    RecyclerView.Adapter<TarefaRecyclerAdapter.ViewHolderTarefa >() {

        class ViewHolderTarefa (item: View) : RecyclerView.ViewHolder(item){
            var nome:TextView = item.findViewById(R.id.txtNomeTarefa)
            var data:TextView = item.findViewById(R.id.txtDataTarefa)
            var desc:TextView = item.findViewById(R.id.txtDescTarefa)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderTarefa {
            var view = LayoutInflater.from(parent.context).inflate(R.layout.item_tarefa,parent,false)
            return ViewHolderTarefa(view)
        }

        override fun getItemCount(): Int {
            return tarefaList.size
        }

        override fun onBindViewHolder(holder: ViewHolderTarefa, position: Int) {
            var tarefa :Tarefa = tarefaList[position]
            holder.nome.text = tarefa.nome
            holder.data.text = tarefa.dataEntrega
            holder.desc.text = tarefa.desc
        }
}