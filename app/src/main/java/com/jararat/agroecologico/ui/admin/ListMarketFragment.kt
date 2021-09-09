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
import kotlin.math.log

class ListMarketFragment: Fragment(), OnClickListener {

    private lateinit var marketAdapter: MarketAdapter
    private lateinit var linearLayoutManager: RecyclerView.LayoutManager
    private lateinit var viewBinding: FragmentListMarketBinding

    private lateinit var rvList: RecyclerView

    lateinit var thiscontext: Context


    private lateinit var dbReference: DatabaseReference
    private lateinit var db: FirebaseDatabase

    val list = mutableListOf<Market>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        thiscontext = context
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_list_market, container, false)

        db = FirebaseDatabase.getInstance()
        dbReference =  db.reference.child("User")
        getMarkets()
        viewBinding = FragmentListMarketBinding.inflate(layoutInflater)

        rvList  =   root.findViewById(R.id.rv_ListMarket)



        return root
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        /*viewBinding.rvListMarket.apply {
            layoutManager =  linearLayoutManager
            adapter =  marketAdapter
        }

        viewBinding.rvListMarket.layoutManager = linearLayoutManager
        viewBinding.rvListMarket.adapter = marketAdapter*/

        updateView()
    }

     private  fun getMarkets() {
        //val list = mutableListOf<Market>()

        var t = Market("q","dd","te","dd@ddd", "wwe", "wwe")
        list.add(t)


         dbReference.get().addOnCompleteListener  { task ->
            //val response = MutableLiveData<Market>()
            if (task.isSuccessful) {
                val result = task.result
                result?.let {
                    /* response = result.children.map { snapShot ->
                         snapShot.getValue(Market::class.java)!!
                     }*/

                    for (postSnapshot in result.children) {
                        // TODO: handle the post
                        var snap = (postSnapshot as DataSnapshot)
                        var myTasksDTO = snap.getValue(Market::class.java)
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
        //editar

         marketAdapter = MarketAdapter(list, this)
         linearLayoutManager = LinearLayoutManager(this.context)

         rvList.layoutManager = linearLayoutManager
         rvList.adapter = marketAdapter
    }


    override fun onClick(market: Market) {
      //editar
    }

    override fun onClick(unit: Unit) {
       //
    }


}