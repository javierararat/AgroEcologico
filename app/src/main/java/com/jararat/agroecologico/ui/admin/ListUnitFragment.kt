package com.jararat.agroecologico.ui.admin

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.jararat.agroecologico.R
import com.jararat.agroecologico.databinding.FragmentListMarketBinding
import com.jararat.agroecologico.databinding.FragmentListUnitBinding

class ListUnitFragment : Fragment(), OnClickListener {

    private lateinit var unitAdapter: UnitAdapter
    private lateinit var linearLayoutManager: RecyclerView.LayoutManager
    private lateinit var viewBinding: FragmentListUnitBinding

    private lateinit var rvList: RecyclerView

    lateinit var thiscontext: Context


    private lateinit var dbReference: DatabaseReference
    private lateinit var db: FirebaseDatabase

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
        val root = inflater.inflate(R.layout.fragment_list_unit, container, false)

        db = FirebaseDatabase.getInstance()
        dbReference =  db.reference.child("Unit")
        getUnits()
        viewBinding = FragmentListUnitBinding.inflate(layoutInflater)

        rvList  =   root.findViewById(R.id.rv_ListUnit)



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

    private  fun getUnits() {

        var t = Unit("Nombre","Descripcion")
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
        //editar

        unitAdapter = UnitAdapter(list, this)
        linearLayoutManager = LinearLayoutManager(this.context)

        rvList.layoutManager = linearLayoutManager
        rvList.adapter = unitAdapter
    }

    override fun onClick(market: Market) {
        //
    }


    override fun onClick(unit: Unit) {
        //editar
    }

    override fun onClick(prod: Product) {
        TODO("Not yet implemented")
    }


}