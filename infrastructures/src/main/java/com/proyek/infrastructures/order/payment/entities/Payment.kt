package com.proyek.infrastructures.order.payment.entities

import com.google.gson.annotations.SerializedName
import com.proyek.infrastructures.order.payment.`interface`.IMethod
import com.proyek.infrastructures.order.payment.`interface`.IPayable
import com.proyek.infrastructures.order.payment.`interface`.IReceiver
import com.proyek.infrastructures.order.payment.`interface`.ISender
import java.sql.Timestamp

data class Payment(
    @SerializedName("payables")
    val payables: List<IPayable>,

    @SerializedName("method")
    val method: IMethod? = null,

    @SerializedName("total")
    val total: Int? = null,

    @SerializedName("status")
    val status: PaymentStatus? = null,

    @SerializedName("sender")
    val sender: ISender? = null,

    @SerializedName("receiver")
    val receiver: IReceiver? = null,

    @SerializedName("receiptPath")
    val receiptPath: String? = null,

    @SerializedName("paidAt")
    val paidAt: Timestamp? = null,

    @SerializedName("verifiedAt")
    val verifiedAt: Timestamp? = null
)