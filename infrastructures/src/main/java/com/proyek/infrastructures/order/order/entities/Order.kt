package com.proyek.infrastructures.order.order.entities

import com.google.gson.annotations.SerializedName
import com.proyek.infrastructures.user.customer.entities.CustomerUser

data class Order(
    @SerializedName("id")
    val id: String? = null,

    @SerializedName("agent_id")
    val agentId: String? = null,

    @SerializedName("customer_id")
    val customerId: String? = null,

    @SerializedName("orderable")
    val order: String? = null,

    @SerializedName("feePrice")
    val feePrice: Int? = null,

    @SerializedName("order_status")
    val orderStatus: Int? = null,

    @SerializedName("order_fee")
    val orderFee: Int? = null,

    @SerializedName("payment_status")
    val paymentStatus: Int? = null,

    @SerializedName("arrival_status")
    val arrivalStatus: Int? = null,

    @SerializedName("agent_note")
    val agentNote: String? = null,

    @SerializedName("customer_review")
    val customerReview: String? = null,

    @SerializedName("created_at")
    val createdAt: String? = null,

    @SerializedName("deliver_at")
    val deliverAt:  String? = null,

    @SerializedName("finished_at")
    val finishedAt: String? = null,

    @SerializedName("cancel_at")
    val cancelAt: String? = null,

    @SerializedName("damage_description")
    val damageDescription: String? = null,

    @SerializedName("customer")
    val customer: CustomerUser?  = null
)