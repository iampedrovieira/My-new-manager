package Classes

import android.content.Context
import android.util.Log
import android.util.SparseArray
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mynewmanager.NovoProjeto
import com.example.mynewmanager.R

class ColabRecyclerAdapter(val listaColab:ArrayList<Colaborador>,var clickListener: OnCheckBoxClickListener) :
    RecyclerView.Adapter<ColabRecyclerAdapter.ViewHolderColaborador>() {

    //private var checkBoxStateArray = SparseBooleanArray()
    //Vais buscar o modelo
    class ViewHolderColaborador (item: View) : RecyclerView.ViewHolder(item) {
        var nome: TextView = item.findViewById(R.id.nomeColaborador)
        var checkBox: CheckBox = item.findViewById(R.id.colabCheckBox)


        fun initialize(item : Colaborador, action:OnCheckBoxClickListener){

            nome.text = item.utilizador.nome
            checkBox.isChecked = item.isSelected

            checkBox.setOnClickListener(){

                //Vai fazer com que ao carregar no check box meta o contrario que estava
                //checkBox.isChecked = !checkBox.isChecked
                //Atribui esse valor ao colab
                item.isSelected=checkBox.isChecked
                action.onItemClick(item,adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderColaborador {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_colaborador,parent,false)
        return ViewHolderColaborador(view)
    }

    override fun getItemCount(): Int {
        return listaColab.size
    }

    override fun onBindViewHolder(holder: ViewHolderColaborador, position: Int) {
        holder.initialize(listaColab.get(position),clickListener)
    }
    interface OnCheckBoxClickListener{
        fun onItemClick(item: Colaborador,position: Int)
    }
}
