package com.example.mynewmanager

import Classes.Tarefa
import Classes.TarefaRecyclerAdapter
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
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
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_tarefas.*
import kotlinx.android.synthetic.main.fragment_tarefas.view.*
import java.util.prefs.Preferences


class Tarefas(val tokenUser: String, val idProjeto:String) : Fragment() {
    private var mContext: Context? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tarefas, container, false)
    }

    override fun onAttach(context: Context) {
        mContext=context
        super.onAttach(context)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadTarefas(object: MyCallback {
            @SuppressLint("WrongConstant")
            override fun onCallback(listTarefas:ArrayList<Tarefa>) {
                if(idProjeto=="VAZIO"){
                    val activity1: user_main_menu? = activity as user_main_menu?
                    if (activity1 != null) {
                        var rec = recyclerViewTarefas
                        rec.layoutManager= LinearLayoutManager(mContext, LinearLayout.VERTICAL,false)
                        var adapter = TarefaRecyclerAdapter(listTarefas)
                        rec.adapter = adapter
                    }
                }
                else{
                    val activity2: ProjetoView? = activity as ProjetoView?
                    if (activity2 != null) {
                        activity2.putTarefas(listTarefas)
                    }
                }

            }
        })

        super.onViewCreated(view, savedInstanceState)
    }


    fun loadTarefas(myCall:MyCallback){
        if(idProjeto == "VAZIO"){
            val refer = FirebaseDatabase.getInstance().getReference("Utilizadores")
            var caminho = refer.child(tokenUser).child("Tarefas")
            val valueEventListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    //Vamos percorrer a lista de projetos
                    var list = ArrayList<Tarefa>()
                    for (snapshot in dataSnapshot.children) {
                        val x = snapshot.getValue(Tarefa::class.java)
                        if (x != null) {
                            list.add(x)
                        }
                    }
                    myCall.onCallback(list)
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("TAG","ERROR")}
            }
            caminho.addListenerForSingleValueEvent(valueEventListener)
        }
        else{
            val refer = FirebaseDatabase.getInstance().getReference("Utilizadores")
            var caminho = refer.child(tokenUser).child("Projetos").child(idProjeto).child("Tarefas")
            val valueEventListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    //Vamos percorrer a lista de projetos
                    var list = ArrayList<Tarefa>()
                    for (snapshot in dataSnapshot.children) {
                        val x = snapshot.getValue(Tarefa::class.java)
                        if (x != null) {
                            list.add(x)
                        }
                    }
                    myCall.onCallback(list)
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("TAG","ERROR")}
            }
            caminho.addListenerForSingleValueEvent(valueEventListener)
        }
    }


    interface MyCallback{
        fun onCallback(listTarefas:ArrayList<Tarefa>)
    }
}
