package com.jararat.agroecologico.ui.admin

import android.os.Parcel
import android.os.Parcelable

data class Unit(
        var name:String? ="",
        var description:String? =""
): Parcelable
{
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(description)
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