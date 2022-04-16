package com.proyek.infrastructures.inventory.invoice.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Invoice (
    @SerializedName("id")
    val id: String? = null,

    @SerializedName("name")
    var name: String? = null,

    @SerializedName("created_at")
    val createdAt: String? = null,

    @SerializedName("updated_at")
    val updatedAt: String? = null
) : Parcelable