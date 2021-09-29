package com.jararat.agroecologico.ui.admin

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.api.Response
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.jararat.agroecologico.R
import com.jararat.agroecologico.databinding.FragmentListMarketBinding
import com.jararat.agroecologico.databinding.FragmentListProductBinding
import kotlin.math.log

class ListProductsFragment: Fragment(), OnClickListener {

    private lateinit var productAdapter: ProductAdapter
    private lateinit var linearLayoutManager: RecyclerView.LayoutManager
    private lateinit var viewBinding: FragmentListProductBinding

    private lateinit var rvList: RecyclerView

    lateinit var thiscontext: Context


    private lateinit var dbReference: DatabaseReference
    private lateinit var db: FirebaseDatabase

    val list = mutableListOf<Product>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        thiscontext = context
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_list_product, container, false)

        db = FirebaseDatabase.getInstance()
        dbReference =  db.reference.child("User").child("010101").child("productos")
        getProducts()
        viewBinding = FragmentListProductBinding.inflate(layoutInflater)

        rvList  =   root.findViewById(R.id.rv_ListProduct)

        return root
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        updateView()
    }

     private  fun getProducts() {

         dbReference.get().addOnCompleteListener  { task ->
            if (task.isSuccessful) {
                val result = task.result
                result?.let {
                    for (postSnapshot in result.children) {
                        // TODO: handle the post
                        var snap = (postSnapshot as DataSnapshot)
                        var myTasksDTO = snap.getValue(Product::class.java)
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
         productAdapter = ProductAdapter(list, this)
         linearLayoutManager = LinearLayoutManager(this.context)
         rvList.layoutManager = linearLayoutManager
         rvList.adapter = productAdapter
    }


    override fun onClick(market: Market) {
      //editar
    }

    override fun onClick(unit: Unit) {
       //
    }

    override fun onClick(prod: Product) {
        TODO("Not yet implemented")
    }


}