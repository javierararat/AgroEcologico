package com.jararat.agroecologico.ui.admin

interface OnClickListener {
    fun onClick(market: Market)

    fun onClick(unit: Unit)
    abstract fun onClick(prod: Product)

    //fun onClick(seller: Seller)
}