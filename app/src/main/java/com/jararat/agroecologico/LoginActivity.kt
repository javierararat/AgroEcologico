package com.jararat.agroecologico

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity() {


    private lateinit var txtuser: EditText
    private lateinit var txtpass: EditText


    private lateinit var dbReference: DatabaseReference
    private lateinit var db: FirebaseDatabase
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        txtuser = findViewById(R.id.et_email)
        txtpass = findViewById(R.id.et_password)

        auth =  FirebaseAuth.getInstance()


        txtuser.setText("javier.ararat926@gmail.com");
        txtpass.setText("12345678");
    }


    fun login (View: View){
        Auth()
        //next()

    }

    fun register (View: View){
        startActivity(Intent(this,RegisterActivity::class.java))
    }

    private fun Auth(){

        val user:String=txtuser.text.toString()
        val pass:String=txtpass.text.toString()

        if(!TextUtils.isEmpty(user) && !TextUtils.isEmpty(pass)){

            auth.signInWithEmailAndPassword(user,pass).addOnCompleteListener(this){
                task ->
                    if(task.isSuccessful){
                        next()
                    }
                    else{
                        Toast.makeText(this, "Error en la autenticación", Toast.LENGTH_LONG).show()
                    }
            }
        }else{
            Toast.makeText(this, "Complete los campos", Toast.LENGTH_LONG).show()

        }
    }

    private  fun next(){
        startActivity(Intent(this,HomeActivity::class.java))
    }

}