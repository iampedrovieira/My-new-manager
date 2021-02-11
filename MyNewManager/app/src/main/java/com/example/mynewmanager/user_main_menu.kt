package com.example.mynewmanager


import Classes.*
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.droidman.ktoasty.KToasty
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_user_main_menu.*
import kotlinx.android.synthetic.main.toolbar_main.*


class user_main_menu : AppCompatActivity(),OnCardItemClickListener {
    lateinit var utilizador :Utilizador

    /*Aqui vais ser atividade é relativa ao menu principal, que é "aberto" depois de fazer login*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_main_menu)
        menu_bar.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        initToolbar()

        var u = intent.getSerializableExtra("user") as? Utilizador
        if (u != null) {
            utilizador = u
        }
        var first =  intent.getStringExtra("login")
        if(first!=null){
            if(first.equals("login")){
                KToasty.normal(this, "Bem Vindo "+ utilizador.nome + "!", Toast.LENGTH_SHORT).show()
            }else{
                KToasty.success(this, "Projeto " + first + " criado!", Toast.LENGTH_SHORT, true).show()
            }
        }
        replaceFragment(Tarefas(utilizador.idUser,"VAZIO"))
    }



    //Função para controlar a navegação da toolbar
    fun initToolbar() {
        custom_toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.navigation_new_project ->{
                    val intent = Intent(this, NovoProjeto::class.java)
                    intent.putExtra("user", utilizador)
                    startActivity(intent)
                }
                R.id.navigation_logout ->{
                    KToasty.normal(this, "Até breve!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this,MainActivity::class.java)
                    startActivity(intent)
                }
            }
            true
        }
    }



    //Esta função é  para saber quando os butões da barra sao premidos
    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_tarefa -> {
                replaceFragment(Tarefas(utilizador.idUser,"VAZIO"))

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_project -> {
                replaceFragment(Projetos(utilizador.idUser))
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_alertas -> {
                //loadFragment(item.itemId)
                KToasty.info(this, "Esta funcionalidade ainda não está implementada nesta versão", Toast.LENGTH_SHORT, true).show()
                return@OnNavigationItemSelectedListener true
            }
        }

        true
    }


    private fun replaceFragment(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        fragmentTransaction.replace(R.id.contentUserMain, fragment)
        fragmentTransaction.commit()
    }

    @SuppressLint("WrongConstant")
    fun putProject(listProjeto :ArrayList<Projeto>){
        var rec = findViewById<RecyclerView>(R.id.recyclerViewProjetos)
        rec.layoutManager= LinearLayoutManager(this, LinearLayout.VERTICAL,false)
        var adapter = ProjetoRecyclerAdapter(listProjeto,this)
        rec.adapter = adapter
    }

    @SuppressLint("WrongConstant")
    fun putTarefas(listTarefa : ArrayList<Tarefa>){
        var rec = findViewById<RecyclerView>(R.id.recyclerViewTarefas)
        rec.layoutManager= LinearLayoutManager(this, LinearLayout.VERTICAL,false)
        var adapter = TarefaRecyclerAdapter(listTarefa)
        rec.adapter = adapter

    }

    //ClickListener do recyclerView de projetos,ao carregar no projeto vai para uma nova atividade
    override fun onItemClick(item: Projeto, position: Int) {
        val novaIntent = Intent(applicationContext, ProjetoView::class.java)
        novaIntent.putExtra("user", utilizador)
        novaIntent.putExtra("idproj",item)
        startActivity(novaIntent)
    }


/*  Funções para carregar o User da firebase
    interface MyCallback{
        fun onCallback(user:Utilizador)
    }

    /*
    fun loadUser(idUs:String,myCall:MyCallback){
        //Le da FireBase
        val rootRef = FirebaseDatabase.getInstance().reference
        val ordersRef = rootRef.child("utilizador").child(idUser)
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val username = dataSnapshot.getValue(Utilizador::class.java)
                if (username != null) {
                    utilizador=username
                    myCall.onCallback(utilizador)
                    Log.d("TAG","Utilizador dentro o data: "+utilizador.nome)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {Log.e("TAG","ERROR")}
        }
        ordersRef.addListenerForSingleValueEvent(valueEventListener)
    */


        //  Adicionar a FireBase
        val ref = FirebaseDatabase.getInstance().getReference("utilizador")
        var user = Utilizador()
        user.idUser=id
        user.nome="Zé da Tina"
        ref.child(id).setValue(user)*/
        /*
        val refC = FirebaseDatabase.getInstance().getReference("TextMessage")
        val idm = refC.push().key

        var sms = textMessage("TESTE DE MENSAGEM",user)

        if (idm != null) {
            refC.child(idm).setValue(sms)
        }
    }
*/


}


/*  Esta função vai ser para carregar o novo fragment
private fun loadFragment(itemId: Int) {
    val tag = itemId.toString()
    var fragment = supportFragmentManager.findFragmentByTag(tag) ?: when (itemId) {
        R.id.navigation_tarefa-> {

        }
        else -> {
            null
        }
    }
}
*/

