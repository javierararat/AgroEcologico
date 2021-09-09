package com.jararat.agroecologico.ui.admin

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable

data class Market(
    var name:String? ="",
    var identification:String? ="",
    var tel:String? ="",
    var email:String? ="",
    var password:String? ="",
    var url:String? =""
):Parcelable
{
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
       // parcel.readParcelable(Bitmap::class.java.classLoader)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(identification)
        parcel.writeString(tel)
        parcel.writeString(email)
        parcel.writeString(password)
        parcel.writeString(url)
        //parcel.writeParcelable(img, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Market> {
        override fun createFromParcel(parcel: Parcel): Market {
            return Market(parcel)
        }

        override fun newArray(size: Int): Array<Market?> {
            return arrayOfNulls(size)
        }
    }

}