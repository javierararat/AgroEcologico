package com.jararat.agroecologico

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity: AppCompatActivity() {

    private lateinit var txtName:EditText
    private lateinit var txtLastName:EditText
    private lateinit var txtEmail:EditText
    private lateinit var txtPass:EditText

    private lateinit var dbReference:DatabaseReference
    private lateinit var db:FirebaseDatabase
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity)

        txtName = findViewById(R.id.res_et_name)
        txtLastName = findViewById(R.id.res_et_lastname)
        txtEmail = findViewById(R.id.res_et_email)
        txtPass = findViewById(R.id.res_et_password)

     /*   db = FirebaseDatabase.getInstance()
        auth =  FirebaseAuth.getInstance()

        dbReference =  db.reference.child("User")*/
    }


    fun register (View: View){
        //registerAccount()
    }





}