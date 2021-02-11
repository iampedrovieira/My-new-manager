package com.example.mynewmanager

import Classes.*
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_tarefas.*


class Projetos (val tokenUser: String) :  Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_projeto, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val activity: user_main_menu? = activity as user_main_menu?
        loadProjetos(tokenUser,object: MyCallback {
            override fun onCallback(listProjeto: ArrayList<Projeto>){
                if (activity != null) {
                    activity.putProject(listProjeto)
                }
            }
        })
        super.onViewCreated(view, savedInstanceState)
    }

    fun loadProjetos(token :String,myCall:MyCallback){
        val refer = FirebaseDatabase.getInstance().getReference("Utilizadores")
        var caminho = refer.child(token).child("Projetos")

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //Vamos percorrer a lista de projetos
                var list = ArrayList<Projeto>()
                for (snapshot in dataSnapshot.children) {

                    val x = snapshot.child("Detalhes").getValue(Projeto::class.java)
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

    interface MyCallback{
        fun onCallback(listProjeto: ArrayList<Projeto>)
    }


}
