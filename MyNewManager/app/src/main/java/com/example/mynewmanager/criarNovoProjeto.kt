package com.example.mynewmanager

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.droidman.ktoasty.KToasty
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_criar_novo_projeto.*
import kotlinx.android.synthetic.main.item_projeto.*


class criarNovoProjeto(val campoNome: String,val campoData: String,val campoDesc: String) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_criar_novo_projeto, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if(campoData.equals("") && campoNome.equals("")){

        }
        else{
            //Significa que vem das tarefas.
            nomeProjetoField.setText(campoNome)
            dataProjetoField.setText(campoData)
            txtDescricaoProjetotext.setText(campoDesc)
        }



        val activity1: NovoProjeto? = activity as NovoProjeto?
        //Butão add tarefa vai mudar o fragment para o Nova tarefa.
        btn_addTarefa.setOnClickListener(){
            if (activity1 != null) {
                activity1.setCampos(dataProjetoField.text.toString(),nomeProjetoField.text.toString(),txtDescricaoProjetotext.text.toString())
                activity1.goToNovaTarefa()
            }
        }
        //Vai mandar de novo para atividade do user_main

        //Vai criar um projeto no FireBase e volta para o User_Main
        btn_confirmar.setOnClickListener(){
            if (activity1 != null) {
                if(!nomeProjetoField.text.toString().isBlank()){
                    if(!dataProjetoField.text.toString().isBlank()){
                        if(!activity1.tarefas.isEmpty()){
                            activity1.criarProjetoClass(nomeProjetoField.text.toString(),dataProjetoField.text.toString(),txtDescricaoProjetotext.text.toString())
                            activity1.backUserMenu(nomeProjetoField.text.toString())
                        } else KToasty.warning(activity1, "Não adicionou tarefas ao projeto", Toast.LENGTH_SHORT, true).show()
                    } else KToasty.warning(activity1, "Não colocou data de entrega do projeto", Toast.LENGTH_SHORT, true).show()
                } else KToasty.warning(activity1, "Não colocou o nome do projeto", Toast.LENGTH_SHORT, true).show()
            }
        }



        if (activity1 != null) {
            num_Tarefas.text=activity1.getTarefasSize()
        }
        super.onViewCreated(view, savedInstanceState)
    }


}

