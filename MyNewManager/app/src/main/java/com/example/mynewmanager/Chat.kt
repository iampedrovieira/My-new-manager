package com.example.mynewmanager

import Classes.Chat
import Classes.ChatRecyclerAdapter
import Classes.Utilizador
import Classes.textMessage
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_chat.*

class Chat (val user: Utilizador, var idProjeto:String, var nomeChat :String) : Fragment() {

    private var mContext: Context? = null
    lateinit var idChat : String
    lateinit var adapter :ChatRecyclerAdapter
    private val handler: Handler = Handler()

    private val runnable: Runnable = object : Runnable {
        override fun run() {
            atualizarChat()
            handler.postDelayed(this, 2000) //vamos atualizar o chat de 2 em 2 segundos
        }
    }

    //Quando o fragment é destruido para de atualizar
    override fun onDestroy() {
        handler.removeCallbacks(runnable)
        super.onDestroy()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        handler.postDelayed(runnable, 1000);
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //Ler o char da fire base e tirar o token
        loadChat(object: MyCallback {
            override fun onCallback(t: String){
                idChat=t
                //Ler mensagens do firebase e colocar adapter

                loadChat2(object: MyCallback2 {
                    @SuppressLint("WrongConstant")
                    override fun onCallback(listaMe1: ArrayList<textMessage>){
                        var rec = recyclerViewChat
                        rec.layoutManager= LinearLayoutManager(mContext, LinearLayout.VERTICAL,false)
                        adapter = ChatRecyclerAdapter(listaMe1,user.nome)
                        rec.adapter = adapter
                        rec.scrollToPosition(listaMe1.size-1) //Coloca o recycler no ultimo item adicionado
                    }
                })
            }
        })


        SendButton.setOnClickListener{
            //Cria uma mensagem no chat no fire base
            var ref = FirebaseDatabase.getInstance().getReference("Utilizadores").child(user.idUser)
            var projeto = ref.child("Projetos").child(idProjeto)
            var chat =projeto.child("ChatRoom").child(idChat)
            var mensagens = chat.child("Mensagens")
            var idMen = mensagens.push().key.toString()
            var mensagem =textMessage()
            mensagem.texto=sms.text.toString()
            mensagem.sentFrom=user.nome
            mensagens.child(idMen).setValue(mensagem)
            //Colocar a mensagem em todos os utilizadores com este projeto[PARA FACILITAR VOU SO AOS 2 USERES DE TESTE]

            if(user.idUser== "CyHuF1QprFXjlYRwRLwAInuVZhi1"){

                var v =FirebaseDatabase.getInstance().getReference("Utilizadores").child("tkiAwyEiaLaPmPw5HIm6o7RgUeH2")
                var chat1 = v.child("Projetos").child(idProjeto).child("ChatRoom").child(idChat)
                chat1.child("Mensagens").child(idMen).setValue(mensagem)
            }else{

                var v =FirebaseDatabase.getInstance().getReference("Utilizadores").child("CyHuF1QprFXjlYRwRLwAInuVZhi1")
                var chat2 = v.child("Projetos").child(idProjeto).child("ChatRoom").child(idChat)
                chat2.child("Mensagens").child(idMen).setValue(mensagem)

            }

            //Atualizar o recycler
            atualizarChat()
            sms.text?.clear()

        }
        super.onViewCreated(view, savedInstanceState)
    }


    fun atualizarChat(){
        loadChat2(object: MyCallback2 {
            @SuppressLint("WrongConstant")
            override fun onCallback(listaMe1: ArrayList<textMessage>){
                if(adapter.smsList.size==listaMe1.size){
                    //Nao atualiza
                }else{

                    var rec = recyclerViewChat
                    rec.layoutManager= LinearLayoutManager(mContext, LinearLayout.VERTICAL,false)
                    adapter = ChatRecyclerAdapter(listaMe1,user.nome)
                    rec.adapter = adapter
                    rec.scrollToPosition(listaMe1.size-1)
                }



            }
        })
    }


    override fun onAttach(context: Context) {
        mContext=context
        super.onAttach(context)
    }

    fun loadChat(myCall:MyCallback){
        val refer = FirebaseDatabase.getInstance().getReference("Utilizadores")
        var caminho = refer.child(user.idUser).child("Projetos").child(idProjeto).child("ChatRoom")
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                var tkChat =String()
                for (snapshot in dataSnapshot.children) {
                    if(snapshot.getValue(Chat::class.java)?.nome.equals(nomeChat)){
                        tkChat = snapshot.key.toString() //passa o id do chat
                        txtNomeSalaNoChat.text = snapshot.getValue(Chat::class.java)?.nome
                    }
                }
                myCall.onCallback(tkChat)
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("TAG","ERROR")}
        }

        caminho.addListenerForSingleValueEvent(valueEventListener)
    }


    interface MyCallback{
        fun onCallback(t: String)
    }


    fun loadChat2(myCall:MyCallback2){
        val refer = FirebaseDatabase.getInstance().getReference("Utilizadores")
        var caminho = refer.child(user.idUser).child("Projetos").child(idProjeto).child("ChatRoom").child(idChat)
            .child("Mensagens")
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //Vamos percorrer a lista de projetos
                var list =ArrayList<textMessage>()
                for (snapshot in dataSnapshot.children) {
                    var m =snapshot.getValue(textMessage::class.java)
                    if (m != null) {
                        list.add(m)
                    }
                }
                myCall.onCallback(list)
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("TAG","ERROR")}
        }
        caminho.addListenerForSingleValueEvent(valueEventListener)
    }


    interface MyCallback2{
        fun onCallback(listaMe1: ArrayList<textMessage>)
    }
}