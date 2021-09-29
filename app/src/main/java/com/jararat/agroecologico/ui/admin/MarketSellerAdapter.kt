package com.jararat.agroecologico.ui.admin


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.jararat.agroecologico.R
import com.jararat.agroecologico.databinding.ItemListSubSellerBinding

class MarketSellerAdapter (private val dataSetSellers: List<Seller>):RecyclerView.Adapter<MarketSellerAdapter.ViewHolder>(){

    private lateinit var context: Context

    inner class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        val viewBinding =  ItemListSubSellerBinding.bind(view)

        fun setListener(seller:Seller){
            viewBinding.root.setOnClickListener{
               // listener.onClick(seller)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context

        val view =  LayoutInflater.from(context).inflate(R.layout.item_list_sub_seller, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = dataSetSellers.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val seller = dataSetSellers.get(position)

        with(holder){
            setListener(seller)
            viewBinding.tvNameSubseller.text = seller.name
            viewBinding.tvTelSubseller.text = seller.phone
            //viewBinding.imageViewSubseller.setImageURI(seller.urlPhoto?.toUri())

            Glide.with(context)
              .load(seller.urlPhoto)
              .diskCacheStrategy(DiskCacheStrategy.ALL)
              .centerCrop()
              .circleCrop()
              .into(viewBinding.imageViewSubseller)
        }
    }
}