package com.jararat.agroecologico.ui.admin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.jararat.agroecologico.HomeActivity
import com.jararat.agroecologico.R
import java.util.*

class CreateUnitFragment : Fragment()  {


    private lateinit var txtName: EditText
    private lateinit var txtDescripcion: EditText
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
        val root = inflater.inflate(R.layout.fragment_add_sales_unit, container, false)


        txtName = root.findViewById(R.id.et_name_unit)
        txtDescripcion = root.findViewById(R.id.et_desc_unit)

        btnRegister = root.findViewById(R.id.bt_registrar_unit)
        btnRegister.setOnClickListener { view ->
            run {
                registerUnit()
            }
        }


        db = FirebaseDatabase.getInstance()
        auth =  FirebaseAuth.getInstance()
        dbReference =  db.reference.child("Unit")

        return root
    }



    fun registerUnit(){
        val name: String =  txtName.text.toString()
        val desc: String =  txtDescripcion.text.toString()

        if(!TextUtils.isEmpty(name)){
            val uniqueID: String = UUID.randomUUID().toString()
            val userDB =  dbReference.child(uniqueID)

            userDB.child("name").setValue(name)
            userDB.child("description").setValue(desc)

            Toast.makeText(thiscontext, "Se ha registrado la unidad de venta", Toast.LENGTH_SHORT)
                    .show()

            startActivity(Intent(thiscontext, HomeActivity::class.java))

        }else{
            Toast.makeText(thiscontext, "Es obligatorio ingresar todos los campos", Toast.LENGTH_SHORT)
                    .show()
        }
    }
}