package com.proyek.infrastructures.order.wallet.entities

import com.google.gson.annotations.SerializedName

data class ModalWallet(
    @SerializedName("amount")
    val amount: Int? = null,

    @SerializedName("type")
    val type: Int? = null
)