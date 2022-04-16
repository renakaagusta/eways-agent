package com.proyek.infrastructures.order.order.network.body

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.proyek.infrastructures.inventory.invoice.entities.Invoice
import kotlinx.android.parcel.Parcelize

@Parcelize
class SOPPBody (
    @SerializedName("invoice")
    val invoice: Invoice,

    @SerializedName("name")
    val name: String,

    @SerializedName("phoneNumber")
    val phoneNumber: String,

    @SerializedName("address")
    val address: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("serviceId")
    val serviceId: String,

    @SerializedName("customerName")
    val customerName: String,

    @SerializedName("customerAddress")
    val customerAddress: String,

    @SerializedName("customerPhoneNumber")
    val customerPhoneNumber: String,

    @SerializedName("customerCluster")
    val customerCluster: String,

    @SerializedName("agentId")
    val agentId: String
) : Parcelable
