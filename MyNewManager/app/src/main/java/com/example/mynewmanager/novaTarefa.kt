package com.example.mynewmanager

import Classes.ColabRecyclerAdapter
import Classes.Colaborador
import Classes.Tarefa
import Classes.Utilizador
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.droidman.ktoasty.KToasty
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_criar_novo_projeto.*
import kotlinx.android.synthetic.main.fragment_nova_tarefa.*


class novaTarefa(val idUser:String) : Fragment() {
    private var mContext: Context? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nova_tarefa, container, false)
    }

    override fun onAttach(context: Context) {
        mContext=context
        super.onAttach(context)
    }

    @SuppressLint("WrongConstant")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val activity: NovoProjeto? = activity as NovoProjeto?

        loadUsers(object: MyCallback {
            override fun onCallback(listColb:ArrayList<Colaborador>) {
                if (activity != null) {
                    activity.putColbs(listColb)
                }
            }
        })

        btn_criarTarefa.setOnClickListener{
            if (activity != null) {
                if(!nomeTarefaField.text.toString().isBlank()) {
                    if (!data.text.toString().isBlank()) {
                        if (activity.tarefUseres.size > 0) {
                            activity.criarTarefa(nomeTarefaField.text.toString(), data.text.toString(), txtDescricaoTarefa3.text.toString())
                            KToasty.success(activity, "Tarefa criada.", Toast.LENGTH_SHORT, true).show()
                            activity.backCriarProjeto()

                        } else KToasty.warning(activity, "Não selecionou nenhum colaborador.", Toast.LENGTH_SHORT, true).show()
                    } else KToasty.warning(activity, "Não colocou data de entrega.", Toast.LENGTH_SHORT, true).show()
                } else KToasty.warning(activity, "Não colocou o nome da tarefa.", Toast.LENGTH_SHORT, true).show()
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    interface MyCallback{
        fun onCallback(listColb: ArrayList<Colaborador>)
    }

    fun loadUsers(myCall:MyCallback){
        val refer = FirebaseDatabase.getInstance().getReference("Utilizadores")
        var caminho = refer

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //Vamos percorrer a lista
                var colbList :ArrayList<Colaborador> = ArrayList()
                for (snapshot in dataSnapshot.children) {
                    val x = snapshot.getValue(Utilizador::class.java)
                    if (x != null) {
                        var arr = ArrayList<Tarefa>()
                        var colb = Colaborador(x,false,arr)
                        colbList.add(colb)
                    }
                }
                myCall.onCallback(colbList)
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("TAG","ERROR")}
        }
        caminho.addListenerForSingleValueEvent(valueEventListener)
    }
}
