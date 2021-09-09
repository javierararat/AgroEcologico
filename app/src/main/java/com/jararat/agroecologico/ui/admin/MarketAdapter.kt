package com.jararat.agroecologico.ui.admin


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.jararat.agroecologico.R
import com.jararat.agroecologico.databinding.ItemListMarketBinding

class MarketAdapter (private val dataSetMarkets: List<Market>,private val listener: OnClickListener):RecyclerView.Adapter<MarketAdapter.ViewHolder>(){

    private lateinit var context: Context

    inner class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        val viewBinding =  ItemListMarketBinding.bind(view)

        fun setListener(market:Market){
            viewBinding.root.setOnClickListener{
                listener.onClick(market)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context

        val view =  LayoutInflater.from(context).inflate(R.layout.item_list_market, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = dataSetMarkets.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val market = dataSetMarkets.get(position)

        with(holder){
            setListener(market)
            viewBinding.tvName.text = market.name
            viewBinding.tvAlterEgo.text = market.tel
            /*Glide.with(context)
                .load(market.url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .circleCrop()
                .into(viewBinding.imageView)*/
        }
    }
}