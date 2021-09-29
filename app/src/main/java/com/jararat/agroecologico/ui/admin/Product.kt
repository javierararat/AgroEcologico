package com.jararat.agroecologico.ui.admin

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable

data class Product(
    var name:String? ="",
    var price:Int =0,
    var unidad:String? ="",
    var url:String? =""
):Parcelable
{
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(price)
        parcel.writeString(unidad)
        parcel.writeString(url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Product> {
        override fun createFromParcel(parcel: Parcel): Product {
            return Product(parcel)
        }

        override fun newArray(size: Int): Array<Product?> {
            return arrayOfNulls(size)
        }
    }

}