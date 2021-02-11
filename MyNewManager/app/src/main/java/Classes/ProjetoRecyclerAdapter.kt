package Classes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mynewmanager.R

class ProjetoRecyclerAdapter (val projetoList :ArrayList<Projeto>,var clickListener: OnCardItemClickListener) :
    RecyclerView.Adapter<ProjetoRecyclerAdapter.ViewHolderProjeto >() {

        class ViewHolderProjeto (item: View) : RecyclerView.ViewHolder(item){
            var nome: TextView = item.findViewById(R.id.txtNomeProjeto)
            var data: TextView = item.findViewById(R.id.txtDataProjeto)
            var desc: TextView = item.findViewById(R.id.txtDescProjeto)

            fun initialize(item : Projeto, action:OnCardItemClickListener){

                nome.text = item.nome
                data.text = item.dataEntrega
                desc.text = item.desc

                itemView.setOnClickListener(){
                    action.onItemClick(item,adapterPosition)
                }
            }


        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderProjeto {
            var view = LayoutInflater.from(parent.context).inflate(R.layout.item_projeto,parent,false)
            return ViewHolderProjeto(view)
        }

        override fun getItemCount(): Int {
            return projetoList.size
        }

        override fun onBindViewHolder(holder: ViewHolderProjeto, position: Int) {
            holder.initialize(projetoList.get(position),clickListener)
        }

}

interface OnCardItemClickListener{
    fun onItemClick(item: Projeto, position: Int)
}