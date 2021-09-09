package com.jararat.agroecologico.ui.admin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jararat.agroecologico.R
import com.jararat.agroecologico.databinding.ItemListUnitBinding

class UnitAdapter (private val dataSetUnits: List<Unit>,private val listener: OnClickListener): RecyclerView.Adapter<UnitAdapter.ViewHolder>(){

    private lateinit var context: Context

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val viewBinding =  ItemListUnitBinding.bind(view)

        fun setListener(unit:Unit){
            viewBinding.root.setOnClickListener{
                listener.onClick(unit)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context

        val view =  LayoutInflater.from(context).inflate(R.layout.item_list_unit, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = dataSetUnits.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val market = dataSetUnits.get(position)

        with(holder){
            setListener(market)
            viewBinding.tvNameUnit.text = market.name
            viewBinding.tvAlterEgoUnit.text = market.descrition
            /*Glide.with(context)
                .load(market.url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .circleCrop()
                .into(viewBinding.imageView)*/
        }
    }
}