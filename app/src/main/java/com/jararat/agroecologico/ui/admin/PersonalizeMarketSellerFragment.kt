package com.jararat.agroecologico.ui.admin

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.jararat.agroecologico.R
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*


class PersonalizeMarketSellerFragment : Fragment()  {


    private lateinit var txtNameSeller: EditText
    private lateinit var txtTelSeller: EditText

    private lateinit var btnAddImgSeller: Button
    private lateinit var btnAddSeller: Button

    private lateinit var dbReference: DatabaseReference
    private lateinit var db: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    lateinit var thiscontext: Context

    private lateinit var img1Seller: ImageView

    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null

    private var fileBitmapSeller: Bitmap? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        thiscontext = context
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_sales_sub_seller, container, false)



        btnAddImgSeller = root.findViewById(R.id.btn_add_img_seller)
        btnAddImgSeller.setOnClickListener { view ->
            run {
                launchGallerySeller()
            }
        }

        btnAddSeller = root.findViewById(R.id.btn_save_seller)
        btnAddSeller.setOnClickListener { view ->
            run {
                addSeller()
            }
        }

        txtNameSeller = root.findViewById(R.id.etUserName_subseller)
        txtTelSeller = root.findViewById(R.id.etUserTel_subseller)



        db = FirebaseDatabase.getInstance()
        auth =  FirebaseAuth.getInstance()
        dbReference =  db.reference.child("User")

        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        img1Seller =  root.findViewById(R.id.image_preview_seller)

        return root
    }


    fun launchGallerySeller() {
        val intentImplicito = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intentImplicito,8)
    }
/*
     fun saveChanges(){
        val nameMarket: String =  txtMarketNameSpecial.text.toString()

        if(!TextUtils.isEmpty(nameMarket)    ){

                    val  user:FirebaseUser? = auth.currentUser

                    val sellerId ="010101"
                    val userDB =  dbReference.child(sellerId)

                    userDB.child("nameSpecial").setValue(nameMarket)

                     uploadImage()

                    Toast.makeText(thiscontext, "Se ha personalizado el puesto", Toast.LENGTH_SHORT)
                            .show()

                    startActivity(Intent(thiscontext, HomeActivity::class.java))

        }else{
            Toast.makeText(thiscontext, "Es obligatorio ingresar todos los campos", Toast.LENGTH_SHORT)
                    .show()
        }
    }*/

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==8 && resultCode == Activity.RESULT_OK) {

            val data_img:Bundle? = data?.extras
            val Image:Bitmap? = data_img?.getParcelable<Bitmap>("data")

            img1Seller.setImageBitmap(Image)
            fileBitmapSeller = Image
        }
    }



    private fun uploadImageSubSeller(idSeller: String){

        val file = File(context?.cacheDir,idSeller) //Get Access to a local file.
        file.delete() // Delete the File, just in Case, that there was still another File
        file.createNewFile()
        val fileOutputStream = file.outputStream()
        val byteArrayOutputStream = ByteArrayOutputStream()
        fileBitmapSeller?.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream)
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
                addUploadRecordToDb(downloadUri, idSeller)

                Toast.makeText(thiscontext, "Se ha  agregado el vendedor", Toast.LENGTH_SHORT)
                    .show()
            } else {
                // Handle failures
            }
        }?.addOnFailureListener{

        }

    }

    fun onBackPressed() {
        val fm: FragmentManager = requireActivity().supportFragmentManager
        fm.popBackStack()
    }

    private fun addUploadRecordToDb(uri: Uri?, seller: String) {
        val vendedorDB =  dbReference.child("010101").child("vendedores").child(seller)
        vendedorDB.child("urlPhoto").setValue(uri.toString())
        onBackPressed()

        /*fragmentManager
            ?.beginTransaction()
            ?.replace(R.id.nav_personalize_sales_position, PersonalizeMarketSellerFragment())
            ?.addToBackStack(null)
            ?.commit()*/
    }


    private fun addSeller(){

                var userName =txtNameSeller.text.toString()
                var userTel =txtTelSeller.text.toString()

                if (userName.isNullOrEmpty()){
                    Toast.makeText(thiscontext, "Debe Ingresar un Nombre", Toast.LENGTH_SHORT) .show()
                    return
                }

                val vendedoresDB =  dbReference.child("010101").child("vendedores").child(userName)
                vendedoresDB.child("phone").setValue(userTel)
                vendedoresDB.child("name").setValue(userName)
                uploadImageSubSeller(userName)
    }



}
