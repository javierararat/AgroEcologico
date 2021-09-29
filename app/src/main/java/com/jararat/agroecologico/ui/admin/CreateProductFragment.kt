package com.jararat.agroecologico.ui.admin

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.jararat.agroecologico.R
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*


class CreateProductFragment : Fragment()  {

    private lateinit var txtProductName: EditText
    private lateinit var txtPriceName: EditText
    private lateinit var spUnidad: Spinner

    private lateinit var btnRegister: Button
    private lateinit var btnAddImg: Button
    private lateinit var ivImagen: ImageView

    private lateinit var dbReference: DatabaseReference
    private lateinit var dbReferenceUnits: DatabaseReference
    private lateinit var db: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    lateinit var thiscontext: Context


    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null

    private var filePath: Uri? = null
    private var fileBitmap: Bitmap? = null
    private var tmpUnit: String = ""

    val list = mutableListOf<Unit>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        thiscontext = context
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_product, container, false)

        txtProductName = root.findViewById(R.id.etName_product)
        txtPriceName = root.findViewById(R.id.etPrice_product)
        spUnidad = root.findViewById(R.id.dropStatus_product)
        ivImagen = root.findViewById(R.id.image_preview_product)

        btnRegister = root.findViewById(R.id.btn_save_product)
        btnRegister.setOnClickListener { view ->
            run {
                register()
            }
        }

        btnAddImg = root.findViewById(R.id.btn_add_img_product)
        btnAddImg.setOnClickListener { view ->
            run {
                launchGallery()
            }
        }


        db = FirebaseDatabase.getInstance()
        auth =  FirebaseAuth.getInstance()
        dbReference =  db.reference.child("User").child("010101").child("productos")

        dbReferenceUnits =  db.reference.child("Unit")
        getUnits()


        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        return root
    }

    private fun launchGallery() {
        val intentImplicito = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intentImplicito,6)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==6 && resultCode == Activity.RESULT_OK) {

            val data_img:Bundle? = data?.extras
            val Image: Bitmap? = data_img?.getParcelable<Bitmap>("data")

            ivImagen.setImageBitmap(Image)
            fileBitmap = Image
        }

    }


    fun register(){
        val name: String =  txtProductName.text.toString()
        val price_s: String =  txtPriceName.text.toString()


        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(tmpUnit) && !TextUtils.isEmpty(price_s)) {

                    val userDB =  dbReference.child(name)

                    userDB.child("name").setValue(name)
                    userDB.child("price").setValue(price_s)
                    userDB.child("unidad").setValue(tmpUnit)
                    userDB.child("url").setValue("")

                    uploadImage(name)

                    Toast.makeText(thiscontext, "Se ha registrado el producto", Toast.LENGTH_SHORT)
                            .show()

            view?.findNavController()?.navigate(R.id.nav_list_products)

        }else{
            Toast.makeText(thiscontext, "Es obligatorio ingresar todos los campos", Toast.LENGTH_SHORT)
                    .show()
        }
    }


    private fun uploadImage(nameProd: String){

        val file = File(context?.cacheDir,"img_prid") //Get Access to a local file.
        file.delete() // Delete the File, just in Case, that there was still another File
        file.createNewFile()
        val fileOutputStream = file.outputStream()
        val byteArrayOutputStream = ByteArrayOutputStream()
        fileBitmap?.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream)
        val bytearray = byteArrayOutputStream.toByteArray()
        fileOutputStream.write(bytearray)
        fileOutputStream.flush()
        fileOutputStream.close()
        byteArrayOutputStream.close()

        val DataUri = file.toUri()

        val ref = storageReference?.child("uploads/" + UUID.randomUUID().toString())
        val uploadTask = ref?.putFile(DataUri)

        val urlTask = uploadTask?.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            return@Continuation ref.downloadUrl
        })?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                addUploadRecordToDb(downloadUri.toString(), nameProd)
            } else {
                // Handle failures
            }
        }?.addOnFailureListener{

        }

    }


    private fun addUploadRecordToDb(uri: String, nameProd: String){

        val userDB =  dbReference.child(nameProd)
        userDB.child("url").setValue(uri)
    }

    private  fun getUnits() {

        dbReferenceUnits.get().addOnCompleteListener  { task ->
            if (task.isSuccessful) {
                val result = task.result
                result?.let {
                    for (postSnapshot in result.children) {
                        // TODO: handle the post
                        var snap = (postSnapshot as DataSnapshot)
                        var myTasksDTO = snap.getValue(Unit::class.java)
                        if (myTasksDTO != null) {
                            list.add(myTasksDTO)
                        }
                    }
                    updateView()
                }
            }
        }
    }

    fun updateView() {
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            thiscontext,
            android.R.layout.simple_spinner_item, list.map { it.name }
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spUnidad.setAdapter(adapter);

        spUnidad.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?,
                position: Int, id: Long
            ): kotlin.Unit {
                Log.v("item", parent.getItemAtPosition(position) as String)
                tmpUnit = parent.getItemAtPosition(position) as String
            }

            override fun onNothingSelected(parent: AdapterView<*>?): kotlin.Unit {
                // TODO Auto-generated method stub
            tmpUnit = ""

            }
        })
    }




}
