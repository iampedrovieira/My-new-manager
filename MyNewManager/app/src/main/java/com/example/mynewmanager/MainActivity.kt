package com.example.mynewmanager


import Classes.Chat
import Classes.Projeto
import Classes.Tarefa
import Classes.Utilizador
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.droidman.ktoasty.KToasty

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    lateinit var utilizador :Utilizador


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()


        /* Vai iniciar com o R.Layout.activity_main, que vai ser o login */
        setContentView(R.layout.activity_main)
        utilizador = Utilizador()

        btn_login.setOnClickListener() {
            loadingBar.setVisibility(View.VISIBLE)

            if (email.text.toString().isEmpty() or pass.text.toString().isEmpty()) {
                KToasty.warning(this, "Campos por preencher.", Toast.LENGTH_SHORT, true).show()
                loadingBar.setVisibility(View.GONE)
            }
            else {
                //Toast.makeText(this, "A comunicar com o servidor", Toast.LENGTH_SHORT).show()
                auth.signInWithEmailAndPassword(email.text.toString(), pass.text.toString())
                    .addOnCompleteListener(this) { task ->
                        //Toast.makeText(this, "A validar login", Toast.LENGTH_SHORT).show()
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            val idUser = user?.uid.toString()
                            loadUser(idUser,object: MyCallback {
                                override fun onCallback(user: Utilizador) {
                                    val novaIntent = Intent(applicationContext, user_main_menu::class.java)
                                    novaIntent.putExtra("user", utilizador)
                                    novaIntent.putExtra("login", "login")
                                    startActivity(novaIntent)
                                }
                            })
                        }
                        else {
                            KToasty.error(this, "Email/password não foram introduzidas corretamente", Toast.LENGTH_SHORT, true).show()
                            loadingBar.setVisibility(View.GONE)
                        }
                    }
            }
            it.hideKeyboard()
        }
    }

    interface MyCallback{
        fun onCallback(user:Utilizador)
    }

    fun loadUser(idUser:String,myCall:MyCallback){
        //Le da FireBase
        val rootRef = FirebaseDatabase.getInstance().reference
        val ordersRef = rootRef.child("Utilizadores").child(idUser)
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val username = dataSnapshot.getValue(Utilizador::class.java)
                if (username != null) {
                    utilizador=username
                    myCall.onCallback(utilizador)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("TAG","ERROR")}
        }
        ordersRef.addListenerForSingleValueEvent(valueEventListener)
    }

    //Esconder o teclado
    fun View.hideKeyboard() {
        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }
}
