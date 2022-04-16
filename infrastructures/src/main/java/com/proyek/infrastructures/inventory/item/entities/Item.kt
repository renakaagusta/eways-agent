package com.proyek.infrastructures.inventory.item.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.proyek.infrastructures.inventory.category.entities.Category
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Item (
    @SerializedName("id")
    val id: String? = null,

    @SerializedName("name")
    var name: String? = null,

    @SerializedName("description")
    var description: String? = null,

    @SerializedName("price")
    var price: Int? = null,

    @SerializedName("imgPath")
    var imgPath: String? = null,

    @SerializedName("category")
    var category: Category? = null,

    @SerializedName("createdAt")
    val createdAt: String? = null,

    @SerializedName("updatedAt")
    val updatedAt: String? = null
) : Parcelable