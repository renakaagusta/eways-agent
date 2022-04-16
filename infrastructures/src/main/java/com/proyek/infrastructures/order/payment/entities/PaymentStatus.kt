package com.proyek.infrastructures.order.payment.entities

import com.google.gson.annotations.SerializedName

data class PaymentStatus(
    @SerializedName("id")
    val id: String? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("description")
    val description: String? = null
)