package com.proyek.infrastructures.order.wallet.entities

import com.google.gson.annotations.SerializedName
import com.proyek.infrastructures.order.wallet.`interface`.IWallet

data class WithdrawalRequest(
    @SerializedName("wallet")
    val wallet: IWallet? = null,

    @SerializedName("amount")
    val amount: Int? = null,

    @SerializedName("status")
    val status: Int? = null
)