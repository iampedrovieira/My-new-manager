package com.example.mynewmanager

import Classes.*
import Classes.Chat
import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.droidman.ktoasty.KToasty
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_projeto_view.*

class ProjetoView : AppCompatActivity() , OnChatItemClickListener {
    lateinit var utilizador :Utilizador
    lateinit var idProjeto :String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_projeto_view)
        menu_barProjeto.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        var u = intent.getSerializableExtra("user") as? Utilizador
        if (u != null) {
            utilizador = u
        }
        var proj = intent.getSerializableExtra("idproj") as? Projeto
        if (proj!= null) {
            idProjeto= proj.id
            nomeProjetoToolbar.text=proj.nome

        }

        replaceFragment(Tarefas(utilizador.idUser,idProjeto))
        btn_backMenu.setOnClickListener{
            val intent = Intent(this, user_main_menu::class.java)
            intent.putExtra("user", utilizador)
            startActivity(intent)
        }
    }


    //Esta função é  para saber quando os butões da barra sao premidos
    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_tarefaProjeto -> {
                replaceFragment(Tarefas(utilizador.idUser,idProjeto))
                //Toast.makeText(this,"Tarefas", Toast.LENGTH_SHORT).show()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_ChatProjeto -> {
                replaceFragment(Chat_room(utilizador.idUser,idProjeto))
                //Toast.makeText(this,"Chat", Toast.LENGTH_SHORT).show()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_alertasProjeto -> {
                //loadFragment(item.itemId)
                //Toast.makeText(this,"Esta funcionalidade ainda não esta implementada nesta versão", Toast.LENGTH_SHORT).show()
                KToasty.info(this, "Esta funcionalidade ainda não esta implementada nesta versão", Toast.LENGTH_SHORT, true).show()
                return@OnNavigationItemSelectedListener true
            }
        }
        true
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        fragmentTransaction.replace(R.id.contentProjetoView, fragment)
        fragmentTransaction.commit()
    }

    @SuppressLint("WrongConstant")
    fun putChatRoom(lista :ArrayList<Chat>){
        var rec = findViewById<RecyclerView>(R.id.recyclerViewChatRoom)
        rec.layoutManager= LinearLayoutManager(this, LinearLayout.VERTICAL,false)
        var adapter = ChatRoomRecyclerAdapter(lista,this)
        rec.adapter = adapter
        //Toast.makeText(this,"Carreguei  chats  ", Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("WrongConstant")
    fun putTarefas(listTarefa : ArrayList<Tarefa>){
        var rec = findViewById<RecyclerView>(R.id.recyclerViewTarefas)
        rec.layoutManager= LinearLayoutManager(this, LinearLayout.VERTICAL,false)
        var adapter = TarefaRecyclerAdapter(listTarefa)
        rec.adapter = adapter

    }

    //ClickListener do recyclerView de chat
    override fun onItemClick(item: Chat, position: Int) {
        //Fazer a animação para o chat

        replaceFragment(Chat(utilizador,idProjeto,item.nome))
    }
}
