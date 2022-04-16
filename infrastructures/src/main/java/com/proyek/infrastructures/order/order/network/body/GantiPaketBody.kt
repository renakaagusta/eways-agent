package com.proyek.infrastructures.order.order.network.body

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.proyek.infrastructures.inventory.internetservice.entities.InternetService
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GantiPaketBody(
    @SerializedName("oldInternetService")
    val oldInternetService: InternetService,

    @SerializedName("newInternetService")
    val newInternetService: InternetService,

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