package com.jararat.agroecologico.ui.admin


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.jararat.agroecologico.R
import com.jararat.agroecologico.databinding.ItemListProductBinding

class ProductAdapter (private val dataSetProducts: List<Product>, private val listener: OnClickListener):RecyclerView.Adapter<ProductAdapter.ViewHolder>(){

    private lateinit var context: Context

    inner class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        val viewBinding =  ItemListProductBinding.bind(view)

        fun setListener(prod:Product){
            viewBinding.root.setOnClickListener{
                listener.onClick(prod)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context

        val view =  LayoutInflater.from(context).inflate(R.layout.item_list_product, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = dataSetProducts.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val prod = dataSetProducts.get(position)

        with(holder){
            setListener(prod)
            viewBinding.tvNameProduct.text = prod.name
            viewBinding.tvPrecioProduct.text = prod.price.toString()
            viewBinding.tvUnidadProduct.text = prod.unidad
            Glide.with(context)
                .load(prod.url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .circleCrop()
                .into(viewBinding.imageViewProduct)
        }
    }
}