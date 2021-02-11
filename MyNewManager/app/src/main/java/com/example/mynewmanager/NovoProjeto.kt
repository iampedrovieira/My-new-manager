package com.example.mynewmanager

import Classes.*
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.TextKeyListener
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_criar_novo_projeto.*
import kotlinx.android.synthetic.main.novo_projeto.*

import kotlinx.android.synthetic.main.novo_projeto_toolbar.*
import kotlinx.android.synthetic.main.toolbar_main.*
import kotlinx.serialization.json.Json.Default.context

class NovoProjeto : AppCompatActivity(), ColabRecyclerAdapter.OnCheckBoxClickListener {
    lateinit var utilizador :Utilizador
    lateinit var projeto :Projeto
    lateinit var tarefas : ArrayList<Tarefa>  // Vai se adicionar isto ao projeto
    lateinit var colaboradores : ArrayList<Colaborador> // Vai se adicionar isto aos utilizadores
    lateinit var tarefUseres : ArrayList<Colaborador> // Vai ter os tokes associados ao user que vai realizar uma tarefa, auxilia a criação de uma tarefa
    lateinit var campoNome: String
    lateinit var campoData: String
    lateinit var campoDesc : String
    var isTarefa = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.novo_projeto)
        replaceFragment(criarNovoProjeto("","",""))
        projeto = Projeto()
        tarefas = ArrayList<Tarefa>()
        colaboradores =ArrayList<Colaborador>()
        tarefUseres= ArrayList<Colaborador>()
        campoData=""
        campoNome=""
        campoDesc=""

        var u = intent.getSerializableExtra("user") as? Utilizador
        if (u != null) {
            utilizador = u
        }

        //Configuração do botão da toolbar
        btn_backCriar.setOnClickListener{
            if(isTarefa){
                backCriarProjeto()
            }else{
                backUserMenu("semProjeto")
            }
        }
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.contentNovoProjeto, fragment)
        fragmentTransaction.commit()
    }

    fun getTarefasSize():String{
        if(tarefas.isNullOrEmpty()){
            return "0"
        }else{
            return tarefas.size.toString()
        }

    }

    //Vai ser usar para que ao ir adicionar uma tarefa,quando voltar os campos do projeto estejam preenchidos.
    fun setCampos(data:String,nome:String,desc:String){
        campoNome=nome
        campoData=data
        campoDesc=desc
    }

    fun goToNovaTarefa(){
        isTarefa=true
        replaceFragment(novaTarefa(utilizador.idUser))
    }

    fun backUserMenu(ns :String){
        val intent = Intent(this, user_main_menu::class.java)
        intent.putExtra("user", utilizador)
        if(!ns.equals("semProjeto")){
            intent.putExtra("login", ns)
        }

        startActivity(intent)
    }

    fun backCriarProjeto(){
        isTarefa=false
        replaceFragment(criarNovoProjeto(campoNome,campoData,campoDesc))
    }

    @SuppressLint("WrongConstant")
    fun putColbs(listColbs :ArrayList<Colaborador>){
        var rec = findViewById<RecyclerView>(R.id.recyclerViewColabs)
        rec.layoutManager= LinearLayoutManager(this, LinearLayout.VERTICAL,false)
        var adapter = ColabRecyclerAdapter(listColbs,this)
        rec.adapter = adapter
    }

    override fun onItemClick(item: Colaborador, position: Int) {
        if(item.isSelected){
            //adiciona-se a lista
            tarefUseres.add(item)
        }
        else{
            //remove-se da lista
            tarefUseres.remove(item)
        }

    }

    //Vai ser usada no frame criar de criar projeto
    fun criarProjetoClass(nomeP :String, dataP: String,descP :String){
        projeto.nome=nomeP
        projeto.dataEntrega=dataP
        projeto.desc=descP
        criarProjetoFireBase()
    }

    //Cria e adiciona uma tarefa
    fun criarTarefa(nomeT :String, dataT: String,descT :String){
        var t = Tarefa()
        t.nome = nomeT
        t.dataEntrega = dataT
        t.desc=descT
        tarefas.add(t)

        //Adicionar as tarefas a cada colaborador
        for(c in tarefUseres){
            var exist = false
            for(col in colaboradores){
                if(c.utilizador.idUser == col.utilizador.idUser){
                    col.listTarefa.add(t)
                    exist = true
                }
            }
            if(!exist){
                //Se nao encontrou adiciona-se um novo
                colaboradores.add(c)
                //adiciona as tarefas a esse colaborador
                for(col in colaboradores){
                    if(c.utilizador.idUser == col.utilizador.idUser){
                        col.listTarefa.add(t)
                    }
                }
            }
        }

        //remover a lista de colaboradores temporaria.
        tarefUseres.clear()
    }



    fun criarProjetoFireBase(){
        var ref = FirebaseDatabase.getInstance().getReference("Utilizadores")
        //Adicionar o projeto ao criador
        var criador = ref.child(utilizador.idUser)
        var projetosCriador = criador.child("Projetos")
        var idProjeto = projetosCriador.push().key.toString()
        //O projeto nesta faze ja tem os atributos necessario, so falta o ID
        projeto.id=idProjeto
        projetosCriador.child(idProjeto).child("Detalhes").setValue(projeto)


        //O projeto ja esta na "pasta" do criador, falta agora adicionar as tarefas a esse projeto
        var projetoFire = projetosCriador.child(idProjeto)
        var tarefasCriadorFire = projetoFire.child("Tarefas")

        //Vamos adicionar todas as tarefas na "pasta" pasta do projeto do utilizador, e tambem na pasta "Tarefas " de cada utilizador que a realiza.
        //Vamos percorrer as tarefas, que contem todas as tarefas criadas para o projeto.
        for(t in tarefas){

            var idTarefa = tarefasCriadorFire.push().key.toString()
            for(c in colaboradores){


                for(tc in c.listTarefa){

                    if(t == tc){

                        tc.idTarefa = idTarefa
                        tc.idProjeto= idProjeto
                        //Adicionar a tarefa no colaborador.
                        var colb = ref.child(c.utilizador.idUser)  //Vamos a pasta colb>Tarefas
                        var colbTarefas = colb.child("Tarefas")
                        colbTarefas.child(idTarefa).setValue(tc)

                    }

                }
            }

            t.idProjeto= idProjeto
            t.idTarefa=idTarefa
            //Depois de adicionar as tarefas a todos os colaboradores que a realizam colocamos no projeto do criado.
            tarefasCriadorFire.child(idTarefa).setValue(t)
        }

        //Adicionar o projeto a todos so colaboradores
        for(c in colaboradores){
            if(utilizador.idUser == c.utilizador.idUser){
            }else{
                var colab = ref.child(c.utilizador.idUser)
                var projetosColab = colab.child("Projetos")
                projetosColab.child(idProjeto).child("Detalhes").setValue(projeto)
                var proC = projetosColab.child(idProjeto)
                var tarefasCriadorFire1 = proC.child("Tarefas")
                for(t in tarefas){
                    tarefasCriadorFire1.child(t.idTarefa).setValue(t)
                }
            }
        }
    }


/*
CRIAÇÃO DO PROJETO
    NOVA TAREFA
    Adicionar Colabs[a uma arrayList]
        Quando finalizar a tarefa, Adicionar essa tarefa ao arrayList de tarefas e adicionar essa tarefa no arralist de tarefas de cada colab
    No fim vamos ter, uma arrayList de tarefas e um arraylist de colbs
    Finalizar projeto,
        Gravar o projeto no firebase
            -Gravar numa pasta de Projetos[Com as tarefas la dentro, depois vai ter chat tambem]
            -Gravar numa pasta projetos dentro da pasta dos utilizadores que tenham tarefas
            -Gravar numa pasta tarefas dentro da pasta do utilizadores que tenha tarefas.
 */

}
