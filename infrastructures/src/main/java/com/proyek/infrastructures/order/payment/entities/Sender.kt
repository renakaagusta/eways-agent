package com.proyek.infrastructures.order.payment.entities

import com.google.gson.annotations.SerializedName

data class Sender(
    @SerializedName("name")
    val name: String? = null,

    @SerializedName("bank")
    val bank: String? = null
)