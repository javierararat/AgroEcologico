package com.jararat.agroecologico.ui.admin

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GetTokenResult
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.jararat.agroecologico.HomeActivity
import com.jararat.agroecologico.R
import java.util.regex.Pattern


class CreateMarketFragment : Fragment()  {

    private lateinit var createMarketViewModel: CreateMarketViewModel

    private lateinit var txtMarketName: EditText
    private lateinit var txtSellerId: EditText
    private lateinit var txtSellerTel: EditText
    private lateinit var txtSellerEmail: EditText
    private lateinit var txtSellerPass: EditText
    private lateinit var btnRegister: Button

    private lateinit var dbReference: DatabaseReference
    private lateinit var db: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    lateinit var thiscontext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        thiscontext = context
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        createMarketViewModel =
                ViewModelProvider(this).get(CreateMarketViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_create_sales_position, container, false)


        txtMarketName = root.findViewById(R.id.et_name_market)
        txtSellerId = root.findViewById(R.id.et_seller_identification)
        txtSellerTel = root.findViewById(R.id.et_seller_number_tel)
        txtSellerEmail = root.findViewById(R.id.et_seller_email)
        txtSellerPass = root.findViewById(R.id.et_seller_password)

        btnRegister = root.findViewById(R.id.bt_registrar)
        btnRegister.setOnClickListener { view ->
            run {
                registerAccount()
            }
        }


        db = FirebaseDatabase.getInstance()
        auth =  FirebaseAuth.getInstance()
        dbReference =  db.reference.child("User")



        return root
    }
    private fun validarEmail(email: String): Boolean {
        val pattern: Pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

    private fun sendEmail(user: FirebaseUser) {
        user.sendEmailVerification().addOnCompleteListener() {
            task ->
            if(task.isComplete){
                Toast.makeText(thiscontext, "Se ha enviado un email al usuario", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        user.getIdToken(true)
            .addOnCompleteListener(OnCompleteListener<GetTokenResult> { task ->
                if (task.isSuccessful) {
                    val idToken = task.result!!.token
                    Toast.makeText(thiscontext, "token: " +idToken, Toast.LENGTH_SHORT)
                        .show()
                }
            })


    }

    private fun sendWhatsapp(tel:String, email: String, pass:String) {

        val phone = "57"+tel
        val msg = "Notificamos la  creacion de tu puesto de venta  AgroEcologico. usuario: "+email+", contraseña: "+pass

        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("whatsapp://send?phone="+phone+msg)
        }

        startActivity(intent)
    }


     fun registerAccount(){
        val nameMarket: String =  txtMarketName.text.toString()
        val sellerId: String =  txtSellerId.text.toString()
        val tel: String =  txtSellerTel.text.toString()
        val email: String =  txtSellerEmail.text.toString()
        val pass: String =  txtSellerPass.text.toString()

        if(!TextUtils.isEmpty(nameMarket) && !TextUtils.isEmpty(sellerId)
                && !TextUtils.isEmpty(email)  && !TextUtils.isEmpty(pass)
                && !TextUtils.isEmpty(tel)
        ){

            if (!validarEmail(email)){
                txtSellerPass.setError("Email no válido")
            }


            auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(){
                task ->
                if(task.isComplete){

                    val  user:FirebaseUser? = auth.currentUser

                    //verifica email
                    if (user != null) {
                        sendEmail(user)
                    }

                    //sendWhatsapp(tel,email, pass)

                    val userDB =  dbReference.child(sellerId)

                    userDB.child("uid").setValue(user?.uid.toString())
                    userDB.child("name").setValue(nameMarket)
                    userDB.child("identification").setValue(sellerId)
                    userDB.child("tel").setValue(tel)
                    userDB.child("email").setValue(email)
                    userDB.child("password").setValue(pass)
                    userDB.child("url").setValue("")
                    userDB.child("nameSpecial").setValue("")

                    Toast.makeText(thiscontext, "Se ha registrado el puesto", Toast.LENGTH_SHORT)
                            .show()

                    startActivity(Intent(thiscontext, HomeActivity::class.java))

                }
            }
        }else{
            Toast.makeText(thiscontext, "Es obligatorio ingresar todos los campos", Toast.LENGTH_SHORT)
                    .show()
        }
    }
}
