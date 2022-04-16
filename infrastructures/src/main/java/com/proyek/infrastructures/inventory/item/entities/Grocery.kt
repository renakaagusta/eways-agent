package com.proyek.infrastructures.inventory.item.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Grocery(
    @SerializedName("item")
    val items: Item,

    @SerializedName("quantity")
    var quantity: Int
) : Parcelable