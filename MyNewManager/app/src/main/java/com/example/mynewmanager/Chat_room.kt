package com.example.mynewmanager

import Classes.Chat
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Chat_room(val tokenUser: String, val idProjeto:String) : Fragment() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val activity: ProjetoView? = activity as ProjetoView?

        loadProjetos(object: MyCallback {
            override fun onCallback(listChat: ArrayList<Chat>){
                if (activity != null) {
                    activity.putChatRoom(listChat)
                }
            }
        })
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat_room, container, false)
    }

    fun loadProjetos(myCall:MyCallback){
        val refer = FirebaseDatabase.getInstance().getReference("Utilizadores")
        var caminho = refer.child(tokenUser).child("Projetos").child(idProjeto).child("ChatRoom")

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //Vamos percorrer a lista de projetos
                var list = ArrayList<Chat>()
                for (snapshot in dataSnapshot.children) {

                    val x = snapshot.getValue(Chat::class.java)
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
        fun onCallback(listChat: ArrayList<Chat>)
    }


}
