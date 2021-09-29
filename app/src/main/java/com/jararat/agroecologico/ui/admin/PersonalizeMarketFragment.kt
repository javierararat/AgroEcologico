package com.jararat.agroecologico.ui.admin

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.jararat.agroecologico.HomeActivity
import com.jararat.agroecologico.R
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.util.*


class PersonalizeMarketFragment : Fragment()  {


    private lateinit var txtMarketNameSpecial: EditText
    private lateinit var btnRegister: Button
    private lateinit var btnAddImg: Button
    private lateinit var btnAddImgSeller: Button
    private lateinit var btnAddSeller: FloatingActionButton

    private lateinit var dbReference: DatabaseReference
    private lateinit var dbReferenceSeller: DatabaseReference
    private lateinit var db: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    lateinit var thiscontext: Context

    private lateinit var img1: ImageView

    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null

    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private var fileBitmap: Bitmap? = null

    var listSeller = mutableListOf<Seller>()
    private lateinit var sellerAdapter: MarketSellerAdapter
    private lateinit var linearLayoutManager: RecyclerView.LayoutManager
    private lateinit var rvList: RecyclerView

    private lateinit var dataMarket: Market


    override fun onAttach(context: Context) {
        super.onAttach(context)
        thiscontext = context
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_personalize_sales_position, container, false)

        txtMarketNameSpecial = root.findViewById(R.id.et_name_market_s)
        btnAddImg = root.findViewById(R.id.btn_choose_image)
        btnAddImg.setOnClickListener { view ->
            run {
                launchGallery()
            }
        }

        btnRegister = root.findViewById(R.id.bt_registrar_s)
        btnRegister.setOnClickListener { view ->
            run {
               saveChanges()
            }
        }

        btnAddSeller = root.findViewById(R.id.bt_add_sub_seller)
        btnAddSeller.setOnClickListener { view ->
            run {
                //addSeller()
                view.findNavController().navigate(R.id.nav_sales_sub_seller)
            }
        }


        db = FirebaseDatabase.getInstance()
        auth =  FirebaseAuth.getInstance()
        dbReference =  db.reference.child("User")
        dbReferenceSeller = dbReference.child("010101").child("vendedores")

        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        img1 =  root.findViewById(R.id.image_preview)

        rvList  =   root.findViewById(R.id.rv_list_sub_seller)

        getData()

        getSellers()

        return root
    }

    private fun launchGallery() {
        val intentImplicito = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intentImplicito,7)
    }



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
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==7 && resultCode == Activity.RESULT_OK) {

            if (data != null) {
                filePath = data.data
            }

            val data_img:Bundle? = data?.extras
            val Image:Bitmap? = data_img?.getParcelable<Bitmap>("data")

            img1.setImageBitmap(Image)
            fileBitmap = Image
        }

    }

    private fun uploadImage(){

        val file = File(context?.cacheDir,"CUSTOM NAME") //Get Access to a local file.
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
                addUploadRecordToDb(downloadUri.toString())
            } else {
                // Handle failures
            }
        }?.addOnFailureListener{

        }

    }


    private fun addUploadRecordToDb(uri: String){

        val userDB =  dbReference.child("010101")
        userDB.child("url").setValue(uri)
    }


    private  fun getData() {

      dbReference.get().addOnCompleteListener  { task ->
            if (task.isSuccessful) {
                val result = task.result
                val let = result?.let {
                    /*var tmp_item = result.children
                    // TODO: handle the post
                    var snap = (tmp_item as DataSnapshot)
                    dataMarket = snap.getValue(Market::class.java)!!

                    img1.setImageURI(dataMarket.url?.toUri())
                    txtMarketNameSpecial.setText(dataMarket.nameSpecial.toString())*/
                }
            }
        }
    }



    private  fun getSellers() {
        listSeller = mutableListOf<Seller>()


        dbReferenceSeller.get().addOnCompleteListener  { task ->
            //val response = MutableLiveData<Market>()
            if (task.isSuccessful) {
                val result = task.result
                result?.let {

                    for (postSnapshot in result.children) {
                        // TODO: handle the post
                        var snap = (postSnapshot as DataSnapshot)
                        var myTasksDTO = snap.getValue(Seller::class.java)
                        if (myTasksDTO != null) {
                            listSeller.add(myTasksDTO)
                        }

                    }

                    updateView()

                }
            }
        }
    }


    fun updateView() {
        //editar

        sellerAdapter = MarketSellerAdapter(listSeller)
        linearLayoutManager = LinearLayoutManager(this.context)

        rvList.layoutManager = linearLayoutManager
        rvList.adapter = sellerAdapter
    }


}
