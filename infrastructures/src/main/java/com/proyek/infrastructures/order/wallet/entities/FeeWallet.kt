package com.proyek.infrastructures.order.wallet.entities

import com.google.gson.annotations.SerializedName

data class FeeWallet(
    @SerializedName("amount")
    val amount: Int? = null,

    @SerializedName("type")
    val type: Int? = null
)